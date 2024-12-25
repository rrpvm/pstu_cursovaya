package com.rrpvm.pstu_curs_rrpvm.di

import com.rrpvm.data.service.MemoryAuthenticationService
import com.rrpvm.domain.service.IAuthenticationService
import com.rrpvm.domain.validator.AuthorizationFieldsValidator
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthorizationModule {
    @Binds
    abstract fun bindAuthorizationService(service: MemoryAuthenticationService): IAuthenticationService

    companion object {
        @Provides
        @Singleton
        fun provideMemoryAuthorizationService(): MemoryAuthenticationService {
            return MemoryAuthenticationService()
        }

        @Provides
        fun provideAuthenticationValidator(): AuthorizationFieldsValidator {
            return AuthorizationFieldsValidator()
        }
    }

}