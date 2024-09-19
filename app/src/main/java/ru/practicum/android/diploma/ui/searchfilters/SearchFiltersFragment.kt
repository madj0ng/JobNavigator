package ru.practicum.android.diploma.ui.searchfilters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentSearchFiltersBinding
import ru.practicum.android.diploma.presentation.viewmodel.FilterViewModel

class SearchFiltersFragment : Fragment() {
    private var _binding: FragmentSearchFiltersBinding? = null
    private val binding get(): FragmentSearchFiltersBinding = _binding!!
    val viewModel: FilterViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchFiltersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.industryBtn?.setOnClickListener {
            findNavController().navigate(SearchFiltersFragmentDirections.actionSearchFiltersFragmentToIndustryChooseFragment())
        }

        binding?.placeOfWork?.setOnClickListener {
            findNavController().navigate(SearchFiltersFragmentDirections.actionSearchFiltersFragmentToPlaceOfWorkFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
