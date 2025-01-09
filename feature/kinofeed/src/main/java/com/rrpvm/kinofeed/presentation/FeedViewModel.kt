package com.rrpvm.kinofeed.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rrpvm.core.TAG
import com.rrpvm.domain.model.KinoModel
import com.rrpvm.domain.repository.KinoRepository
import com.rrpvm.kinofeed.presentation.model.ActualKinoFeedItem
import com.rrpvm.kinofeed.presentation.model.FeedItemUi
import com.rrpvm.kinofeed.presentation.model.SeenKinoFeedItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(private val kinoRepository: KinoRepository) : ViewModel() {
    private val cre = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.e(TAG, "FeedViewModel:: $throwable")
    }
    private val actualFeedState = MutableStateFlow(ActualKinoFeedItem(emptyList()))
    private val seenFeedState = MutableStateFlow<SeenKinoFeedItem?>(null)
    private val _mAdapterLoadingState = MutableStateFlow(true)
    val mAdapterState =
        combine(actualFeedState, seenFeedState) { actualKinoFeedItem, seenKinoFeedItem ->
            val builder = mutableListOf<FeedItemUi>()
            if (actualKinoFeedItem.kinoList.isNotEmpty()) builder.add(actualKinoFeedItem)
            if (seenKinoFeedItem != null) builder.add(seenKinoFeedItem)
            return@combine builder.takeIf { it.isNotEmpty() } ?: run {
                Log.e("null", "${actualKinoFeedItem.kinoList}")
                return@combine null
            }
        }.flowOn(Dispatchers.Default + cre)
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val mAdapterLoadingState =
        _mAdapterLoadingState.combine(mAdapterState) { networkFetch, adapterData ->
            return@combine networkFetch || adapterData?.isNotEmpty() == true
        }.flowOn(Dispatchers.Default + cre)
    val mEmptyScreenHolderVisible =
        mAdapterLoadingState.combine(mAdapterState) { isLoading, adapterState ->
            return@combine !isLoading && adapterState.isNullOrEmpty()
        }.flowOn(Dispatchers.Default + cre)

    init {
        fetchKinoFeed()
        observeActualFeedState()
    }

    fun onRetryFetch() {
        fetchKinoFeed()
    }

    private fun fetchKinoFeed() {
        viewModelScope.launch(Dispatchers.IO + cre) {
            _mAdapterLoadingState.value = true
            kinoRepository.fetchKinoFeed()
        }.invokeOnCompletion {
            _mAdapterLoadingState.value = false
        }
    }

    private fun observeActualFeedState() {
        viewModelScope.launch(Dispatchers.Default + cre) {
            kinoRepository.getKinoFilmsByMinSessionDate(Calendar.getInstance().time)
                .distinctUntilChanged()
                .collectLatest { films: List<KinoModel> ->
                    actualFeedState.value = ActualKinoFeedItem(films)
                }
        }
    }

}