package com.rrpvm.kinofeed.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rrpvm.domain.repository.KinoRepository
import com.rrpvm.kinofeed.presentation.model.ActualKinoFeedItem
import com.rrpvm.kinofeed.presentation.model.FeedItemUi
import com.rrpvm.kinofeed.presentation.model.NewsKinoFeedItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(kinoRepository: KinoRepository) : ViewModel() {
    private val _mAdapterState = MutableStateFlow<List<FeedItemUi>>(emptyList())
    val mAdapterState = _mAdapterState.asStateFlow()

    init {
        viewModelScope.launch {
            _mAdapterState.value = listOf(
                ActualKinoFeedItem(),
                NewsKinoFeedItem()
            )
        }
    }

}