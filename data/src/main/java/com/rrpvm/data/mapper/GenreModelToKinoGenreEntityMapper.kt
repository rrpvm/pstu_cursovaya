package com.rrpvm.data.mapper

import com.rrpvm.data.room.entity.KinoGenreEntity
import com.rrpvm.domain.model.GenreModel

object GenreModelToKinoGenreEntityMapper : GenreModel.Mapper<KinoGenreEntity> {
    override fun map(obj: GenreModel): KinoGenreEntity {
        return KinoGenreEntity(obj.genreId, obj.title)
    }
}