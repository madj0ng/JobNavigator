package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.viewmodel.FavoriteJobsViewModel
import ru.practicum.android.diploma.presentation.viewmodel.FilterViewModel
import ru.practicum.android.diploma.presentation.viewmodel.JobSearchViewModel
import ru.practicum.android.diploma.presentation.viewmodel.VacancyDetailsViewModel

val viewModelModule = module {

    viewModel {
        JobSearchViewModel(get(), get())
    }

    viewModel {
        VacancyDetailsViewModel(get(), get(), get())
    }

    viewModel {
        FavoriteJobsViewModel(get())
    }
    viewModel {
        FilterViewModel(get())
    }
}
