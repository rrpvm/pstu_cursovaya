package com.rrpvm.domain.model

import com.rrpvm.domain.model.kino.KinoModel
import com.rrpvm.domain.util.toLocalDateTime


sealed class FilterModel() {
    data class GenreFilter(val genreId: String) : FilterModel() {
        override fun isFilmConstraint(film: KinoModel): Boolean {
            return film.genres.any { it.genreId == genreId }
        }
    }

    data class YearFilter(val year: Int) : FilterModel() {
        override fun isFilmConstraint(film: KinoModel): Boolean {
            return film.releasedDate.toLocalDateTime().year == year
        }
    }

    data class CountryFilter(val country: String) : FilterModel() {
        override fun isFilmConstraint(film: KinoModel): Boolean {
            return film.byCountry == country
        }
    }

    abstract fun isFilmConstraint(film: KinoModel): Boolean
}