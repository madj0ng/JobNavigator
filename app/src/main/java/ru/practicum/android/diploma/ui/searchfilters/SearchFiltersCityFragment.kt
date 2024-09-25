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
import ru.practicum.android.diploma.databinding.SearchFiltersCityFragmentBinding
import ru.practicum.android.diploma.presentation.viewmodel.FilterViewModel

class SearchFiltersCityFragment : Fragment() {
    private var _binding: SearchFiltersCityFragmentBinding? = null
    private val binding: SearchFiltersCityFragmentBinding get() = _binding!!
    private val viewModel: FilterViewModel by activityViewModel()
    private var filtersViewAdapterCity: FiltersViewAdapterCity? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = SearchFiltersCityFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.cityLiveData.observe(viewLifecycleOwner) { city ->
            if (city.isEmpty()) {
                hideAll()
                binding.cityError.visibility = View.VISIBLE
            } else {
                viewModel.setRegionModel(null)
                hideAll()
                binding.cityRecyclerview.visibility = View.VISIBLE
                filtersViewAdapterCity?.setList(city)
            }
        }

        filtersViewAdapterCity = FiltersViewAdapterCity() { city ->
            viewModel.selectCity(city)
            findNavController()
                .popBackStack(R.id.placeOfWorkFragment, false)
        }

        binding.cityRecyclerview.adapter = filtersViewAdapterCity
        viewModel.getCity()

        binding.buttonBack.setOnClickListener {
            findNavController()
                .navigateUp()
        }

        binding.Search.addTextChangedListener { str ->
            viewModel.searchCity(str.toString())
        }
    }

    private fun hideAll() {
        with(binding) {
            cityRecyclerview.visibility = View.GONE
            cityError.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
