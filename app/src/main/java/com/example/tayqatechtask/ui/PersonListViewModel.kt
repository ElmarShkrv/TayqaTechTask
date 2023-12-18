package com.example.tayqatechtask.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tayqatechtask.data.model.Country
import com.example.tayqatechtask.data.model.People
import com.example.tayqatechtask.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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

    private val _filteredPersons = MutableLiveData<List<People>>()
    val filteredPersons: LiveData<List<People>> get() = _filteredPersons

    private val selectedCountries = MutableLiveData<Set<Country>>(emptySet())

    init {
        // Observe changes in selected countries and trigger filtering
        selectedCountries.observeForever { selected ->
            viewModelScope.launch {
                // Ensure selectedCountries is not empty before filtering
                if (selected.isNotEmpty()) {
                    val filteredPeople = repository.filterPeopleByCountries(selected.toList())
                    Log.d("ViewModel", "selected to list: $selected")
                    _filteredPersons.value = filteredPeople
                } else {
                    // Handle the case when no countries are selected (show all)
                    _filteredPersons.value = _countries.value?.flatMap { it.cityList.flatMap { it.peopleList } }
                }
            }
        }
    }

//    init {
//        // Observe changes in selected countries and trigger filtering
//        selectedCountries.observeForever { selected ->
//            viewModelScope.launch(Dispatchers.Main) {
//                // Ensure selectedCountries is not empty before filtering
//                if (selected.isNotEmpty()) {
//                    val filteredPeople = repository.filterPeopleByCountries(selected.toList())
//                    Log.d("ViewModel", "Filtered people: $filteredPeople")
//                    _filteredPersons.value = filteredPeople
//                    Log.d("ViewModel", "Filtered people in ViewModel: $filteredPeople")
//                }
//            }
//        }
//    }

    fun updateSelectedCountries(selectedCountries: Set<Country>) {
        this.selectedCountries.value = selectedCountries
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
