package com.rrpvm.pstu_curs_rrpvm.di

import android.content.Context
import androidx.room.Room
import com.rrpvm.data.room.KinoZDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val DATABASE_NAME = "kinoZDatabase"

@Module
@InstallIn(SingletonComponent::class)
abstract class RoomModule {
    companion object {
        @Provides
        @Singleton
        fun provideRoomDataBase(@ApplicationContext applicationContext: Context) =
            Room.databaseBuilder(
                applicationContext,
                KinoZDatabase::class.java, DATABASE_NAME
            ).fallbackToDestructiveMigration()
                .build()
    }
}