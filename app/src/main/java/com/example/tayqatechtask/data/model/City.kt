package com.example.tayqatechtask.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "City")
data class City(
    @PrimaryKey
    val cityId: Int,
    val name: String,
    val countryId: Int,

//    @ColumnInfo(name = "peopleList")
    val peopleList: List<People>,
) {
    constructor() : this(0, "", 0, emptyList())
}
