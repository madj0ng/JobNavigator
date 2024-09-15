package ru.practicum.android.diploma.ui.jobdetails

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentJobDetailsBinding
import ru.practicum.android.diploma.domain.models.VacancyDetailsModel
import ru.practicum.android.diploma.presentation.models.VacancyDetailsScreenState
import ru.practicum.android.diploma.presentation.viewmodel.VacancyDetailsViewModel

class JobDetailsFragment : Fragment() {
    private var binding: FragmentJobDetailsBinding? = null
    private val viewModel: VacancyDetailsViewModel by viewModel()
    private lateinit var vacancyId: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            vacancyId = JobDetailsFragmentArgs.fromBundle(it).vacancyId
        }
        binding = FragmentJobDetailsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getScreenLiveData().observe(viewLifecycleOwner) { state ->
            when (state) {
                is VacancyDetailsScreenState.Content -> {
                    showContent(state.data)
                    Log.d("Content", "Content")
                }

                is VacancyDetailsScreenState.Loading -> {
                    showLoading()
                    Log.d("Content", "Loading")

                }

                is VacancyDetailsScreenState.NotFound -> {
                    vacancyNotFoundOrDeleted()
                    Log.d("Content", "NotFound")

                }

                else -> {
                    serverError()
                    Log.d("Content", "serverError")

                }
            }
        }

        viewModel.getVacancy(vacancyId)

        binding?.backButton?.setOnClickListener{
            findNavController().popBackStack()
        }

    }


    private fun showLoading() {
        hideAll()
        binding?.pbPage?.visibility = View.VISIBLE
    }

    private fun serverError() {
        hideAll()
        binding?.groupServerError?.visibility = View.VISIBLE
    }

    private fun vacancyNotFoundOrDeleted() {
        hideAll()
        binding?.vacancyNotFound?.visibility = View.VISIBLE
    }

    fun hideAll() {
        binding?.vacancyDetails?.visibility = View.GONE
        binding?.groupServerError?.visibility = View.GONE
        binding?.vacancyNotFound?.visibility = View.GONE
        binding?.pbPage?.visibility = View.GONE
    }

    private fun showContent(vacancy: VacancyDetailsModel) {
        hideAll()
        binding?.vacancyDetails?.visibility = View.VISIBLE
        binding?.vacancyName?.text = vacancy.name
        binding?.vacancySalary?.text = vacancy.salary
        binding?.experienceValues?.text = vacancy.experience
        binding?.isFulltimeIsRemote?.text = vacancy.schedule
        binding?.let { insertHtml(it.vacancyDescriptionValue, vacancy.description) }
        if (vacancy.keySkills.isEmpty()) {
            binding?.keySkills?.visibility = View.GONE
        } else {
            binding?.let { insertHtml(it.vacancyKeySkillsValue, generateHtmlList(vacancy.keySkills)) }
        }
        binding?.let {
            Glide.with(requireContext())
                .load(vacancy.employerIcon)
                .placeholder(R.drawable.company_logo_placeholder)
                .centerCrop()
                .transform(RoundedCorners(12))
                .into(it.cardVacancy)
        }
        binding?.city?.text = vacancy.address
        binding?.companyName?.text = vacancy.employerName

    }

    fun generateHtmlList(inputList: List<String>): String {
        val listType = "ul"
        val htmlBuilder = StringBuilder()
        htmlBuilder.append("<$listType>\n")
        for (item in inputList) {
            htmlBuilder.append("<li>$item</li>\n")
        }
        htmlBuilder.append("</$listType>")
        return htmlBuilder.toString()
    }

    fun insertHtml(textView: TextView, html: String) {
        textView.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(html)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
