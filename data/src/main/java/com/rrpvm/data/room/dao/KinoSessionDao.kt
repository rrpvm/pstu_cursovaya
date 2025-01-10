package com.rrpvm.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.rrpvm.data.room.entity.KinoSessionEntity
import com.rrpvm.data.room.entity.query_model.SessionsWithKino
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Dao
interface KinoSessionDao {
    //One to Many
    @Transaction
    @Query("SELECT * FROM kinos_table")
    fun getSessionsWithKinoFlow(): Flow<List<SessionsWithKino>>


    fun getSessionsWithKinoByOrderDateFlow(): Flow<List<SessionsWithKino>> {
        return getSessionsWithKinoFlow().map {
            it.asSequence()
                .filter { kinoWithSession ->
                    kinoWithSession.sessionList.isNotEmpty()
                }
                .sortedBy { kinoWithSession ->
                    kinoWithSession.sessionList.minByOrNull { session ->
                        session.sessionStartDate
                    }!!.sessionStartDate
                }.toList()
        }
    }


    @Query("SELECT * FROM kino_sessions")
    fun getKinoSessionList(): List<KinoSessionEntity>

    @Query("SELECT * FROM kino_sessions")
    fun getKinoSessionFlow(): Flow<List<KinoSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSession(session: KinoSessionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSession(sessionList: List<KinoSessionEntity>)
}