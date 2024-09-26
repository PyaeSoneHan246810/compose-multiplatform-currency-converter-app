package com.example.currencyapp.presentation.home.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.currencyapp.domain.model.Currency
import com.example.currencyapp.presentation.ui.theme.ICON_SIZE_MEDIUM
import com.example.currencyapp.presentation.ui.theme.PADDING_EXTRA_SMALL
import com.example.currencyapp.presentation.ui.theme.WHITE
import com.example.currencyapp.util.RequestState
import currencyapp.composeapp.generated.resources.Res
import currencyapp.composeapp.generated.resources.switch_ic
import org.jetbrains.compose.resources.painterResource

@Composable
fun CurrencyInputBar(
    modifier: Modifier = Modifier,
    sourceCurrencyState: RequestState<Currency>,
    targetCurrencyState: RequestState<Currency>,
    onSourceCurrencyClicked: () -> Unit,
    onTargetCurrencyClicked: () -> Unit,
    onSwitch: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (sourceCurrencyState is RequestState.Success) {
            CurrencyInput(
                modifier = Modifier
                    .weight(1f),
                label = "From",
                currency = sourceCurrencyState.data,
                onClick = onSourceCurrencyClicked
            )
        } else {
            CurrencyInputLoading(
                modifier = Modifier
                    .weight(1f)
            )
        }
        Spacer(
            modifier = Modifier.width(PADDING_EXTRA_SMALL)
        )
        IconButton(
            onClick = onSwitch
        ) {
            Icon(
                modifier = Modifier
                    .size(ICON_SIZE_MEDIUM),
                painter = painterResource(Res.drawable.switch_ic),
                contentDescription = null,
                tint = WHITE
            )
        }
        Spacer(
            modifier = Modifier.width(PADDING_EXTRA_SMALL)
        )
        if (targetCurrencyState is RequestState.Success) {
            CurrencyInput(
                modifier = Modifier
                    .weight(1f),
                label = "To",
                currency = targetCurrencyState.data,
                onClick = onTargetCurrencyClicked
            )
        } else {
            CurrencyInputLoading(
                modifier = Modifier
                    .weight(1f)
            )
        }
    }
}