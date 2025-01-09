package com.rrpvm.data.repository

import com.rrpvm.data.mapper.FromDomainDateStringMapper
import com.rrpvm.data.mapper.KinoEntityToModelMapper
import com.rrpvm.data.mapper.KinoModelToKinoEntityMapper
import com.rrpvm.data.mapper.KinoSessionModelToKinoSessionEntityMapper
import com.rrpvm.data.room.dao.KinoDao
import com.rrpvm.data.room.dao.KinoSessionDao
import com.rrpvm.data.room.entity.KinoSessionEntity
import com.rrpvm.data.room.entity.SessionsWithKino
import com.rrpvm.data.datasource.KinofilmsDataSource
import com.rrpvm.data.mapper._data.KinoDtoToKinoModelMapper
import com.rrpvm.domain.model.KinoModel
import com.rrpvm.domain.model.KinoSessionModel
import com.rrpvm.domain.repository.KinoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class RoomCachedKinoRepository @Inject constructor(
    private val kinoDao: KinoDao,
    private val kinoSessionDao: KinoSessionDao,
    private val kinoDataSource: KinofilmsDataSource,
    private val kinoDtoToKinoModelMapper: KinoDtoToKinoModelMapper
) : KinoRepository {

    override fun getKinoFilmsByDateConstraintSessionDate(
        minDate: Date,
        maxDate: Date
    ): Flow<List<KinoModel>> {
        return kinoSessionDao.getSessionsWithKinoByOrderDateFlow()
            .map { sessionsWithKinos: List<SessionsWithKino> ->
                sessionsWithKinos.filter { sessionsWithKino: SessionsWithKino ->
                    sessionsWithKino.sessionList.any { kinoSessionEntity: KinoSessionEntity ->
                        val sessionTime =
                            FromDomainDateStringMapper.mapToDomainDate(kinoSessionEntity.sessionStartDate).time
                        //попадаем во временные рамки
                        sessionTime >= minDate.time && sessionTime <= maxDate.time
                    } && sessionsWithKino.sessionList.isNotEmpty() //на всякий случай сверяем на пустоту
                }.sortedBy {
                    it.sessionList.first().sessionStartDate
                }
            }.map { filteredSessions ->
                filteredSessions.map { sessionWithKino ->
                    sessionWithKino.kinoModel.map(KinoEntityToModelMapper)
                }
            }
    }

    override fun getKinoSessions(minDate: Date): Flow<List<KinoSessionModel>> {
        return kinoSessionDao.getSessionsWithKinoFlow().map { list ->
            val result = mutableListOf<KinoSessionModel>()
            list.map { sessionsWithKino ->
                sessionsWithKino.sessionList.forEach {
                    result.add(
                        KinoSessionModel(
                            sessionId = it.sessionId,
                            kinoModel = sessionsWithKino.kinoModel.map(
                                KinoEntityToModelMapper
                            ),
                            sessionStartDate = FromDomainDateStringMapper.mapToDomainDate(it.sessionStartDate)
                        )
                    )
                }
            }
            result
        }
    }

    override suspend fun fetchKinoFeed(): Result<Boolean> {
        val result = runCatching {
            //refresh films
            kinoDataSource.getAllAfishaKinos().let { films ->
                kinoDao.fullUpdateKinoList(films.asSequence().map { e ->
                    e.map(kinoDtoToKinoModelMapper)
                }.map { domainModel ->
                    domainModel.map(KinoModelToKinoEntityMapper)
                }.toList())
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