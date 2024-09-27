package com.example.currencyapp.presentation.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import currencyapp.composeapp.generated.resources.Res
import currencyapp.composeapp.generated.resources.bebas_neue_regular
import org.jetbrains.compose.resources.Font

val BEBAS_FONT_FAMILY
    @Composable
    get() = FontFamily(
        Font(Res.font.bebas_neue_regular)
    )