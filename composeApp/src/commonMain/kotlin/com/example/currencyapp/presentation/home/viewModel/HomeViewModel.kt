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
import com.example.currencyapp.presentation.home.event.HomeEvent
import com.example.currencyapp.util.CurrenciesStatus
import com.example.currencyapp.util.RequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class HomeViewModel(
    private val localUserPreferencesRepository: LocalUserPreferencesRepository,
    private val localDataRepository: LocalDataRepository,
    private val remoteDataRepository: RemoteDataRepository
): ScreenModel {
    private val _currenciesStatus: MutableStateFlow<CurrenciesStatus> = MutableStateFlow(
        CurrenciesStatus.Idle)
    val currenciesStatus: StateFlow<CurrenciesStatus> = _currenciesStatus.asStateFlow()

    private val _currenciesState: MutableStateFlow<RequestState<List<Currency>>> = MutableStateFlow(RequestState.Idle)
    val currenciesState: StateFlow<RequestState<List<Currency>>> = _currenciesState.asStateFlow()

    private val _sourceCurrencyState: MutableStateFlow<RequestState<Currency>> = MutableStateFlow(RequestState.Idle)
    val sourceCurrencyState: StateFlow<RequestState<Currency>> = _sourceCurrencyState.asStateFlow()

    private val _targetCurrencyState: MutableStateFlow<RequestState<Currency>> = MutableStateFlow(RequestState.Idle)
    val targetCurrencyState: StateFlow<RequestState<Currency>> = _targetCurrencyState.asStateFlow()

    var inputAmount by  mutableStateOf("")
        private set

    var exchangeAmount by mutableStateOf(0.00)
        private set

    init {
        screenModelScope.launch(Dispatchers.Default) {
            getCurrenciesState()
            getCurrenciesStatus()
            getSourceCurrencyState()
            getTargetCurrencyState()
        }
    }

    fun onEvent(event: HomeEvent) {
        when(event) {
            is HomeEvent.RefreshRates -> {
                screenModelScope.launch(Dispatchers.Default) {
                    getCurrenciesState()
                    getCurrenciesStatus()
                }
            }
            is HomeEvent.SwitchCurrencies -> {
                switchCurrencies()
                if (sourceCurrencyState.value.isSuccess()) {
                    val sourceCurrencyCode = sourceCurrencyState.value.getSuccessData().code
                    saveSourceCurrencyCode(
                        sourceCurrencyCode = sourceCurrencyCode
                    )
                }
                if (targetCurrencyState.value.isSuccess()) {
                    val targetCurrencyCode = targetCurrencyState.value.getSuccessData().code
                    saveTargetCurrencyCode(
                        targetCurrencyCode = targetCurrencyCode
                    )
                }
                convertCurrency()
            }
            is HomeEvent.ChangeInputAmount -> {
                inputAmount = event.amount
            }
            is HomeEvent.SaveSourceCurrencyCode -> {
                saveSourceCurrencyCode(sourceCurrencyCode = event.code)
            }
            is HomeEvent.SaveTargetCurrencyCode -> {
                saveTargetCurrencyCode(targetCurrencyCode = event.code)
            }
            is HomeEvent.ConvertCurrency -> {
                convertCurrency()
            }
        }
    }

    private suspend fun getCurrenciesState() {
        try {
            //start loading data from database
            _currenciesState.value = RequestState.Loading
            when(val localRequestState = localDataRepository.readCurrencyObjects().first()) {
                is RequestState.Idle -> {
                    _currenciesState.value = RequestState.Idle
                }
                is RequestState.Loading -> {
                    _currenciesState.value = RequestState.Loading
                }
                is RequestState.Error -> {
                    //if error, get message
                    val errorMessage = localRequestState.message
                    _currenciesState.value = RequestState.Error(message = errorMessage)
                }
                is RequestState.Success -> {
                    //if success, check if the data is empty
                    val currenciesData = localRequestState.data
                    if (currenciesData.isNotEmpty()) {
                        //if the data is not empty, check if the data is fresh
                        if (isDataFresh()) {
                            //if the data is fresh, update currencies
                            _currenciesState.value = RequestState.Success(data = currenciesData)
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
            _currenciesState.value = RequestState.Error(message = e.message.toString())
        }
    }

    private suspend fun fetchAndCacheNewCurrencies() {
        //fetch data from api
        when(val networkRequestState = remoteDataRepository.getLatestExchangeRates()) {
            is RequestState.Idle -> {
                _currenciesState.value = RequestState.Idle
            }
            is RequestState.Loading -> {
                _currenciesState.value = RequestState.Loading
            }
            is RequestState.Error -> {
                //if error, get message
                val errorMessage = networkRequestState.message
                _currenciesState.value = RequestState.Error(message = errorMessage)
            }
            is RequestState.Success -> {
                //if success, clean up old data
                localDataRepository.cleanUpCurrencyObjects()
                //cache new data
                val newCurrenciesData = networkRequestState.data
                newCurrenciesData.forEach { currency ->
                    localDataRepository.insertCurrencyObject(currency)
                }
                //and update currencies with new data
                _currenciesState.value = RequestState.Success(data = newCurrenciesData)
            }
        }
    }

    private suspend fun isDataFresh(): Boolean {
        return localUserPreferencesRepository.isDataFresh(
            currentTimeStamp = Clock.System.now().toEpochMilliseconds()
        )
    }

    private fun getCurrenciesStatus() {
        screenModelScope.launch(Dispatchers.Main) {
            _currenciesStatus.value = if (isDataFresh()) CurrenciesStatus.Fresh else CurrenciesStatus.Stale
        }
    }

    private fun getSourceCurrencyState() {
        screenModelScope.launch(Dispatchers.Main) {
            _sourceCurrencyState.value = RequestState.Loading
            if (_currenciesState.value.isSuccess()) {
                localUserPreferencesRepository.getSourceCurrencyCode().collectLatest {  sourceCurrencyCode ->
                    val sourceCurrency = _currenciesState.value.getSuccessData().find { currency ->
                        currency.code == sourceCurrencyCode
                    }
                    if (sourceCurrency != null) {
                        _sourceCurrencyState.value = RequestState.Success(data = sourceCurrency)
                    } else {
                        _sourceCurrencyState.value = RequestState.Error(message = "Couldn't load the source currency.")
                    }
                }
            } else {
                _sourceCurrencyState.value = RequestState.Error(message = "Couldn't load the source currency.")
            }
        }
    }

    private suspend fun getTargetCurrencyState() {
        screenModelScope.launch(Dispatchers.Main) {
            _targetCurrencyState.value = RequestState.Loading
            if (_currenciesState.value.isSuccess()) {
                localUserPreferencesRepository.getTargetCurrencyCode().collectLatest { targetCurrencyCode ->
                    val targetCurrency = _currenciesState.value.getSuccessData().find { currency ->
                        currency.code == targetCurrencyCode
                    }
                    if (targetCurrency != null) {
                        _targetCurrencyState.value = RequestState.Success(data = targetCurrency)
                    } else {
                        _targetCurrencyState.value = RequestState.Error(message = "Couldn't load the target currency.")
                    }
                }
            } else {
                _targetCurrencyState.value = RequestState.Error(message = "Couldn't load the target currency.")
            }
        }
    }

    private fun switchCurrencies() {
        val sourceCurrencyState = _sourceCurrencyState.value
        val targetCurrencyState = _targetCurrencyState.value
        _sourceCurrencyState.value = targetCurrencyState
        _targetCurrencyState.value = sourceCurrencyState
    }

    private fun saveSourceCurrencyCode(sourceCurrencyCode: String) {
        screenModelScope.launch(Dispatchers.IO) {
            localUserPreferencesRepository.saveSourceCurrencyCode(
                code = sourceCurrencyCode
            )
        }

    }

    private fun saveTargetCurrencyCode(targetCurrencyCode: String) {
        screenModelScope.launch(Dispatchers.IO) {
            localUserPreferencesRepository.saveTargetCurrencyCode(
                code = targetCurrencyCode
            )
        }
    }

    private fun convertCurrency() {
        if (currenciesState.value.isSuccess() && sourceCurrencyState.value.isSuccess() && targetCurrencyState.value.isSuccess()) {
            val sourceCurrency = sourceCurrencyState.value.getSuccessData()
            val targetCurrency = targetCurrencyState.value.getSuccessData()
            val sourceCurrencyAmountInOneUSD = sourceCurrency.value
            val targetCurrencyAmountInOneUSD = targetCurrency.value
            val sourceCurrencyInputAmount = inputAmount.ifEmpty(defaultValue = {"0"}).toDouble()
            exchangeAmount = (sourceCurrencyInputAmount / sourceCurrencyAmountInOneUSD) * targetCurrencyAmountInOneUSD
            println(exchangeAmount)
        }
    }

}