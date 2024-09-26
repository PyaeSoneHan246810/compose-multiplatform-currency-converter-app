package com.example.currencyapp.presentation.home.event

sealed class HomeEvent {
    data object RefreshRates: HomeEvent()
}