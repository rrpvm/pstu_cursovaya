package com.rrpvm.profile.presentation

import androidx.lifecycle.ViewModel
import com.rrpvm.domain.repository.ClientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val clientRepository: ClientRepository
) : ViewModel() {
    val mUsername = clientRepository.getClientFlow()
}