package com.example.currencyapp.presentation.home.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import com.example.currencyapp.presentation.home.component.HomeHeader
import com.example.currencyapp.presentation.home.component.LoadingIndicator
import com.example.currencyapp.presentation.home.viewModel.HomeViewModel
import com.example.currencyapp.presentation.ui.theme.SURFACE_COLOR

class HomeScreen: Screen {
    @Composable
    override fun Content() {
        val homeViewModel = koinScreenModel<HomeViewModel>()
        val ratesStatus by homeViewModel.currenciesStatus.collectAsState()
        val currencies by homeViewModel.currencies.collectAsState()
        val sourceCurrencyState by homeViewModel.sourceCurrencyState.collectAsState()
        val targetCurrencyState by homeViewModel.targetCurrencyState.collectAsState()
        val isDataLoading = homeViewModel.isDataLoading
        var inputAmount by rememberSaveable {
            mutableStateOf("")
        }
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = SURFACE_COLOR
        ) {
            if (isDataLoading) {
                LoadingIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    HomeHeader(
                        modifier = Modifier
                            .fillMaxWidth(),
                        currenciesStatus = ratesStatus,
                        onRefresh = homeViewModel::onEvent,
                        sourceCurrencyState = sourceCurrencyState,
                        targetCurrencyState = targetCurrencyState,
                        onSourceCurrencyClicked = {},
                        onTargetCurrencyClicked = {},
                        onSwitch = {},
                        currencyAmountInput = inputAmount,
                        onCurrencyInputAmountChanged = { newInputAmount ->
                            inputAmount = newInputAmount
                        }
                    )
                }
            }
        }
    }
}