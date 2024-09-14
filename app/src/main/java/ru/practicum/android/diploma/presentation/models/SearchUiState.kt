package ru.practicum.android.diploma.presentation.models

import ru.practicum.android.diploma.R

sealed interface SearchUiState {
    val isJobsCount: Boolean
    val isJobsList: Boolean
    val isJobsListBrogressBar: Boolean
    val isBrogressBar: Boolean
    val isInformImage: Boolean
    val isBottomText: Boolean
    val url: Int?
    val bottomText: Int?
    val topText: Int?

    data class ErrorConnect(
        override val isJobsCount: Boolean = false,
        override val isJobsList: Boolean = false,
        override val isJobsListBrogressBar: Boolean = false,
        override val isBrogressBar: Boolean = false,
        override val isInformImage: Boolean = true,
        override val isBottomText: Boolean = true,
        override val url: Int = R.drawable.error_no_connect,
        override val bottomText: Int = R.string.search_error_no_connect,
        override val topText: Int? = null
    ) : SearchUiState

    data class ErrorServer(
        override val isJobsCount: Boolean = false,
        override val isJobsList: Boolean = false,
        override val isJobsListBrogressBar: Boolean = false,
        override val isBrogressBar: Boolean = false,
        override val isInformImage: Boolean = true,
        override val isBottomText: Boolean = true,
        override val url: Int = R.drawable.error_server,
        override val bottomText: Int = R.string.error_server,
        override val topText: Int? = null,
    ) : SearchUiState

    data class ErrorData(
        override val isJobsCount: Boolean = true,
        override val isJobsList: Boolean = false,
        override val isJobsListBrogressBar: Boolean = false,
        override val isBrogressBar: Boolean = false,
        override val isInformImage: Boolean = true,
        override val isBottomText: Boolean = true,
        override val url: Int = R.drawable.error_no_data,
        override val bottomText: Int = R.string.search_error_no_data,
        override val topText: Int = R.string.search_job_list_empty,
    ) : SearchUiState

    data class Empty(
        override val isJobsCount: Boolean = false,
        override val isJobsList: Boolean = false,
        override val isJobsListBrogressBar: Boolean = false,
        override val isBrogressBar: Boolean = false,
        override val isInformImage: Boolean = true,
        override val isBottomText: Boolean = true,
        override val url: Int = R.drawable.search_empty,
        override val bottomText: Int? = null,
        override val topText: Int? = null,
    ) : SearchUiState

    data class Loading(
        override val isJobsCount: Boolean = false,
        override val isJobsList: Boolean = false,
        override val isJobsListBrogressBar: Boolean = false,
        override val isBrogressBar: Boolean = true,
        override val isInformImage: Boolean = false,
        override val isBottomText: Boolean = false,
        override val url: Int?,
        override val topText: Int?,
        override val bottomText: Int?,
    ) : SearchUiState

    data class Content(
        override val isJobsCount: Boolean = true,
        override val isJobsList: Boolean = true,
        override val isJobsListBrogressBar: Boolean = false,
        override val isBrogressBar: Boolean = false,
        override val isInformImage: Boolean = false,
        override val isBottomText: Boolean = false,
        override val url: Int?,
        override val topText: Int?,
        override val bottomText: Int?,
        val data: List<VacancyInfo>,
    ) : SearchUiState
}
