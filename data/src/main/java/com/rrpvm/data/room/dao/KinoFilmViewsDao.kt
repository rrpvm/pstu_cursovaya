package com.rrpvm.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rrpvm.data.room.entity.KinoFilmViewEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface KinoFilmViewsDao {
    @Query("SELECT viewed_kino_id FROM kino_film_views_table")
    fun getViewedKinoFilmIds(): Flow<List<String>>

    @Insert(entity = KinoFilmViewEntity::class, onConflict = OnConflictStrategy.ABORT)
    fun insertKinoView(view: KinoFilmViewEntity)


}