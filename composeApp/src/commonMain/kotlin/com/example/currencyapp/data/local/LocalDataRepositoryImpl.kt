package com.example.currencyapp.data.local

import com.example.currencyapp.domain.model.Currency
import com.example.currencyapp.domain.repository.LocalDataRepository
import com.example.currencyapp.util.RequestState
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class LocalDataRepositoryImpl: LocalDataRepository {
    private var realm: Realm? = null
    init {
        configureRealmDatabase()
    }
    override fun configureRealmDatabase() {
        if (realm == null || realm!!.isClosed()) {
            val configuration = RealmConfiguration.Builder(
                schema = setOf(Currency::class)
            ).compactOnLaunch().build()
            realm = Realm.open(configuration = configuration)
        }
    }

    override suspend fun insertCurrencyObject(currency: Currency) {
        realm?.write {
            copyToRealm(currency)
        }
    }

    override fun readCurrencyObjects(): Flow<RequestState<List<Currency>>> {
        val currencyObjectsCollection = realm?.query<Currency>()
        return currencyObjectsCollection
            ?.asFlow()
            ?.map {  result ->
                RequestState.Success(data = result.list)
            } ?: flow {
                RequestState.Error(message = "Realm is not configured.")
        }
    }

    override suspend fun cleanUpCurrencyObjects() {
        realm?.write {
            val currencyObjectsCollection = this.query<Currency>()
            delete(currencyObjectsCollection)
        }
    }
}