package ru.practicum.android.diploma.ui.favoritejobs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.koin.java.KoinJavaComponent.getKoin
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.VacancyItemBinding
import ru.practicum.android.diploma.presentation.models.VacancyInfo
import ru.practicum.android.diploma.util.FormatConverter
import ru.practicum.android.diploma.util.GlideApp

class FavoriteJobsViewAdapter(
    private val clickListener: ViewHolder.OnClickListener
) : RecyclerView.Adapter<FavoriteJobsViewAdapter.ViewHolder>() {
    private var vacancies = mutableListOf<VacancyInfo>()

    fun setList(list: List<VacancyInfo>) {
        this.vacancies.clear()
        this.vacancies.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = VacancyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(
            binding = binding,
            clickListener = clickListener,
            converter = getKoin().get()
        )
    }

    override fun getItemCount(): Int = vacancies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(vacancies[position])
    }

    class ViewHolder(
        private val binding: VacancyItemBinding,
        private val clickListener: OnClickListener,
        converter: FormatConverter,
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            private const val IMG_RADIUS_PX = 12F
        }

        private val imgRadius = converter.dpToPx(IMG_RADIUS_PX)

        fun bind(vacancy: VacancyInfo) {
            binding.departmentName.text = vacancy.departamentName
            binding.vacancyName.text = "${vacancy.vacancyName}, ${vacancy.city}"
            binding.sallary.text = vacancy.salary

            GlideApp.with(itemView)
                .load(vacancy.logoUrl)
                .placeholder(R.drawable.company_logo_placeholder)
                .centerCrop()
                .transform(RoundedCorners(imgRadius))
                .into(binding.companyLogo)

            itemView.setOnClickListener { clickListener.onTrackClick(vacancy.id) }
        }

        fun interface OnClickListener {
            fun onTrackClick(vacancyId: String)
        }
    }
}
