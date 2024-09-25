package com.example.currencyapp.domain.api

import com.example.currencyapp.domain.model.Currency
import com.example.currencyapp.util.RequestState

interface CurrencyApi {
    suspend fun getLatestExchangeRates(): RequestState<List<Currency>>
}