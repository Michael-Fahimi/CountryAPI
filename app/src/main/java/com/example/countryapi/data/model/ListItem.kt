package com.example.countryapi.data.model


// sealed class to represent items in the list
sealed class ListItem {
    data class CountryItem(val country: Country) : ListItem()
    data class HeaderItem(val letter: Char) : ListItem()
}