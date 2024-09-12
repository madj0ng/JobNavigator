package ru.practicum.android.diploma.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.util.Connected

val utillModule = module {

    factory {
        Connected(androidContext())
    }

}
