package com.rrpvm.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.rrpvm.data.room.entity.KinoEntity
import com.rrpvm.data.room.entity.query_model.KinoWithGenres
import com.rrpvm.data.room.entity.query_model.KinoWithSessionsAndGenres
import com.rrpvm.data.room.entity.query_model.SessionWithKino
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Dao
interface KinoDao {
    @Query("DELETE from kinos_table")
    fun clearTable()

    @Query("SELECT * FROM kinos_table")
    fun getKinoList(): List<KinoEntity>

    @Query("SELECT * FROM kinos_table WHERE is_liked=1")
    fun getLikedKinoList(): Flow<List<KinoEntity>>

    @Query("SELECT * FROM kinos_table")
    @Transaction
    fun getKinoListFlow(): Flow<List<KinoWithGenres>>

    @Query("SELECT * FROM kinos_table WHERE kino_id = :kinoId")
    fun getKino(kinoId: String): KinoEntity?

    @Query("SELECT * FROM kinos_table WHERE kino_id=:kinoId")
    fun getFullKinoModel(kinoId: String): KinoWithSessionsAndGenres

    @Delete(entity = KinoEntity::class)
    fun deleteKinoList(list: List<KinoEntity>)

    //replace будет сбивать KinoSessionEntity на каскаде
    @Insert(entity = KinoEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertKinoList(list: List<KinoEntity>)

    //replace будет сбивать KinoSessionEntity на каскаде
    @Insert(entity = KinoEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertKino(kino: KinoEntity)

    @Update(entity = KinoEntity::class)
    fun updateKino(kinoEntity: KinoEntity)

    @Update(entity = KinoEntity::class)
    fun updateKinoList(kinoList: List<KinoEntity>)

    @Transaction
    fun fullUpdateKinoList(newList: List<KinoEntity>) {
        val newData: Map<String, KinoEntity> = newList.associateBy { e -> e.kinoId }
        val oldList = getKinoList()
        val toRemove = oldList.filter { oldItem ->
            newData[oldItem.kinoId] == null //новый список не содержит старого
        }
        //уже существующие на обнову
        val existedUpdatedList = (oldList - toRemove).associateBy { e -> e.kinoId }.toMutableMap()
            .mapValues { it ->
                checkNotNull(newData[it.key])
            }

        val uniqueList =
            newList.filter { newItem -> existedUpdatedList[newItem.kinoId] == null }//новые уникальные
        deleteKinoList(toRemove)//очистили ненужные
        updateKinoList(existedUpdatedList.values.toList())
        insertKinoList(uniqueList)

    }

    //One to Many
    @Transaction
    @Query("SELECT * FROM kinos_table")
    fun getKinoWithSessionAndGenres(): Flow<List<KinoWithSessionsAndGenres>>

    @Query("SELECT * FROM kinos_table LEFT JOIN kino_film_views_table ON kinos_table.kino_id=kino_film_views_table.viewed_kino_id WHERE kino_id=viewed_kino_id")
    @Transaction
    fun getFullViewedKinoFilms(): Flow<List<KinoWithGenres>>

    @Query("SELECT * FROM kinos_table LEFT JOIN kino_film_views_table ON kinos_table.kino_id=kino_film_views_table.viewed_kino_id WHERE kino_id=viewed_kino_id")
    @Transaction
    fun getBaseViewedKinoFilms(): Flow<List<KinoEntity>>

    @Query("SELECT * FROM kino_sessions LEFT JOIN kinos_table ON kino_sessions.bound_kino_id=kinos_table.kino_id WHERE sessionId=:sessionId")
    fun getSessionWithKinoBySessionId(sessionId: String): SessionWithKino


    fun getSessionsWithKinoByOrderDateFlow(): Flow<List<KinoWithSessionsAndGenres>> {
        return getKinoWithSessionAndGenres().map {
            it.asSequence()
                .filter { kinoWithSessionAndGenres ->
                    kinoWithSessionAndGenres.kinoWithSessions.sessionList.isNotEmpty()
                }
                .sortedBy { kinoWithSessionsAndGenres ->
                    kinoWithSessionsAndGenres.kinoWithSessions.sessionList.minByOrNull { session ->
                        session.sessionStartDate
                    }!!.sessionStartDate
                }.toList()
        }
    }
}