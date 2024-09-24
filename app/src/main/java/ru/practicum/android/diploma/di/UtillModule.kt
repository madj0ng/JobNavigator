package ru.practicum.android.diploma.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.mapper.MapperFilter
import ru.practicum.android.diploma.util.Connected
import ru.practicum.android.diploma.util.FilterConverter
import ru.practicum.android.diploma.util.FormatConverter

val utillModule = module {

    factory {
        Connected(androidContext())
    }

    factory {
        FormatConverter(androidContext())
    }

    factory {
        FilterConverter()
    }

    factory { MapperFilter() }
}
