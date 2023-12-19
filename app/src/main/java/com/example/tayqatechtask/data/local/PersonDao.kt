package com.example.tayqatechtask.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tayqatechtask.data.model.CountryList

@Dao
interface PersonDao {

    @Query("SELECT * FROM countries")
    suspend fun getAllCountries(): CountryList

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCountries(persons: CountryList)







}