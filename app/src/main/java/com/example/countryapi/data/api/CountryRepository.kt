package com.example.countryapi.data.api

import com.example.countryapi.data.model.Country
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class CountryRepository {

    private val apiService = CountryApiService.create()

    // sealed class to represent the result of the API call
    sealed class Result {
        data class Success(val countries: List<Country>) : Result()
        data class Error(val message: String) : Result()
    }


    // function to fetch countries from the API
    // handles network errors and unexpected exceptions
    suspend fun getCountries(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getCountries()
                if (response.isSuccessful) {
                    val countries = response.body() ?: emptyList()
                    Result.Success(countries)
                } else {
                    Result.Error("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: IOException) {
                Result.Error("Network error: ${e.message}")
            } catch (e: Exception) {
                Result.Error("Unexpected error: ${e.message}")
            }
        }
    }
}