package com.example.currencyapp.presentation.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.example.currencyapp.presentation.home.HomeEvent
import com.example.currencyapp.presentation.ui.theme.HEADER_COLOR
import com.example.currencyapp.presentation.ui.theme.PADDING_LARGE
import com.example.currencyapp.presentation.ui.theme.RADIUS_SMALL

@Composable
fun HomeHeader(
    modifier: Modifier = Modifier,
    ratesStatus: RatesStatus,
    onRefresh: (event: HomeEvent) -> Unit
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(bottomStart = RADIUS_SMALL, bottomEnd = RADIUS_SMALL)),
        color = HEADER_COLOR
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PADDING_LARGE)
                .statusBarsPadding()
        ) {
            RatesStatusBar(
                modifier = Modifier
                    .fillMaxWidth(),
                ratesStatus = ratesStatus,
                onRefresh = {
                    onRefresh(HomeEvent.RefreshRates)
                }
            )
        }
    }
}