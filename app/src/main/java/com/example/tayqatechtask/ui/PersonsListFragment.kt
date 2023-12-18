package com.example.tayqatechtask.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.tayqatechtask.R
import com.example.tayqatechtask.adapters.CountryFilterAdapter
import com.example.tayqatechtask.adapters.PersonAdapter
import com.example.tayqatechtask.data.model.Country
import com.example.tayqatechtask.data.model.People
import com.example.tayqatechtask.databinding.FragmentFilterCitiesBinding
import com.example.tayqatechtask.databinding.FragmentFilterCountriesBinding
import com.example.tayqatechtask.databinding.FragmentPersonsListBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonsListFragment : Fragment() {

    private lateinit var binding: FragmentPersonsListBinding
    private val viewModel: PersonListViewModel by viewModels()
    private val selectedItems = HashSet<Country>()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val swipeRefreshLayout: SwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)

        val personAdapter = PersonAdapter()
        binding.personRv.adapter = personAdapter

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshData()
        }

        // Observe the filteredPersons LiveData here
        viewModel.filteredPersons.observe(viewLifecycleOwner) { filteredPeople ->
            // Update the existing adapter with the new list of filtered people
            Log.d("Fragment", "Filtered people received: $filteredPeople")
            personAdapter.submitList(filteredPeople)
        }

        viewModel.countries.observe(viewLifecycleOwner, Observer { countryLiveData ->

            val countries: List<Country> = countryLiveData
            val peopleList: List<People> = countries.flatMap { country ->
                country.cityList.flatMap { city ->
                    city.peopleList
                }
            }
            personAdapter.submitList(peopleList)
            swipeRefreshLayout.isRefreshing = false
        })

        viewModel.getCountries()
        viewModel.getCountriesForFilter()
    }

    private fun showFilterDialogForCountries(inflater: LayoutInflater) {
        val bindingForCountriesFilter = FragmentFilterCountriesBinding.inflate(inflater)
        val dialogForCountries = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        dialogForCountries.setContentView(bindingForCountriesFilter.root)
        initCountriesFilter(bindingForCountriesFilter)

        bindingForCountriesFilter.filterApplyBtn.setOnClickListener {
            val selectedCountries =
                (bindingForCountriesFilter.filterCountryRv.adapter as CountryFilterAdapter).getSelectedItems()
                    .toSet()
            Log.d("Fragment", "selectedCountries: $selectedCountries")
            viewModel.updateSelectedCountries(selectedCountries)
            dialogForCountries.dismiss()
        }

        dialogForCountries.show()
    }


    private fun initCountriesFilter(binding: FragmentFilterCountriesBinding) {
        val filterCountryRecyclerView: RecyclerView = binding.filterCountryRv
        val adapter = CountryFilterAdapter { selectedCountry ->
            selectedItems.add(selectedCountry)
        }

        filterCountryRecyclerView.adapter = adapter

        viewModel.countriesForFilter.observe(viewLifecycleOwner) { countryList ->
            adapter.updateCountryList(countryList)
        }
    }

    private fun showFilterDialogForCities(inflater: LayoutInflater) {
        val bindingForCitiesFilter = FragmentFilterCitiesBinding.inflate(inflater)
        val dialogForCities = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        dialogForCities.setContentView(bindingForCitiesFilter.root)
        dialogForCities.show()
    }
}