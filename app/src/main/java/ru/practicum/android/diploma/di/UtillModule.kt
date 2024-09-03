package ru.practicum.android.diploma.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.util.IsConnected

val utillModule = module {

    factory {
        IsConnected(androidContext())
    }

}
