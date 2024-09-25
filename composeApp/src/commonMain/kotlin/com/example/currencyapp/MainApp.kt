package com.example.currencyapp

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.example.currencyapp.di.appModule
import com.example.currencyapp.presentation.home.screen.HomeScreen
import com.example.currencyapp.presentation.ui.theme.MainAppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.context.startKoin

@Composable
@Preview
fun MainApp() {
    initializeKoin()
    MainAppTheme {
        Navigator(HomeScreen())
    }
}

fun initializeKoin() {
    startKoin {
        modules(appModule)
    }
}