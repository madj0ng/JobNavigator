package ru.practicum.android.diploma.ui.jobsearch

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentJobSearchBinding
import ru.practicum.android.diploma.domain.models.FilterModel
import ru.practicum.android.diploma.domain.models.VacancySearchParams
import ru.practicum.android.diploma.presentation.models.QueryUiState
import ru.practicum.android.diploma.presentation.models.SearchUiState
import ru.practicum.android.diploma.presentation.viewmodel.JobSearchViewModel
import ru.practicum.android.diploma.ui.searchfilters.SearchFiltersFragment

class JobSearchFragment : Fragment() {
    private var _binding: FragmentJobSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: JobSearchViewModel by viewModel()
    private var jobSearchViewAdapter: JobSearchViewAdapter? = null
//    private var filterModel: FilterModel? = null
//    private var lastQuery: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJobSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFilter()
        var newQuery = findNavController().currentBackStackEntry
            ?.savedStateHandle
            ?.remove<Boolean>(SearchFiltersFragment.NEW_QUERY_FLAG)
        viewModel.setNewQueryFlag(newQuery)
        viewModel.searchFilterLiveData.observe(viewLifecycleOwner) {
            viewModel.onSearchFilterChanged()
            if (it != null) {
                binding.ifbFilter.background = view.resources.getDrawable(R.drawable.background_blue_small)
                binding.ifbFilter.setImageResource(R.drawable.ic_menu_filter_white)
//                if (newQuery != null && newQuery!!) {
//                    newQuery = false
//                    this.filterModel = filterModel
//                    val query = binding.etSearch.text
//                    viewModel.searchRequest(setQueryParam(query.toString(), filterModel))
//                }
            } else {
////                val query = binding.etSearch.text
////                viewModel.searchRequest(setQueryParam(query.toString(), filterModel))
                binding.ifbFilter.background = view?.resources?.getDrawable(R.drawable.background_transparent)
                binding.ifbFilter.setImageResource(R.drawable.ic_menu_filter)
//                filterModelIsNotNull(filterModel)
            }
        }
        binding.etSearch.addTextChangedListener { query ->
//            if (lastQuery != query.toString()) {
                viewModel.onSearchQueryChanged(query.toString())
//                viewModel.onSearchQueryChanged(setQueryParam(query.toString(), filterModel))
//            }
        }
        jobSearchViewAdapter = JobSearchViewAdapter {
            findNavController().navigate(JobSearchFragmentDirections.actionJobSearchFragmentToJobDetailsFragment(it))
        }
        binding.rvJobList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = jobSearchViewAdapter
        }
        viewModel.screenLiveData.observe(viewLifecycleOwner, this::updateUiState)
        viewModel.observeSearch().observe(viewLifecycleOwner, this::updateSearchText)
        viewModel.getToast().observe(viewLifecycleOwner, this::showToast)
        binding.ifbFilter.setOnClickListener {
//            lastQuery = binding.etSearch.text.toString()
            findNavController().navigate(JobSearchFragmentDirections.actionJobSearchFragmentToSearchFiltersFragment())
        }
        binding.ivSearchClear.setOnClickListener { viewModel.onClickSearchClear() }
        binding.rvJobList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val pos = (binding.rvJobList.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    if (pos >= jobSearchViewAdapter!!.itemCount - 1) {
                        viewModel.onLastItemReached(
//                            setQueryParam(viewModel.observeSearch().value?.query ?: "", filterModel)
                        )
                    }
                }
            }
        })
    }

    private fun updateSearchText(state: QueryUiState) {
        if (state.src != null) binding.ivSearchClear.setImageResource(state.src)
        if (!state.isClose) binding.etSearch.text = null
    }

    private fun updateUiState(uiState: SearchUiState) {
        when (uiState) {
            is SearchUiState.Content -> showContent(uiState)
            is SearchUiState.Default -> showDefault(uiState)
            else -> showScreen(uiState)
        }
    }

    private fun showToast(message: String) { Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show() }

    private fun showScreen(uiState: SearchUiState) {
        binding.rvJobList.isVisible = uiState.isJobsList
        binding.tvJobSearchCount.isVisible = uiState.isJobsCount
        binding.pbJobList.isVisible = uiState.isJobsListBrogressBar
        binding.pbPage.isVisible = uiState.isProgressBar
        binding.pbJobList.isVisible = uiState.isPaginationProgressBar
        binding.ivInformImage.isVisible = uiState.isInformImage
        binding.ivInformBottomText.isVisible = uiState.isBottomText
        if (uiState.topText != null) binding.tvJobSearchCount.setText(uiState.topText!!)
        if (uiState.bottomText != null) binding.ivInformBottomText.setText(uiState.bottomText!!)
        if (uiState.url != null) binding.ivInformImage.setImageResource(uiState.url!!)
        if (uiState is SearchUiState.Loading) view?.hideKeyboard()
    }

    private fun showContent(uiState: SearchUiState.Content) {
        showScreen(uiState)
        if (uiState.topText != null) binding.tvJobSearchCount.text = getString(uiState.topText, uiState.found)
        jobSearchViewAdapter?.setList(uiState.data) // Обновление списка
        binding.rvJobList.adapter = jobSearchViewAdapter
        jobSearchViewAdapter?.notifyDataSetChanged()
    }

    private fun showDefault(uiState: SearchUiState.Default) {
        showScreen(uiState)
        jobSearchViewAdapter?.setList(listOf())
        jobSearchViewAdapter?.notifyDataSetChanged()
    }

    private fun setQueryParam(query: String): VacancySearchParams = VacancySearchParams(query)
//    private fun setQueryParam(query: String, filterModel: FilterModel?): VacancySearchParams {
//        return if (filterModel != null) {
//            VacancySearchParams(
//                query,
//                if (filterModel.area != null) filterModel.area?.id else filterModel.country?.id,
//                filterModel.salary,
//                filterModel.onlyWithSalary ?: false,
//                filterModel.industries?.id
//            )
//        } else {
//            VacancySearchParams(query)
//        }
//    }

    private fun View.hideKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }

//    private fun filterModelIsNotNull(filter: FilterModel?) {
//        this.filterModel = filter
//        val query = binding.etSearch.text
//        viewModel.searchRequest(setQueryParam(query.toString(), filterModel))
//        binding.ifbFilter.background = view?.resources?.getDrawable(R.drawable.background_transparent)
//        binding.ifbFilter.setImageResource(R.drawable.ic_menu_filter)
//    }

    override fun onResume() {
        super.onResume()
        viewModel.getFilter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        jobSearchViewAdapter = null
    }
}
