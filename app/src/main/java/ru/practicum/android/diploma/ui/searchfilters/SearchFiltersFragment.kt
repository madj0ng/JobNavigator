package ru.practicum.android.diploma.ui.searchfilters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchFiltersBinding
import ru.practicum.android.diploma.presentation.viewmodel.FilterViewModel

class SearchFiltersFragment : Fragment() {
    private var _binding: FragmentSearchFiltersBinding? = null
    private val binding get(): FragmentSearchFiltersBinding = _binding!!
    private val viewModel: FilterViewModel by activityViewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchFiltersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.groupButtons.visibility = View.GONE

        binding.industryBtn.setOnClickListener {
            findNavController()
                .navigate(
                    SearchFiltersFragmentDirections.actionSearchFiltersFragmentToIndustryChooseFragment()
                )
        }

        binding.placeOfWork.setOnClickListener {
            findNavController()
                .navigate(
                    SearchFiltersFragmentDirections.actionSearchFiltersFragmentToPlaceOfWorkFragment()
                )
        }

        binding.buttonBack.setOnClickListener {
            findNavController()
                .navigate(SearchFiltersFragmentDirections.actionSearchFiltersFragmentToJobSearchFragment())
        }

        binding.buttonApply.setOnClickListener {
            findNavController()
                .navigate(SearchFiltersFragmentDirections.actionSearchFiltersFragmentToJobSearchFragment())
            viewModel.setDontShowWithoutSalary(binding.ischeced.isChecked)
            val str = checkSellary(binding.earn.text.toString())
            viewModel.setSalary(str)
        }

        binding.buttonCancel.setOnClickListener {
            viewModel.unSelectCountry()
            viewModel.unSelectIndustry()
            canselFilter()
            binding.groupButtons.visibility = View.GONE
            init()
        }

        binding.ischeced.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.groupButtons.visibility = View.VISIBLE
            } else {
                binding.groupButtons.visibility = View.GONE
            }
        }

        binding.earn.addTextChangedListener {str ->
            if (str.toString().isNotEmpty()) {
                binding.groupButtons.visibility = View.VISIBLE
            } else {
                binding.groupButtons.visibility = View.GONE
            }
        }

        init()
    }

    fun init() {
        if (viewModel.getSalary() != null) {
            binding.earn.setText(viewModel.getSalary().toString())
        }
        binding.ischeced.isChecked = viewModel.getDontShowWithoutSalary()
        val tempI = viewModel.getSavedIndustry()
        val tempR = viewModel.getCitySaved()
        val tempC = viewModel.getCountrySaved()
        val area = StringBuilder()
        if (tempI != null) {
            binding.industryPlace.text = tempI.name
            binding.hintIndustryPlace.text = context?.getString(R.string.Industry)
            binding.groupButtons.visibility = View.VISIBLE
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
            binding.placeOfWorkCountryRegion.text = area
            binding.hintPlaceOfWorkCountryRegion.text = context?.getString(R.string.place_of_work)
            binding.groupButtons.visibility = View.VISIBLE
        }

    }

    private fun canselFilter() {
        with(binding) {
            industryPlace.text = ""
            placeOfWorkCountryRegion.text = ""
            earn.setText("")
            viewModel.setSalary("")
            ischeced.isChecked = false
            viewModel.setDontShowWithoutSalary(false)
            hideHint()
        }
    }

    private fun checkSellary(str: String): String {
        try {
            (str.toInt() > 0)
            return str
        } catch (e: NumberFormatException) {
            return ""
        }
    }

    private fun hideHint() {
        binding.hintPlaceOfWorkCountryRegion.text = context?.getString(R.string.empty)
        binding.hintIndustryPlace.text = context?.getString(R.string.empty)
    }

    override fun onResume() {
        super.onResume()
        init()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
