package com.example.currencyapp.presentation.home.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
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
    var animationStarted by rememberSaveable {
        mutableStateOf(false)
    }
    val animatedRotation by animateFloatAsState(
        targetValue = if (animationStarted) 180f else 0f,
    )
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        sourceCurrencyState.DisplayResult(
            onIdle = {
                CurrencyInputLoading(
                    modifier = Modifier
                        .weight(1f)
                )
            },
            onLoading = {
                CurrencyInputLoading(
                    modifier = Modifier
                        .weight(1f)
                )
            },
            onSuccess = { sourceCurrency ->
                CurrencyInput(
                    modifier = Modifier
                        .weight(1f),
                    label = "From",
                    currency = sourceCurrency,
                    onClick = onSourceCurrencyClicked
                )
            }
        )
        Spacer(
            modifier = Modifier.width(PADDING_EXTRA_SMALL)
        )
        IconButton(
            modifier = Modifier
                .graphicsLayer {
                    rotationY = animatedRotation
                },
            onClick = {
                animationStarted = !animationStarted
                onSwitch()
            }
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
        targetCurrencyState.DisplayResult(
            onIdle = {
                CurrencyInputLoading(
                    modifier = Modifier
                        .weight(1f)
                )
            },
            onLoading = {
                CurrencyInputLoading(
                    modifier = Modifier
                        .weight(1f)
                )
            },
            onSuccess = { targetCurrency ->
                CurrencyInput(
                    modifier = Modifier
                        .weight(1f),
                    label = "To",
                    currency = targetCurrency,
                    onClick = onTargetCurrencyClicked
                )
            }
        )
    }
}