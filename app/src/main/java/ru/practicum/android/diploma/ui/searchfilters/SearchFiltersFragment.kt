package ru.practicum.android.diploma.ui.searchfilters

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchFiltersBinding
import ru.practicum.android.diploma.domain.models.FilterModel
import ru.practicum.android.diploma.presentation.viewmodel.FilterViewModel

class SearchFiltersFragment : Fragment() {
    private var _binding: FragmentSearchFiltersBinding? = null
    private val binding get(): FragmentSearchFiltersBinding = _binding!!
    private val viewModel: FilterViewModel by activityViewModel()
    private var filter: FilterModel? = null
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

        viewModel.searchFilterLiveData.observe(viewLifecycleOwner) { filter ->
            this.filter = filter
            viewModel.saveSelectedFromFilter(filter!!)
            Log.d("FFFFFFF", "${viewModel.selectedCountry}")
            init()
        }
        viewModel.getFilter()


        binding.industryBtn.setOnClickListener {
            findNavController()
                .navigate(
                    SearchFiltersFragmentDirections.actionSearchFiltersFragmentToIndustryChooseFragment()
                )
        }

        binding.placeOfWork.setOnClickListener {
            Log.d("FFFFFFF", "${viewModel.selectedCountry}")
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
            viewModel.setDontShowWithoutSalary(binding.ischeced.isChecked)
            val str = checkSellary(binding.earn.text.toString())
            viewModel.setSalary(str)
            if (binding.ischeced.isChecked) {
                viewModel.setDontShowWithoutSalary(true)
            }
            findNavController()
                .navigate(SearchFiltersFragmentDirections.actionSearchFiltersFragmentToJobSearchFragment())
        }

        binding.buttonCancel.setOnClickListener {
            viewModel.unSelectCountry()
            viewModel.unSelectIndustry()
            viewModel.saveFilter(null)
            canselFilter()
            filter = null
            init()
            binding.groupButtons.visibility = View.GONE

        }

        binding.ischeced.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.groupButtons.visibility = View.VISIBLE
            } else {
                if (!getCondition()) {
                    binding.groupButtons.visibility = View.GONE
                }
            }
        }

        binding.earn.addTextChangedListener { str ->
            if (str.toString().isNotEmpty()) {
                binding.groupButtons.visibility = View.VISIBLE
            } else {
                if (!getCondition()) {
                    binding.groupButtons.visibility = View.GONE
                }
            }
            viewModel.setSalary(checkSellary(str.toString()))
        }
    }

    private fun init() {
        if (filter?.salary != null) {
            binding.earn.setText(filter?.salary.toString())
        }
        binding.ischeced.isChecked = filter?.onlyWithSalary ?: false
        val tempI = filter?.industries
        val tempR = filter?.area
        val tempC = filter?.country
        val area = StringBuilder()
        if (!tempI?.name.isNullOrBlank()) {
            binding.industryPlace.text = tempI?.name
            binding.hintIndustryPlace.text = context?.getString(R.string.Industry)
            binding.groupButtons.visibility = View.VISIBLE
        }
        if (!tempC?.name.isNullOrBlank()) {
            area.append(tempC?.name)
        }
        if (!tempR?.name.isNullOrBlank()) {
            if (area.isNotEmpty()) {
                area.append(", ")
            }
            area.append(tempR?.name)
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
        }
        hideHint()
    }

    private fun checkSellary(str: String): String {
        try {
            return if (str.toInt() > 0) {
                str
            } else {
                ""
            }
        } catch (e: NumberFormatException) {
            return ""
        }
    }

    private fun hideHint() {
        binding.hintPlaceOfWorkCountryRegion.text = context?.getString(R.string.empty)
        binding.hintIndustryPlace.text = context?.getString(R.string.empty)
    }

    private fun getCondition(): Boolean {
        return binding.placeOfWorkCountryRegion.text.isNotEmpty() ||
            binding.industryPlace.text.isNotEmpty() ||
            checkSellary(binding.earn.text.toString()).isNotEmpty() ||
            binding.ischeced.isChecked
    }

    override fun onStart() {
        super.onStart()
        viewModel.getFilter()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFilter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
