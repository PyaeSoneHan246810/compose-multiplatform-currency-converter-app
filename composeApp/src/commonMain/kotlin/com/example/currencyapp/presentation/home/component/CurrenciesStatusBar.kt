package com.example.currencyapp.presentation.home.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.currencyapp.presentation.ui.theme.HOME_IMAGE_SIZE
import com.example.currencyapp.presentation.ui.theme.ICON_SIZE_LARGE
import com.example.currencyapp.presentation.ui.theme.ORANGE
import com.example.currencyapp.presentation.ui.theme.PADDING_EXTRA_SMALL
import com.example.currencyapp.presentation.ui.theme.PADDING_MEDIUM
import com.example.currencyapp.presentation.ui.theme.WHITE
import com.example.currencyapp.util.getFormattedCurrentDateTime
import currencyapp.composeapp.generated.resources.Res
import currencyapp.composeapp.generated.resources.exchange_rate
import org.jetbrains.compose.resources.painterResource

@Composable
fun CurrenciesStatusBar(
    modifier: Modifier = Modifier,
    currenciesStatus: CurrenciesStatus,
    onRefresh: () -> Unit,
) {
    val areCurrenciesStale = currenciesStatus == CurrenciesStatus.Stale
    val formattedCurrentDateTime = getFormattedCurrentDateTime()
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(HOME_IMAGE_SIZE),
                painter = painterResource(Res.drawable.exchange_rate),
                contentDescription = null
            )
            Spacer(
                modifier = Modifier
                    .width(PADDING_MEDIUM)
            )
            Column {
                Text(
                    text = formattedCurrentDateTime,
                    color = WHITE,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(
                    modifier = Modifier
                        .height(PADDING_EXTRA_SMALL),
                )
                Text(
                    text = currenciesStatus.title,
                    color = currenciesStatus.color,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        AnimatedVisibility(
            visible = areCurrenciesStale
        ) {
            IconButton(
                onClick = onRefresh
            ) {
                Icon(
                    modifier = Modifier
                        .size(ICON_SIZE_LARGE),
                    imageVector = Icons.Rounded.Refresh,
                    contentDescription = null,
                    tint = ORANGE
                )
            }
        }
    }
}