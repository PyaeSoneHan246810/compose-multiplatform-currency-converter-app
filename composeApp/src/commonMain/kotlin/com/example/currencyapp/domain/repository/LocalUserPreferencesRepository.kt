package com.example.currencyapp.domain.repository

import kotlinx.coroutines.flow.Flow

interface LocalUserPreferencesRepository {
    suspend fun saveLastUpdatedTimeStamp(lastUpdated: String)
    suspend fun isDataFresh(currentTimeStamp: Long): Boolean
    suspend fun saveSourceCurrencyCode(code: String)
    suspend fun saveTargetCurrencyCode(code: String)
    fun getSourceCurrencyCode(): Flow<String>
    fun getTargetCurrencyCode(): Flow<String>

}