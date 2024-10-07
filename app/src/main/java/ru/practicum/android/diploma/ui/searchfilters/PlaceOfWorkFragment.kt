package ru.practicum.android.diploma.ui.searchfilters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import org.koin.android.ext.android.getKoin
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.PlaceOfWorkFragmentBinding
import ru.practicum.android.diploma.domain.models.FilterModel
import ru.practicum.android.diploma.presentation.models.PlaceOfWorkModel
import ru.practicum.android.diploma.presentation.viewmodel.PlaceOfWorkViewModel

class PlaceOfWorkFragment : Fragment() {
    private var _binding: PlaceOfWorkFragmentBinding? = null
    private val binding: PlaceOfWorkFragmentBinding get() = _binding!!
    private val viewModel: PlaceOfWorkViewModel by activityViewModel()
    private val gson: Gson = getKoin().get()
    private var filter: FilterModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PlaceOfWorkFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        restore()
        checkFilter()
        viewModel.placeOfWorkLiveData.observe(viewLifecycleOwner) { placeOfWork ->
            init(placeOfWork)
        }

        viewModel.getAreas()

        binding.countryBtn.setOnClickListener {
            findNavController()
                .navigate(
                    PlaceOfWorkFragmentDirections.actionPlaceOfWorkFragmentToCountryChooseFragment()
                )
        }

        binding.regionBtn.setOnClickListener {
            checkCountry(binding.countryTextarea.text.toString())
            findNavController()
                .navigate(
                    PlaceOfWorkFragmentDirections.actionPlaceOfWorkFragmentToSearchFiltersRegionFragment()
                )
        }

        binding.buttonBack.setOnClickListener {
            filter = null
            viewModel.selectCountry(null)
            findNavController()
                .popBackStack()
        }

        binding.btnSelect.setOnClickListener {
            filter = null
            viewModel.saveArea()
            findNavController()
                .popBackStack()
        }
    }

    private fun init(placeOfWork: PlaceOfWorkModel) {
        restore()
        if (placeOfWork.countryModel != null) {
            filter = null
            binding.countryTextarea.text = placeOfWork.countryModel.name
            binding.hintCountryTextarea.text = context?.getString(R.string.country)
        }
        if (placeOfWork.regionModel != null) {
            binding.regionTextarea.text = placeOfWork.regionModel.name
            binding.hintRegionTextarea.text = context?.getString(R.string.region)
        }
        if (placeOfWork.cityModel != null) {
            binding.regionTextarea.text = placeOfWork.cityModel.name
            binding.hintRegionTextarea.text = context?.getString(R.string.region)
        }
    }

    private fun powFromFilter(filterModel: FilterModel) {
        if (filterModel.country != null) {
            binding.countryTextarea.text = filterModel.country?.name
            binding.hintCountryTextarea.text = context?.getString(R.string.country)
            if (filterModel.area != null) {
                binding.regionTextarea.text = filterModel.area?.name
                binding.hintRegionTextarea.text = context?.getString(R.string.region)
            }
        }
    }

    private fun checkFilter() {
        filter = gson.fromJson(requireArguments().getString(SearchFiltersFragment.FILTER), FilterModel::class.java)
        if (filter != null && viewModel.isCountrySelect()) {
            powFromFilter(filter!!)
        }
    }

    private fun restore() {
        val str = ""
        binding.countryTextarea.text = str
        binding.hintCountryTextarea.text = str
        binding.regionTextarea.text = str
        binding.hintRegionTextarea.text = str
    }

    private fun checkCountry(str: String) {
        if (str.isNotEmpty() && filter != null) {
            viewModel.saveContryFromFilter(filter!!)

        }
    }

    override fun onResume() {
        super.onResume()
        checkFilter()
        viewModel.getAreas()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
