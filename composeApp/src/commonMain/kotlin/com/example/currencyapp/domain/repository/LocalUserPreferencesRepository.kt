package com.example.currencyapp.domain.repository

interface LocalUserPreferencesRepository {
    suspend fun saveLastUpdatedTimeStamp(lastUpdated: String)
    suspend fun isDataFresh(currentTimeStamp: Long): Boolean
    suspend fun saveSourceCurrencyCode(code: String)
    suspend fun saveTargetCurrencyCode(code: String)
    suspend fun getSourceCurrencyCode(): String
    suspend fun getTargetCurrencyCode(): String

}