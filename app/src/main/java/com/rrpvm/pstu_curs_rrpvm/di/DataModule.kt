package com.rrpvm.pstu_curs_rrpvm.di

import com.rrpvm.data.repository.MemoryKinoRepository
import com.rrpvm.data.repository.RoomClientRepository
import com.rrpvm.data.room.KinoZDatabase
import com.rrpvm.data.room.dao.ClientDao
import com.rrpvm.data.room.dao.KinoDao
import com.rrpvm.data.room.dao.KinoSessionDao
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
        fun provideKinoDao(db: KinoZDatabase): KinoDao {
            return db.getKinoEntityDao()
        }

        @Provides
        fun provideKinoSessionDao(db: KinoZDatabase): KinoSessionDao {
            return db.getKinoSessionEntityDao()
        }

        @Provides
        fun provideMemoryKinoRepository(
            kinoDao: KinoDao,
            kinoSessionDao: KinoSessionDao
        ): MemoryKinoRepository {
            return MemoryKinoRepository(kinoDao, kinoSessionDao)
        }

    }
}
