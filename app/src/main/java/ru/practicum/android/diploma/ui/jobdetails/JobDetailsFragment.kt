package ru.practicum.android.diploma.ui.jobdetails

import android.os.Build
import android.os.Bundle
import android.text.Html
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
import ru.practicum.android.diploma.presentation.models.VacancyInfo
import ru.practicum.android.diploma.presentation.viewmodel.VacancyDetailsViewModel

private const val ICON_RADIUS = 12

class JobDetailsFragment : Fragment() {
    private var _binding: FragmentJobDetailsBinding? = null
    private val binding get(): FragmentJobDetailsBinding = _binding!!
    private val viewModel: VacancyDetailsViewModel by viewModel()
    private var vacancyInfo: VacancyInfo? = null
    private var vacancyDetailsModel: VacancyDetailsModel? = null
    private var vacancyId: String = ""
    private var vacancyUrl: String = ""
    private var isFavorite: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJobDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            vacancyId = JobDetailsFragmentArgs.fromBundle(it).vacancyId
        }
        viewModel.getScreenLiveData().observe(viewLifecycleOwner) { state ->
            when (state) {
                is VacancyDetailsScreenState.Loading -> showLoading()

                is VacancyDetailsScreenState.Content -> {
                    showContent(state.data)
                    setParam(state)
                }

                is VacancyDetailsScreenState.NotFound -> vacancyNotFoundOrDeleted()

                else -> serverError()
            }
        }

        viewModel.getVacancy(vacancyId)

        viewModel.likeLiveData.observe(viewLifecycleOwner) { state ->
            setLikeButton(state)
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.shareButton.setOnClickListener {
            viewModel.sharingVacancy(vacancyUrl)
        }

        binding.likeButton.setOnClickListener {
            if (isFavorite) {
                viewModel.deleteVcancyFromFavorite(vacancyInfo!!, vacancyDetailsModel!!)
                isFavorite = false
            } else {
                viewModel.addVacansyAtFavorite(vacancyInfo!!, vacancyDetailsModel!!)
                isFavorite = true
            }
        }
    }

    private fun setParam(state: VacancyDetailsScreenState.Content) {
        vacancyUrl = state.data.alternativeUrl
        vacancyInfo = VacancyInfo(
            vacancyId,
            state.data.name,
            state.data.employerName,
            state.data.salary ?: "",
            state.data.employerIcon,
            state.data.address ?: "",
        )
        vacancyDetailsModel = state.data
        isFavorite = state.data.isFavorite
    }

    private fun showLoading() {
        hideAll()
        binding.pbPage.visibility = View.VISIBLE
    }

    private fun serverError() {
        hideAll()
        binding.groupServerError.visibility = View.VISIBLE
    }

    private fun vacancyNotFoundOrDeleted() {
        hideAll()
        binding.vacancyNotFound.visibility = View.VISIBLE
    }

    fun hideAll() {
        binding.vacancyDetails.visibility = View.GONE
        binding.groupServerError.visibility = View.GONE
        binding.vacancyNotFound.visibility = View.GONE
        binding.pbPage.visibility = View.GONE
        binding.shareButton.visibility = View.GONE
        binding.likeButton.visibility = View.GONE
    }

    private fun showContent(vacancy: VacancyDetailsModel) {
        hideAll()
        setLikeButton(vacancy.isFavorite)
        binding.shareButton.visibility = View.VISIBLE
        binding.likeButton.visibility = View.VISIBLE
        binding.vacancyDetails.visibility = View.VISIBLE
        binding.vacancyName.text = vacancy.name
        binding.vacancySalary.text = vacancy.salary
        binding.experienceValues.text = vacancy.experience
        binding.isFulltimeIsRemote.text = vacancy.schedule
        binding.let { insertHtml(it.vacancyDescriptionValue, vacancy.description) }
        if (vacancy.keySkills.isEmpty()) {
            binding.keySkills.visibility = View.GONE
        } else {
            binding.let { insertHtml(it.vacancyKeySkillsValue, generateHtmlList(vacancy.keySkills)) }
        }
        binding.let {
            Glide.with(requireContext())
                .load(vacancy.employerIcon)
                .placeholder(R.drawable.company_logo_placeholder)
                .centerCrop()
                .transform(RoundedCorners(ICON_RADIUS))
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

    fun setLikeButton(isFavorite: Boolean) {
        if (isFavorite) {
            binding.likeButton.setImageResource(R.drawable.ic_is_liked)
        } else {
            binding.likeButton.setImageResource(R.drawable.ic_liked)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
