package com.example.currencyapp.presentation.home.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.example.currencyapp.domain.model.Currency
import com.example.currencyapp.presentation.ui.theme.ICON_SIZE_SMALL
import com.example.currencyapp.presentation.ui.theme.INPUT_BOX_HEIGHT
import com.example.currencyapp.presentation.ui.theme.PADDING_MEDIUM
import com.example.currencyapp.presentation.ui.theme.PADDING_SMALL
import com.example.currencyapp.presentation.ui.theme.RADIUS_SMALL
import com.example.currencyapp.presentation.ui.theme.WHITE
import com.example.currencyapp.util.CurrencyCode
import org.jetbrains.compose.resources.painterResource

@Composable
fun CurrencyInput(
    modifier: Modifier = Modifier,
    label: String,
    currency: Currency,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = WHITE
        )
        Spacer(
            modifier = Modifier.height(PADDING_SMALL)
        )
        AnimatedContent(
            targetState = currency,
            transitionSpec = {
                scaleIn(
                    tween(durationMillis = 400)
                ) + fadeIn(
                    tween(durationMillis = 800)
                ) togetherWith scaleOut(
                    tween(durationMillis = 400)
                ) + fadeOut(
                    tween(durationMillis = 800)
                )
            },
            label = "Content Animation"
        ) { currency ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(INPUT_BOX_HEIGHT)
                    .clip(RoundedCornerShape(RADIUS_SMALL))
                    .background(WHITE.copy(alpha = 0.05f))
                    .clickable {
                        onClick()
                    }
                    .padding(PADDING_MEDIUM),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    modifier = Modifier
                        .size(ICON_SIZE_SMALL),
                    painter = painterResource(
                        CurrencyCode.valueOf(currency.code).flag
                    ),
                    contentDescription = null
                )
                Spacer(
                    modifier = Modifier.width(PADDING_MEDIUM)
                )
                Text(
                    text = CurrencyCode.valueOf(currency.code).name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = WHITE,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}