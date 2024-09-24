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
        super.onViewCreated(view, savedInstanceState)
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
            findNavController().popBackStack()
        }

        binding.btnSelect.setOnClickListener {
            findNavController()
                .navigate(PlaceOfWorkFragmentDirections.actionPlaceOfWorkFragmentToSearchFiltersFragment())
            viewModel.saveArea()
        }

    }

    override fun onResume() {
        super.onResume()
        val tempC = viewModel.getSelectedCountry()
        val tempR = viewModel.getSelectedCity()
        if (tempC != null) {
            binding.countryTextarea.text = tempC.name
            binding.hintCountryTextarea.text = context?.getString(R.string.country)
        }
        if (tempR != null) {
            binding.regionTextarea.text = tempR.name
            binding.hintRegionTextarea.text = context?.getString(R.string.region)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
