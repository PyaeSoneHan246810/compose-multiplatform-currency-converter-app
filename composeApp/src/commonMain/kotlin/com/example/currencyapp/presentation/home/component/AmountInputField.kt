package com.example.currencyapp.presentation.home.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.example.currencyapp.presentation.ui.theme.INPUT_BOX_HEIGHT
import com.example.currencyapp.presentation.ui.theme.RADIUS_SMALL
import com.example.currencyapp.presentation.ui.theme.WHITE

@Composable
fun AmountInputField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChanged: (newValue: String) -> Unit
) {
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .height(INPUT_BOX_HEIGHT)
            .clip(RoundedCornerShape(RADIUS_SMALL)),
        value = value,
        onValueChange = onValueChanged,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = WHITE.copy(alpha = 0.05f),
            focusedContainerColor = WHITE.copy(alpha = 0.05f),
            unfocusedTextColor = WHITE,
            focusedTextColor = WHITE,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            cursorColor = WHITE
        ),
        placeholder = {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "0.0",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                    color = WHITE.copy(alpha = 0.8f)
                )
            }
        },
        textStyle = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Done
        )
    )
}