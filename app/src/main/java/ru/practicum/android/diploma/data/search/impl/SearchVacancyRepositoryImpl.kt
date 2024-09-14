package ru.practicum.android.diploma.data.search.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.dto.VacancySearchRequest
import ru.practicum.android.diploma.data.dto.VacancySearchResponse
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.data.search.SearchVacancyRepository
import ru.practicum.android.diploma.domain.models.EmployerModel
import ru.practicum.android.diploma.domain.models.LogoModel
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.SalaryModel
import ru.practicum.android.diploma.domain.models.VacancyModel
import ru.practicum.android.diploma.domain.models.VacancySearchParams

class SearchVacancyRepositoryImpl(private val retrofitNetworkClient: RetrofitNetworkClient) : SearchVacancyRepository {
    override fun search(
        params: VacancySearchParams
    ): Flow<Resource<List<VacancyModel>>> = flow {
        val queryOptions = mapVacancySearchParamsToQueryMap(params)
        val response = retrofitNetworkClient.doRequest(VacancySearchRequest(queryOptions))

        when (response.resultCode) {
            200 -> {
                with(response as VacancySearchResponse) {
                    val vacancies = items.map { dto ->
                        val logoModel = LogoModel(
                            original = dto.employer?.logoUrls?.original,
                            size90 = dto.employer?.logoUrls?.logo90,
                            siz240 = dto.employer?.logoUrls?.logo240
                        )
                        val employer =
                            EmployerModel(
                                id = dto.employer?.id,
                                name = dto.employer?.name,
                                logoUrls = logoModel
                            )
                        val salary = SalaryModel(
                            currency = dto.salary?.currency,
                            from = dto.salary?.from,
                            to = dto.salary?.to,
                            gross = dto.salary?.gross
                        )

                        VacancyModel(
                            id = dto.id,
                            name = dto.name,
                            employer = employer,
                            salary = salary
                        )
                    }
                    val found = response.found
                    emit(Resource.Success(vacancies, found))
                }
            }

            -1 -> {
                emit(Resource.Error(response.message))
            }

            else -> {
                emit(Resource.Error(response.message))
            }
        }
    }

    private fun mapVacancySearchParamsToQueryMap(params: VacancySearchParams): HashMap<String, String> {
        val options: HashMap<String, String> = HashMap()
        if (params.vacancyName.isNotBlank()) {
            options["text"] = params.vacancyName
        }
        params.area?.let { options["area"] = it }
        params.salary?.let { options["salary"] = it.toString() }
        params.onlyWithSalary.let { options["onlyWithSalary"] = it.toString() }
        params.professionalRole?.let { options["professionalRole"] = it }
        options["per_page"] = "10"
        return options
    }

}
