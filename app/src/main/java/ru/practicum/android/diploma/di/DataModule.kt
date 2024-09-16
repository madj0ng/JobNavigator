package ru.practicum.android.diploma.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.data.network.HHApiService
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient

val dataModule = module {

    single<HHApiService> {
        Retrofit.Builder()
            .baseUrl("https://api.hh.ru")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HHApiService::class.java)
    }

    single<RetrofitNetworkClient> {
        RetrofitNetworkClient(androidContext(), get(), get())
    }
}
