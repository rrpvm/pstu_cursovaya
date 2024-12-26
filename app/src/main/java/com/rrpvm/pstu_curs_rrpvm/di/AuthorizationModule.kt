package com.rrpvm.pstu_curs_rrpvm.di

import android.content.Context
import androidx.room.Room
import com.rrpvm.data.repository.RoomClientRepository
import com.rrpvm.data.room.KinoZDatabase
import com.rrpvm.data.room.dao.ClientDao
import com.rrpvm.data.service.MemoryAuthenticationService
import com.rrpvm.domain.repository.ClientRepository
import com.rrpvm.domain.service.IAuthenticationService
import com.rrpvm.domain.validator.AuthorizationFieldsValidator
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthorizationModule {
    @Binds
    abstract fun bindAuthorizationService(service: MemoryAuthenticationService): IAuthenticationService
    @Binds
    abstract fun bindClientRepository(service: RoomClientRepository): ClientRepository
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


        @Provides
        fun provideClientRepository(clientDao: ClientDao): RoomClientRepository {
            return RoomClientRepository(clientDao)
        }

        @Provides
        fun provideClientDao(db: KinoZDatabase): ClientDao {
             return db.getUserEntityDao()
        }
        @Provides
        @Singleton
        fun provideRoomDataBase(@ApplicationContext applicationContext: Context) = Room.databaseBuilder(
            applicationContext,
            KinoZDatabase::class.java, "kino"
        ).build()
    }

}