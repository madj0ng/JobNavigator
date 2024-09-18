package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.favoritejobs.FavoriteJobsInteractor
import ru.practicum.android.diploma.domain.favoritejobs.impl.FavoriteJobsInteractorImpl
import ru.practicum.android.diploma.domain.search.SearchVacancyInteractor
import ru.practicum.android.diploma.domain.search.impl.SearchVacancyInteractorImpl
import ru.practicum.android.diploma.domain.vacancydetails.VacancyDetailsInteractor
import ru.practicum.android.diploma.domain.vacancydetails.impl.VacancyDetailsInteractorImpl

val interactorModule = module {

    single<SearchVacancyInteractor> {
        SearchVacancyInteractorImpl(get())
    }

    single<VacancyDetailsInteractor> {
        VacancyDetailsInteractorImpl(get())
    }

    single<FavoriteJobsInteractor> {
        FavoriteJobsInteractorImpl(get())
    }
}
