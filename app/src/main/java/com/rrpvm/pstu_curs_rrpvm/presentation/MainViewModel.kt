package com.rrpvm.pstu_curs_rrpvm.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.rrpvm.domain.repository.ClientRepository
import com.rrpvm.domain.service.IAuthenticationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val authenticationService: IAuthenticationService,
    private val clientRepository: ClientRepository
) : AndroidViewModel(application = application) {
    val isAuth = authenticationService.isAuthenticated
    val isShowActivityProgress = authenticationService.isAuthJobFirst.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        true
    )
}