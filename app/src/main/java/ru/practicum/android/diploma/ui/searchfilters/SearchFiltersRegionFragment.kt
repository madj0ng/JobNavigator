package ru.practicum.android.diploma.ui.searchfilters

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import ru.practicum.android.diploma.databinding.SearchFiltersRegionFragmentBinding
import ru.practicum.android.diploma.presentation.viewmodel.FilterViewModel

class SearchFiltersRegionFragment : Fragment() {
    private var binding: SearchFiltersRegionFragmentBinding? = null
    private val viewModel: FilterViewModel by activityViewModel()
    private var filtersViewAdapterRegion: FilterViewAdapterRegion? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SearchFiltersRegionFragmentBinding.inflate(inflater, container, false)
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
            findNavController().popBackStack()
        }
        binding?.regionRecyclerview?.adapter = filtersViewAdapterRegion
        viewModel?.getRegions()

        binding?.buttonBack?.setOnClickListener {
            findNavController().popBackStack()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel?.searchRegion(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        binding?.Search?.addTextChangedListener(simpleTextWatcher)

    }

    fun hideAll() {
        binding?.regionRecyclerview?.visibility = View.GONE
        binding?.regionError?.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}

