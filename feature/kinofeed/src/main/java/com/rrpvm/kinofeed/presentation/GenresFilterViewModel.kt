package com.rrpvm.kinofeed.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rrpvm.domain.model.FilterModel
import com.rrpvm.domain.model.GenreModel
import com.rrpvm.domain.repository.FilterRepository
import com.rrpvm.domain.repository.KinoRepository
import com.rrpvm.kinofeed.domain.FilterItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class GenresFilterViewModel @Inject constructor(
    private val kinoRepository: KinoRepository,
    private val filterRepository: FilterRepository
) : ViewModel() {
    private val dirtyGenresList = kinoRepository.getKinoGenres().map {
        return@map it.toSet()
    }.flowOn(Dispatchers.IO).stateIn(viewModelScope, SharingStarted.Eagerly, setOf())
    val genresAdapterList =
        dirtyGenresList.combine(filterRepository.getFilterBy(FilterModel.GenreFilter::class.java)) { genreModelList, filterList ->
            genreModelList.map { genreModel: GenreModel ->
                FilterItem(
                    value = genreModel.title,
                    isEnabled = filterList.any { it.genreId == genreModel.genreId })
            }
        }.flowOn(Dispatchers.IO).stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())


    fun onFilterItemStateChanged(label: String, filterState: Boolean) {
        val genreId = dirtyGenresList.value.firstOrNull { it.title == label }?.genreId ?: return
        if (filterState)
            filterRepository.addFilter(FilterModel.GenreFilter(genreId))
        else filterRepository.removeFilter(FilterModel.GenreFilter(genreId))
    }
}