package com.example.tayqatechtask.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.tayqatechtask.R
import com.example.tayqatechtask.adapters.CountryFilterAdapter
import com.example.tayqatechtask.adapters.PersonAdapter
import com.example.tayqatechtask.data.model.City
import com.example.tayqatechtask.data.model.Country
import com.example.tayqatechtask.data.model.People
import com.example.tayqatechtask.databinding.FragmentFilterCitiesBinding
import com.example.tayqatechtask.databinding.FragmentFilterCountriesBinding
import com.example.tayqatechtask.databinding.FragmentPersonsListBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class PersonsListFragment : Fragment() {

    private lateinit var binding: FragmentPersonsListBinding
    private val viewModel: PersonListViewModel by viewModels()
    private lateinit var countryFilterAdapter: CountryFilterAdapter
    private lateinit var personAdapter: PersonAdapter
    private lateinit var selectedCountries: Set<Country>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPersonsListBinding.inflate(inflater)

        binding.filterCountry.setOnClickListener {
            showFilterDialogForCountries(inflater)
        }
        binding.filterCity.setOnClickListener {
            showFilterDialogForCities(inflater)
        }

        return binding.root
    }

    private fun showFilterDialogForCountries(inflater: LayoutInflater) {

        val bindingForCountriesFilter = FragmentFilterCountriesBinding.inflate(inflater)
        val dialogForCountries = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        dialogForCountries.setContentView(bindingForCountriesFilter.root)

        initCountriesFilter(bindingForCountriesFilter)

        bindingForCountriesFilter.filterCountryRv.adapter = countryFilterAdapter

        bindingForCountriesFilter.filterApplyBtn.setOnClickListener {
            selectedCountries = countryFilterAdapter.getSelectedItems()
            viewModel.updateSelectedCountries(selectedCountries)
            dialogForCountries.dismiss()
        }

        dialogForCountries.show()

    }

    private fun initCountriesFilter(binding: FragmentFilterCountriesBinding) {
        binding.apply {
            val filterCountryRecyclerView: RecyclerView = filterCountryRv
            viewModel.countriesForFilter.observe(viewLifecycleOwner) { countryList ->
                countryFilterAdapter = CountryFilterAdapter(countryList) { selectedCountries ->
                    // Handle selection if needed
                    updateUIForSelectedCountries(selectedCountries)
                }
                filterCountryRecyclerView.adapter = countryFilterAdapter
            }
        }
    }

    private fun updateUIForSelectedCountries(selectedCountries: Set<Country>) {
        viewModel.viewModelScope.launch {
            val filteredPeople = viewModel.getPeopleFromSelectedCountries(selectedCountries)
            withContext(Dispatchers.Main) {
                if (isActive) {
                    viewModel.setFilteredPeople(filteredPeople)
                }
            }
        }
    }


    private fun showFilterDialogForCities(inflater: LayoutInflater) {

        val bindingForCitiesFilter = FragmentFilterCitiesBinding.inflate(inflater)
        val dialogForCities = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        dialogForCities.setContentView(bindingForCitiesFilter.root)
        dialogForCities.show()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCountries()
        viewModel.getCountriesForFilter()

        val recyclerView: RecyclerView = view.findViewById(R.id.personRvAdapter)
        val swipeRefreshLayout: SwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)

        personAdapter = PersonAdapter()
        recyclerView.adapter = personAdapter

        viewModel.filteredPeople.observe(viewLifecycleOwner, Observer { filteredPeopleList ->
            personAdapter.submitList(filteredPeopleList)
        })

        viewModel.countries.observe(viewLifecycleOwner, Observer { countryList ->

            val peopleList = countryList.flatMap { country ->
                country.cityList.flatMap { city ->
                    city.peopleList.orEmpty()
                }
            }

            personAdapter.submitList(peopleList)

            viewModel.selectedCountries.observe(viewLifecycleOwner, Observer { selectedCountries ->
                if (selectedCountries.isNotEmpty()) {
                    viewModel.viewModelScope.launch {
                        val filteredPeopleList = viewModel.getPeopleFromSelectedCountries(selectedCountries)
                        personAdapter.submitList(filteredPeopleList)
                    }
                }
            })

            personAdapter.submitList(peopleList)
            swipeRefreshLayout.isRefreshing = false
        })

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshData()
        }

    }
}