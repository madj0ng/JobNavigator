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
import ru.practicum.android.diploma.domain.models.FilterModel
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
        binding.groupButtons.visibility = View.GONE

        viewModel.searchFilterLiveData.observe(viewLifecycleOwner) { filter ->
            viewModel.saveSelectedFromFilter(filter)
            init(filter)
        }


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

        viewModel.getFilter()

        binding.buttonBack.setOnClickListener {
            findNavController()
                .navigateUp()
        }

        binding.buttonApply.setOnClickListener {
            viewModel.setDontShowWithoutSalary(binding.ischeced.isChecked)
            val str = checkSellary(binding.earn.text.toString())
            viewModel.setSalary(str)
            if (binding.ischeced.isChecked) {
                viewModel.setDontShowWithoutSalary(true)
            }
            val newQuery = true
            findNavController()
                .navigateUp()
            findNavController().currentBackStackEntry
                ?.savedStateHandle
                ?.set(NEW_QUERY_FLAG, newQuery)
        }

        binding.buttonCancel.setOnClickListener {
            viewModel.unSelectCountry()
            viewModel.unSelectIndustry()
            viewModel.saveFilter(null)
            canselFilter()
            viewModel.getFilter()
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

        binding.buttonClearToPow.setOnClickListener {
            restartPlaceOfWork()
            viewModel.unSelectCountry()
            viewModel.deletePlaceOfWork()
            viewModel.getFilter()
        }

        binding.buttonClearToInd.setOnClickListener {
            restartIndusytries()
            viewModel.unSelectIndustry()
            viewModel.deleteIndustries()
            viewModel.getFilter()
        }
    }

    private fun init(filter: FilterModel?) {
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
            binding.buttonClearToInd.setImageResource(R.drawable.ic_close_to_filter)
            binding.buttonClearToInd.isClickable = true
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
            binding.buttonClearToPow.setImageResource(R.drawable.ic_close_to_filter)
            binding.buttonClearToPow.isClickable = true
        }

    }

    private fun canselFilter() {
        with(binding) {
            earn.setText("")
            viewModel.setSalary("")
            ischeced.isChecked = false
        }
        restartIndusytries()
        restartPlaceOfWork()
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

    private fun getCondition(): Boolean {
        return binding.placeOfWorkCountryRegion.text.isNotEmpty() ||
            binding.industryPlace.text.isNotEmpty() ||
            checkSellary(binding.earn.text.toString()).isNotEmpty() ||
            binding.ischeced.isChecked
    }

    private fun restartPlaceOfWork() {
        with(binding) {
            placeOfWorkCountryRegion.text = ""
            hintPlaceOfWorkCountryRegion.text = context?.getString(R.string.empty)
            buttonClearToPow.setImageResource(R.drawable.icon_next)
            buttonClearToPow.isClickable = false
        }
        if (!getCondition()) {
            binding.groupButtons.visibility = View.GONE
        }
    }

    private fun restartIndusytries() {
        with(binding) {
            industryPlace.text = ""
            hintIndustryPlace.text = context?.getString(R.string.empty)
            buttonClearToInd.setImageResource(R.drawable.icon_next)
            buttonClearToInd.isClickable = false
        }
        if (!getCondition()) {
            binding.groupButtons.visibility = View.GONE
        }
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

    companion object {
        const val NEW_QUERY_FLAG = "new_query_flag"
    }
}
