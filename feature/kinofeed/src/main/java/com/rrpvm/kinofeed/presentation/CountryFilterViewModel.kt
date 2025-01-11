package com.rrpvm.kinofeed.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rrpvm.domain.model.FilterModel
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
class CountryFilterViewModel @Inject constructor(
    kinoRepository: KinoRepository,
    private val filterRepository: FilterRepository
) : ViewModel() {
    private val dirtyCountryFilterList = kinoRepository.getAllKinoFilmsWithAnyActualSession().map {
        return@map it.map { kino -> kino.byCountry }.toSet()
    }.flowOn(Dispatchers.IO).stateIn(viewModelScope, SharingStarted.Eagerly, setOf())
    val genresAdapterList =
        dirtyCountryFilterList.combine(filterRepository.getFilterBy(FilterModel.CountryFilter::class.java)) { countryList, filterList ->
            val filterMap = filterList.associateBy { it.country }
            countryList.map { country: String ->
                FilterItem(
                    value = country,
                    isEnabled = filterMap[country] != null
                )
            }
        }.flowOn(Dispatchers.IO).stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())


    fun onFilterItemStateChanged(label: String, filterState: Boolean) {
        if (filterState)
            filterRepository.addFilter(FilterModel.CountryFilter(label))
        else filterRepository.removeFilter(FilterModel.CountryFilter(label))
    }
}