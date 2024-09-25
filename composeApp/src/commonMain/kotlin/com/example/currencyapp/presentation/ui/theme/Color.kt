package com.example.currencyapp.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val PRIMARY_COLOR
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF86A8FC) else Color(0xFF283556)

val HEADER_COLOR
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF0C0C0C) else Color(0xFF283556)

val SURFACE_COLOR
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF161616) else Color(0xFFFFFFFF)

val TEXT_COLOR
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFFFFFFFF) else Color(0xFF161616)

val WHITE = Color(0xFFFFFFFF)

val ORANGE = Color(0xffFB773C)

val GREEN = Color(0xff55CA7C)