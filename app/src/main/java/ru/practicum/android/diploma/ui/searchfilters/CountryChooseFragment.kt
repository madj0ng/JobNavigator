package ru.practicum.android.diploma.ui.searchfilters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import ru.practicum.android.diploma.databinding.CountryChooseFragmentBinding
import ru.practicum.android.diploma.presentation.models.AreasScreenState
import ru.practicum.android.diploma.presentation.viewmodel.FilterViewModel

class CountryChooseFragment : Fragment() {
    private var _binding: CountryChooseFragmentBinding? = null
    private val binding: CountryChooseFragmentBinding get() = _binding!!
    private val viewModel: FilterViewModel by activityViewModel()
    private var filtersViewAdapterCountry: FiltersViewAdapterArea? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CountryChooseFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.areaLiveData.observe(viewLifecycleOwner) { areas ->
            if (areas is AreasScreenState.Error) {
                hideAll()
                binding.countryError.visibility = View.VISIBLE
            } else {
                hideAll()
                binding.countryRecyclerview.visibility = View.VISIBLE
                filtersViewAdapterCountry?.setList((areas as AreasScreenState.Content).data)
            }
        }

        filtersViewAdapterCountry = FiltersViewAdapterArea() { area ->
            viewModel.selectCountry(area)
            findNavController()
                .navigate(CountryChooseFragmentDirections.actionCountryChooseFragmentToPlaceOfWorkFragment())
        }
        binding.countryRecyclerview.adapter = filtersViewAdapterCountry

        binding.buttonBack.setOnClickListener {
            findNavController()
                .navigate(CountryChooseFragmentDirections.actionCountryChooseFragmentToPlaceOfWorkFragment())
        }
    }

    fun hideAll() {
        binding.countryRecyclerview.visibility = View.GONE
        binding.countryError.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
