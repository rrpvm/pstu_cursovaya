package com.rrpvm.domain.model


sealed class FilterModel() {
    data class GenreFilter(val genre : String) : FilterModel() {
        override fun isFilmConstraint(film: KinoModel): Boolean {
            return true
        }
    }

    data class YearFilter(val year : Int) : FilterModel(){
        override fun isFilmConstraint(film: KinoModel): Boolean {
            return true
        }
    }
    data class CountryFilter(val country : String) : FilterModel(){
        override fun isFilmConstraint(film: KinoModel): Boolean {
            return true
        }
    }
    abstract fun isFilmConstraint(film : KinoModel):Boolean
}