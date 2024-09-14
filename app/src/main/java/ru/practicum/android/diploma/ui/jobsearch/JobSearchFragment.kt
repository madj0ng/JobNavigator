package ru.practicum.android.diploma.ui.jobsearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentJobSearchBinding
import ru.practicum.android.diploma.presentation.models.SearchUiState
import ru.practicum.android.diploma.presentation.viewmodel.JobSearchViewModel

class JobSearchFragment : Fragment() {
    private var _binding: FragmentJobSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: JobSearchViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentJobSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getToastLiveData().observe(viewLifecycleOwner, this::toast)
        viewModel.getScreenLiveData().observe(viewLifecycleOwner, this::updateUiState)
    }

    private fun updateUiState(uiState: SearchUiState) {
        when (uiState) {
            is SearchUiState.Content -> showContent(uiState)
            else -> showScreen(uiState)
        }
    }

    private fun showScreen(uiState: SearchUiState) {
        binding.rvJobList.isVisible = uiState.isJobsList
        binding.tvJobSearchCount.isVisible = uiState.isJobsCount
        binding.pbJobList.isVisible = uiState.isJobsListBrogressBar
        binding.pbPage.isVisible = uiState.isBrogressBar
        binding.ivInformImage.isVisible = uiState.isInformImage
        binding.tvInformBottomText.isVisible = uiState.isBottomText
        if (uiState.topText != null) {
            binding.tvJobSearchCount.setText(uiState.topText!!)
        }
        if (uiState.bottomText != null) {
            binding.tvInformBottomText.setText(uiState.bottomText!!)
        }
        if (uiState.url != null) {
            binding.ivInformImage.setImageResource(uiState.url!!)
        }
    }

    private fun showContent(uiState: SearchUiState.Content) {
        showScreen(uiState)
        // Обновление списка
    }

    private fun toast(@StringRes res: Int) {
        Toast.makeText(requireActivity(), res, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
