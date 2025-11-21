package com.woowa.weatherfit.di

import com.woowa.weatherfit.data.repository.ClothRepositoryImpl
import com.woowa.weatherfit.data.repository.CodyRepositoryImpl
import com.woowa.weatherfit.data.repository.RegionRepositoryImpl
import com.woowa.weatherfit.data.repository.WeatherRepositoryImpl
import com.woowa.weatherfit.domain.repository.ClothRepository
import com.woowa.weatherfit.domain.repository.CodyRepository
import com.woowa.weatherfit.domain.repository.RegionRepository
import com.woowa.weatherfit.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindClothRepository(
        clothRepositoryImpl: ClothRepositoryImpl
    ): ClothRepository

    @Binds
    @Singleton
    abstract fun bindCodyRepository(
        codyRepositoryImpl: CodyRepositoryImpl
    ): CodyRepository

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        weatherRepositoryImpl: WeatherRepositoryImpl
    ): WeatherRepository

    @Binds
    @Singleton
    abstract fun bindRegionRepository(
        regionRepositoryImpl: RegionRepositoryImpl
    ): RegionRepository
}
