package ru.practicum.android.diploma.ui.searchfilters

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import ru.practicum.android.diploma.databinding.IndustryChoosingFragmentBinding
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.presentation.viewmodel.FilterViewModel

class IndustryChooseFragment : Fragment() {

    private var binding: IndustryChoosingFragmentBinding? = null
    private val viewModel: FilterViewModel by activityViewModel()
    private var filtersViewAdapterIndustry: FiltersViewAdapterIndustry? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = IndustryChoosingFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel?.getIndustryLiveData()?.observe(viewLifecycleOwner) { industries ->
            if (industries is Resource.Error) {
                hideAll()
                binding?.industriesErrorNoIndustry?.visibility = View.VISIBLE
            } else {
                hideAll()
                binding?.industriesRecuclerView?.visibility = View.VISIBLE
                filtersViewAdapterIndustry?.setList((industries as Resource.Success).data)
            }
        }

        viewModel?.getSelectedIndustryLiveData()?.observe(viewLifecycleOwner) { selectedIndustry ->
            if (selectedIndustry == null) {
                binding?.btnSelect?.visibility = View.GONE
            } else {
                binding?.btnSelect?.visibility = View.VISIBLE
            }
        }

        filtersViewAdapterIndustry = FiltersViewAdapterIndustry { industry ->
            viewModel?.selectIndustry(industry)
            filtersViewAdapterIndustry!!.notifyDataSetChanged()
        }
        binding?.industriesRecuclerView?.adapter = filtersViewAdapterIndustry
        viewModel?.getIndustries()

        binding?.buttonBack?.setOnClickListener {
            findNavController().popBackStack()
        }
        binding?.search?.addTextChangedListener { str ->
            viewModel?.searchIndustry(str.toString())
        }

        binding?.btnSelect?.setOnClickListener {
            viewModel?.saveIndustry()
            findNavController().popBackStack()
        }
    }

    fun hideAll() {
        binding?.industriesErrorNoIndustry?.visibility = View.GONE
        binding?.industriesRecuclerView?.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
