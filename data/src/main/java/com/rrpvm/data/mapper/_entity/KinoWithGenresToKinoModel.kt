package com.rrpvm.data.mapper._entity

import com.rrpvm.data.mapper.FromDomainDateStringMapper
import com.rrpvm.data.room.entity.query_model.KinoWithGenres
import com.rrpvm.domain.model.GenreModel
import com.rrpvm.domain.model.KinoModel

object KinoWithGenresToKinoModel : KinoWithGenres.Mapper<KinoModel> {
    override fun map(obj: KinoWithGenres): KinoModel {
        return KinoModel(
            id = obj.kino.kinoId,
            title = obj.kino.mTitle,
            description = obj.kino.mDescription,
            previewImage = obj.kino.mPreviewImage,
            releasedDate = FromDomainDateStringMapper.mapToDomainDate(obj.kino.mReleasedDate),
            genres = obj.genres.map {
                GenreModel(it.mGenreId, it.mGenreName)
            }, byCountry = obj.kino.mCountry,
            isLiked = obj.kino.isLiked,
            ageRatingId = obj.kino.ageRatingId,
            duration = obj.kino.mDuration
        )
    }
}