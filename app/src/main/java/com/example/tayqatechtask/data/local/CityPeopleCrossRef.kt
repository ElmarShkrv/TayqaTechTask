package com.example.tayqatechtask.data.local

import androidx.room.Entity

@Entity(tableName = "CityPeopleCrossRef", primaryKeys = ["cityId", "humanId"])
data class CityPeopleCrossRef(
    val cityId: Int,
    val humanId: Int
)
