package com.example.currencyapp.presentation.home.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.currencyapp.domain.model.Currency
import com.example.currencyapp.presentation.home.event.HomeEvent
import com.example.currencyapp.presentation.ui.theme.BEBAS_FONT_FAMILY
import com.example.currencyapp.presentation.ui.theme.BUTTON_HEIGHT
import com.example.currencyapp.presentation.ui.theme.PADDING_LARGE
import com.example.currencyapp.presentation.ui.theme.PADDING_MEDIUM
import com.example.currencyapp.presentation.ui.theme.PADDING_SMALL
import com.example.currencyapp.presentation.ui.theme.PRIMARY_COLOR
import com.example.currencyapp.presentation.ui.theme.RADIUS_ROUND
import com.example.currencyapp.presentation.ui.theme.TEXT_COLOR
import com.example.currencyapp.presentation.ui.theme.WHITE
import com.example.currencyapp.util.RequestState
import kotlin.math.round

@Composable
fun HomeBody(
    modifier: Modifier = Modifier,
    sourceCurrencyState: RequestState<Currency>,
    targetCurrencyState: RequestState<Currency>,
    amount: Double,
    onConvertClick: (event: HomeEvent) -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            AnimatedVisibility(
                visible = sourceCurrencyState.isSuccess() && targetCurrencyState.isSuccess(),
                enter = slideInVertically(
                    initialOffsetY = { it / 2 }
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = PADDING_LARGE),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AnimatedContent(
                        targetState = amount,
                        transitionSpec = {
                            slideInVertically(
                                tween(durationMillis = 400)
                            ) + fadeIn(
                                tween(durationMillis = 800)
                            ) togetherWith slideOutVertically(
                                tween(durationMillis = 400)
                            ) + fadeOut(
                                tween(durationMillis = 800)
                            )
                        }
                    ) { targetAmount ->
                        Text(
                            text = targetAmount.round(2).toString(),
                            fontFamily = BEBAS_FONT_FAMILY,
                            fontWeight = FontWeight.Medium,
                            fontSize = MaterialTheme.typography.displayMedium.fontSize,
                            color = TEXT_COLOR
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .height(PADDING_SMALL)
                    )
                    Text(
                        text = targetCurrencyState.getSuccessData().code,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = TEXT_COLOR
                    )
                }
            }
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(BUTTON_HEIGHT)
                .padding(horizontal = PADDING_LARGE),
            shape = RoundedCornerShape(RADIUS_ROUND),
            onClick = {
                onConvertClick(HomeEvent.ConvertCurrency)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = PRIMARY_COLOR,
                contentColor = WHITE
            )
        ) {
            Text(
                text = "Convert",
            )
        }
        Spacer(
            modifier = Modifier
                .height(PADDING_MEDIUM)
        )
    }
}

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}