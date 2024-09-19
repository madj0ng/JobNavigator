package ru.practicum.android.diploma.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.network.HHApiService
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.data.sharing.SharingRepositoryImpl
import ru.practicum.android.diploma.domain.sharing.SharingRepository

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

    single {
        synchronized(this) {
            Room.databaseBuilder(androidContext(), AppDatabase::class.java, "jobs.db").build()
        }
    }
}
