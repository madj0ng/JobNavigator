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
import ru.practicum.android.diploma.domain.models.AreaFilterModel
import ru.practicum.android.diploma.domain.models.CountryFilterModel
import ru.practicum.android.diploma.domain.models.FilterModel
import ru.practicum.android.diploma.domain.models.IndustriesFilterModel
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
        viewModel.selectRegion = null
        viewModel.selectCity = null
        viewModel.selectedCountry = null
        viewModel.selectIndustry(null)

        binding.groupButtons.visibility = View.GONE

        viewModel.searchFilterLiveData.observe(viewLifecycleOwner) { init() }
        viewModel.getFilter()

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
            viewModel.setDontShowWithoutSalary(binding.ischeced.isChecked)
            val str = checkSellary(binding.earn.text.toString())
            viewModel.setSalary(str)
            saveFiler()

            findNavController()
                .navigate(SearchFiltersFragmentDirections.actionSearchFiltersFragmentToJobSearchFragment())
        }

        binding.buttonCancel.setOnClickListener {
            viewModel.unSelectCountry()
            viewModel.unSelectIndustry()
            canselFilter()
            binding.groupButtons.visibility = View.GONE
            viewModel.saveFilter(null)
            init()
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
        }

        init()
    }

    private fun init() {
        if (viewModel.salaryBase != null) {
            binding.earn.setText(viewModel.salaryBase.toString())
        }
        binding.ischeced.isChecked = viewModel.doNotShowWithoutSalary
        val tempI = viewModel.savedIndustry
        val tempCity = viewModel.savedCity
        val tempR = viewModel.saveRegion
        val tempC = viewModel.savedCountry
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
        } else if (tempCity != null) {
            if (area.isNotEmpty()) {
                area.append(", ")
            }
            area.append(tempCity.name)
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

    private fun saveFiler() {
        val countryModel: CountryFilterModel?
        val areaModel: AreaFilterModel?
        val industryModel: IndustriesFilterModel?
        val salary: Int?
        var onlyWithSalary: Boolean? = true

        if (getCondition()) {
            if (binding.placeOfWorkCountryRegion.text.isNotEmpty()) {
                val saveCountry = viewModel.savedCountry
                countryModel = CountryFilterModel(saveCountry!!.id, saveCountry.name)
                areaModel = setRegion()
            } else {
                countryModel = null
                areaModel = null
            }

            if (binding.industryPlace.text.isNotEmpty()) {
                val saveIndustry = viewModel.savedIndustry
                industryModel = IndustriesFilterModel(saveIndustry!!.id, saveIndustry.name)
            } else {
                industryModel = null
            }

            if (checkSellary(binding.earn.text.toString()).isNotEmpty()) {
                salary = viewModel.salaryBase
            } else {
                salary = null
            }

            if (!binding.ischeced.isChecked) {
                onlyWithSalary = null
            }

            viewModel.saveFilter(FilterModel(countryModel, areaModel, industryModel, salary, onlyWithSalary))
        } else {
            viewModel.saveFilter(null)
        }
    }

    private fun setRegion(): AreaFilterModel? {
        val areaModel: AreaFilterModel?
        val city = viewModel.savedCity
        val region = viewModel.saveRegion
        if (city == null) {
            if (region == null) {
                areaModel = null
            } else {
                areaModel = AreaFilterModel(region.id, region.name)
            }
        } else {
            areaModel = AreaFilterModel(city.id, city.name)
        }
        return areaModel
    }

    private fun getCondition(): Boolean {
        return binding.placeOfWorkCountryRegion.text.isNotEmpty() ||
            binding.industryPlace.text.isNotEmpty() ||
            checkSellary(binding.earn.text.toString()).isNotEmpty() ||
            binding.ischeced.isChecked
    }

    override fun onStart() {
        super.onStart()
        init()
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
