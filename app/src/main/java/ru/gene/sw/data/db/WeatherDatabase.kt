package ru.gene.sw.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.gene.sw.data.db.dao.WeatherDao
import ru.gene.sw.data.db.entity.WeatherEntity

@Database(
    entities = [WeatherEntity::class],
    version = 1,
    exportSchema = false
)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}