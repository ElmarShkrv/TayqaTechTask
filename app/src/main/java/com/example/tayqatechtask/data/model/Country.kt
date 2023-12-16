package com.example.tayqatechtask.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey


@Entity(tableName = "Country")
data class Country(
    @PrimaryKey
    val countryId: Int,
    val name: String,

    val cityList: List<City>
) {
    constructor(): this(0,"", emptyList())
}