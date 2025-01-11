package com.rrpvm.data.datasource.impl

import com.rrpvm.data.datasource.KinofilmsDataSource
import com.rrpvm.data.mapper._data.KinoDtoToKinoModelMapper
import com.rrpvm.data.model.kinofilms.KinoModelDto
import com.rrpvm.domain.model.GenreModel
import com.rrpvm.domain.model.KinoModel
import com.rrpvm.domain.model.KinoSessionModel
import kotlinx.coroutines.delay
import java.util.Calendar
import javax.inject.Inject

class MemoryKinoFilmsDataSource @Inject constructor(private val kinoModelMapper: KinoDtoToKinoModelMapper) :
    KinofilmsDataSource {
    companion object {
        enum class DtoGenres(val genre: GenreModel) {
            BOEVIK(GenreModel("d0ad42e1-7450-4519-aef8-2f36be7df341", "Боевик")),
            DRAMA(GenreModel("01d775cf-3c83-4048-b374-cee0f6d08620", "Драма")),
            COMEDY(GenreModel("c08af339-2489-40bb-a4e4-9615186ec330", "Комедия")),
        }
    }

    private val kinoList: Map<String, KinoModelDto> = listOf(
        KinoModelDto(
            id = "5874b417-5417-4b01-be85-aa9f647bd35f",
            title = "Аватар",
            description = "",
            previewImage = "https://m.media-amazon.com/images/M/MV5BZDA0OGQxNTItMDZkMC00N2UyLTg3MzMtYTJmNjg3Nzk5MzRiXkEyXkFqcGdeQXVyMjUzOTY1NTc@._V1_FMjpg_UX1000_.jpg",
            releasedDate = Calendar.Builder().setDate(2009, 11, 18).build().time,
            byCountry = "USA",
            genres = listOf(DtoGenres.DRAMA.genre, DtoGenres.BOEVIK.genre)
        ),
        KinoModelDto(
            id = "cf65ed1d-b79a-460a-9606-1d2edaf3586c",
            title = "Последний ронин",
            description = "",
            previewImage = "https://avatars.mds.yandex.net/i?id=1508d997ad6dc85da055bbb434f34b73_l-10414746-images-thumbs&n=13",
            releasedDate = Calendar.Builder().setDate(2024, 6, 19).build().time,
            byCountry = "USA",
            genres = listOf(DtoGenres.DRAMA.genre, DtoGenres.BOEVIK.genre)
        ),
        KinoModelDto(
            id = "ebb65720-2389-4a7e-9fe3-7d1458d240a0",
            title = "Постучись в мою дверь",
            description = "",
            previewImage = "https://yastatic.net/naydex/yandex-search/bTFy12402/d21c4fyqx/EVXP3tawtuggczhscKX0EJ-siEZ_6PFmTUAZlZURLVdixeCovuXQQnV1HKdUpF3F1J80-wV7eOyBOs0O8i53WehFq3IufH9Y6N-98IgFdGSP9aBqCoGFLqXs",
            releasedDate = Calendar.Builder().setDate(2022, 5, 24).build().time,
            byCountry = "Russia",
            genres = listOf(DtoGenres.DRAMA.genre, DtoGenres.COMEDY.genre)
        ),
        KinoModelDto(
            id = "abb08203-ac9b-4467-9b76-dbb7d890e9b9",
            title = "Волшебник изумрудного города",
            description = "",
            previewImage = "https://pic.rutubelist.ru/video/2025-01-06/6b/6d/6b6dc1541d3e46cedf0c1209b51fb86f.jpg",
            releasedDate = Calendar.Builder().setDate(2025, 1, 1).build().time,
            byCountry = "USA",
            genres = listOf(DtoGenres.DRAMA.genre)
        ),
    ).associateBy { it.id }

    override suspend fun getAllAfishaKinoSessions(): List<KinoSessionModel> {
        delay(1000L)//имитация сетевого запроса
        return listOf<KinoSessionModel>(
            //Аватар
            KinoSessionModel(
                kinoModel = kinoList["5874b417-5417-4b01-be85-aa9f647bd35f"]!!.map(
                    kinoModelMapper
                ),
                sessionStartDate = Calendar.Builder().setDate(2025, 0, 10).setTimeOfDay(13, 30, 30)
                    .build().time,
                sessionId = "db60116a-f83e-4b09-a3d0-5dadb1f179c8"
            ),
            KinoSessionModel(
                kinoModel = kinoList["5874b417-5417-4b01-be85-aa9f647bd35f"]!!.map(
                    kinoModelMapper
                ),
                sessionStartDate = Calendar.Builder().setDate(2025, 0, 10).setTimeOfDay(16, 30, 30)
                    .build().time,
                sessionId = "3632ca1d-8784-454d-b103-eac719abd1f2"
            ),
            //Ронин
            KinoSessionModel(
                kinoModel = kinoList["cf65ed1d-b79a-460a-9606-1d2edaf3586c"]!!.map(
                    kinoModelMapper
                ),
                sessionStartDate = Calendar.Builder().setDate(2025, 0, 10).setTimeOfDay(16, 30, 30)
                    .build().time,
                sessionId = "eb2772e7-beb1-42b9-838a-6b80d494935d"
            ),
            KinoSessionModel(
                kinoModel = kinoList["cf65ed1d-b79a-460a-9606-1d2edaf3586c"]!!.map(
                    kinoModelMapper
                ),
                sessionStartDate = Calendar.Builder().setDate(2025, 0, 10).setTimeOfDay(18, 30, 30)
                    .build().time,
                sessionId = "4095449a-ce4f-467d-b3ed-0af6d0d3d2a6"
            ),
            KinoSessionModel(
                kinoModel = kinoList["cf65ed1d-b79a-460a-9606-1d2edaf3586c"]!!.map(
                    kinoModelMapper
                ),
                sessionStartDate = Calendar.Builder().setDate(2025, 0, 10).setTimeOfDay(21, 30, 30)
                    .build().time,
                sessionId = "d5e8edd1-de15-45e7-9377-37bf7503a28c"
            ),
            //Постучись в мою дверь
            KinoSessionModel(
                kinoModel = kinoList["ebb65720-2389-4a7e-9fe3-7d1458d240a0"]!!.map(
                    kinoModelMapper
                ),
                sessionStartDate = Calendar.Builder().setDate(2025, 0, 13).setTimeOfDay(9, 15, 0)
                    .build().time,
                sessionId = "5d7e1b9c-d0ba-4903-ba91-087dc179c28d"
            ),
            KinoSessionModel(
                kinoModel = kinoList["ebb65720-2389-4a7e-9fe3-7d1458d240a0"]!!.map(
                    kinoModelMapper
                ),
                sessionStartDate = Calendar.Builder().setDate(2025, 0, 13).setTimeOfDay(11, 15, 0)
                    .build().time,
                sessionId = "34b94e26-99e6-40e2-9f2e-05e263768a90"
            ),
            KinoSessionModel(
                kinoModel = kinoList["ebb65720-2389-4a7e-9fe3-7d1458d240a0"]!!.map(
                    kinoModelMapper
                ),
                sessionStartDate = Calendar.Builder().setDate(2025, 0, 13).setTimeOfDay(21, 15, 0)
                    .build().time,
                sessionId = "fd7edc7a-7a9b-46a3-885e-d4973cbe546c"
            ),
            //Волшебник изумрудного города
            KinoSessionModel(
                kinoModel = kinoList["abb08203-ac9b-4467-9b76-dbb7d890e9b9"]!!.map(
                    kinoModelMapper
                ),
                sessionStartDate = Calendar.Builder().setDate(2025, 0, 11).setTimeOfDay(22, 15, 0)
                    .build().time,
                sessionId = "db84083c-0f50-43f7-9b05-7b4882ae8073"
            ),
            KinoSessionModel(
                kinoModel = kinoList["abb08203-ac9b-4467-9b76-dbb7d890e9b9"]!!.map(
                    kinoModelMapper
                ),
                sessionStartDate = Calendar.Builder().setDate(2025, 0, 11).setTimeOfDay(21, 15, 0)
                    .build().time,
                sessionId = "db84083c-0f50-43f7-9b05-7b4882ae8073"
            ),
        )
    }

    override suspend fun getAllAfishaKinos(): List<KinoModelDto> {
        return kinoList.values.toList()
    }
}