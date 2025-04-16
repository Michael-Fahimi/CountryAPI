package com.example.countryapi.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countryapi.data.api.CountryRepository
import com.example.countryapi.data.model.Country
import kotlinx.coroutines.launch

class CountryViewModel : ViewModel() {
    private val repository = CountryRepository()

    private val _countries = MutableLiveData<List<Country>>()
    val countries: LiveData<List<Country>> = _countries

    private val _error = MutableLiveData<String?>()
    val error: MutableLiveData<String?> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun loadCountries() {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = repository.getCountries()) {
                is CountryRepository.Result.Success -> {
                    _countries.value = result.countries
                    _error.value = null
                }
                is CountryRepository.Result.Error -> {
                    _countries.value = emptyList()
                    _error.value = result.message
                }
            }
            _isLoading.value = false
        }
    }
}
