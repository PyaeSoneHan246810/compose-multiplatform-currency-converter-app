package com.example.currencyapp.presentation.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.currencyapp.presentation.ui.theme.ICON_SIZE_LARGE
import com.example.currencyapp.presentation.ui.theme.WHITE

@Composable
fun CurrencyInputLoading(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(ICON_SIZE_LARGE),
            color = WHITE,
        )
    }
}