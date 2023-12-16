package com.example.tayqatechtask.data.local


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.tayqatechtask.data.model.City
import com.example.tayqatechtask.data.model.Country
import com.example.tayqatechtask.data.model.CountryList
import com.example.tayqatechtask.data.model.People
import com.example.tayqatechtask.util.Converters

@Database(
    entities = [CountryList::class, City::class, Country::class, CityPeopleCrossRef::class, People::class],
    version = 4
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun personDao(): PersonDao
}