package com.rrpvm.data.mapper._entity

import com.rrpvm.data.mapper.FromDomainDateStringMapper
import com.rrpvm.data.room.entity.query_model.KinoWithSessionsAndGenres
import com.rrpvm.domain.model.GenreModel
import com.rrpvm.domain.model.KinoModel

object KinoWithSessionsAndGenresToKinoModel : KinoWithSessionsAndGenres.Mapper<KinoModel> {
    override fun map(obj: KinoWithSessionsAndGenres): KinoModel {
        return KinoModel(
            id = obj.kinoWithSessions.kinoModel.kinoId,
            title = obj.kinoWithSessions.kinoModel.mTitle,
            description = obj.kinoWithSessions.kinoModel.mDescription,
            previewImage = obj.kinoWithSessions.kinoModel.mPreviewImage,
            releasedDate = FromDomainDateStringMapper.mapToDomainDate(obj.kinoWithSessions.kinoModel.mReleasedDate),
            genres = obj.kinoGenres.map { GenreModel(it.mGenreId, it.mGenreName) },
            byCountry = obj.kinoWithSessions.kinoModel.mCountry,
            isLiked = obj.kinoWithSessions.kinoModel.isLiked
        )
    }
}