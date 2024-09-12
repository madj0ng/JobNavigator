package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.search.impl.SearchVacancyInteractorImpl
import ru.practicum.android.diploma.domain.search.SearchVacancyInteractor

val interactorModule = module {
    // Здесь будут зависимости для интеракторов
    single<SearchVacancyInteractor> {
        SearchVacancyInteractorImpl(get())
    }
}
