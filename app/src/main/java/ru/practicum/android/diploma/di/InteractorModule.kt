package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.favoritejobs.FavoriteJobsInteractor
import ru.practicum.android.diploma.domain.favoritejobs.impl.FavoriteJobsInteractorImpl
import ru.practicum.android.diploma.domain.filters.FilterInteractor
import ru.practicum.android.diploma.domain.filters.impl.FilterInteractorImpl
import ru.practicum.android.diploma.domain.search.SearchVacancyInteractor
import ru.practicum.android.diploma.domain.search.impl.SearchVacancyInteractorImpl
import ru.practicum.android.diploma.domain.sharing.SharingInteractor
import ru.practicum.android.diploma.domain.sharing.impl.SharingInteractorImpl
import ru.practicum.android.diploma.domain.vacancydetails.MapperVacancyDetails
import ru.practicum.android.diploma.domain.vacancydetails.VacancyDetailsInteractor
import ru.practicum.android.diploma.domain.vacancydetails.impl.VacancyDetailsInteractorImpl

val interactorModule = module {

    single<SearchVacancyInteractor> {
        SearchVacancyInteractorImpl(get())
    }

    single<VacancyDetailsInteractor> {
        VacancyDetailsInteractorImpl(get())
    }

    factory {
        MapperVacancyDetails(get())
    }

    single<FavoriteJobsInteractor> {
        FavoriteJobsInteractorImpl(get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get())
    }
    single<FilterInteractor> {
        FilterInteractorImpl(get(), get())
    }
}
