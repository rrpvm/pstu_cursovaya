package com.rrpvm.data.repository

import com.rrpvm.data.mapper.FromDomainDateStringMapper
import com.rrpvm.data.mapper.KinoEntityToModelMapper
import com.rrpvm.data.mapper.KinoModelToKinoEntityMapper
import com.rrpvm.data.mapper.KinoSessionModelToKinoSessionEntityMapper
import com.rrpvm.data.room.dao.KinoDao
import com.rrpvm.data.room.dao.KinoSessionDao
import com.rrpvm.data.room.entity.KinoSessionEntity
import com.rrpvm.data.room.entity.SessionsWithKino
import com.rrpvm.domain.model.KinoModel
import com.rrpvm.domain.model.KinoSessionModel
import com.rrpvm.domain.repository.KinoRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class MemoryKinoRepository @Inject constructor(
    private val kinoDao: KinoDao,
    private val kinoSessionDao: KinoSessionDao,
) : KinoRepository {
    override fun getKinoFilmsByMinSessionDate(minDate: Date): Flow<List<KinoModel>> {
        return kinoSessionDao.getSessionsWithKinoFlow()
            .map { sessionsWithKinos: List<SessionsWithKino> ->
                sessionsWithKinos.filter { sessionsWithKino: SessionsWithKino ->
                    sessionsWithKino.sessionList.any { kinoSessionEntity: KinoSessionEntity ->
                        FromDomainDateStringMapper.mapToDomainDate(kinoSessionEntity.sessionStartDate).time >= minDate.time
                    }
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
        delay(1500L)//имитация работы сетевого запроса
        val kinoList = fetchCinemaList()
        val kinoSessions = fetchKinoSession(kinoList)

        kinoSessionDao.insertSession(kinoSessions.map {
            it.map(KinoSessionModelToKinoSessionEntityMapper)
        })
        return Result.success(true)
    }

    private suspend fun fetchCinemaList(): List<KinoModel> {
        return listOf<KinoModel>(
            KinoModel(
                id = "5874b417-5417-4b01-be85-aa9f647bd35f",
                title = "Аватар",
                description = "",
                previewImage = "https://m.media-amazon.com/images/M/MV5BZDA0OGQxNTItMDZkMC00N2UyLTg3MzMtYTJmNjg3Nzk5MzRiXkEyXkFqcGdeQXVyMjUzOTY1NTc@._V1_FMjpg_UX1000_.jpg",
                releasedDate = Calendar.Builder().setDate(2009, 12, 18).build().time,
                isLiked = false
            ),
            KinoModel(
                id = "cf65ed1d-b79a-460a-9606-1d2edaf3586c",
                title = "Последний ронин",
                description = "",
                previewImage = "https://avatars.mds.yandex.net/i?id=1508d997ad6dc85da055bbb434f34b73_l-10414746-images-thumbs&n=13",
                releasedDate = Calendar.Builder().setDate(2024, 12, 19).build().time,
                isLiked = false
            ),
            KinoModel(
                id = "ebb65720-2389-4a7e-9fe3-7d1458d240a0",
                title = "Постучись в мою дверь",
                description = "",
                previewImage = "https://yastatic.net/naydex/yandex-search/bTFy12402/d21c4fyqx/EVXP3tawtuggczhscKX0EJ-siEZ_6PFmTUAZlZURLVdixeCovuXQQnV1HKdUpF3F1J80-wV7eOyBOs0O8i53WehFq3IufH9Y6N-98IgFdGSP9aBqCoGFLqXs",
                releasedDate = Calendar.Builder().setDate(2024, 12, 24).build().time,
            ),
            KinoModel(
                id = "abb08203-ac9b-4467-9b76-dbb7d890e9b9",
                title = "Волшебник изумрудного города",
                description = "",
                previewImage = "https://pic.rutubelist.ru/video/2025-01-06/6b/6d/6b6dc1541d3e46cedf0c1209b51fb86f.jpg",
                releasedDate = Calendar.Builder().setDate(2025, 1, 1).build().time,
            ),
        ).also {
            kinoDao.fullUpdateKinoList(it.map {e->
                e.map(KinoModelToKinoEntityMapper)
            })
        }
    }

    private suspend fun fetchKinoSession(list: List<KinoModel>): List<KinoSessionModel> {
        delay(250L)
        return listOf<KinoSessionModel>(
            //Аватар
            KinoSessionModel(
                kinoModel = list.first { it.id == "5874b417-5417-4b01-be85-aa9f647bd35f" },
                sessionStartDate = Calendar.Builder().setDate(2025, 0, 8).setTimeOfDay(13, 30, 30)
                    .build().time,
                sessionId = "db60116a-f83e-4b09-a3d0-5dadb1f179c8"
            ),
            KinoSessionModel(
                kinoModel = list.first { it.id == "5874b417-5417-4b01-be85-aa9f647bd35f" },
                sessionStartDate = Calendar.Builder().setDate(2025, 0, 8).setTimeOfDay(16, 30, 30)
                    .build().time,
                sessionId = "3632ca1d-8784-454d-b103-eac719abd1f2"
            ),
            //Ронин
            KinoSessionModel(
                kinoModel = list.first { it.id == "cf65ed1d-b79a-460a-9606-1d2edaf3586c" },
                sessionStartDate = Calendar.Builder().setDate(2025, 0, 8).setTimeOfDay(16, 30, 30)
                    .build().time,
                sessionId = "eb2772e7-beb1-42b9-838a-6b80d494935d"
            ),
            KinoSessionModel(
                kinoModel = list.first { it.id == "cf65ed1d-b79a-460a-9606-1d2edaf3586c" },
                sessionStartDate = Calendar.Builder().setDate(2025, 0, 8).setTimeOfDay(18, 30, 30)
                    .build().time,
                sessionId = "4095449a-ce4f-467d-b3ed-0af6d0d3d2a6"
            ),
            KinoSessionModel(
                kinoModel = list.first { it.id == "cf65ed1d-b79a-460a-9606-1d2edaf3586c" },
                sessionStartDate = Calendar.Builder().setDate(2025, 0, 8).setTimeOfDay(21, 30, 30)
                    .build().time,
                sessionId = "d5e8edd1-de15-45e7-9377-37bf7503a28c"
            ),
            //Постучись в мою дверь
            KinoSessionModel(
                kinoModel = list.first { it.id =="ebb65720-2389-4a7e-9fe3-7d1458d240a0" },
                sessionStartDate = Calendar.Builder().setDate(2025, 0, 9).setTimeOfDay(9, 15, 0)
                    .build().time,
                sessionId = "5d7e1b9c-d0ba-4903-ba91-087dc179c28d"
            ),
            KinoSessionModel(
                kinoModel = list.first { it.id == "ebb65720-2389-4a7e-9fe3-7d1458d240a0" },
                sessionStartDate = Calendar.Builder().setDate(2025, 0, 9).setTimeOfDay(11, 15, 0)
                    .build().time,
                sessionId = "34b94e26-99e6-40e2-9f2e-05e263768a90"
            ),
            KinoSessionModel(
                kinoModel = list.first { it.id == "ebb65720-2389-4a7e-9fe3-7d1458d240a0" },
                sessionStartDate = Calendar.Builder().setDate(2025, 0, 9).setTimeOfDay(13, 15, 0)
                    .build().time,
                sessionId = "fd7edc7a-7a9b-46a3-885e-d4973cbe546c"
            ),
            //Волшебник изумрудного города
            KinoSessionModel(
                kinoModel = list.first { it.id == "abb08203-ac9b-4467-9b76-dbb7d890e9b9" },
                sessionStartDate = Calendar.Builder().setDate(2025, 0, 9).setTimeOfDay(4, 15, 0)
                    .build().time,
                sessionId = "db84083c-0f50-43f7-9b05-7b4882ae8073"
            ),
        )
    }


}