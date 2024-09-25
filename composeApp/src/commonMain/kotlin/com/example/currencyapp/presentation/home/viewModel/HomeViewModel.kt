package com.example.currencyapp.presentation.home.viewModel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.currencyapp.domain.api.CurrencyApi
import com.example.currencyapp.domain.manager.LocalUserManager
import com.example.currencyapp.presentation.home.HomeEvent
import com.example.currencyapp.presentation.home.component.RatesStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class HomeViewModel(
    private val localUserManager: LocalUserManager,
    private val currencyApi: CurrencyApi
): ScreenModel {
    private val _ratesStatus: MutableStateFlow<RatesStatus> = MutableStateFlow(RatesStatus.Idle)
    val ratesStatus: StateFlow<RatesStatus> = _ratesStatus.asStateFlow()

    init {
        getNewRates()
    }

    fun onEvent(event: HomeEvent) {
        when(event) {
            is HomeEvent.RefreshRates -> {
                getNewRates()
            }
        }
    }

    private fun getRatesStatus() {
        screenModelScope.launch {
            val isFresh =  localUserManager.isDataFresh(
                currentTimeStamp = Clock.System.now().toEpochMilliseconds()
            )
            _ratesStatus.value = if (isFresh) RatesStatus.Fresh else RatesStatus.Stale
        }
    }

    private fun getNewRates() {
        screenModelScope.launch {
            try {
                currencyApi.getLatestExchangeRates()
                getRatesStatus()
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }
}