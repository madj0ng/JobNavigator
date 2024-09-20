package ru.practicum.android.diploma.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.viewmodel.FavoriteJobsViewModel
import ru.practicum.android.diploma.presentation.viewmodel.JobSearchViewModel
import ru.practicum.android.diploma.presentation.viewmodel.VacancyDetailsViewModel

val viewModelModule = module {

    viewModel {
        JobSearchViewModel(get(), androidContext())
    }

    viewModel {
        VacancyDetailsViewModel(get(), get(), get())
    }

    viewModel {
        FavoriteJobsViewModel(get())
    }
}
