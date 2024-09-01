package ru.practicum.android.diploma.ui.searchfilters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.practicum.android.diploma.databinding.FragmentSearchFiltersBinding

class SearchFiltersFragment : Fragment() {
    private lateinit var binding: FragmentSearchFiltersBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchFiltersBinding.inflate(inflater, container, false)
        return binding.root
    }
}
