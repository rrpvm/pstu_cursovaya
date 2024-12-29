package com.rrpvm.pstu_curs_rrpvm.di

import android.content.Context
import com.rrpvm.core.service.AndroidStringProvider
import com.rrpvm.domain.service.IStringProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class CoreModule {
    @Binds
    abstract fun bindStringProvider(provider: AndroidStringProvider): IStringProvider

    companion object {
        @Provides
        fun provideAndroidStringProvider(@ApplicationContext context: Context): AndroidStringProvider {
            return AndroidStringProvider(context)
        }
    }
}