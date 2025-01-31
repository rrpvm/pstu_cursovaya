package com.rrpvm.data.datasource.impl

import com.rrpvm.data.datasource.KinofilmsDataSource
import com.rrpvm.data.mapper._data.KinoDtoToKinoModelMapper
import com.rrpvm.data.model.agerating.AgeRatingDto
import com.rrpvm.data.model.hall.HallInfoDto
import com.rrpvm.data.model.kinofilms.KinoModelDto
import com.rrpvm.data.room.dao.TicketsDao
import com.rrpvm.domain.model.GenreModel
import com.rrpvm.domain.model.HallPlaceModel
import com.rrpvm.domain.model.kino.KinoSessionModel
import com.rrpvm.domain.model.SessionHallInfoModel
import com.rrpvm.domain.model.TicketModel
import kotlinx.coroutines.delay
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject

class MemoryKinoFilmsDataSource @Inject constructor(
    private val kinoModelMapper: KinoDtoToKinoModelMapper, private val kinoTicketsDao: TicketsDao
) : KinofilmsDataSource {
    companion object {
        enum class DtoGenres(val genre: GenreModel) {
            BOEVIK(
                GenreModel(
                    "d0ad42e1-7450-4519-aef8-2f36be7df341",
                    "Боевик"
                )
            ),
            DRAMA(
                GenreModel(
                    "01d775cf-3c83-4048-b374-cee0f6d08620",
                    "Драма"
                )
            ),
            COMEDY(GenreModel("c08af339-2489-40bb-a4e4-9615186ec330", "Комедия")), MELODRAMA(
                GenreModel("8d9504be-4d4c-4539-8caa-aa02686ff80b", "Мелодрама")
            ),
            FANTASY(GenreModel("e718963f-914e-4eb7-b3da-7643fcd404c2", "Фантастика")), TRAVEL(
                GenreModel("855789c1-2f83-4899-9614-64d5cb089601", "Приключения")
            ),
            HORROR(GenreModel("3bd22d52-2e39-4fe2-8e0a-cb2d03416b3c", "Ужасы")),
        }

        enum class Halls(val info: HallInfoDto) {
            HALL_R(
                HallInfoDto("GustavoFring", "Зал R", 9, 14)
            ),
            HALL_G(
                HallInfoDto("WillyWonka", "Зал G", 8, 17)
            ),
            HALL_B(
                HallInfoDto("WalterWhite", "Зал B", 10, 13)
            );
            //палитра
        }


        private fun createHall(
            info: HallInfoDto, owner: String, price: Int,
            busyByOtherList: List<Int>,
        ): SessionHallInfoModel {
            val size = info.rows * info.columns
            return SessionHallInfoModel(
                hallId = info.hallId,
                ownerId = owner,
                rows = info.rows,
                columns = info.columns,
                placesSize = size,
                places = MutableList(size) {
                    HallPlaceModel(
                        index = it,
                        price = price,
                        isBusyByOther = busyByOtherList.contains(it),
                        isBusyByUser = false
                    )
                },
                hallName = info.hallName
            )
        }

        enum class AgeRatings(val dto: AgeRatingDto) {
            `0`(
                AgeRatingDto(
                    id = 1,
                    present = "0+",
                    iconUrl = "https://img.icons8.com/?size=100&id=iLLnZLo56xxm&format=png&color=000000",
                    minAge = 0,

                    )
            ),
            `6`(
                AgeRatingDto(
                    id = 2,
                    present = "6+",
                    iconUrl = "https://img.icons8.com/?size=100&id=4Lbwo64nJNK1&format=png&color=000000",
                    minAge = 6,
                )
            ),
            `12`(
                AgeRatingDto(
                    id = 3,
                    present = "12+",
                    iconUrl = "https://img.icons8.com/?size=100&id=OMkPnTo5cjzp&format=png&color=000000",
                    minAge = 12,
                )
            ),
            `16`(
                AgeRatingDto(
                    id = 4,
                    present = "16+",
                    iconUrl = "https://img.icons8.com/?size=100&id=AczAVUihwpMA&format=png&color=000000",
                    minAge = 16,
                )
            ),
            `18`(
                AgeRatingDto(
                    id = 5,
                    present = "18+",
                    iconUrl = "https://img.icons8.com/?size=100&id=8Utbqkpx2EVt&format=png&color=000000",
                    minAge = 18,
                )
            )
        }
    }

    private val kinoList: Map<String, KinoModelDto> = listOf(
        KinoModelDto(
            id = "5874b417-5417-4b01-be85-aa9f647bd35f",
            title = "Аватар",
            description = "Бывший морпех Джейк Салли прикован к инвалидному креслу. Несмотря на немощное тело, Джейк в душе по-прежнему остается воином. Он получает задание совершить путешествие в несколько световых лет к базе землян на планете Пандора, где корпорации добывают редкий минерал, имеющий огромное значение для выхода Земли из энергетического кризиса.",
            previewImage = "https://m.media-amazon.com/images/M/MV5BZDA0OGQxNTItMDZkMC00N2UyLTg3MzMtYTJmNjg3Nzk5MzRiXkEyXkFqcGdeQXVyMjUzOTY1NTc@._V1_FMjpg_UX1000_.jpg",
            releasedDate = Calendar.Builder().setDate(2009, 11, 18).build().time,
            byCountry = "USA",
            genres = listOf(
                DtoGenres.DRAMA.genre,
                DtoGenres.BOEVIK.genre,
                DtoGenres.FANTASY.genre,
                DtoGenres.TRAVEL.genre,
                DtoGenres.COMEDY.genre,
                DtoGenres.HORROR.genre,
                DtoGenres.MELODRAMA.genre
            ),
            duration = 3600 * 2,
            ageRating = AgeRatings.`18`.dto
        ),
        KinoModelDto(
            id = "cf65ed1d-b79a-460a-9606-1d2edaf3586c",
            title = "Последний ронин",
            description = "(Не)далекое будущее. В результате изменений климата, приведших к глобальной ядерной войне, цивилизация практически уничтожена: земли выжжены, города разрушены. Техника не работает, бензин давно потерял свои свойства. Главная валюта в этом мире — два патрона: 9 и 7,62 мм от АК-47. Преследуемый призраками прошлого одинокий путник Ронин странствует по пустошам в поисках убийцы своего отца, годами создавая карту континента. На каждом шагу его подстерегают охотники за головами и бандиты. Но однажды судьба сводит Ронина со своенравной девочкой-подростком Марией, которая обещает ему наивысшую плату, если он доведет ее до места, где она родилась.",
            previewImage = "https://avatars.mds.yandex.net/i?id=1508d997ad6dc85da055bbb434f34b73_l-10414746-images-thumbs&n=13",
            releasedDate = Calendar.Builder().setDate(2024, 6, 19).build().time,
            byCountry = "USA",
            duration = 3600 * 2,
            genres = listOf(DtoGenres.DRAMA.genre, DtoGenres.BOEVIK.genre),
            ageRating = AgeRatings.`18`.dto
        ),
        KinoModelDto(
            id = "ebb65720-2389-4a7e-9fe3-7d1458d240a0",
            title = "Постучись в мою дверь",
            description = "Эда живёт вместе со своей тётей и работает в семейном цветочном магазине. Она мечтает уехать на стажировку в Италию, но лишается стипендии, которую местное архитектурное бюро Art Life выделяет для талантливых студентов. Обидевшись, девушка решает отомстить его владельцу, молодому и талантливому архитектору Серкану Болату. Но их знакомство оборачивается неожиданным договором — Эда получит полностью оплаченную поездку в Италию, если 2 месяца будет притворяться девушкой Серкана, чтобы он смог вернуть свою бывшую.",
            previewImage = "https://yastatic.net/naydex/yandex-search/bTFy12402/d21c4fyqx/EVXP3tawtuggczhscKX0EJ-siEZ_6PFmTUAZlZURLVdixeCovuXQQnV1HKdUpF3F1J80-wV7eOyBOs0O8i53WehFq3IufH9Y6N-98IgFdGSP9aBqCoGFLqXs",
            releasedDate = Calendar.Builder().setDate(2022, 5, 24).build().time,
            byCountry = "Russia",
            genres = listOf(DtoGenres.DRAMA.genre, DtoGenres.COMEDY.genre),
            duration = (3600 * 0.5F).toInt(),
            ageRating = AgeRatings.`18`.dto
        ),
        KinoModelDto(
            id = "abb08203-ac9b-4467-9b76-dbb7d890e9b9",
            title = "Волшебник изумрудного города",
            description = "В далёком городе живёт девочка Элли. Однажды злая колдунья Гингема наколдовала ураган, который унёс Элли и ее собачку Тотошку в страну Жевунов. Чтобы вернуться домой, Элли вместе с новыми друзьями — Страшилой, Железным Дровосеком и Трусливым Львом — отправится по желтой кирпичной дороге в Изумрудный город на поиски Волшебника, который исполнит их заветные желания.",
            previewImage = "https://pic.rutubelist.ru/video/2025-01-06/6b/6d/6b6dc1541d3e46cedf0c1209b51fb86f.jpg",
            releasedDate = Calendar.Builder().setDate(2025, 1, 1).build().time,
            byCountry = "USA",
            duration = (3600 * 1.5).toInt(),
            genres = listOf(DtoGenres.DRAMA.genre, DtoGenres.TRAVEL.genre, DtoGenres.FANTASY.genre),
            ageRating = AgeRatings.`18`.dto

        ),
    ).associateBy { it.id }
    private val kinoSessionList by lazy {
        mutableListOf(
            //Аватар
            KinoSessionModel(
                kinoModel = kinoList["5874b417-5417-4b01-be85-aa9f647bd35f"]!!.map(
                    kinoModelMapper
                ),
                sessionStartDate = Calendar.getInstance().apply {
                    this.add(Calendar.HOUR, 1)
                }.time,
                sessionId = "db60116a-f83e-4b09-a3d0-5dadb1f179c8",
                hallName = Halls.HALL_R.info.hallName
            ),
            KinoSessionModel(
                kinoModel = kinoList["5874b417-5417-4b01-be85-aa9f647bd35f"]!!.map(
                    kinoModelMapper
                ),
                sessionStartDate = Calendar.getInstance().apply {
                    this.add(Calendar.HOUR, 2)
                }.time,
                sessionId = "50373c0c-e3cc-4f09-9772-fc36c5fb4628",
                hallName = Halls.HALL_G.info.hallName
            ),
            //third
            KinoSessionModel(
                kinoModel = kinoList["5874b417-5417-4b01-be85-aa9f647bd35f"]!!.map(
                    kinoModelMapper
                ),
                sessionStartDate = Calendar.getInstance().apply {
                    this.add(Calendar.HOUR, 2)
                    this.add(Calendar.MINUTE, 30)
                }.time,
                sessionId = "7a4f3094-a1c1-4be8-a462-d828d1ea792e",
                hallName = Halls.HALL_B.info.hallName
            ),
            //a-4
            KinoSessionModel(
                kinoModel = kinoList["5874b417-5417-4b01-be85-aa9f647bd35f"]!!.map(
                    kinoModelMapper
                ),
                sessionStartDate = Calendar.getInstance().apply {
                    this.add(Calendar.HOUR, 8)
                }.time,
                sessionId = "bc2f80b1-566b-4c04-aa78-a5f9161e1f24",
                hallName = Halls.HALL_R.info.hallName
            ),
            //a-5
            KinoSessionModel(
                kinoModel = kinoList["5874b417-5417-4b01-be85-aa9f647bd35f"]!!.map(
                    kinoModelMapper
                ),
                sessionStartDate = Calendar.getInstance().apply {
                    this.add(Calendar.HOUR, 12)
                }.time,
                sessionId = "3632ca1d-8784-454d-b103-eac719abd1f2",
                hallName = Halls.HALL_G.info.hallName
            ),
            //Ронин
            KinoSessionModel(
                kinoModel = kinoList["cf65ed1d-b79a-460a-9606-1d2edaf3586c"]!!.map(
                    kinoModelMapper
                ),
                sessionStartDate = Calendar.getInstance().apply {
                    this.add(Calendar.HOUR, 1)
                }.time,
                sessionId = "eb2772e7-beb1-42b9-838a-6b80d494935d",
                hallName = Halls.HALL_R.info.hallName
            ),
            KinoSessionModel(
                kinoModel = kinoList["cf65ed1d-b79a-460a-9606-1d2edaf3586c"]!!.map(
                    kinoModelMapper
                ),
                sessionStartDate = Calendar.getInstance().apply {
                    this.add(Calendar.HOUR, 1)
                }.time,
                sessionId = "4095449a-ce4f-467d-b3ed-0af6d0d3d2a6",
                hallName = Halls.HALL_G.info.hallName
            ),
            KinoSessionModel(
                kinoModel = kinoList["cf65ed1d-b79a-460a-9606-1d2edaf3586c"]!!.map(
                    kinoModelMapper
                ),
                sessionStartDate = Calendar.getInstance().apply {
                    this.add(Calendar.HOUR, 12)
                }.time,
                sessionId = "d5e8edd1-de15-45e7-9377-37bf7503a28c",
                hallName = Halls.HALL_B.info.hallName
            ),
            //Постучись в мою дверь
            KinoSessionModel(
                kinoModel = kinoList["ebb65720-2389-4a7e-9fe3-7d1458d240a0"]!!.map(
                    kinoModelMapper
                ),
                sessionStartDate = Calendar.getInstance().apply {
                    this.add(Calendar.HOUR, 2)
                }.time,
                sessionId = "5d7e1b9c-d0ba-4903-ba91-087dc179c28d",
                hallName = Halls.HALL_R.info.hallName
            ),
            KinoSessionModel(
                kinoModel = kinoList["ebb65720-2389-4a7e-9fe3-7d1458d240a0"]!!.map(
                    kinoModelMapper
                ),
                sessionStartDate = Calendar.getInstance().apply {
                    this.add(Calendar.DAY_OF_YEAR, 5)
                }.time,
                sessionId = "34b94e26-99e6-40e2-9f2e-05e263768a90",
                hallName = Halls.HALL_R.info.hallName
            ),
            KinoSessionModel(
                kinoModel = kinoList["ebb65720-2389-4a7e-9fe3-7d1458d240a0"]!!.map(
                    kinoModelMapper
                ),
                sessionStartDate = Calendar.getInstance().apply {
                    this.add(Calendar.DAY_OF_YEAR, 2)
                }.time,
                sessionId = "fd7edc7a-7a9b-46a3-885e-d4973cbe546c",
                hallName = Halls.HALL_G.info.hallName
            ),
            //Волшебник изумрудного города
            KinoSessionModel(
                kinoModel = kinoList["abb08203-ac9b-4467-9b76-dbb7d890e9b9"]!!.map(
                    kinoModelMapper
                ),
                sessionStartDate = Calendar.getInstance().apply {
                    this.add(Calendar.HOUR_OF_DAY, 9)
                }.time,
                sessionId = "db84083c-0f50-43f7-9b05-7b4882ae8073",
                hallName = Halls.HALL_R.info.hallName
            ),
        )
    }

    //session - sessionhall
    private val hallsBySessions: MutableMap<String, SessionHallInfoModel> by lazy {
        val kinoSessionListCached = kinoSessionList
        mutableMapOf(
            //avatar_first
            with("db60116a-f83e-4b09-a3d0-5dadb1f179c8") {
                this to createHall(info = Halls.entries.first { it.info.hallName == kinoSessionListCached.first { e -> e.sessionId == this }.hallName }.info,
                    owner = this,
                    price = 400,
                    busyByOtherList = listOf(
                        1, 4, 7, 12, 16, 19, 21, 25
                    ) + kinoTicketsDao.getTickets().filter { it.sessionId == this }
                        .map { it.placeIndex }

                )
            },
            //avatar_second
            with("50373c0c-e3cc-4f09-9772-fc36c5fb4628") {
                this to createHall(info = Halls.entries.first { it.info.hallName == kinoSessionListCached.first { e -> e.sessionId == this }.hallName }.info,
                    owner = this,
                    price = 500,
                    busyByOtherList = listOf(
                        2, 3, 9, 25, 25, 27, 41, 42
                    ) + kinoTicketsDao.getTickets().filter { it.sessionId == this }
                        .map { it.placeIndex })
            },
            //avatar_third
            with("7a4f3094-a1c1-4be8-a462-d828d1ea792e") {
                this to createHall(info = Halls.entries.first { it.info.hallName == kinoSessionListCached.first { e -> e.sessionId == this }.hallName }.info,
                    owner = this,
                    price = 500,
                    busyByOtherList = listOf(
                        51, 52, 53, 77, 78, 91, 92, 22, 21, 17, 4, 8
                    ) + kinoTicketsDao.getTickets().filter { it.sessionId == this }
                        .map { it.placeIndex })
            },
            //avatar fourth
            with("bc2f80b1-566b-4c04-aa78-a5f9161e1f24") {
                this to createHall(info = Halls.entries.first { it.info.hallName == kinoSessionListCached.first { e -> e.sessionId == this }.hallName }.info,
                    owner = this,
                    price = 500,
                    busyByOtherList = listOf(
                        41, 42, 43, 44, 51, 52, 53, 54, 1, 9, 13, 16
                    ) + kinoTicketsDao.getTickets().filter { it.sessionId == this }
                        .map { it.placeIndex })
            },
            //a-5
            with("3632ca1d-8784-454d-b103-eac719abd1f2") {
                this to createHall(info = Halls.entries.first { it.info.hallName == kinoSessionListCached.first { e -> e.sessionId == this }.hallName }.info,
                    owner = this,
                    price = 500,
                    busyByOtherList = listOf(
                        41, 42, 43, 44, 51, 52, 53, 54, 1, 9, 13, 16
                    ) + kinoTicketsDao.getTickets().filter { it.sessionId == this }
                        .map { it.placeIndex })
            },
            //ронин
            with("eb2772e7-beb1-42b9-838a-6b80d494935d") {
                this to createHall(info = Halls.entries.first { it.info.hallName == kinoSessionListCached.first { e -> e.sessionId == this }.hallName }.info,
                    owner = this,
                    price = 500,
                    busyByOtherList = listOf(
                        2, 4, 6, 8, 24, 36, 48, 60, 72, 84, 96, 0
                    ) + kinoTicketsDao.getTickets().filter { it.sessionId == this }
                        .map { it.placeIndex })
            },
            //ronin-2
            with("4095449a-ce4f-467d-b3ed-0af6d0d3d2a6") {
                this to createHall(info = Halls.entries.first { it.info.hallName == kinoSessionListCached.first { e -> e.sessionId == this }.hallName }.info,
                    owner = this,
                    price = 500,
                    busyByOtherList = listOf(
                        6, 9, 12, 15, 18, 24, 30, 33, 37, 90, 80, 71
                    ) + kinoTicketsDao.getTickets().filter { it.sessionId == this }
                        .map { it.placeIndex })
            },
            //ronin-3
            with("d5e8edd1-de15-45e7-9377-37bf7503a28c") {
                this to createHall(info = Halls.entries.first { it.info.hallName == kinoSessionListCached.first { e -> e.sessionId == this }.hallName }.info,
                    owner = this,
                    price = 500,
                    busyByOtherList = listOf(
                        4, 8, 12, 16, 20, 24, 28, 32, 36, 40, 44, 48, 52
                    ) + kinoTicketsDao.getTickets().filter { it.sessionId == this }
                        .map { it.placeIndex })
            },
            //стучись в дверь
            with("5d7e1b9c-d0ba-4903-ba91-087dc179c28d") {
                this to createHall(info = Halls.entries.first { it.info.hallName == kinoSessionListCached.first { e -> e.sessionId == this }.hallName }.info,
                    owner = this,
                    price = 500,
                    busyByOtherList = listOf(
                        5, 10, 15, 25, 30, 35, 40, 45
                    ) + List(5) {
                        45 + it * 5
                    } + kinoTicketsDao.getTickets().filter { it.sessionId == this }
                        .map { it.placeIndex })
            },
            //стучись в дверь - 2
            with("34b94e26-99e6-40e2-9f2e-05e263768a90") {
                this to createHall(info = Halls.entries.first { it.info.hallName == kinoSessionListCached.first { e -> e.sessionId == this }.hallName }.info,
                    owner = this,
                    price = 500,
                    busyByOtherList = List(13) {
                        it + it * 2 + it % 13
                    } + kinoTicketsDao.getTickets().filter { it.sessionId == this }
                        .map { it.placeIndex })
            },
            //стучись в дверь - 3
            with("fd7edc7a-7a9b-46a3-885e-d4973cbe546c") {
                this to createHall(info = Halls.entries.first { it.info.hallName == kinoSessionListCached.first { e -> e.sessionId == this }.hallName }.info,
                    owner = this,
                    price = 500,
                    busyByOtherList = List(17) {
                        it + it * 3 + it % 17
                    } + kinoTicketsDao.getTickets().filter { it.sessionId == this }
                        .map { it.placeIndex })
            },

            )
    }


    override suspend fun getAllAfishaKinoSessions(): List<KinoSessionModel> {
        delay(1000L)//имитация сетевого запроса
        return kinoSessionList
    }

    override suspend fun getAllAfishaKinos(): List<KinoModelDto> {
        delay(1000)//имитация сетевого запроса
        return kinoList.values.toList()
    }

    override suspend fun getHallInformationBySessionId(sessionId: String): SessionHallInfoModel {
        delay(1000L)//имитация сетевого запроса
        return hallsBySessions[sessionId]!!
    }

    override suspend fun buyTickets(
        sessionId: String, places: List<Int>
    ): Result<List<TicketModel>> {
        return kotlin.runCatching {
            if (places.isEmpty()) throw RuntimeException("empty")
            val hall = hallsBySessions[sessionId]!!
            val ticketList = mutableListOf<TicketModel>()
            hallsBySessions[sessionId] = hall.copy(places = hall.places.toMutableList().apply {
                for (index in places) {
                    if (this[index].isBusy) {
                        throw RuntimeException("занято")
                    }
                    this[index] = this[index].copy(isBusyByOther = true)
                    ticketList.add(
                        TicketModel(
                            ticketId = UUID.randomUUID().toString(),
                            sessionId = sessionId,
                            hallId = hall.hallId,
                            placeIndex = index
                        )
                    )
                }
            })
            ticketList
        }
    }
}