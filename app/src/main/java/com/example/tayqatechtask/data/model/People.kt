package com.example.tayqatechtask.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "People")
data class People(
    @PrimaryKey
    val humanId: Int,
    val name: String,
    val surname: String,
    val cityId: Int
)