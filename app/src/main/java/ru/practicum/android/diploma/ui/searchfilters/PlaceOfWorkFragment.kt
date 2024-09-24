package ru.practicum.android.diploma.ui.searchfilters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.PlaceOfWorkFragmentBinding
import ru.practicum.android.diploma.domain.models.CityModel
import ru.practicum.android.diploma.domain.models.CountryModel
import ru.practicum.android.diploma.domain.models.RegionModel
import ru.practicum.android.diploma.presentation.viewmodel.FilterViewModel

class PlaceOfWorkFragment : Fragment() {
    private var _binding: PlaceOfWorkFragmentBinding? = null
    private val binding: PlaceOfWorkFragmentBinding get() = _binding!!
    private val viewModel: FilterViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PlaceOfWorkFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getSelectCityLiveData().observe(viewLifecycleOwner, this::init)
        viewModel.getSelectRegionLiveData().observe(viewLifecycleOwner, this::init)
        viewModel.getSelectCountryLiveData().observe(viewLifecycleOwner, this::init)

        binding.countryBtn.setOnClickListener {
            findNavController()
                .navigate(
                    PlaceOfWorkFragmentDirections.actionPlaceOfWorkFragmentToCountryChooseFragment()
                )
        }

        binding.regionBtn.setOnClickListener {
            findNavController()
                .navigate(
                    PlaceOfWorkFragmentDirections.actionPlaceOfWorkFragmentToSearchFiltersRegionFragment()
                )
        }

        binding.buttonBack.setOnClickListener {
            viewModel.unSelectCountry()
            findNavController()
                .navigateUp()
        }

        binding.btnSelect.setOnClickListener {
            viewModel.saveArea()
            findNavController()
                .navigateUp()
        }
    }

    private fun init(country: CountryModel?) {
        if (country != null) {
            binding.countryTextarea.text = country.name
            binding.hintCountryTextarea.text = context?.getString(R.string.country)
        }
    }

    private fun init(region: RegionModel?) {
        if (region != null) {
            binding.regionTextarea.text = region.name
            binding.hintRegionTextarea.text = context?.getString(R.string.region)
        }
    }

    private fun init(city: CityModel?) {
        if (city != null) {
            binding.regionTextarea.text = city.name
            binding.hintRegionTextarea.text = context?.getString(R.string.region)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
