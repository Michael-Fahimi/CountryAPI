package com.example.countryapi.domain
import com.example.countryapi.data.api.CountryRepository
import com.example.countryapi.data.model.Country
import com.example.countryapi.data.model.ListItem



class GetCountriesWithHeadersUseCase(private val repository: CountryRepository) {

    sealed class Result {
        data class Success(val items: List< ListItem>) : Result()
        data class Error(val message: String) : Result()
    }

    suspend fun execute(): Result {
        return when (val result = repository.getCountries()) {
            is CountryRepository.Result.Success -> {
                val sortedCountries = result.countries.sortedBy { it.name }
                Result.Success(createListWithHeaders(sortedCountries))
            }
            is CountryRepository.Result.Error -> {
                Result.Error(result.message)
            }
        }
    }

    private fun createListWithHeaders(countries: List<Country>): List<ListItem> {
        if (countries.isEmpty()) return emptyList()

        val items = mutableListOf<ListItem>()
        var currentLetter: Char? = null

        countries.forEach { country ->
            val firstLetter = country.name.first().uppercaseChar()

            if (currentLetter != firstLetter) {
                currentLetter = firstLetter
                items.add(ListItem.HeaderItem(firstLetter))
            }

            items.add(ListItem.CountryItem(country))
        }

        return items
    }
}