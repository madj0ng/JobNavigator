package ru.practicum.android.diploma.ui.searchfilters

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import ru.practicum.android.diploma.databinding.FragmentSearchFiltersBinding
import ru.practicum.android.diploma.presentation.viewmodel.FilterViewModel
import java.lang.StringBuilder

class SearchFiltersFragment : Fragment() {
    private var _binding: FragmentSearchFiltersBinding? = null
    private val binding get(): FragmentSearchFiltersBinding = _binding!!
    private val viewModel: FilterViewModel by activityViewModel()
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
        binding?.buttonBack?.setOnClickListener {
            findNavController().popBackStack()
            viewModel.setDontShowWithoutSalary(binding!!.ischeced.isChecked)
        }
    }

    override fun onResume() {
        super.onResume()
        val tempI = viewModel?.getSavedIndustry()
        val tempR = viewModel?.getRegionSaved()
        val tempC = viewModel?.getCountrySaved()
        val area = StringBuilder()
        if (tempI != null) {
            binding?.industryPlace?.text = tempI.name
        }
        if (tempC != null) {
            area.append(tempC.name)
        }
        if (tempR != null) {
            if (area.isNotEmpty()) {
                area.append(", ")
            }
            area.append(tempR.name)
        }
        if (area.isNotEmpty()) {
            binding?.placeOfWorkCountryRegion?.text = area
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
