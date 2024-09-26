package com.example.currencyapp.data.remote.api

import com.example.currencyapp.data.remote.dto.LatestExchangeRatesResponse
import com.example.currencyapp.domain.model.Currency
import com.example.currencyapp.domain.repository.LocalUserPreferencesRepository
import com.example.currencyapp.domain.repository.RemoteDataRepository
import com.example.currencyapp.util.Constants
import com.example.currencyapp.util.CurrencyCode
import com.example.currencyapp.util.RequestState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.json.Json

class RemoteDataRepositoryImpl(
    private val httpClient: HttpClient,
    private val localUserPreferencesRepository: LocalUserPreferencesRepository,
): RemoteDataRepository {
    override suspend fun getLatestExchangeRates(): RequestState<List<Currency>> {
        return try {
            val response = httpClient.get(
                urlString = Constants.BASE_URL
            )
            if (response.status.value == 200) {
                val latestExchangeRatesResponse = Json.decodeFromString<LatestExchangeRatesResponse>(response.body())
                val lastUpdated = latestExchangeRatesResponse.meta.lastUpdatedAt
                val availableCurrencyCodes = latestExchangeRatesResponse.data.keys
                    .filter { key ->
                        CurrencyCode.entries
                            .map {  entry ->
                                entry.name
                            }
                            .contains(key)
                    }
                val currencyList = latestExchangeRatesResponse.data.values.filter { currency ->
                    availableCurrencyCodes.contains(currency.code)
                }
                localUserPreferencesRepository.saveLastUpdatedTimeStamp(lastUpdated)
                RequestState.Success(
                    data = currencyList
                )
            } else {
                RequestState.Error(
                    message = "Error Code: ${response.status.value}"
                )
            }
        } catch (e: Exception) {
            RequestState.Error(
                message = e.message.toString()
            )
        }
    }
}