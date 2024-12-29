package com.rrpvm.profile.presentation.menu.model

import androidx.annotation.DrawableRes

enum class DefaultTextMenuTypes {
    MyTickets,
    MyFavourites,
    MySettings
}

sealed class ProfileMenuItem(val viewType: Int) {
    data class DefaultTextMenu(
        val mText: String,
        @DrawableRes val mIcon: Int,
        val type: DefaultTextMenuTypes
    ) : ProfileMenuItem(
        viewType = DEFAULT_TEXT_MENU_VIEW_TYPE
    ) {
        companion object {
            const val VIEW_TYPE = DEFAULT_TEXT_MENU_VIEW_TYPE
        }
    }

    data class ClientItem(
        val avatarUri: String? = null,
        val mClientName: String = "",
        val mCreationDate: String = ""
    ) : ProfileMenuItem(CLIENT_ITEM_VIEW_TYPE) {
        companion object {
            const val VIEW_TYPE = CLIENT_ITEM_VIEW_TYPE
        }
    }

    data object LogoutButtonItem : ProfileMenuItem(LOGOUT_BUTTON_VIEW_TYPE) {
        const val VIEW_TYPE: Int = LOGOUT_BUTTON_VIEW_TYPE
    }

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false
        return this === other
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    companion object {
        private const val DEFAULT_TEXT_MENU_VIEW_TYPE = 0xFF
        private const val CLIENT_ITEM_VIEW_TYPE = 0xFFF
        private const val LOGOUT_BUTTON_VIEW_TYPE = 0xFFFF
    }
}