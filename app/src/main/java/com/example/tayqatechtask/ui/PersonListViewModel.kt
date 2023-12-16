package com.example.tayqatechtask.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tayqatechtask.data.model.Country
import com.example.tayqatechtask.data.model.People
import com.example.tayqatechtask.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonListViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _countries = MutableLiveData<List<Country>>()
    val countries: LiveData<List<Country>> get() = _countries

    private val _countriesForFilter = MutableLiveData<List<Country>>()
    val countriesForFilter: LiveData<List<Country>> get() = _countriesForFilter

    private val _selectedCountries = MutableLiveData<Set<Country>>().apply { value = emptySet() }
    val selectedCountries: LiveData<Set<Country>> get() = _selectedCountries

    private val _filteredPeople = MutableLiveData<List<People>>()
    val filteredPeople: LiveData<List<People>> get() = _filteredPeople


    fun setFilteredPeople(people: List<People>) {
        _filteredPeople.value = people
    }

    fun refreshData() {
        viewModelScope.launch {
            val refreshedData = repository.refreshCountries()
            // Update local database with fresh data
            repository.updateCountriesToLocal(refreshedData)
            // Update UI with the refreshed data
            _countries.value = refreshedData.countryList
        }
    }

    fun getCountries() {
        viewModelScope.launch {
            val countryListResult = repository.getCountriesFromLocal()

            if (countryListResult.isNotEmpty()) {
                // Data is available in the local database, show it
                _countries.value = countryListResult
            } else {
                // Local database is empty or data is not available, fetch from the API
                refreshData()
            }
        }
    }

    fun getCountriesForFilter() {
        viewModelScope.launch {
            _countriesForFilter.value = repository.getCountries().countryList
        }
    }

    fun updateSelectedCountries(selectedCountries: Set<Country>) {
        _selectedCountries.value = selectedCountries
    }

    suspend fun getPeopleFromSelectedCountries(selectedCountries: Set<Country>): List<People> {
        return repository.getPeopleFromSelectedCountries(selectedCountries)
    }
}