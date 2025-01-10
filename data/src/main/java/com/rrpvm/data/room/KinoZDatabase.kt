package com.rrpvm.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rrpvm.data.room.dao.ClientDao
import com.rrpvm.data.room.dao.KinoDao
import com.rrpvm.data.room.dao.KinoFilmViewsDao
import com.rrpvm.data.room.dao.KinoSessionDao
import com.rrpvm.data.room.entity.KinoEntity
import com.rrpvm.data.room.entity.KinoFilmViewEntity
import com.rrpvm.data.room.entity.KinoSessionEntity
import com.rrpvm.data.room.entity.UserEntity

@Database(entities = [UserEntity::class,KinoEntity::class,KinoSessionEntity::class,KinoFilmViewEntity::class], version = 1)
abstract class KinoZDatabase : RoomDatabase() {
    abstract fun getUserEntityDao(): ClientDao
    abstract fun getKinoEntityDao() : KinoDao
    abstract fun getKinoSessionEntityDao() : KinoSessionDao
    abstract fun getKinoFilmsViewsDao() : KinoFilmViewsDao

}