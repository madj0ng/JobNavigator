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
            SUCCESS_CODE -> {
                with(response as VacancySearchResponse) {
                    val vacancies = items.map { dto ->
                        val logoModel = LogoModel(
                            original = dto.employer?.logoUrls?.original,
                            size90 = dto.employer?.logoUrls?.logo90,
                            siz240 = dto.employer?.logoUrls?.logo240
                        )
                        val employer =
                            EmployerModel(id = dto.employer?.id, name = dto.employer?.name, logoUrls = logoModel)
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
                    emit(Resource.Success(vacancies, response.found, response.page, response.pages))
                }
            }

            ERROR_CODE -> {
                emit(Resource.Error(response.resultCode, response.message))
            }

            else -> {
                emit(Resource.Error(response.resultCode, response.message))
            }
        }
    }

    private fun mapVacancySearchParamsToQueryMap(params: VacancySearchParams): HashMap<String, String> {
        val options: HashMap<String, String> = HashMap()
        if (params.vacancyName.isNotBlank()) {
            options["text"] = params.vacancyName
        }
        if (params.area != null && params.area.isNotBlank()) {
            options["area"] = params.area
        }
        if (params.salary != null && params.salary != 0) {
            options["salary"] = params.salary.toString()
        }
        if (params.professionalRole != null && params.professionalRole.isNotBlank()) {
            options["industry"] = params.salary.toString()
        }
        options["only_with_salary"] = params.onlyWithSalary.toString()
        options["page"] = params.page.toString()
        options["per_page"] = params.perPage.toString()
        return options
    }

    companion object {
        private const val SUCCESS_CODE = 200
        private const val ERROR_CODE = -1
    }
}
