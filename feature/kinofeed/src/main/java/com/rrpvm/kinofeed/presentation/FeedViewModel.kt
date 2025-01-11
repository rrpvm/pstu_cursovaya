package com.rrpvm.kinofeed.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rrpvm.core.TAG
import com.rrpvm.domain.contract.KinoModelFilterContract
import com.rrpvm.domain.model.FilterModel
import com.rrpvm.domain.model.KinoModel
import com.rrpvm.domain.repository.FilterRepository
import com.rrpvm.domain.repository.KinoRepository
import com.rrpvm.kinofeed.presentation.listener.ActualFeedItemListener
import com.rrpvm.kinofeed.presentation.listener.SeenFeedItemListener
import com.rrpvm.kinofeed.presentation.model.ActualKinoFeedItem
import com.rrpvm.kinofeed.presentation.model.FeedItemUi
import com.rrpvm.kinofeed.presentation.model.MainFeedViewEffect
import com.rrpvm.kinofeed.presentation.model.PickDateModeUi
import com.rrpvm.kinofeed.presentation.model.SeenKinoFeedItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val kinoRepository: KinoRepository,
    private val filterRepository: FilterRepository
) : ViewModel(),
    ActualFeedItemListener, SeenFeedItemListener {
    private val cre = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.e(TAG, "FeedViewModel:: $throwable")
    }
    private val _viewScreenEffects = MutableSharedFlow<MainFeedViewEffect>()
    val viewScreenEffects = _viewScreenEffects.asSharedFlow()
    private val actualFeedState =
        MutableStateFlow(
            ActualKinoFeedItem(
                kinoList = emptyList(),
                dateMode = PickDateModeUi.TODAY
            )
        )
    private val seenFeedState = MutableStateFlow(SeenKinoFeedItem(viewedKinoList = emptyList()))
    private val _mAdapterLoadingState = MutableStateFlow(true)

    val mAdapterState =
        combine(
            actualFeedState,
            seenFeedState,
            _mAdapterLoadingState
        ) { actualKinoFeedItem, seenKinoFeedItem, isLoadingNetwork ->
            val builder = mutableListOf<FeedItemUi>()
            if (!isLoadingNetwork || actualKinoFeedItem.kinoList.isNotEmpty()) {
                //если мы не фетчим данные или список не пустой -> можем отобразить айтем с прокручиванием
                builder.add(actualKinoFeedItem)
            }
            if (seenKinoFeedItem.viewedKinoList.isNotEmpty()) {
                builder.add(seenKinoFeedItem)
            }
            return@combine builder.takeIf { it.isNotEmpty() } ?: run {
                Log.e("null", "${actualKinoFeedItem.kinoList}")
                return@combine null
            }
        }
            .flowOn(Dispatchers.Default + cre)
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    //стейт плейсхолдера при первой загрузке
    val mAdapterLoadingState =
        _mAdapterLoadingState.combine(mAdapterState) { isLoading, adapterState ->
            return@combine isLoading && adapterState == null
        }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    //стейт верхнего индикатора обновления
    val networkFetchState =
        _mAdapterLoadingState.combine(mAdapterState) { fetchRequest, adapterState ->
            return@combine fetchRequest && adapterState.isNullOrEmpty().not()
        }

    init {
        fetchKinoFeed()
        observeActualFeedState()
        observeViewedFeedState()
    }

    override fun onRetryFetchData() {
        fetchKinoFeed()
    }

    fun onGenreFilterClicked() {
        viewModelScope.launch {
            _viewScreenEffects.emit(MainFeedViewEffect.OpenGenresFilter)
        }
    }

    fun onYearFilterClicked() {
        viewModelScope.launch {
            _viewScreenEffects.emit(MainFeedViewEffect.OpenYearFilter)
        }
    }

    fun onCountryFilterClicked() {
        viewModelScope.launch {
            _viewScreenEffects.emit(MainFeedViewEffect.OpenCountryFilter)
        }
    }

    override fun onShiftLeft() {
        val currentDateMode = actualFeedState.value.dateMode
        val currentIndex = PickDateModeUi.entries.indexOf(currentDateMode)
        if (currentIndex == 0) return
        actualFeedState.value =
            actualFeedState.value.copy(dateMode = PickDateModeUi.entries[currentIndex - 1])
        observeActualFeedState()
    }

    override fun onShiftRight() {
        val currentDateMode = actualFeedState.value.dateMode
        val currentIndex = PickDateModeUi.entries.indexOf(currentDateMode)
        if (currentIndex == PickDateModeUi.entries.size - 1) return
        actualFeedState.value =
            actualFeedState.value.copy(dateMode = PickDateModeUi.entries[currentIndex + 1])
        observeActualFeedState()
    }

    override fun onKinoSelected(kinoId: String) {
        viewModelScope.launch(Dispatchers.IO + cre) {
            _viewScreenEffects.emit(MainFeedViewEffect.GoToKinoDetail(kinoId))
            kinoRepository.viewKino(kinoId).getOrThrow()
        }
    }

    override fun onItemSelected(kinoId: String) {
        viewModelScope.launch(cre) {
            _viewScreenEffects.emit(MainFeedViewEffect.GoToKinoDetail(kinoId))
        }
    }

    private fun fetchKinoFeed() {
        viewModelScope.launch(Dispatchers.IO + cre) {
            _mAdapterLoadingState.value = true
            kinoRepository.fetchKinoFeed().onFailure {
                it.printStackTrace()
            }
        }.invokeOnCompletion {
            _mAdapterLoadingState.value = false
        }
    }

    private var observeActualFeedStateJob: Job? = null
    private fun observeActualFeedState() {
        observeActualFeedStateJob?.cancel()
        observeActualFeedStateJob = viewModelScope.launch(Dispatchers.Default + cre) {
            val now = Calendar.getInstance().time
            val endTime = when (actualFeedState.value.dateMode) {
                PickDateModeUi.TODAY -> {
                    Date(
                        LocalDate.now().atTime(23, 59, 59, 999).atZone(ZoneId.systemDefault())
                            .toInstant()
                            .toEpochMilli()
                    )
                }

                PickDateModeUi.TOMORROW -> {
                    Date(
                        LocalDate.now().plusDays(1)
                            .atTime(23, 59, 59, 999)
                            .atZone(ZoneId.systemDefault())
                            .toInstant()
                            .toEpochMilli()
                    )
                }

                PickDateModeUi.AT_WEEK -> {
                    Date(
                        LocalDate.now().plusDays(7).atTime(23, 59, 59, 999)
                            .atZone(ZoneId.systemDefault())
                            .toInstant()
                            .toEpochMilli()
                    )
                }
            }

            kinoRepository.getKinoFilmsByDateConstraintSessionDate(now, endTime)
                .distinctUntilChanged()
                .combine(filterRepository.getFilters()) { unfilteredList, filters ->
                    unfilteredList.filter { unfiliteredItem ->
                        KinoModelFilterContract.checkKinoModel(
                            kinoModel = unfiliteredItem,
                            filterList = filters
                        )
                    }
                }
                .collectLatest { films: List<KinoModel> ->
                    actualFeedState.value =
                        ActualKinoFeedItem(
                            kinoList = films,
                            dateMode = actualFeedState.value.dateMode
                        )
                }
        }
    }

    private fun observeViewedFeedState() {
        viewModelScope.launch(Dispatchers.Default + cre) {
            kinoRepository.getKinoFilmsViewed().collectLatest {
                seenFeedState.value = SeenKinoFeedItem(it)
            }
        }
    }

}