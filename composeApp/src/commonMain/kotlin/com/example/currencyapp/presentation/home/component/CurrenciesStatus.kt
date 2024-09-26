package com.example.currencyapp.presentation.home.component

import androidx.compose.ui.graphics.Color
import com.example.currencyapp.presentation.ui.theme.GREEN
import com.example.currencyapp.presentation.ui.theme.ORANGE
import com.example.currencyapp.presentation.ui.theme.WHITE

enum class CurrenciesStatus(
    val title: String,
    val color: Color
) {
    Idle(
        title = "Rates",
        color = WHITE
    ),
    Fresh(
        title = "Fresh Rates",
        color = GREEN
    ),
    Stale(
        title = "Rates are not fresh",
        color = ORANGE
    )
}