package com.rrpvm.pstu_curs_rrpvm.di

import com.rrpvm.data.datasource.MemoryKinoFilmsDataSource
import com.rrpvm.data.repository.RoomCachedKinoRepository
import com.rrpvm.data.repository.RoomClientRepository
import com.rrpvm.data.room.KinoZDatabase
import com.rrpvm.data.room.dao.ClientDao
import com.rrpvm.data.room.dao.KinoDao
import com.rrpvm.data.room.dao.KinoSessionDao
import com.rrpvm.domain.datasource.KinofilmsDataSource
import com.rrpvm.domain.repository.ClientRepository
import com.rrpvm.domain.repository.KinoRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    abstract fun bindClientRepository(service: RoomClientRepository): ClientRepository

    @Binds
    abstract fun bindKinoRepository(repository: RoomCachedKinoRepository): KinoRepository

    @Binds
    abstract fun bindKinoFilmDataSource(dataSource: MemoryKinoFilmsDataSource): KinofilmsDataSource

    companion object {
        @Provides
        @Singleton
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
        @Singleton
        fun provideMemoryKinoFilmsDataSource(): MemoryKinoFilmsDataSource {
            return MemoryKinoFilmsDataSource()
        }

        @Provides
        @Singleton
        fun provideMemoryKinoRepository(
            kinoDao: KinoDao,
            kinoSessionDao: KinoSessionDao,
            kinofilmsDataSource: KinofilmsDataSource
        ): RoomCachedKinoRepository {
            return RoomCachedKinoRepository(
                kinoDao = kinoDao,
                kinoSessionDao = kinoSessionDao,
                kinoDataSource = kinofilmsDataSource
            )
        }

    }
}
