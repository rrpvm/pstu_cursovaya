package com.rrpvm.kinofeed.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rrpvm.domain.model.FilterModel
import com.rrpvm.domain.repository.FilterRepository
import com.rrpvm.domain.repository.KinoRepository
import com.rrpvm.domain.util.toLocalDateTime
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
class YearFilterViewModel @Inject constructor(
    private val kinoRepository: KinoRepository,
    private val filterRepository: FilterRepository
) : ViewModel() {
    private val dirtyEntities = kinoRepository.getAllKinoFilmsWithAnyActualSession().map {
        return@map it.map { e ->
            e.releasedDate.toLocalDateTime().year
        }.toSet()
    }.flowOn(Dispatchers.IO).stateIn(viewModelScope, SharingStarted.Eagerly, setOf())
    val yearAdapterList =
        dirtyEntities.combine(filterRepository.getFilterBy(FilterModel.YearFilter::class.java)) { entities, filterList ->
            val map = filterList.associateBy { it.year }
            entities.map { ent ->
                FilterItem(
                    value = ent.toString(),
                    isEnabled = map[ent] != null
                )
            }
        }.flowOn(Dispatchers.IO).stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())


    fun onFilterItemStateChanged(label: String, filterState: Boolean) {
        runCatching {
            if (filterState)
                filterRepository.addFilter(FilterModel.YearFilter(label.toInt()))
            else filterRepository.removeFilter(FilterModel.YearFilter(label.toInt()))
        }
    }
}