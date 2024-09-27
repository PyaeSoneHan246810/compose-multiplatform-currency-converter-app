package com.example.currencyapp.presentation.home.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.example.currencyapp.domain.model.Currency
import com.example.currencyapp.presentation.ui.theme.CURRENCIES_LIST_HEIGHT
import com.example.currencyapp.presentation.ui.theme.PADDING_MEDIUM
import com.example.currencyapp.presentation.ui.theme.PRIMARY_COLOR
import com.example.currencyapp.presentation.ui.theme.RADIUS_ROUND
import com.example.currencyapp.presentation.ui.theme.SURFACE_COLOR
import com.example.currencyapp.presentation.ui.theme.TEXT_COLOR
import com.example.currencyapp.util.CurrencyCode
import com.example.currencyapp.util.CurrencyType

@Composable
fun CurrencyPickerDialog(
    modifier: Modifier = Modifier,
    currencies: List<Currency>,
    currencyType: CurrencyType,
    onConfirm: (currencyCode: CurrencyCode) -> Unit,
    onDismiss: () -> Unit
) {
    val currenciesList = remember {
        mutableStateListOf<Currency>().apply {
            addAll(currencies)
        }
    }
    var searchQuery by rememberSaveable {
        mutableStateOf("")
    }
    var selectedCurrencyCode by remember {
        mutableStateOf(currencyType.code)
    }
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        containerColor = SURFACE_COLOR,
        title = {
            Text(
                text = "Select a currency.",
                color = TEXT_COLOR
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(RADIUS_ROUND)),
                    value = searchQuery,
                    onValueChange = { newQuery ->
                        searchQuery = newQuery.uppercase()
                        if (searchQuery.isNotEmpty()) {
                            val filteredCurrencies = currenciesList.filter { currency ->
                                currency.code.contains(newQuery.uppercase())
                            }
                            currenciesList.clear()
                            currenciesList.addAll(filteredCurrencies)
                        } else {
                            currenciesList.clear()
                            currenciesList.addAll(currencies)
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedContainerColor = TEXT_COLOR.copy(alpha = 0.1f),
                        focusedContainerColor = TEXT_COLOR.copy(alpha = 0.1f),
                        cursorColor = TEXT_COLOR
                    )
                )
                Spacer(
                    modifier = Modifier
                        .height(PADDING_MEDIUM)
                )
                AnimatedContent(
                    targetState = currenciesList
                ) { currenciesList ->
                    if (currenciesList.isNotEmpty()) {
                        CurrenciesList(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(CURRENCIES_LIST_HEIGHT),
                            currenciesList = currenciesList,
                            selectedCurrencyCode = selectedCurrencyCode,
                            onCurrencySelected = { currencyCode ->
                                selectedCurrencyCode = currencyCode
                            }
                        )
                    } else {
                        CurrenciesEmptyList(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(CURRENCIES_LIST_HEIGHT)
                        )
                    }
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = TEXT_COLOR.copy(
                        alpha = 0.6f
                    )
                ),
                content = {
                    Text("Cancel")
                }
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(selectedCurrencyCode)
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = PRIMARY_COLOR
                ),
                content = {
                    Text("Confirm")
                }
            )
        },
    )
}