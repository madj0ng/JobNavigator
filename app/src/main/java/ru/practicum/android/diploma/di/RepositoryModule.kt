package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.search.SearchVacancyRepository
import ru.practicum.android.diploma.data.search.impl.SearchVacancyRepositoryImpl

val repositoryModule = module {
    // Здесь будут зависимости для репозиториев
    single<SearchVacancyRepository> {
        SearchVacancyRepositoryImpl(get())
    }
}
