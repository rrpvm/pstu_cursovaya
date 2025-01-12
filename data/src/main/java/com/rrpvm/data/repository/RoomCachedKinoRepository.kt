package com.rrpvm.data.repository

import com.rrpvm.data.mapper.FromDomainDateStringMapper
import com.rrpvm.data.mapper.KinoModelToKinoEntityMapper
import com.rrpvm.data.mapper.KinoSessionModelToKinoSessionEntityMapper
import com.rrpvm.data.room.dao.KinoDao
import com.rrpvm.data.room.dao.KinoSessionDao
import com.rrpvm.data.room.entity.KinoSessionEntity
import com.rrpvm.data.datasource.KinofilmsDataSource
import com.rrpvm.data.mapper.GenreModelToKinoGenreEntityMapper
import com.rrpvm.data.mapper._data.AgeRatingDtoToAgeRatingEntityMapper
import com.rrpvm.data.mapper._data.KinoDtoToKinoModelMapper
import com.rrpvm.data.mapper._entity.KinoWithGenresToKinoModel
import com.rrpvm.data.mapper._entity.KinoWithSessionsAndGenresToKinoModel
import com.rrpvm.data.mapper._entity.KinoWithSessionsAndGenresToKinoWithSessionModelMapper
import com.rrpvm.data.model.agerating.AgeRatingDto
import com.rrpvm.data.room.dao.AgeRatingDao
import com.rrpvm.data.room.dao.KinoFilmViewsDao
import com.rrpvm.data.room.dao.KinoGenresDao
import com.rrpvm.data.room.entity.AgeRatingEntity
import com.rrpvm.data.room.entity.KinoFilmViewEntity
import com.rrpvm.data.room.entity.KinoGenreCrossRefEntity
import com.rrpvm.data.room.entity.KinoGenreEntity
import com.rrpvm.data.room.entity.query_model.KinoWithSessionsAndGenres
import com.rrpvm.domain.model.GenreModel
import com.rrpvm.domain.model.KinoModel
import com.rrpvm.domain.model.KinoWithSessionsModel
import com.rrpvm.domain.repository.KinoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.util.Date
import javax.inject.Inject

class RoomCachedKinoRepository @Inject constructor(
    private val kinoDao: KinoDao,
    private val kinoSessionDao: KinoSessionDao,
    private val kinoFilmViewsDao: KinoFilmViewsDao,
    private val kinoGenresDao: KinoGenresDao,
    private val ageRatingDao: AgeRatingDao,
    private val kinoDataSource: KinofilmsDataSource,
    private val kinoDtoToKinoModelMapper: KinoDtoToKinoModelMapper
) : KinoRepository {
    //выдает только те,у которых есть сессии
    override fun getKinoFilmsByDateConstraintSessionDate(
        minDate: Date,
        maxDate: Date
    ): Flow<List<KinoModel>> {
        return kinoDao.getSessionsWithKinoByOrderDateFlow()
            .map { sessionsWithKinos: List<KinoWithSessionsAndGenres> ->
                sessionsWithKinos.filter { sessionsWithKino: KinoWithSessionsAndGenres ->
                    sessionsWithKino.kinoWithSessions.sessionList.any { kinoSessionEntity: KinoSessionEntity ->
                        val sessionTime =
                            FromDomainDateStringMapper.mapToDomainDate(kinoSessionEntity.sessionStartDate).time
                        //попадаем во временные рамки
                        sessionTime >= minDate.time && sessionTime <= maxDate.time
                    } && sessionsWithKino.kinoWithSessions.sessionList.isNotEmpty() //на всякий случай сверяем на пустоту
                }.sortedBy {
                    it.kinoWithSessions.sessionList.first().sessionStartDate
                }
            }.map { filteredSessionsWithGenres ->
                filteredSessionsWithGenres.map { sessionWithKinoAndGenres: KinoWithSessionsAndGenres ->
                    sessionWithKinoAndGenres.map(KinoWithSessionsAndGenresToKinoModel)
                }
            }
    }

    override fun getKinoFilmsViewed(): Flow<List<KinoModel>> {
        return kinoDao.getViewedKinoFilms().map {
            it.map { e ->
                e.map(KinoWithGenresToKinoModel)
            }
        }
    }

    override fun getAllKinoFilms(): Flow<List<KinoModel>> {
        return kinoDao.getKinoListFlow().map {
            it.map { e ->
                e.map(KinoWithGenresToKinoModel)
            }
        }
    }

    override fun getAllKinoFilmsWithAnyActualSession(): Flow<List<KinoModel>> {
        val actualTime = Instant.now().toEpochMilli()
        return kinoDao.getSessionsWithKinoByOrderDateFlow().map { kinoWithSessionsList ->
            kinoWithSessionsList.asSequence().filter {
                return@filter FromDomainDateStringMapper.mapToDomainDate(
                    it.kinoWithSessions.sessionList.firstOrNull()?.sessionStartDate
                        ?: return@filter false
                ).time >= actualTime
            }.map {
                it.map(KinoWithSessionsAndGenresToKinoModel)
            }.toList()
        }
    }

    override fun viewKino(kinoId: String): Result<Boolean> {
        return kotlin.runCatching {
            kinoFilmViewsDao.insertKinoView(KinoFilmViewEntity(kinoId))
            return@runCatching true
        }
    }

    override fun getKinoGenres(): Flow<List<GenreModel>> {
        return kinoGenresDao.observeAllGenres().map {
            it.map { e ->
                GenreModel(e.mGenreId, e.mGenreName)
            }
        }
    }

    override fun getKinoById(kinoId: String): Result<KinoWithSessionsModel> {
        return kotlin.runCatching {
            return@runCatching kinoDao.getFullKinoModel(kinoId).map(
                KinoWithSessionsAndGenresToKinoWithSessionModelMapper
            )
        }
    }

    override suspend fun fetchKinoFeed(): Result<Boolean> {
        val result = runCatching {
            //refresh films and genres

            kinoDataSource.getAllAfishaKinos().let { films ->
                val genresList = mutableSetOf<KinoGenreEntity>()
                val ageRatingList = mutableSetOf<AgeRatingDto>()
                val kinoEntityList = films.asSequence().map { e ->
                    ageRatingList.add(e.ageRating)

                    //добавляем в бд(пока локально) все жанры из существующих фильмов
                    e.genres.forEach { genreModel ->
                        genresList.add(genreModel.map(GenreModelToKinoGenreEntityMapper))
                    }
                    e.map(kinoDtoToKinoModelMapper)
                }.map { domainModel ->
                    domainModel.map(KinoModelToKinoEntityMapper)
                }.toList()
                //обновляем бд
                //возрастные рейтинги,найденные из дто'шки фильмов
                ageRatingDao.fullUpdateAgeRatings(ageRatingList.map {
                    it.map(AgeRatingDtoToAgeRatingEntityMapper)
                })
                kinoGenresDao.setKinoGenres(genresList.toList())//жанры
                kinoDao.fullUpdateKinoList(kinoEntityList)//ставим кино в бд
                films.forEach { filmDto ->//проставляем жанры к фильмам
                    filmDto.genres.forEach {
                        kinoGenresDao.insertKinoGenreCrossRef(
                            KinoGenreCrossRefEntity(
                                filmDto.id,
                                it.genreId
                            )
                        )
                    }
                }
            }
            //refresh kino sessions
            kinoDataSource.getAllAfishaKinoSessions().let { kinoSessions ->
                kinoSessionDao.insertSession(kinoSessions.map {
                    it.map(KinoSessionModelToKinoSessionEntityMapper)
                })
            }
            return@runCatching true
        }.onFailure { it: Throwable ->
            //abort all
            it.printStackTrace()
        }
        return result
    }

}