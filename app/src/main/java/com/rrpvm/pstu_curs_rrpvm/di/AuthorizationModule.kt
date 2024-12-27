package com.rrpvm.pstu_curs_rrpvm.di

import android.content.Context
import com.rrpvm.data.service.MemoryAuthenticationService
import com.rrpvm.domain.repository.ClientRepository
import com.rrpvm.domain.service.IAuthenticationService
import com.rrpvm.domain.validator.AuthorizationFieldsValidator
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
        fun provideMemoryAuthorizationService(
            clientRepository: ClientRepository,
            @ApplicationContext context: Context
        ): MemoryAuthenticationService {
            return MemoryAuthenticationService(clientRepository, context)
        }

        @Provides
        fun provideAuthenticationValidator(): AuthorizationFieldsValidator {
            return AuthorizationFieldsValidator()
        }
    }

}