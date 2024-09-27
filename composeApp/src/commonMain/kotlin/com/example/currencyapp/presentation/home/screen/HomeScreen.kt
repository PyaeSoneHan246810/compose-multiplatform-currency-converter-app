package com.example.currencyapp.presentation.home.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import com.example.currencyapp.presentation.home.component.CurrencyPickerDialog
import com.example.currencyapp.presentation.home.component.DisplayResult
import com.example.currencyapp.presentation.home.component.HomeBody
import com.example.currencyapp.presentation.home.component.HomeHeader
import com.example.currencyapp.presentation.home.component.LoadingIndicator
import com.example.currencyapp.presentation.home.event.HomeEvent
import com.example.currencyapp.presentation.home.viewModel.HomeViewModel
import com.example.currencyapp.presentation.ui.theme.SURFACE_COLOR
import com.example.currencyapp.util.CurrencyCode
import com.example.currencyapp.util.CurrencyType

class HomeScreen: Screen {
    @Composable
    override fun Content() {
        val homeViewModel = koinScreenModel<HomeViewModel>()
        val ratesStatus by homeViewModel.currenciesStatus.collectAsState()
        val currenciesState by homeViewModel.currenciesState.collectAsState()
        val sourceCurrencyState by homeViewModel.sourceCurrencyState.collectAsState()
        val targetCurrencyState by homeViewModel.targetCurrencyState.collectAsState()
        val inputAmount = homeViewModel.inputAmount
        val exchangeAmount = homeViewModel.exchangeAmount
        var openCurrencyPicker by rememberSaveable {
            mutableStateOf(false)
        }
        var currencyPickerCurrencyType: CurrencyType by remember {
            mutableStateOf(CurrencyType.None)
        }
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = SURFACE_COLOR
        ) {
            currenciesState.DisplayResult(
                onIdle = {
                    LoadingIndicator(
                        modifier = Modifier
                            .fillMaxSize()
                    )
                },
                onLoading = {
                    LoadingIndicator(
                        modifier = Modifier
                            .fillMaxSize()
                    )
                },
                onSuccess = { currencies ->
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
                            onSourceCurrencyClicked = {
                                if (sourceCurrencyState.isSuccess()) {
                                    currencyPickerCurrencyType = CurrencyType.Source(currencyCode = CurrencyCode.valueOf(sourceCurrencyState.getSuccessData().code))
                                    openCurrencyPicker = true
                                }
                            },
                            onTargetCurrencyClicked = {
                                if (targetCurrencyState.isSuccess()) {
                                    currencyPickerCurrencyType = CurrencyType.Target(currencyCode = CurrencyCode.valueOf(targetCurrencyState.getSuccessData().code))
                                    openCurrencyPicker = true
                                }
                            },
                            onSwitch = homeViewModel::onEvent,
                            currencyAmountInput = inputAmount,
                            onCurrencyInputAmountChanged = homeViewModel::onEvent
                        )
                        HomeBody(
                            modifier = Modifier
                                .fillMaxWidth()
                                .navigationBarsPadding()
                                .imePadding(),
                            sourceCurrencyState = sourceCurrencyState,
                            targetCurrencyState = targetCurrencyState,
                            amount = exchangeAmount,
                            onConvertClick = homeViewModel::onEvent
                        )
                    }
                    if (openCurrencyPicker && currencyPickerCurrencyType != CurrencyType.None) {
                        CurrencyPickerDialog(
                            currencies = currencies,
                            currencyType = currencyPickerCurrencyType,
                            onConfirm = { currencyCode ->
                                when(currencyPickerCurrencyType) {
                                    is CurrencyType.None -> {}
                                    is CurrencyType.Source -> {
                                        homeViewModel.onEvent(HomeEvent.SaveSourceCurrencyCode(code = currencyCode.name))
                                    }
                                    is CurrencyType.Target -> {
                                        homeViewModel.onEvent(HomeEvent.SaveTargetCurrencyCode(code = currencyCode.name))
                                    }
                                }
                                currencyPickerCurrencyType = CurrencyType.None
                                openCurrencyPicker = false
                            },
                            onDismiss = {
                                currencyPickerCurrencyType = CurrencyType.None
                                openCurrencyPicker = false
                            }
                        )
                    }
                }
            )
        }
    }
}