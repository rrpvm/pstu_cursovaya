package com.rrpvm.pstu_curs_rrpvm.di

import com.rrpvm.data.datasource.impl.MemoryKinoFilmsDataSource
import com.rrpvm.data.repository.RoomCachedKinoRepository
import com.rrpvm.data.repository.RoomClientRepository
import com.rrpvm.data.room.KinoZDatabase
import com.rrpvm.data.room.dao.ClientDao
import com.rrpvm.data.room.dao.KinoDao
import com.rrpvm.data.room.dao.KinoSessionDao
import com.rrpvm.data.datasource.KinofilmsDataSource
import com.rrpvm.data.mapper._data.KinoDtoToKinoModelMapper
import com.rrpvm.data.mapper_helpers.IsLikedKinoChecker
import com.rrpvm.data.mapper_helpers.IsLikedKinoCheckerRoomImpl
import com.rrpvm.data.repository.FilterRepositoryImpl
import com.rrpvm.data.repository.HallNonCacheRepository
import com.rrpvm.data.repository.RoomAgeRatingRepository
import com.rrpvm.data.room.dao.AgeRatingDao
import com.rrpvm.data.room.dao.KinoFilmViewsDao
import com.rrpvm.data.room.dao.KinoGenresDao
import com.rrpvm.domain.repository.AgeRatingRepository
import com.rrpvm.domain.repository.ClientRepository
import com.rrpvm.domain.repository.FilterRepository
import com.rrpvm.domain.repository.HallRepository
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
    abstract fun bindFilterRepository(repository: FilterRepositoryImpl): FilterRepository

    @Binds
    abstract fun bindAgeRatingRepository(repository: RoomAgeRatingRepository): AgeRatingRepository

    @Binds
    abstract fun bindHallRepository(repository: HallNonCacheRepository): HallRepository


    @Binds
    abstract fun bindKinoFilmDataSource(dataSource: MemoryKinoFilmsDataSource): KinofilmsDataSource

    @Binds
    abstract fun bindIsLikedKinoChecker(checker: IsLikedKinoCheckerRoomImpl): IsLikedKinoChecker

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
        fun provideKinoGenresDao(db: KinoZDatabase): KinoGenresDao {
            return db.getKinoGenresDao()
        }

        @Provides
        fun provideKinoSessionDao(db: KinoZDatabase): KinoSessionDao {
            return db.getKinoSessionEntityDao()
        }

        @Provides
        fun provideKinoFilmViewsDao(db: KinoZDatabase): KinoFilmViewsDao {
            return db.getKinoFilmsViewsDao()
        }

        @Provides
        fun provideAgeRatingDao(db: KinoZDatabase): AgeRatingDao {
            return db.getAgeRatingDao()
        }

        @Provides
        @Singleton
        fun provideRoomAgeRatingRepository(dao: AgeRatingDao) = RoomAgeRatingRepository(dao)

        @Provides
        @Singleton
        fun provideNonCacheHallRepository(kinofilmsDataSource: KinofilmsDataSource) =
            HallNonCacheRepository(kinofilmsDataSource)

        @Provides
        @Singleton
        fun provideMemoryKinoFilmsDataSource(mapper: KinoDtoToKinoModelMapper): MemoryKinoFilmsDataSource {
            return MemoryKinoFilmsDataSource(mapper)
        }


        @Provides
        @Singleton
        fun provideIsLikedKinoCheckerRoom(dao: KinoDao): IsLikedKinoCheckerRoomImpl {
            return IsLikedKinoCheckerRoomImpl(dao)
        }

        @Provides
        fun provideKinoDtoToKinoModelMapper(checker: IsLikedKinoChecker): KinoDtoToKinoModelMapper {
            return KinoDtoToKinoModelMapper(checker)
        }

        @Provides
        @Singleton
        fun provideFilterRepositoryImpl(): FilterRepositoryImpl {
            return FilterRepositoryImpl()
        }

        @Provides
        @Singleton
        fun provideMemoryKinoRepository(
            kinoDao: KinoDao,
            kinoSessionDao: KinoSessionDao,
            kinoFilmViewsDao: KinoFilmViewsDao,
            kinoGenresDao: KinoGenresDao,
            ageRatingDao: AgeRatingDao,
            kinofilmsDataSource: KinofilmsDataSource,
            kinoModelMapper: KinoDtoToKinoModelMapper
        ): RoomCachedKinoRepository {
            return RoomCachedKinoRepository(
                kinoDao = kinoDao,
                kinoSessionDao = kinoSessionDao,
                kinoFilmViewsDao = kinoFilmViewsDao,
                kinoGenresDao = kinoGenresDao,
                ageRatingDao = ageRatingDao,
                kinoDataSource = kinofilmsDataSource,
                kinoDtoToKinoModelMapper = kinoModelMapper
            )
        }

    }
}
