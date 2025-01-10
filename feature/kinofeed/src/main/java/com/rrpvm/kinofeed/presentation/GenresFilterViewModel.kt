package com.rrpvm.kinofeed.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rrpvm.domain.model.KinoModel
import com.rrpvm.domain.repository.FilterRepository
import com.rrpvm.domain.repository.KinoRepository
import com.rrpvm.kinofeed.domain.FilterItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class GenresFilterViewModel @Inject constructor(
    kinoRepository: KinoRepository,
    filterRepository: FilterRepository
) : ViewModel() {
    val genresAdapterList = kinoRepository.getKinoFilmsViewed().map {
        it.map { kinoModel: KinoModel ->
            FilterItem("боевик", false)
        }.toSet().toList()
    }.flowOn(Dispatchers.IO).stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())


    fun onFilterItemStateChanged(filterLabel: String, filterState: Boolean) {

    }
}