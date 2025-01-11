package com.rrpvm.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rrpvm.data.room.entity.KinoSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface KinoSessionDao {

    @Query("SELECT * FROM kino_sessions")
    fun getKinoSessionList(): List<KinoSessionEntity>

    @Query("SELECT * FROM kino_sessions")
    fun getKinoSessionFlow(): Flow<List<KinoSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSession(session: KinoSessionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSession(sessionList: List<KinoSessionEntity>)
}