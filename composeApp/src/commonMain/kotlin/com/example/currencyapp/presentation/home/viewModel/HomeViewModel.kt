package com.example.currencyapp.presentation.home.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.currencyapp.domain.model.Currency
import com.example.currencyapp.domain.repository.LocalDataRepository
import com.example.currencyapp.domain.repository.LocalUserPreferencesRepository
import com.example.currencyapp.domain.repository.RemoteDataRepository
import com.example.currencyapp.presentation.home.component.CurrenciesStatus
import com.example.currencyapp.presentation.home.event.HomeEvent
import com.example.currencyapp.util.RequestState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class HomeViewModel(
    private val localUserPreferencesRepository: LocalUserPreferencesRepository,
    private val localDataRepository: LocalDataRepository,
    private val remoteDataRepository: RemoteDataRepository
): ScreenModel {
    private val _currenciesStatus: MutableStateFlow<CurrenciesStatus> = MutableStateFlow(CurrenciesStatus.Idle)
    val currenciesStatus: StateFlow<CurrenciesStatus> = _currenciesStatus.asStateFlow()

    private val _currencies: MutableStateFlow<List<Currency>> = MutableStateFlow(listOf())
    val currencies: StateFlow<List<Currency>> = _currencies.asStateFlow()

    private val _sourceCurrencyState: MutableStateFlow<RequestState<Currency>> = MutableStateFlow(RequestState.Idle)
    val sourceCurrencyState: StateFlow<RequestState<Currency>> = _sourceCurrencyState.asStateFlow()

    private val _targetCurrencyState: MutableStateFlow<RequestState<Currency>> = MutableStateFlow(RequestState.Idle)
    val targetCurrencyState: StateFlow<RequestState<Currency>> = _targetCurrencyState.asStateFlow()

    var isDataLoading by mutableStateOf(false)
        private set

    init {
        screenModelScope.launch {
            getCurrencies()
            getCurrenciesStatus()
            getSourceCurrencyState()
            getTargetCurrencyState()
        }
    }

    fun onEvent(event: HomeEvent) {
        when(event) {
            is HomeEvent.RefreshRates -> {
                screenModelScope.launch {
                    getCurrencies()
                    getCurrenciesStatus()
                }
            }
        }
    }

    private suspend fun getCurrencies() {
        try {
            //start loading data from database
            isDataLoading = true
            when(val currenciesRequestState = localDataRepository.readCurrencyObjects().first()) {
                is RequestState.Idle -> {}
                is RequestState.Loading -> {}
                is RequestState.Error -> {
                    //if error, print message
                    isDataLoading = false
                    println(currenciesRequestState.message)
                }
                is RequestState.Success -> {
                    //if success, check if the data is empty
                    val currenciesData = currenciesRequestState.data
                    if (currenciesData.isNotEmpty()) {
                        //if the data is not empty, check if the data is fresh
                        if (isDataFresh()) {
                            //if the data is fresh, update currencies
                            isDataLoading = false
                            _currencies.value = currenciesData
                        } else {
                            //if the data is not fresh, fetch and cache new data
                            fetchAndCacheNewCurrencies()
                        }
                    } else {
                        //if the data is empty, fetch and cache new data
                        fetchAndCacheNewCurrencies()
                    }
                }
            }
        } catch (e: Exception) {
            println(e.message)
        }
    }

    private suspend fun fetchAndCacheNewCurrencies() {
        //fetch data from api
        when(val newCurrenciesRequestState = remoteDataRepository.getLatestExchangeRates()) {
            is RequestState.Idle -> {}
            is RequestState.Loading -> {}
            is RequestState.Error -> {
                //if error, println message
                isDataLoading = false
                println(newCurrenciesRequestState.message)
            }
            is RequestState.Success -> {
                //if success, clean up old data
                localDataRepository.cleanUpCurrencyObjects()
                //cache new data
                val newCurrenciesData = newCurrenciesRequestState.data
                newCurrenciesData.forEach { currency ->
                    localDataRepository.insertCurrencyObject(currency)
                }
                //and update currencies with new data
                isDataLoading = false
                _currencies.value = newCurrenciesData
            }
        }
    }

    private suspend fun isDataFresh(): Boolean {
        return localUserPreferencesRepository.isDataFresh(
            currentTimeStamp = Clock.System.now().toEpochMilliseconds()
        )
    }

    private suspend fun getCurrenciesStatus() {
        _currenciesStatus.value = if (isDataFresh()) CurrenciesStatus.Fresh else CurrenciesStatus.Stale
    }

    private suspend fun getSourceCurrencyState() {
        val sourceCurrencyCode = localUserPreferencesRepository.getSourceCurrencyCode()
        val sourceCurrency = _currencies.value.find {  currency ->
            currency.code == sourceCurrencyCode
        }
        if (sourceCurrency != null) {
            _sourceCurrencyState.value = RequestState.Success(data = sourceCurrency)
        } else {
            _sourceCurrencyState.value = RequestState.Error(message = "Couldn't load the source currency.")
            println("Home ViewModel: SOURCE CURRENCY IS NULL")
        }
    }

    private suspend fun getTargetCurrencyState() {
        val targetCurrencyCode = localUserPreferencesRepository.getTargetCurrencyCode()
        val targetCurrency = _currencies.value.find { currency ->
            currency.code == targetCurrencyCode
        }
        if (targetCurrency != null) {
            _targetCurrencyState.value = RequestState.Success(data = targetCurrency)
        } else {
            _targetCurrencyState.value = RequestState.Error(message = "Couldn't load the target currency.")
            println("Home ViewModel: TARGET CURRENCY IS NULL")
        }
    }
}