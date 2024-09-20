package ru.practicum.android.diploma.ui.jobsearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentJobSearchBinding
import ru.practicum.android.diploma.domain.models.VacancySearchParams
import ru.practicum.android.diploma.presentation.models.QueryUiState
import ru.practicum.android.diploma.presentation.models.SearchUiState
import ru.practicum.android.diploma.presentation.viewmodel.FilterViewModel
import ru.practicum.android.diploma.presentation.viewmodel.JobSearchViewModel
import ru.practicum.android.diploma.util.debounce

class JobSearchFragment : Fragment() {
    private var _binding: FragmentJobSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: JobSearchViewModel by viewModel()
    private val filtersViewModel: FilterViewModel by activityViewModel()
    private var jobSearchViewAdapter: JobSearchViewAdapter? = null

    private val debounceSearch = debounce<String>(
        delayMillis = 2000L,
        coroutineScope = lifecycleScope,
        useLastParam = true
    ) { query ->
        viewModel.onSearchQueryChanged(
            VacancySearchParams(
                query,
                if (filtersViewModel.getRegionSaved() != null) {
                    filtersViewModel.getRegionSaved()!!.id
                } else {
                    filtersViewModel.getCountrySaved()?.id
                },
                filtersViewModel.getSalary(),
                filtersViewModel.getDontShowWithoutSalary(),
                filtersViewModel.getSavedIndustry()?.id
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentJobSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etSearch.addTextChangedListener { query ->
            debounceSearch(query.toString())
        }

        jobSearchViewAdapter = JobSearchViewAdapter {
            val action = JobSearchFragmentDirections.actionJobSearchFragmentToJobDetailsFragment(it)
            findNavController().navigate(action)
        }

        binding.rvJobList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = jobSearchViewAdapter
        }

        viewModel.screenLiveData.observe(viewLifecycleOwner, this::updateUiState)
        viewModel.observeSearch().observe(viewLifecycleOwner, this::updateSearchText)
        binding.ifbFilter.setOnClickListener {
            findNavController().navigate(JobSearchFragmentDirections.actionJobSearchFragmentToSearchFiltersFragment())
        }

        binding.rvJobList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val pos = (binding.rvJobList.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    val itemsCount = jobSearchViewAdapter!!.itemCount
                    if (pos >= itemsCount - 1) {
                        viewModel.onLastItemReached(
                            VacancySearchParams(
                                viewModel.getLastSearchQuery(),
                                if (filtersViewModel.getRegionSaved() != null) {
                                    filtersViewModel.getRegionSaved()!!.id
                                } else {
                                    filtersViewModel.getCountrySaved()?.id
                                },
                                filtersViewModel.getSalary(),
                                filtersViewModel.getDontShowWithoutSalary(),
                                filtersViewModel.getSavedIndustry()?.id
                            )
                        )
                    }
                }
            }
        })
    }

    private fun updateSearchText(state: QueryUiState) {
        if (state.src != null) {
            binding.ivSearchClear.setImageResource(state.src!!)
        }
    }

    private fun updateUiState(uiState: SearchUiState) {
        when (uiState) {
            is SearchUiState.Content -> showContent(uiState)
            else -> showScreen(uiState)
        }
    }

    private fun showScreen(uiState: SearchUiState) {
        binding.rvJobList.isVisible = uiState.isJobsList
        binding.tvJobSearchCount.isVisible = uiState.isJobsCount
        binding.pbJobList.isVisible = uiState.isJobsListBrogressBar
        binding.pbPage.isVisible = uiState.isProgressBar
        binding.pbJobList.isVisible = uiState.isPaginationProgressBar
        binding.ivInformImage.isVisible = uiState.isInformImage
        binding.ivInformBottomText.isVisible = uiState.isBottomText
        if (uiState.topText != null) {
            binding.tvJobSearchCount.setText(uiState.topText!!)
        }
        if (uiState.bottomText != null) {
            binding.tvInformBottomText.setText(uiState.bottomText!!)
        }
        if (uiState.url != null) {
            binding.ivInformImage.setImageResource(uiState.url!!)
        }
    }

    private fun showContent(uiState: SearchUiState.Content) {
        showScreen(uiState)
        if (uiState.topText != null) {
            binding.tvJobSearchCount.text = getString(uiState.topText!!, uiState.found)
        }
        jobSearchViewAdapter?.setList(uiState.data) // Обновление списка
    }

    override fun onResume() {
        super.onResume()
        viewModel.onSearchQueryChanged(
            VacancySearchParams(
                viewModel.getLastSearchQuery(),
                if (filtersViewModel.getRegionSaved() != null) {
                    filtersViewModel.getRegionSaved()!!.id
                } else {
                    filtersViewModel.getCountrySaved()?.id
                },
                filtersViewModel.getSalary(),
                filtersViewModel.getDontShowWithoutSalary(),
                filtersViewModel.getSavedIndustry()?.id
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        jobSearchViewAdapter = null
    }
}
