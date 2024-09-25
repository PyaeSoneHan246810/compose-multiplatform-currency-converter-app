package com.example.currencyapp.domain.manager

interface LocalUserManager {
    suspend fun saveLastUpdatedTimeStamp(lastUpdated: String)
    suspend fun isDataFresh(currentTimeStamp: Long): Boolean
}