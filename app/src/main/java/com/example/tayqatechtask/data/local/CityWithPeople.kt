package com.example.tayqatechtask.data.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation
import com.example.tayqatechtask.data.model.City
import com.example.tayqatechtask.data.model.People

@Entity(tableName = "CityWithPeople")
data class CityWithPeople(
    @Embedded
    val city: City,
    @Relation(
        parentColumn = "cityId",
        entityColumn = "humanId",
        associateBy = Junction(CityPeopleCrossRef::class)
    )
    val peopleList: List<People>
)
