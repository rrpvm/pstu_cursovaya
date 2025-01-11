package com.rrpvm.kinodetail.presentation.fragment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rrpvm.domain.repository.KinoRepository
import com.rrpvm.kinodetail.presentation.model.KinoDetailViewData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KinoDetailViewModel @Inject constructor(
    savedInstance: SavedStateHandle,
    private val repository: KinoRepository
) : ViewModel() {
    private val _screenState =
        MutableStateFlow<KinoDetailViewData>(KinoDetailViewData.ScreenLoading)
    val screenState = _screenState.asStateFlow()
    val genresAdapterData = _screenState.filterIsInstance<KinoDetailViewData.Success>().map {
        return@map it.kino.kinoModel.genres
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        viewModelScope.launch(Dispatchers.Default) {
            delay(1000L)//имитируем полезность
            repository.getKinoById(savedInstance.get<String>("kinoId") ?: run {
                _screenState.value = KinoDetailViewData.ScreenFail
                return@launch
            }).onSuccess {
                _screenState.value = KinoDetailViewData.Success(it)
            }.onFailure {
                _screenState.value = KinoDetailViewData.ScreenFail
            }
        }
    }
}