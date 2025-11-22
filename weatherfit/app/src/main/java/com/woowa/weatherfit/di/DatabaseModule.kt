package com.woowa.weatherfit.di

import android.content.Context
import androidx.room.Room
import com.woowa.weatherfit.data.local.dao.ClothDao
import com.woowa.weatherfit.data.local.dao.CodyDao
import com.woowa.weatherfit.data.local.database.WeatherFitDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): WeatherFitDatabase {
        return Room.databaseBuilder(
            context,
            WeatherFitDatabase::class.java,
            WeatherFitDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideClothDao(database: WeatherFitDatabase): ClothDao {
        return database.clothDao()
    }

    @Provides
    @Singleton
    fun provideCodyDao(database: WeatherFitDatabase): CodyDao {
        return database.codyDao()
    }
}
