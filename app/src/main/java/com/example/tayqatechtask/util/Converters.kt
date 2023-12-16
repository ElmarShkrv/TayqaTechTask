package com.example.tayqatechtask.util

import androidx.room.TypeConverter
import com.example.tayqatechtask.data.model.City
import com.example.tayqatechtask.data.model.Country
import com.example.tayqatechtask.data.model.People
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromCountryList(countryList: List<Country>?): String? {
        return Gson().toJson(countryList)
    }

    @TypeConverter
    fun toCountryList(countryListString: String?): List<Country>? {
        val type = object : TypeToken<List<Country>>() {}.type
        return Gson().fromJson(countryListString, type)
    }

    @TypeConverter
    fun fromCityList(cityList: List<City>?): String? {
        return Gson().toJson(cityList)
    }

    @TypeConverter
    fun toCityList(cityListString: String?): List<City>? {
        val type = object : TypeToken<List<City>>() {}.type
        return Gson().fromJson(cityListString, type)
    }

    @TypeConverter
    fun fromJson(json: String): List<People> {
        val listType = object : TypeToken<List<People>>() {}.type
        return Gson().fromJson(json, listType)
    }

    @TypeConverter
    fun toJson(list: List<People>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}