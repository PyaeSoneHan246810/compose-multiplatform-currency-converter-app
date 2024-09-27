package com.example.currencyapp.presentation.home.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import com.example.currencyapp.presentation.ui.theme.ICON_SIZE_EXTRA_SMALL
import com.example.currencyapp.presentation.ui.theme.ICON_SIZE_SMALL
import com.example.currencyapp.presentation.ui.theme.PADDING_SMALL
import com.example.currencyapp.presentation.ui.theme.PRIMARY_COLOR
import com.example.currencyapp.presentation.ui.theme.RADIUS_SMALL
import com.example.currencyapp.presentation.ui.theme.SELECTION_CIRCLE_SIZE
import com.example.currencyapp.presentation.ui.theme.SURFACE_COLOR
import com.example.currencyapp.presentation.ui.theme.TEXT_COLOR
import com.example.currencyapp.util.CurrencyCode
import org.jetbrains.compose.resources.painterResource

@Composable
fun CurrencyItem(
    modifier: Modifier = Modifier,
    currencyCode: CurrencyCode,
    isSelected: Boolean,
    onSelected: (currencyCode: CurrencyCode) -> Unit
) {
    val saturation = remember {
        Animatable(
            initialValue = if (isSelected) 1f else 0f
        )
    }
    LaunchedEffect(isSelected) {
        saturation.animateTo(if (isSelected) 1f else 0f)
    }
    val colorMatrix = remember(saturation.value) {
        ColorMatrix().apply {
            setToSaturation(saturation.value)
        }
    }
    val animatedAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.5f,
        animationSpec = tween(durationMillis = 300)
    )
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(RADIUS_SMALL))
            .clickable {
                onSelected(currencyCode)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Image(
                modifier = Modifier
                    .size(ICON_SIZE_SMALL),
                painter = painterResource(currencyCode.flag),
                contentDescription = null,
                colorFilter = ColorFilter.colorMatrix(colorMatrix)
            )
            Spacer(
                modifier = Modifier
                    .width(PADDING_SMALL)
            )
            Text(
                modifier = Modifier
                    .alpha(animatedAlpha),
                text = currencyCode.name,
                color = TEXT_COLOR
            )
        }
        SelectionCircleBox(
            isSelected = isSelected
        )
    }
}

@Composable
fun SelectionCircleBox(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
) {
    val animatedColor by animateColorAsState(
        targetValue = if (isSelected) PRIMARY_COLOR else TEXT_COLOR.copy(alpha = 0.5f)
    )
    Box(
        modifier = modifier
            .size(SELECTION_CIRCLE_SIZE)
            .clip(CircleShape)
            .background(animatedColor),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = isSelected
        ) {
            Icon(
                modifier = Modifier
                    .size(ICON_SIZE_EXTRA_SMALL),
                imageVector = Icons.Rounded.Check,
                contentDescription = null,
                tint = SURFACE_COLOR
            )
        }
    }
}