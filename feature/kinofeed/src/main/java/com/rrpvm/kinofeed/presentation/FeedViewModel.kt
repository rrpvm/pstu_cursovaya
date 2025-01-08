package com.rrpvm.kinofeed.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rrpvm.domain.model.KinoModel
import com.rrpvm.domain.model.KinoSessionModel
import com.rrpvm.domain.repository.KinoRepository
import com.rrpvm.kinofeed.presentation.model.ActualKinoFeedItem
import com.rrpvm.kinofeed.presentation.model.FeedItemUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(kinoRepository: KinoRepository) : ViewModel() {
    private val _mAdapterState = MutableStateFlow<List<FeedItemUi>>(emptyList())
    val mAdapterState = _mAdapterState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            kinoRepository.fetchKinoFeed()
            val flow = kinoRepository.getKinoSessions(Date())
            flow.collectLatest { sessions: List<KinoSessionModel> ->
                _mAdapterState.value = listOf(ActualKinoFeedItem(
                    kinoList = sessions.map { session ->
                        session.kinoModel
                    }
                ))
            }
        }
        viewModelScope.launch {
            _mAdapterState.value = listOf(
                /*ActualKinoFeedItem(
                    listOf(
                        KinoModel(
                            1,
                            "Обосраться",
                            "",
                            "https://m.media-amazon.com/images/M/MV5BZDA0OGQxNTItMDZkMC00N2UyLTg3MzMtYTJmNjg3Nzk5MzRiXkEyXkFqcGdeQXVyMjUzOTY1NTc@._V1_FMjpg_UX1000_.jpg"
                        ),
                        KinoModel(
                            2,
                            "Обосраться2",
                            "",
                            "https://m.media-amazon.com/images/M/MV5BZDA0OGQxNTItMDZkMC00N2UyLTg3MzMtYTJmNjg3Nzk5MzRiXkEyXkFqcGdeQXVyMjUzOTY1NTc@._V1_FMjpg_UX1000_.jpg"
                        ),
                        KinoModel(
                            3,
                            "Обосраться3",
                            "",
                            "https://m.media-amazon.com/images/M/MV5BZDA0OGQxNTItMDZkMC00N2UyLTg3MzMtYTJmNjg3Nzk5MzRiXkEyXkFqcGdeQXVyMjUzOTY1NTc@._V1_FMjpg_UX1000_.jpg"
                        ),
                        KinoModel(
                            4,
                            "Обосраться4",
                            "",
                            "https://m.media-amazon.com/images/M/MV5BZDA0OGQxNTItMDZkMC00N2UyLTg3MzMtYTJmNjg3Nzk5MzRiXkEyXkFqcGdeQXVyMjUzOTY1NTc@._V1_FMjpg_UX1000_.jpg"
                        ),
                        KinoModel(
                            5,
                            "Обосраться5",
                            "",
                            "https://m.media-amazon.com/images/M/MV5BZDA0OGQxNTItMDZkMC00N2UyLTg3MzMtYTJmNjg3Nzk5MzRiXkEyXkFqcGdeQXVyMjUzOTY1NTc@._V1_FMjpg_UX1000_.jpg"
                        ),
                        KinoModel(
                            6,
                            "Обосраться6",
                            "",
                            "https://m.media-amazon.com/images/M/MV5BZDA0OGQxNTItMDZkMC00N2UyLTg3MzMtYTJmNjg3Nzk5MzRiXkEyXkFqcGdeQXVyMjUzOTY1NTc@._V1_FMjpg_UX1000_.jpg"
                        )
                        ,
                        KinoModel(
                            7,
                            "Обосраться7",
                            "",
                            "https://m.media-amazon.com/images/M/MV5BZDA0OGQxNTItMDZkMC00N2UyLTg3MzMtYTJmNjg3Nzk5MzRiXkEyXkFqcGdeQXVyMjUzOTY1NTc@._V1_FMjpg_UX1000_.jpg"
                        )
                    )
                ),*/
                // NewsKinoFeedItem()
            )
        }
    }

}