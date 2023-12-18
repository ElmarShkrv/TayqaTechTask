package com.example.tayqatechtask.repository

import android.util.Log
import com.example.tayqatechtask.data.local.PersonDao
import com.example.tayqatechtask.data.model.Country
import com.example.tayqatechtask.data.model.CountryList
import com.example.tayqatechtask.data.model.People
import com.example.tayqatechtask.data.remote.TayqaTechApi
import javax.inject.Inject


class AppRepository @Inject constructor(
    private val tayqaTechApi: TayqaTechApi,
    private val personDao: PersonDao,
) {

    suspend fun filterPeopleByCountries(selectedCountries: List<Country>): List<People> {
        Log.d("Repository", "Filtering people by countries: $selectedCountries")

        val countryIds = selectedCountries.map { it.countryId }

        val filteredPeople = personDao.filterPeopleByCountries(countryIds)

        Log.d("Repository", "Filtered people from DAO: $filteredPeople")

        return filteredPeople
    }

    suspend fun refreshCountries(): CountryList {
        return try {
            val countries = tayqaTechApi.getData()
            personDao.insertAllCountries(countries)
            countries
        } catch (e: Exception) {
            CountryList(0, emptyList())
        }
    }

    suspend fun getCountriesFromLocal(): List<Country> {
        val countryListResult = personDao.getAllCountries()
        return countryListResult?.countryList.orEmpty()
    }

    suspend fun updateCountriesToLocal(countries: CountryList) {
        personDao.insertAllCountries(countries)
    }

    suspend fun getCountriesForFilter(): CountryList {
        return tayqaTechApi.getData()
    }
}