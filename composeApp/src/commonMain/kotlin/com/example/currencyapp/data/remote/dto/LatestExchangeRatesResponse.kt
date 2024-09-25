package com.example.currencyapp.data.remote.dto

import com.example.currencyapp.domain.model.Currency
import com.example.currencyapp.domain.model.MetaData
import kotlinx.serialization.Serializable

@Serializable
data class LatestExchangeRatesResponse(
    val meta: MetaData,
    val data: Map<String, Currency>
)