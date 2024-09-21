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
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.SearchFiltersRegionFragmentBinding
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
    ): View? {
        _binding = SearchFiltersRegionFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getRegionLiveData().observe(viewLifecycleOwner) { areas ->
            Log.d("AREAS", areas.toString())
            if (areas.isEmpty()) {
                hideAll()
                binding?.regionError?.visibility = View.VISIBLE
            } else {
                Log.d("else", areas.toString())

                hideAll()
                binding?.regionRecyclerview?.visibility = View.VISIBLE
                filtersViewAdapterRegion?.setList(areas)
            }
        }

        filtersViewAdapterRegion = FilterViewAdapterRegion() { area ->
            viewModel?.selectRegion(area)
            // findNavController().popBackStack()
            findNavController().navigate(R.id.action_searchFiltersRegionFragment_to_searchFiltersCityFragment)
        }
        binding?.regionRecyclerview?.adapter = filtersViewAdapterRegion
        viewModel?.getRegions()

        binding?.buttonBack?.setOnClickListener {
            findNavController().popBackStack()
        }

        binding?.Search?.addTextChangedListener { str ->
            viewModel?.searchRegion(str.toString())
        }
    }

    fun hideAll() {
        binding?.regionRecyclerview?.visibility = View.GONE
        binding?.regionError?.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
