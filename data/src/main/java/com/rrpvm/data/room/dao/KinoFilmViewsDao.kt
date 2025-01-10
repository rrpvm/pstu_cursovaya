package com.rrpvm.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rrpvm.data.room.entity.KinoEntity
import com.rrpvm.data.room.entity.KinoFilmViewEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface KinoFilmViewsDao {
    @Query("SELECT viewed_kino_id FROM kino_film_views_table")
    fun getViewedKinoFilmIds(): Flow<List<String>>

    @Insert(entity = KinoFilmViewEntity::class)
    fun insertKinoView(view: KinoFilmViewEntity)

    @Query("SELECT * FROM kino_film_views_table LEFT JOIN kinos_table ON kino_id=viewed_kino_id")
    fun getViewedKinoFilms(): Flow<List<KinoEntity>>
}