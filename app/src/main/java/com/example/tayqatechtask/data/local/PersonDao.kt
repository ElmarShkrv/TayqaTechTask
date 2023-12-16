package com.example.tayqatechtask.data.local

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.tayqatechtask.data.model.Country
import com.example.tayqatechtask.data.model.CountryList
import com.example.tayqatechtask.data.model.People

@Dao
interface PersonDao {

    @Query("SELECT * FROM countries")
    suspend fun getAllCountries(): CountryList

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCountries(persons: CountryList)

    @Transaction
    @Query("SELECT * FROM City WHERE countryId IN (SELECT countryId FROM Country WHERE countryId IN (:countryIds))")
    suspend fun getCitiesByCountryIds(countryIds: List<Int>): List<CityWithPeople>

    @Transaction
    suspend fun getPeopleByCountryIds(countryIds: List<Int>): List<People> {
        val citiesWithPeople = getCitiesByCountryIds(countryIds)
        return citiesWithPeople.flatMap { it.peopleList }
    }

//    @Transaction
//    @Query("SELECT * FROM City WHERE countryId IN (SELECT countryId FROM Country WHERE countryId IN (:countryIds))")
//    suspend fun getCitiesByCountryIds(countryIds: List<Int>): List<CityWithPeople>
//
//    @Transaction
//    suspend fun getPeopleByCountryIds(countryIds: List<Int>): List<People> {
//        val cities = getCitiesByCountryIds(countryIds)
//        return cities.flatMap { cityWithPeople -> cityWithPeople.peopleList }
//    }
}