package com.example.tayqatechtask.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "City")
data class City(
    @PrimaryKey
    val cityId: Int,
    val name: String,

    val peopleList: List<People>,
) {
    constructor() : this(0,  "",  emptyList())
}
