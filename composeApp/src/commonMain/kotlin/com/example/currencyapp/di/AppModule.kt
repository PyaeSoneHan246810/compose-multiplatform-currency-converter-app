package com.example.currencyapp.di

import com.example.currencyapp.BuildKonfig
import com.example.currencyapp.data.local.LocalDataRepositoryImpl
import com.example.currencyapp.data.local.LocalUserPreferencesRepositoryImpl
import com.example.currencyapp.data.remote.api.RemoteDataRepositoryImpl
import com.example.currencyapp.domain.repository.LocalDataRepository
import com.example.currencyapp.domain.repository.LocalUserPreferencesRepository
import com.example.currencyapp.domain.repository.RemoteDataRepository
import com.example.currencyapp.presentation.home.viewModel.HomeViewModel
import com.example.currencyapp.util.Constants
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

private val apiKey = BuildKonfig.API_KEY

val appModule = module {
    single<Settings> {
        Settings()
    }
    single<LocalUserPreferencesRepository> {
        LocalUserPreferencesRepositoryImpl(
            settings = get()
        )
    }
    single<LocalDataRepository> {
        LocalDataRepositoryImpl()
    }
    single<HttpClient> {
        HttpClient {
            install(ContentNegotiation) {
                json(
                    json = Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    }
                )
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 15000
            }
            install(DefaultRequest) {
                headers {
                    append(Constants.API_KEY_HEADER_NAME, apiKey)
                }
            }
        }
    }
    single<RemoteDataRepository> {
        RemoteDataRepositoryImpl(
            httpClient = get(),
            localUserPreferencesRepository = get()
        )
    }
    factory {
        HomeViewModel(
            localUserPreferencesRepository = get(),
            localDataRepository = get(),
            remoteDataRepository = get()
        )
    }
}