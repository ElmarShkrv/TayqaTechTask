package com.example.tayqatechtask.data.local

import android.database.Cursor
import android.util.Log
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
    @Query("SELECT * FROM People WHERE cityId IN (SELECT cityId FROM City WHERE countryId IN (:countryIds))")
    suspend fun filterPeopleByCountries(countryIds: List<Int>): List<People>


}