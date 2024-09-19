package ru.practicum.android.diploma.ui.favoritejobs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentFavoriteJobsBinding
import ru.practicum.android.diploma.presentation.models.FavoriteJobsScreenState
import ru.practicum.android.diploma.presentation.models.VacancyInfo
import ru.practicum.android.diploma.presentation.viewmodel.FavoriteJobsViewModel

class FavoriteJobsFragment : Fragment() {

    private var _binding: FragmentFavoriteJobsBinding? = null
    private val binding get(): FragmentFavoriteJobsBinding = _binding!!

    private val viewModel: FavoriteJobsViewModel by viewModel()
    private var favoriteJobsViewAdapter: FavoriteJobsViewAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteJobsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteJobsViewAdapter = FavoriteJobsViewAdapter {
            val action = FavoriteJobsFragmentDirections.actionFavoriteJobsFragmentToJobDetailsFragment(it)
            findNavController().navigate(action)
        }

        binding.rvJobList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favoriteJobsViewAdapter
        }

        viewModel.screenLiveData.observe(viewLifecycleOwner) { state ->
            when(state) {
                is FavoriteJobsScreenState.Empty -> showEmptyState()
                is FavoriteJobsScreenState.Error -> showErrorState()
                is FavoriteJobsScreenState.Content -> showContent(state.data)
            }
        }

        viewModel.getFavoriteJobs()
    }

    fun showEmptyState() {
        hideAll()
        binding.groupNoVacancies.visibility = View.VISIBLE
    }

    fun showErrorState() {
        hideAll()
        binding.groupNoListVacancies.visibility = View.VISIBLE
    }

    fun showContent(data: List<VacancyInfo>) {
        hideAll()
        favoriteJobsViewAdapter?.setList(data)
        binding.rvJobList.visibility = View.VISIBLE
    }

    fun hideAll() {
        with(binding){
            rvJobList.visibility = View.GONE
            groupNoListVacancies.visibility = View.GONE
            groupNoVacancies.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        favoriteJobsViewAdapter = null
    }
}
