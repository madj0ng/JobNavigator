package ru.practicum.android.diploma.ui.jobsearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentJobSearchBinding
import ru.practicum.android.diploma.presentation.models.JobSearchScreenState
import ru.practicum.android.diploma.presentation.models.VacancyInfo
import ru.practicum.android.diploma.presentation.viewmodel.JobSearchViewModel
import ru.practicum.android.diploma.util.debounce

class JobSearchFragment : Fragment() {
    private var _binding: FragmentJobSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: JobSearchViewModel by viewModel()
    private var jobSearchViewAdapter: JobSearchViewAdapter? = null

    private val debounceSearch = debounce<String>(
        delayMillis = 2000L,
        coroutineScope = lifecycleScope,
        useLastParam = true
    ) { query ->
        viewModel.onSearchQueryChanged(query)
    }

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

        viewModel.screenLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is JobSearchScreenState.Content -> {
                    showContent(state.data, state.found)
                }

                is JobSearchScreenState.Loading -> {
                    showLoading()
                }

                is JobSearchScreenState.Empty -> {
                    showEmptyState()
                }

                is JobSearchScreenState.ShowPaginationLoading -> {
                    showPaginationLoading()
                }

                else -> {
                    showDefault()
                }
            }
        }

        binding.rvJobList.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val pos = (binding.rvJobList.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    val itemsCount = jobSearchViewAdapter!!.itemCount
                    if (pos >= itemsCount - 1) {
                        viewModel.onLastItemReached()
                    }
                }
            }
        })
    }

    private fun showLoading() {
        hideAll()
        binding.pbPage.visibility = View.VISIBLE
    }

    private fun showPaginationLoading() {
        binding.pbJobList.visibility = View.VISIBLE
    }

    private fun showEmptyState() {
        hideAll()
        binding.ivInformImage.setImageResource(R.drawable.error_no_data)
        binding.ivInformImage.visibility = View.VISIBLE
        binding.tvJobSearchCount.text = getString(R.string.search_job_no_such_vacancies)
        binding.tvJobSearchCount.visibility = View.VISIBLE
        binding.ivInformBottomText.text = getString(R.string.search_job_no_such_vacancies)
        binding.ivInformBottomText.visibility = View.VISIBLE
    }

    private fun showContent(data: List<VacancyInfo>, found: Int) {
        hideAll()
        if (viewModel.currentPage.value == 0) {
            jobSearchViewAdapter?.setList(data)
        } else {
            jobSearchViewAdapter?.addItems(data)
        }
        binding.rvJobList.visibility = View.VISIBLE
        binding.tvJobSearchCount.visibility = View.VISIBLE
        binding.tvJobSearchCount.text = getString(R.string.search_job_list_count, found)
    }

    private fun showDefault() {
        hideAll()
        binding.ivInformImage.visibility = View.VISIBLE
    }

    private fun hideAll() {
        binding.rvJobList.visibility = View.GONE
        binding.pbJobList.visibility = View.GONE
        binding.ivInformImage.visibility = View.GONE
        binding.tvJobSearchCount.visibility = View.GONE
        binding.ivInformBottomText.visibility = View.GONE
        binding.pbPage.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        jobSearchViewAdapter = null
    }
}
