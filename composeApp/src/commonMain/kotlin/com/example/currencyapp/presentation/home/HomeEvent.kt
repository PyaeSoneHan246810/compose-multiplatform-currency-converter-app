package com.example.currencyapp.presentation.home

sealed class HomeEvent {
    data object RefreshRates: HomeEvent()
}