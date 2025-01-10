package com.rrpvm.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.rrpvm.data.room.entity.KinoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface KinoDao {
    @Query("SELECT * FROM kinos_table")
    fun getKinoList(): List<KinoEntity>
    @Query("SELECT * FROM kinos_table")
    fun getKinoListFlow(): Flow<List<KinoEntity>>

    @Query("SELECT * FROM kinos_table WHERE kino_id = :kinoId")
    fun getKino(kinoId: String): KinoEntity?

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
}