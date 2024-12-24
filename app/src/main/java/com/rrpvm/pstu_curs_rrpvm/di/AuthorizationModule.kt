package com.rrpvm.pstu_curs_rrpvm.di

import com.rrpvm.data.service.MemoryAuthenticationService
import com.rrpvm.domain.service.IAuthenticationService
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthorizationModule {
    @Binds
    abstract fun bindAuthorizationService(service : MemoryAuthenticationService) : IAuthenticationService
    companion object{
        @Provides
        fun provideMemoryAuthorizationService() : MemoryAuthenticationService{
            return MemoryAuthenticationService()
        }
    }
}