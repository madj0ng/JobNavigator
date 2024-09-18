package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.db.impl.FavoriteJobsRepositoryImpl
import ru.practicum.android.diploma.data.dto.vacancy.VacancyRepository
import ru.practicum.android.diploma.data.dto.vacancy.impl.VacancyRepositoryImpl
import ru.practicum.android.diploma.data.search.SearchVacancyRepository
import ru.practicum.android.diploma.data.search.impl.SearchVacancyRepositoryImpl
import ru.practicum.android.diploma.domain.favoritejobs.FavoriteJobsRepository
import ru.practicum.android.diploma.domain.favoritejobs.mapper.MapperVacansyToJob

val repositoryModule = module {

    single<SearchVacancyRepository> {
        SearchVacancyRepositoryImpl(get())
    }

    single<VacancyRepository> {
        VacancyRepositoryImpl(get())
    }

    single<FavoriteJobsRepository> {
        FavoriteJobsRepositoryImpl(get(), get())
    }

    factory { MapperVacansyToJob() }
}
