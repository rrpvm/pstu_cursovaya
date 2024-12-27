package com.rrpvm.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rrpvm.domain.repository.ClientRepository
import com.rrpvm.domain.service.IAuthenticationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authenticationService: IAuthenticationService,
    private val clientRepository: ClientRepository
) : ViewModel() {

    @OptIn(FlowPreview::class)
    val mAccount = authenticationService.currentAccountId.flatMapMerge { authedUUID ->
        runCatching {
            clientRepository.getClientFlow(authedUUID)
        }.getOrElse {
            flow { }
        }
    }

    init {
        viewModelScope.launch {
            mAccount.launchIn(this)
        }
    }
}