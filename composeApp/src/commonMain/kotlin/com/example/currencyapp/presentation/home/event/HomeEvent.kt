package com.example.currencyapp.presentation.home.event

sealed class HomeEvent {
    data object RefreshRates: HomeEvent()
    data object SwitchCurrencies: HomeEvent()

    data class ChangeInputAmount(val amount: String): HomeEvent()
    data class SaveSourceCurrencyCode(val code: String): HomeEvent()
    data class SaveTargetCurrencyCode(val code: String): HomeEvent()
    data object ConvertCurrency: HomeEvent()
}