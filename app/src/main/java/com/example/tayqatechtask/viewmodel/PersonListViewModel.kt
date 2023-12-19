package com.example.tayqatechtask.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tayqatechtask.data.model.City
import com.example.tayqatechtask.data.model.Country
import com.example.tayqatechtask.data.model.People
import com.example.tayqatechtask.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonListViewModel @Inject constructor(
    private val repository: AppRepository,
) : ViewModel() {

    private val _countries = MutableLiveData<List<Country>>()
    val countries: LiveData<List<Country>> get() = _countries

    private val _countriesForFilter = MutableLiveData<List<Country>>()
    val countriesForFilter: LiveData<List<Country>> get() = _countriesForFilter

    private val _filteredPersons = MutableLiveData<List<People>>()
    val filteredPersons: LiveData<List<People>> get() = _filteredPersons

    private val selectedCountries = MutableLiveData<Set<Country>>(emptySet())
    private val selectedCities = MutableLiveData<Set<City>>(emptySet())

    private val _citiesForFilter = MutableLiveData<List<City>>()
    val citiesForFilter: LiveData<List<City>> get() = _citiesForFilter

    private val selectedCountriesForCitesFilter = mutableSetOf<Country>()


    init {
        selectedCountries.observeForever { selected ->
            viewModelScope.launch {
                if (selected.isNotEmpty()) {
                    val countryIds = selected.map { it.countryId }
                    val filteredPeople = repository.filterPeopleByCountries(countryIds)
                    Log.d("ViewModel", "selected to list: $selected")
                    _filteredPersons.value = filteredPeople
                } else {
                    _filteredPersons.value =
                        _countries.value?.flatMap { it.cityList.flatMap { it.peopleList } }
                }
            }
        }

        selectedCities.observeForever { selected ->
            viewModelScope.launch {
                if (selected.isNotEmpty()) {
                    val cityIds = selected.map { it.cityId }
                    val filteredPeople = repository.filterPeopleByCities(cityIds)
                    Log.d("ViewModel", "selected to list: $selected")
                    _filteredPersons.value = filteredPeople
                } else {
                    fetchCities()
                }
            }
        }
    }

    fun updateSelectedCountriesForCitesFilter(countries: Set<Country>) {
        selectedCountriesForCitesFilter.clear()
        selectedCountriesForCitesFilter.addAll(countries)
        fetchCities()
    }

    private fun fetchCities() {
        viewModelScope.launch {
            val cities = if (selectedCountriesForCitesFilter.isEmpty()) {
                repository.getCitiesForFilter().countryList.flatMap { it.cityList }
            } else {
                repository.filterCitiesByCountries(selectedCountriesForCitesFilter.map { it.countryId })
            }
            _citiesForFilter.postValue(cities)
        }
    }

    fun updateSelectedCountries(selectedCountries: Set<Country>) {
        this.selectedCountries.value = selectedCountries
    }

    fun updateSelectedCities(selectedCities: Set<City>) {
        this.selectedCities.value = selectedCities
    }

    fun getCountries() {
        viewModelScope.launch {
            val countryListResult = repository.getCountriesFromLocal()

            if (countryListResult.isNotEmpty()) {
                _countries.value = countryListResult
            } else {
                refreshData()
            }
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            val refreshedData = repository.refreshCountries()
            repository.updateCountriesToLocal(refreshedData)
            _countries.value = refreshedData.countryList
        }
    }

    fun getCountriesForFilter() {
        viewModelScope.launch {
            _countriesForFilter.value = repository.getCountriesForFilter().countryList
        }
    }
}
