package com.example.countryapi.ui

import com.example.countryapi.domain.GetCountriesWithHeadersUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countryapi.data.api.CountryRepository
import com.example.countryapi.data.model.Country
import com.example.countryapi.data.model.ListItem
import kotlinx.coroutines.launch

class CountryViewModel : ViewModel() {
    private val repository = CountryRepository()
    private val getCountriesWithHeadersUseCase = GetCountriesWithHeadersUseCase(repository)

    private val _listItems = MutableLiveData<List<ListItem>>()
    val listItems: LiveData<List<ListItem>> = _listItems

    private val _error = MutableLiveData<String?>()
    val error: MutableLiveData<String?> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun loadCountries() {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = getCountriesWithHeadersUseCase.execute()) {
                is GetCountriesWithHeadersUseCase.Result.Success -> {
                    _listItems.value = result.items
                    _error.value = null
                }
                is GetCountriesWithHeadersUseCase.Result.Error -> {
                    _listItems.value = emptyList()
                    _error.value = result.message
                }
            }
            _isLoading.value = false
        }
    }
}