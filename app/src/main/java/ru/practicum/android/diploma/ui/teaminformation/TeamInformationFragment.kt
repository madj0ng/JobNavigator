package ru.practicum.android.diploma.ui.teaminformation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.practicum.android.diploma.databinding.FragmentTeamInformationBinding

class TeamInformationFragment : Fragment() {

    private lateinit var binding: FragmentTeamInformationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeamInformationBinding.inflate(inflater, container, false)
        return binding.root
    }
}
