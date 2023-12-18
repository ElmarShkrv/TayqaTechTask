package com.example.tayqatechtask.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.tayqatechtask.util.Converters

@Entity(tableName = "countries")
data class CountryList(
    @PrimaryKey
    val id: Int,

    val countryList: List<Country> = emptyList()
) {
    constructor() : this(0, emptyList())
}