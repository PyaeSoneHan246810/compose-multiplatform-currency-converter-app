package com.example.currencyapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Currency(
    val code: String,
    val value: Double
)
