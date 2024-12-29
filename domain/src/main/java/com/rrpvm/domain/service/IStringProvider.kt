package com.rrpvm.domain.service

import kotlin.jvm.Throws

interface IStringProvider {
    @Throws(IllegalArgumentException::class)
    fun provideString(resourceId: Int): String

    @Throws(IllegalArgumentException::class)
    fun provideString(resourceId: Int, vararg args: Any): String
}