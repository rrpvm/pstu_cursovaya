package com.rrpvm.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rrpvm.data.room.entity.KinoEntity

@Dao
interface KinoDao {
    @Query("SELECT * FROM kinos_table")
    fun getKinoList(): List<KinoEntity>

    @Insert(entity = KinoEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertKinoList(list: List<KinoEntity>)

    @Update(entity = KinoEntity::class)
    fun updateKino(kinoEntity: KinoEntity)

}