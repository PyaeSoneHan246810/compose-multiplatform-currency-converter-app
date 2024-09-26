package com.example.currencyapp.presentation.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.example.currencyapp.domain.model.Currency
import com.example.currencyapp.presentation.home.event.HomeEvent
import com.example.currencyapp.presentation.ui.theme.HEADER_COLOR
import com.example.currencyapp.presentation.ui.theme.PADDING_LARGE
import com.example.currencyapp.presentation.ui.theme.PADDING_MEDIUM
import com.example.currencyapp.presentation.ui.theme.RADIUS_MEDIUM
import com.example.currencyapp.util.RequestState

@Composable
fun HomeHeader(
    modifier: Modifier = Modifier,
    currenciesStatus: CurrenciesStatus,
    onRefresh: (event: HomeEvent) -> Unit,
    sourceCurrencyState: RequestState<Currency>,
    targetCurrencyState: RequestState<Currency>,
    onSourceCurrencyClicked: () -> Unit,
    onTargetCurrencyClicked: () -> Unit,
    onSwitch: () -> Unit,
    currencyAmountInput: String,
    onCurrencyInputAmountChanged: (newInputAmount: String) -> Unit
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(bottomStart = RADIUS_MEDIUM, bottomEnd = RADIUS_MEDIUM)),
        color = HEADER_COLOR
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PADDING_LARGE)
                .statusBarsPadding()
        ) {
            CurrenciesStatusBar(
                modifier = Modifier
                    .fillMaxWidth(),
                currenciesStatus = currenciesStatus,
                onRefresh = {
                    onRefresh(HomeEvent.RefreshRates)
                }
            )
            Spacer(
                modifier = Modifier
                    .height(PADDING_MEDIUM)
            )
            CurrencyInputBar(
                modifier = Modifier
                    .fillMaxWidth(),
                sourceCurrencyState = sourceCurrencyState,
                targetCurrencyState = targetCurrencyState,
                onSourceCurrencyClicked = onSourceCurrencyClicked,
                onTargetCurrencyClicked = onTargetCurrencyClicked,
                onSwitch = onSwitch
            )
            Spacer(
                modifier = Modifier
                    .height(PADDING_MEDIUM)
            )
            AmountInputField(
                value = currencyAmountInput,
                onValueChanged = onCurrencyInputAmountChanged
            )
        }
    }
}