package ru.practicum.android.diploma.ui.searchfilters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import ru.practicum.android.diploma.databinding.CountryChooseFragmentBinding
import ru.practicum.android.diploma.domain.models.CountryModel
import ru.practicum.android.diploma.presentation.models.AreasScreenState
import ru.practicum.android.diploma.presentation.viewmodel.PlaceOfWorkViewModel

class CountryChooseFragment : Fragment() {
    private var _binding: CountryChooseFragmentBinding? = null
    private val binding: CountryChooseFragmentBinding get() = _binding!!
    private val viewModel: PlaceOfWorkViewModel by activityViewModel()
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
            when (areas) {
                AreasScreenState.Error -> seeError()
                AreasScreenState.ErrorInternet -> seeErrorNoInternet()
                AreasScreenState.Content((areas as AreasScreenState.Content).data) -> seeContent(areas.data)
                else -> seeError()
            }
        }

        viewModel.getAreas()

        filtersViewAdapterCountry = FiltersViewAdapterArea { area ->
            viewModel.selectCountry(country = area)
            findNavController()
                .navigateUp()
        }
        binding.countryRecyclerview.adapter = filtersViewAdapterCountry

        binding.buttonBack.setOnClickListener {
            findNavController()
                .navigateUp()
        }
    }

    private fun seeContent(list: List<CountryModel>) {
        hideAll()
        binding.countryRecyclerview.visibility = View.VISIBLE
        filtersViewAdapterCountry?.setList(list)
    }

    private fun seeErrorNoInternet() {
        hideAll()
        binding.countryErrorNoInternet.visibility = View.VISIBLE
    }

    private fun seeError() {
        hideAll()
        binding.countryError.visibility = View.VISIBLE
    }

    private fun hideAll() {
        binding.countryRecyclerview.visibility = View.GONE
        binding.countryError.visibility = View.GONE
        binding.countryErrorNoInternet.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
