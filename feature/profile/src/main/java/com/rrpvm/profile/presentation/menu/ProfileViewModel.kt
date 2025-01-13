package com.rrpvm.profile.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rrpvm.core.presentation.mapper.UserModelToUiMapper
import com.rrpvm.domain.repository.ClientRepository
import com.rrpvm.domain.service.IAuthenticationService
import com.rrpvm.domain.service.IStringProvider
import com.rrpvm.profile.R
import com.rrpvm.profile.presentation.model.DefaultTextMenuTypes
import com.rrpvm.profile.presentation.model.ProfileMenuAdapterState
import com.rrpvm.profile.presentation.model.ProfileMenuItem
import com.rrpvm.profile.presentation.model.ProfileScreenEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authenticationService: IAuthenticationService,
    private val clientRepository: ClientRepository,
    private val stringProvider: IStringProvider,
) : ViewModel() {
    private val _menuState = MutableStateFlow(ProfileMenuAdapterState())
    val menuState = _menuState.asStateFlow()
    private val _screenEffect = MutableSharedFlow<ProfileScreenEffect>()
    val screenEffect = _screenEffect.asSharedFlow()

    @OptIn(FlowPreview::class)
    private val mAccount = authenticationService.currentAccountId.flatMapMerge { authedUUID ->
        clientRepository.getClientFlow(authedUUID).onFailure {
            it.printStackTrace()
        }.getOrNull() ?: flowOf(null)
    }.filterNotNull().onEach { userModel ->
        val uiModel = userModel.map(UserModelToUiMapper)
        _menuState.value = ProfileMenuAdapterState(
            listOf(
                ProfileMenuItem.ClientItem(
                    mClientName = uiModel.userName,
                    mCreationDate = uiModel.userCreationDate,
                    avatarUri = uiModel.userAvatar
                ),
                ProfileMenuItem.DefaultTextMenu(
                    mText = stringProvider.provideString(R.string.my_tickets),
                    mIcon = R.drawable.ic_tickets,
                    type = DefaultTextMenuTypes.MyTickets
                ),

                ProfileMenuItem.DefaultTextMenu(
                    mText = stringProvider.provideString(R.string.settings),
                    mIcon = com.rrpvm.core.R.drawable.ic_settings,
                    type = DefaultTextMenuTypes.MySettings
                ),
                ProfileMenuItem.LogoutButtonItem
            )
        )
    }

    fun onLogoutClicked() {
        viewModelScope.launch {
            authenticationService.logout()
        }
    }

    fun onMenuOptionClicked(type: DefaultTextMenuTypes) {
        if (type == DefaultTextMenuTypes.MyTickets) {
            viewModelScope.launch {
                _screenEffect.emit(ProfileScreenEffect.GoMyTickets)
            }
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            mAccount.launchIn(this)
        }
    }
}