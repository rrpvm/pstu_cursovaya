package com.rrpvm.domain.contract

import com.rrpvm.domain.model.FilterModel
import com.rrpvm.domain.model.kino.KinoModel

object  KinoModelFilterContract {
    fun checkKinoModel(kinoModel: KinoModel, filterList : List<FilterModel>):Boolean{
        var isSatisfiesCountryFilter: Boolean? = null
        var isSatisfiesGenreFilter: Boolean? = null
        var isSatisfiesYearFilter: Boolean? = null
        filterList.forEach { filter ->
            when (filter) {
                is FilterModel.CountryFilter -> {
                    isSatisfiesCountryFilter = (isSatisfiesCountryFilter
                        ?: false) or filter.isFilmConstraint(kinoModel)
                }

                is FilterModel.GenreFilter -> {
                    isSatisfiesGenreFilter = (isSatisfiesGenreFilter
                        ?: false) or filter.isFilmConstraint(kinoModel)
                }

                is FilterModel.YearFilter -> {
                    isSatisfiesYearFilter =
                        (isSatisfiesYearFilter ?: false) or filter.isFilmConstraint(
                            kinoModel
                        )
                }
            }
        }
       return isSatisfiesCountryFilter != false && isSatisfiesGenreFilter != false && isSatisfiesYearFilter != false
    }
}