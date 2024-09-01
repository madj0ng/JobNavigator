package ru.practicum.android.diploma.ui.favoritejobs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.practicum.android.diploma.databinding.FragmentFavoriteJobsBinding

class FavoriteJobsFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteJobsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteJobsBinding.inflate(inflater, container, false)
        return binding.root
    }
}
