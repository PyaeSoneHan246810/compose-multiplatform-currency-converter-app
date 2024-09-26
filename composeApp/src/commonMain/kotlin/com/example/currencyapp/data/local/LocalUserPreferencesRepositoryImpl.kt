package com.example.currencyapp.data.local

import com.example.currencyapp.domain.repository.LocalUserPreferencesRepository
import com.example.currencyapp.util.CurrencyCode
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.toFlowSettings
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class LocalUserPreferencesRepositoryImpl(
    private val settings: Settings
): LocalUserPreferencesRepository {
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

    @OptIn(ExperimentalSettingsApi::class)
    override suspend fun saveSourceCurrencyCode(code: String) {
        flowSettings.putString(
            key = SOURCE_CURRENCY_CODE_KEY,
            value = code
        )
    }

    @OptIn(ExperimentalSettingsApi::class)
    override suspend fun saveTargetCurrencyCode(code: String) {
        flowSettings.putString(
            key = TARGET_CURRENCY_CODE_KEY,
            value = code
        )
    }

    @OptIn(ExperimentalSettingsApi::class)
    override suspend fun getSourceCurrencyCode(): String {
        return flowSettings.getString(
            key = SOURCE_CURRENCY_CODE_KEY,
            defaultValue = DEFAULT_SOURCE_CURRENCY_CODE
        )
    }

    @OptIn(ExperimentalSettingsApi::class)
    override suspend fun getTargetCurrencyCode(): String {
        return flowSettings.getString(
            key = TARGET_CURRENCY_CODE_KEY,
            defaultValue = DEFAULT_TARGET_CURRENCY_CODE
        )
    }

    companion object {
        private const val TIMESTAMP_KEY = "lastUpdated"
        private const val SOURCE_CURRENCY_CODE_KEY = "sourceCurrencyCode"
        private const val TARGET_CURRENCY_CODE_KEY = "targetCurrencyCode"
        private val DEFAULT_SOURCE_CURRENCY_CODE = CurrencyCode.USD.name
        private val DEFAULT_TARGET_CURRENCY_CODE = CurrencyCode.EUR.name
    }
}