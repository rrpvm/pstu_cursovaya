package com.rrpvm.pstu_curs_rrpvm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.rrpvm.data.room.KinoZDatabase
import com.rrpvm.domain.repository.ClientRepository
import com.rrpvm.domain.service.IAuthenticationService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val authenticationService: IAuthenticationService,
    private val clientRepository: ClientRepository
) :
    AndroidViewModel(application = application) {
    val isAuth = authenticationService.isAuthenticated
}