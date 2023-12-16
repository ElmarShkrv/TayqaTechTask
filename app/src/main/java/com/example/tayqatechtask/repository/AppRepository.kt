package com.example.tayqatechtask.repository

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

    suspend fun getPeopleFromSelectedCountries(selectedCountries: Set<Country>): List<People> {
        val countryIds = selectedCountries.map { it.countryId }
        return personDao.getPeopleByCountryIds(countryIds)
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

    suspend fun getCountries(): CountryList {
        return tayqaTechApi.getData()
    }

    suspend fun updateCountriesToLocal(countries: CountryList) {
        // Update your local database with the new data
        personDao.insertAllCountries(countries)
    }
}