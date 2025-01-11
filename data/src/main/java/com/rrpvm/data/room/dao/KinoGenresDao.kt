package com.rrpvm.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.rrpvm.data.room.entity.KinoEntity
import com.rrpvm.data.room.entity.KinoGenreCrossRefEntity
import com.rrpvm.data.room.entity.KinoGenreEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface KinoGenresDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertKinoGenres(kinoGenreList: List<KinoGenreEntity>)

    @Query("DELETE FROM kino_genre")
    fun clearGenresTable()

    @Query("SELECT * FROM kino_genre")
    fun observeAllGenres(): Flow<List<KinoGenreEntity>>

    @Transaction
    fun setKinoGenres(genreList: List<KinoGenreEntity>) {
        clearGenresTable()
        insertKinoGenres(kinoGenreList = genreList)
    }
    @Insert
    fun insertKinoGenreCrossRef(kinoGenreCrossRefEntity: KinoGenreCrossRefEntity)
}