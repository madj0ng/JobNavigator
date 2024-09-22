package ru.practicum.android.diploma.ui.searchfilters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.SearchFiltersRegionFragmentBinding
import ru.practicum.android.diploma.domain.models.RegionModel
import ru.practicum.android.diploma.presentation.models.RegionScreenState
import ru.practicum.android.diploma.presentation.viewmodel.FilterViewModel

class SearchFiltersRegionFragment : Fragment() {
    private var _binding: SearchFiltersRegionFragmentBinding? = null
    private val binding: SearchFiltersRegionFragmentBinding get() = _binding!!
    private val viewModel: FilterViewModel by activityViewModel()
    private var filtersViewAdapterRegion: FilterViewAdapterRegion? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchFiltersRegionFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.regionsLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                RegionScreenState.ErrorNoList -> seeErrorNoList()
                RegionScreenState.ErrorNoRegion -> seeErrorNoRegion()
                RegionScreenState.Content((state as RegionScreenState.Content).data) -> render(state.data)
                else -> seeErrorNoList()
            }
        }

        filtersViewAdapterRegion = FilterViewAdapterRegion() { area ->
            viewModel.selectRegion(area)
            if (area.city.isEmpty()) {
                viewModel.saveArea()
                findNavController().navigateUp()
            } else {
                findNavController().navigate(R.id.action_searchFiltersRegionFragment_to_searchFiltersCityFragment)
            }
        }
        binding.regionRecyclerview.adapter = filtersViewAdapterRegion
        viewModel.getRegions()

        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.Search.addTextChangedListener { str ->
            changeIcon(str.toString())
            viewModel.searchRegion(str.toString())
        }

        binding.clearText.setOnClickListener {
            binding.Search.setText("")
        }
    }

    private fun seeErrorNoList() {
        hideAll()
        with(binding) {
            regionError.visibility = View.VISIBLE
            ivInformImage
                .setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.cant_fetch_region))
            ivInformBottomText.text = view?.resources?.getString(R.string.search_error_no_list)
        }
    }

    private fun seeErrorNoRegion() {
        hideAll()
        with(binding) {
            regionError.visibility = View.VISIBLE
            ivInformImage.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.error_no_data))
            ivInformBottomText.text = view?.resources?.getString(R.string.no_region)
        }
    }

    private fun render(list: List<RegionModel>) {
        hideAll()
        binding.regionRecyclerview.visibility = View.VISIBLE
        filtersViewAdapterRegion?.setList(list)
    }

    private fun hideAll() {
        binding.regionRecyclerview.visibility = View.GONE
        binding.regionError.visibility = View.GONE
    }

    private fun changeIcon(str: String) {
        if (str.isNotEmpty()) {
            binding.searchBtn.visibility = View.GONE
            binding.clearText.visibility = View.VISIBLE
        } else {
            binding.searchBtn.visibility = View.VISIBLE
            binding.clearText.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
