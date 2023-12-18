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

    suspend fun filterPeopleByCountries(countryIds: List<Int>): List<People> {
        val countryList: CountryList = personDao.getAllCountries()

        val filteredPeople = countryList.countryList
            .filter { it.countryId in countryIds }
            .flatMap { it.cityList }
            .flatMap { it.peopleList }

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