package com.example.currencyapp.presentation.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import com.example.currencyapp.domain.model.Currency
import com.example.currencyapp.presentation.ui.theme.PADDING_MEDIUM
import com.example.currencyapp.util.CurrencyCode

@Composable
fun CurrenciesList(
    modifier: Modifier = Modifier,
    currenciesList: SnapshotStateList<Currency>,
    selectedCurrencyCode: CurrencyCode,
    onCurrencySelected: (currencyCode: CurrencyCode) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(PADDING_MEDIUM)
    ) {
        items(
            items = currenciesList,
            key = { currency ->
                currency._id.toString()
            }
        ) { currency ->
            CurrencyItem(
                modifier = Modifier
                    .fillMaxWidth(),
                currencyCode = CurrencyCode.valueOf(currency.code),
                isSelected = currency.code == selectedCurrencyCode.name,
                onSelected = onCurrencySelected
            )
        }
    }
}