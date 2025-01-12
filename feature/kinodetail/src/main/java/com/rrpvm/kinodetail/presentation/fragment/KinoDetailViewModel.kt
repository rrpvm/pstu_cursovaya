package com.rrpvm.kinodetail.presentation.fragment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rrpvm.domain.repository.AgeRatingRepository
import com.rrpvm.domain.repository.KinoRepository
import com.rrpvm.kinodetail.presentation.model.detail.FullDetailKinoModelUi
import com.rrpvm.kinodetail.presentation.model.detail.KinoDetailViewData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

sealed class KinoDetailScreenEffect {
    data class GoBuyTicket(val sessionId: String) : KinoDetailScreenEffect()
}

@HiltViewModel
class KinoDetailViewModel @Inject constructor(
    savedInstance: SavedStateHandle,
    private val repository: KinoRepository,
    private val ageRatingRepository: AgeRatingRepository
) : ViewModel() {
    private val _screenState =
        MutableStateFlow<KinoDetailViewData>(KinoDetailViewData.ScreenLoading)
    private val _screenEffect = MutableSharedFlow<KinoDetailScreenEffect>()
    val screenEffect = _screenEffect.asSharedFlow()


    internal val screenState = _screenState.asStateFlow()
    val genresAdapterData = _screenState.filterIsInstance<KinoDetailViewData.Success>().map {
        return@map it.kino.base.kinoModel.genres.sortedBy { genre ->
            genre.title
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val sessionsAdapterData = _screenState.filterIsInstance<KinoDetailViewData.Success>().map {
        return@map it.kino.base.sessions.asSequence()
            .filter { shortSessionModel ->
                shortSessionModel.sessionDate.time >= Calendar.getInstance().time.time
            }.sortedBy { e -> e.sessionDate }.toList()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            delay(1000L)//имитируем полезность
            repository.getKinoById(savedInstance.get<String>("kinoId") ?: run {
                _screenState.value = KinoDetailViewData.ScreenFail
                return@launch
            }).onSuccess {
                val ageRatingModel = ageRatingRepository.getAgeRatingById(it.kinoModel.ageRatingId)

                _screenState.value = KinoDetailViewData.Success(
                    FullDetailKinoModelUi(
                        base = it,
                        ageRatingModel = ageRatingModel.getOrNull()
                    )
                )
            }.onFailure {
                _screenState.value = KinoDetailViewData.ScreenFail
            }
        }
    }

    fun onLike() {
        viewModelScope.launch(Dispatchers.IO) {
            //так делать нельзя, но время поджимает
            val prevResult = screenState.value as KinoDetailViewData.Success
            repository.doLike(prevResult.kino.base.kinoModel.id)
                .onSuccess { updatedKinoMode ->
                    _screenState.value = KinoDetailViewData.Success(
                        FullDetailKinoModelUi(
                            base = prevResult.kino.base.copy(kinoModel = updatedKinoMode),
                            prevResult.kino.ageRatingModel
                        )
                    )
                }.onFailure {
                    it.printStackTrace()
                }
        }
    }

    fun onBuyTicket(sessionId: String) {
        viewModelScope.launch {
            _screenEffect.emit(KinoDetailScreenEffect.GoBuyTicket(sessionId))
        }
    }

}