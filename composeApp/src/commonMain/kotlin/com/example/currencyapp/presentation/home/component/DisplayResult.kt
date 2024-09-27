package com.example.currencyapp.presentation.home.component

import androidx.compose.runtime.Composable
import com.example.currencyapp.util.RequestState

@Composable
fun <T> RequestState<T>.DisplayResult(
    onIdle: (@Composable () -> Unit)? = null,
    onLoading: (@Composable () -> Unit)? = null,
    onError: (@Composable (errorMessage: String) -> Unit)? = null,
    onSuccess: (@Composable (data: T) -> Unit)? = null
) {
    when(this) {
        is RequestState.Idle -> {
            onIdle?.invoke()
        }
        is RequestState.Loading -> {
            onLoading?.invoke()
        }
        is RequestState.Error -> {
            onError?.invoke(this.message)
        }
        is RequestState.Success -> {
            onSuccess?.invoke(this.data)
        }
    }
}