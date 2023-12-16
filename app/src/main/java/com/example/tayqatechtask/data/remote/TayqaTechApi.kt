package com.example.tayqatechtask.data.remote

import com.example.tayqatechtask.data.model.CountryList
import retrofit2.http.GET

interface TayqaTechApi {

    @GET("getdata")
    suspend fun getData(): CountryList

}