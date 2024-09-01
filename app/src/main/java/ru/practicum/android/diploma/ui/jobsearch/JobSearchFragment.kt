package ru.practicum.android.diploma.ui.jobsearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentJobSearchBinding

class JobSearchFragment : Fragment() {
    private var binding: FragmentJobSearchBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJobSearchBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.buttonNavigateToSearchFilters?.setOnClickListener {
            findNavController().navigate(
                R.id.action_jobSearchFragment_to_searchFiltersFragment
            )
        }
        binding?.buttonNavigateToJobDetails?.setOnClickListener {
            findNavController().navigate(
                R.id.action_jobSearchFragment_to_jobDetailsFragment
            )
        }
        binding?.buttonNavigateToFavoriteJobs?.setOnClickListener {
            findNavController().navigate(
                R.id.action_jobSearchFragment_to_favoriteJobsFragment
            )
        }
        binding?.buttonNavigateToTeamInformation?.setOnClickListener {
            findNavController().navigate(
                R.id.action_jobSearchFragment_to_teamInformationFragment
            )
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
