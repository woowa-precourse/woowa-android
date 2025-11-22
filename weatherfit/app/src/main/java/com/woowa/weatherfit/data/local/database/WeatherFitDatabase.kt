package com.woowa.weatherfit.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.woowa.weatherfit.data.local.dao.ClothDao
import com.woowa.weatherfit.data.local.dao.CodyDao
import com.woowa.weatherfit.data.local.entity.ClothEntity
import com.woowa.weatherfit.data.local.entity.CodyEntity

@Database(
    entities = [ClothEntity::class, CodyEntity::class],
    version = 2,
    exportSchema = false
)
abstract class WeatherFitDatabase : RoomDatabase() {
    abstract fun clothDao(): ClothDao
    abstract fun codyDao(): CodyDao

    companion object {
        const val DATABASE_NAME = "weatherfit_database"
    }
}
