package com.example.currencyapp.domain.repository

import com.example.currencyapp.domain.model.Currency
import com.example.currencyapp.util.RequestState
import kotlinx.coroutines.flow.Flow

interface LocalDataRepository {
    fun configureRealmDatabase()
    suspend fun insertCurrencyObject(currency: Currency)
    fun readCurrencyObjects(): Flow<RequestState<List<Currency>>>
    suspend fun cleanUpCurrencyObjects()
}