package com.rrpvm.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rrpvm.data.room.dao.AgeRatingDao
import com.rrpvm.data.room.dao.ClientDao
import com.rrpvm.data.room.dao.KinoDao
import com.rrpvm.data.room.dao.KinoFilmViewsDao
import com.rrpvm.data.room.dao.KinoGenresDao
import com.rrpvm.data.room.dao.KinoSessionDao
import com.rrpvm.data.room.dao.TicketsDao
import com.rrpvm.data.room.entity.AgeRatingEntity
import com.rrpvm.data.room.entity.KinoEntity
import com.rrpvm.data.room.entity.KinoFilmViewEntity
import com.rrpvm.data.room.entity.KinoGenreCrossRefEntity
import com.rrpvm.data.room.entity.KinoGenreEntity
import com.rrpvm.data.room.entity.KinoSessionEntity
import com.rrpvm.data.room.entity.TicketEntity
import com.rrpvm.data.room.entity.UserEntity

@Database(
    entities = [UserEntity::class, KinoEntity::class, KinoSessionEntity::class, KinoFilmViewEntity::class, KinoGenreEntity::class, KinoGenreCrossRefEntity::class, AgeRatingEntity::class,TicketEntity::class],
    version = 1
)
abstract class KinoZDatabase : RoomDatabase() {
    abstract fun getUserEntityDao(): ClientDao
    abstract fun getKinoEntityDao(): KinoDao
    abstract fun getKinoSessionEntityDao(): KinoSessionDao
    abstract fun getKinoFilmsViewsDao(): KinoFilmViewsDao
    abstract fun getKinoGenresDao(): KinoGenresDao
    abstract fun getAgeRatingDao() : AgeRatingDao
    abstract fun getTicketsDao() : TicketsDao
}