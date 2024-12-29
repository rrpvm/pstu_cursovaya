package com.rrpvm.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rrpvm.data.room.dao.ClientDao
import com.rrpvm.data.room.entity.UserEntity

@Database(entities = [UserEntity::class], version = 2)
abstract class KinoZDatabase : RoomDatabase() {
    abstract fun getUserEntityDao(): ClientDao
}