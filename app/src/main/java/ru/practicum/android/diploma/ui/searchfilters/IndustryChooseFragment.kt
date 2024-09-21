package ru.practicum.android.diploma.ui.searchfilters

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnNextLayout
import androidx.core.view.marginBottom
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import ru.practicum.android.diploma.databinding.IndustryChoosingFragmentBinding
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.presentation.viewmodel.FilterViewModel

class IndustryChooseFragment : Fragment() {

    private var _binding: IndustryChoosingFragmentBinding? = null
    private val binding: IndustryChoosingFragmentBinding get() = _binding!!
    private val viewModel: FilterViewModel by activityViewModel()
    private var filtersViewAdapterIndustry: FiltersViewAdapterIndustry? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = IndustryChoosingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getIndustryLiveData().observe(viewLifecycleOwner) { industries ->
            if (industries is Resource.Error) {
                hideAll()
                binding.industriesErrorNoIndustry.visibility = View.VISIBLE
            } else {
                hideAll()
                binding.industriesRecyclerView.visibility = View.VISIBLE
                filtersViewAdapterIndustry?.setList((industries as Resource.Success).data)
            }
        }

        viewModel.getSelectedIndustryLiveData().observe(viewLifecycleOwner) { selectedIndustry ->
            if (selectedIndustry == null) {
                binding.btnSelect.visibility = View.GONE
            } else {
                binding.btnSelect.visibility = View.VISIBLE
                moveRvList()
                filtersViewAdapterIndustry!!.notifyDataSetChanged()
            }
        }

        filtersViewAdapterIndustry = FiltersViewAdapterIndustry { industry ->
            viewModel.selectIndustry(industry)
        }
        binding.industriesRecyclerView.adapter = filtersViewAdapterIndustry
        viewModel.getIndustries()

        binding.buttonBack.setOnClickListener {
            viewModel.unSelectIndustry()
            findNavController().popBackStack()
        }
        binding.search.addTextChangedListener { str ->
            changeIcon(str.toString())
            viewModel.searchIndustry(str.toString())
        }

        binding.btnSelect.setOnClickListener {
            viewModel.saveIndustry()
            findNavController().popBackStack()
        }

        binding.clearText.setOnClickListener {
            binding.search.setText("")
        }
    }

    fun hideAll() {
        binding.industriesErrorNoIndustry.visibility = View.GONE
        binding.industriesRecyclerView.visibility = View.GONE
    }

    fun changeIcon(str: String) {
        if (str.isNotEmpty()) {
            binding.searchBtn.visibility = View.GONE
            binding.clearText.visibility = View.VISIBLE
        } else {
            binding.searchBtn.visibility = View.VISIBLE
            binding.clearText.visibility = View.GONE
        }
    }

    fun moveRvList() {
        binding.root.doOnNextLayout {
            val pad = binding.frameLayout.bottom - binding.btnSelect.bottom
            Log.d("PADDING", "$pad")
            binding.industriesRecyclerView.setPadding(0, 0, 0, pad / 2)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
