package com.rrpvm.profile.presentation.menu

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rrpvm.core.TAG
import com.rrpvm.core.presentation.mapper.UserModelToUiMapper
import com.rrpvm.domain.repository.ClientRepository
import com.rrpvm.domain.service.IAuthenticationService
import com.rrpvm.domain.service.IStringProvider
import com.rrpvm.profile.R
import com.rrpvm.profile.presentation.menu.model.ProfileMenuAdapterState
import com.rrpvm.profile.presentation.menu.model.ProfileMenuItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authenticationService: IAuthenticationService,
    private val clientRepository: ClientRepository,
    private val userModelToUiMapper: UserModelToUiMapper,
    private val stringProvider: IStringProvider,
) : ViewModel() {
    private val _menuState = MutableStateFlow(ProfileMenuAdapterState())
    val menuState = _menuState.asStateFlow()

    @OptIn(FlowPreview::class)
    private val mAccount = authenticationService.currentAccountId.flatMapMerge { authedUUID ->
        return@flatMapMerge runCatching {
            clientRepository.getClientFlow(authedUUID)
        }.getOrElse {
            Log.w(TAG, "ProfileViewModel::mAccount is null")
            flowOf(null)
        }
    }.filterNotNull().onEach { userModel ->
        val uiModel = userModel.map(userModelToUiMapper)
        _menuState.value = ProfileMenuAdapterState(
            listOf(
                ProfileMenuItem.ClientItem(
                    mClientName = uiModel.userName,
                    mCreationDate = uiModel.userCreationDate,
                    avatarUri = uiModel.userAvatar
                ),
                ProfileMenuItem.DefaultTextMenu(
                    stringProvider.provideString(R.string.my_tickes),
                    R.drawable.ic_tickets
                ),
                ProfileMenuItem.DefaultTextMenu(
                    stringProvider.provideString(R.string.saved),
                    R.drawable.ic_like
                ),

                ProfileMenuItem.DefaultTextMenu(
                    stringProvider.provideString(R.string.settings),
                    R.drawable.ic_settings
                ),
                ProfileMenuItem.LogoutButtonItem
            )
        )
    }


    init {
        viewModelScope.launch {
            mAccount.launchIn(this)
        }
    }
}