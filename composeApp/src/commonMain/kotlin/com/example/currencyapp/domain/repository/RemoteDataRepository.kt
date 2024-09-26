package com.example.currencyapp.domain.repository

import com.example.currencyapp.domain.model.Currency
import com.example.currencyapp.util.RequestState

interface RemoteDataRepository {
    suspend fun getLatestExchangeRates(): RequestState<List<Currency>>
}