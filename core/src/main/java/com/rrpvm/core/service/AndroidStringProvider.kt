package com.rrpvm.core.service

import android.content.Context
import com.rrpvm.domain.service.IStringProvider

class AndroidStringProvider(private val context: Context) : IStringProvider {
    override fun provideString(resourceId: Int): String {
        return context.getString(resourceId)
    }

    override fun provideString(resourceId: Int, vararg args: Any): String {
        return context.getString(resourceId, *args)
    }
}