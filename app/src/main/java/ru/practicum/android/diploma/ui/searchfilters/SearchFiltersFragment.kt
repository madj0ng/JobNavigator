package ru.practicum.android.diploma.ui.searchfilters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedDispatcher
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import org.koin.android.ext.android.getKoin
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchFiltersBinding
import ru.practicum.android.diploma.domain.models.FilterModel
import ru.practicum.android.diploma.presentation.viewmodel.FilterViewModel
import ru.practicum.android.diploma.util.FormatConverter

class SearchFiltersFragment : Fragment() {
    private var _binding: FragmentSearchFiltersBinding? = null
    private val binding get(): FragmentSearchFiltersBinding = _binding!!
    private val viewModel: FilterViewModel by activityViewModel()
    private val format: FormatConverter = getKoin().get()
    private val gson: Gson = getKoin().get()
    private var filterToSend: String = ""
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
            filterToSend = gson.toJson(filter)
            init(filter)
        }
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
                    R.id.action_searchFiltersFragment_to_placeOfWorkFragment,
                    bundleOf(FILTER to filterToSend)
                )
        }
        binding.buttonBack.setOnClickListener {
            viewModel.saveCheckSalary(binding.ischeced.isChecked)
            viewModel.saveSalary()
            findNavController()
                .navigateUp()
        }
        binding.buttonApply.setOnClickListener {
            viewModel.setDontShowWithoutSalary(binding.ischeced.isChecked)
            viewModel.setSalary(format.clearIfNotInt(binding.earn.text.toString()))
            viewModel.saveSalary()
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
            viewModel.selectIndustry()
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
            viewModel.setSalary(format.clearIfNotInt(str.toString()))
        }
        binding.buttonClearToPow.setOnClickListener {
            restartPlaceOfWork()
            viewModel.deletePlaceOfWork()
            viewModel.getFilter()
        }
        binding.buttonClearToInd.setOnClickListener {
            restartIndusytries()
            viewModel.selectIndustry()
            viewModel.deleteIndustries()
            viewModel.getFilter()
        }
        OnBackPressedDispatcher {
            viewModel.saveCheckSalary(binding.ischeced.isChecked)
            viewModel.saveSalary()
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

    private fun getCondition(): Boolean {
        return binding.placeOfWorkCountryRegion.text.isNotEmpty() ||
            binding.industryPlace.text.isNotEmpty() ||
            format.clearIfNotInt(binding.earn.text.toString()).isNotEmpty() ||
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
        const val FILTER = "filter"
    }
}
