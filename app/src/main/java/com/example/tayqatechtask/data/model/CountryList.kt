package com.example.tayqatechtask.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries")
data class CountryList(
    @PrimaryKey
    val id: Int,

    val countryList: List<Country> = emptyList()
) {
    constructor() : this(0, emptyList())
}