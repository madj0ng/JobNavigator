package ru.practicum.android.diploma.ui.searchfilters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedDispatcher
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.doOnNextLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.IndustryChoosingFragmentBinding
import ru.practicum.android.diploma.domain.models.IndustryModel
import ru.practicum.android.diploma.presentation.models.IndustryScreenState
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
        viewModel.industryLiveData.observe(viewLifecycleOwner) { state ->
            render(state)
        }

        viewModel.selectIndustryLiveData.observe(viewLifecycleOwner) { selectedIndustry ->
            if (selectedIndustry == null) {
                binding.btnSelect.visibility = View.GONE
            } else {
                binding.btnSelect.visibility = View.VISIBLE
                filtersViewAdapterIndustry?.setSelectId(selectedIndustry.id)
                moveRvList()
                filtersViewAdapterIndustry!!.notifyDataSetChanged()
            }
        }

        filtersViewAdapterIndustry = FiltersViewAdapterIndustry { industry ->
            viewModel.selectIndustry(industry)
        }
        binding.industriesRecyclerView.adapter = filtersViewAdapterIndustry

        binding.buttonBack.setOnClickListener {
            viewModel.selectIndustry(null)
            findNavController()
                .navigateUp()
            binding.btnSelect.visibility = View.GONE
            restoreRvList()
        }
        binding.search.addTextChangedListener { str ->
            changeIcon(str.toString())
            viewModel.searchIndustry(str.toString())
        }

        binding.btnSelect.setOnClickListener {
            binding.btnSelect.visibility = View.GONE
            restoreRvList()
            viewModel.saveIndustry()
            findNavController()
                .navigateUp()
        }

        binding.clearText.setOnClickListener {
            binding.search.setText("")
        }

        OnBackPressedDispatcher {
            binding.btnSelect.visibility = View.GONE
            restoreRvList()
            findNavController().navigateUp()
        }
    }

    private fun render(state: IndustryScreenState) {
        when (state) {
            is IndustryScreenState.ErrorContent -> seeErrorContent()
            is IndustryScreenState.ErrorInternet -> seeErrorInternet()
            is IndustryScreenState.Content -> seeContent(state.data)
        }
    }

    private fun seeErrorContent() {
        hideAll()
        with(binding) {
            industriesErrorNoIndustry.visibility = View.VISIBLE
            ivInformImage.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.error_no_data))
            ivInformBottomText.text = view?.resources?.getString(R.string.no_industry)
        }
    }

    private fun seeErrorInternet() {
        hideAll()
        with(binding) {
            industriesErrorNoIndustry.visibility = View.VISIBLE
            ivInformImage
                .setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.cant_fetch_region))
            ivInformBottomText.text = view?.resources?.getString(R.string.search_error_no_list)
        }
    }

    private fun seeContent(list: List<IndustryModel>) {
        hideAll()
        binding.industriesRecyclerView.visibility = View.VISIBLE
        filtersViewAdapterIndustry?.setList(list)
    }

    private fun hideAll() {
        binding.industriesErrorNoIndustry.visibility = View.GONE
        binding.industriesRecyclerView.visibility = View.GONE
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

    private fun moveRvList() {
        binding.root.doOnNextLayout {
            val pad = binding.frameLayout.bottom - binding.btnSelect.bottom
            binding.industriesRecyclerView.setPadding(0, 0, 0, pad / 2)
        }
    }

    private fun restoreRvList() {
        binding.root.doOnNextLayout {
            binding.industriesRecyclerView.setPadding(0, 0, 0, 0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
