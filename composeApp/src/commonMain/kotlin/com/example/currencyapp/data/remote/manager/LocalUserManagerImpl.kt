package com.example.currencyapp.data.remote.manager

import com.example.currencyapp.domain.manager.LocalUserManager
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.toFlowSettings
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class LocalUserManagerImpl(
    private val settings: Settings
): LocalUserManager {
    @OptIn(ExperimentalSettingsApi::class)
    private val flowSettings = (settings as ObservableSettings).toFlowSettings()

    @OptIn(ExperimentalSettingsApi::class)
    override suspend fun saveLastUpdatedTimeStamp(lastUpdated: String) {
        flowSettings.putLong(
            key = TIMESTAMP_KEY,
            value = Instant.parse(lastUpdated).toEpochMilliseconds()
        )
    }

    @OptIn(ExperimentalSettingsApi::class)
    override suspend fun isDataFresh(currentTimeStamp: Long): Boolean {
        val savedTimeStamp = flowSettings.getLong(
            key = TIMESTAMP_KEY,
            defaultValue = 0L
        )
        return if (savedTimeStamp != 0L) {
            val savedDateTime = Instant.fromEpochMilliseconds(savedTimeStamp).toLocalDateTime(
                TimeZone.currentSystemDefault())
            val currentDateTime = Instant.fromEpochMilliseconds(currentTimeStamp).toLocalDateTime(
                TimeZone.currentSystemDefault()
            )
            val daysDifference = currentDateTime.date.dayOfYear - savedDateTime.date.dayOfYear
            daysDifference <= 1
        } else {
            false
        }
    }

    companion object {
        private const val TIMESTAMP_KEY = "lastUpdated"
    }
}