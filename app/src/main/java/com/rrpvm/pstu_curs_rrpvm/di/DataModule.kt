package com.rrpvm.pstu_curs_rrpvm.di

import com.rrpvm.data.repository.MemoryKinoRepository
import com.rrpvm.data.repository.RoomClientRepository
import com.rrpvm.data.room.KinoZDatabase
import com.rrpvm.data.room.dao.ClientDao
import com.rrpvm.domain.repository.ClientRepository
import com.rrpvm.domain.repository.KinoRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    abstract fun bindClientRepository(service: RoomClientRepository): ClientRepository

    @Binds
    abstract fun bindKinoRepository(repository: MemoryKinoRepository): KinoRepository

    companion object {
        @Provides
        fun provideClientRepository(clientDao: ClientDao): RoomClientRepository {
            return RoomClientRepository(clientDao)
        }

        @Provides
        fun provideClientDao(db: KinoZDatabase): ClientDao {
            return db.getUserEntityDao()
        }

        @Provides
        fun provideMemoryKinoRepository(): MemoryKinoRepository {
            return MemoryKinoRepository()
        }

    }
}
