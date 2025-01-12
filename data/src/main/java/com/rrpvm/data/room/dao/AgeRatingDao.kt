package com.rrpvm.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.rrpvm.data.room.entity.AgeRatingEntity

@Dao
interface AgeRatingDao {
    @Insert(entity = AgeRatingEntity::class)
    fun insertAgeRatings(list: List<AgeRatingEntity>)

    @Query("DELETE FROM age_ratings")
    fun deleteAll()

    @Query("SELECT * FROM age_ratings WHERE id=:ageRatingId")
    fun getAgeRatingById(ageRatingId: Int): AgeRatingEntity

    @Transaction
    fun fullUpdateAgeRatings(list: List<AgeRatingEntity>) {
        deleteAll()
        insertAgeRatings(list)
    }
}