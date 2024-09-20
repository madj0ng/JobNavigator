package ru.practicum.android.diploma.ui.searchfilters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.LittleViewIndustryRoundBinding
import ru.practicum.android.diploma.domain.models.IndustryModel

class FiltersViewAdapterIndustry(
    private val clickListener: OnClickListener,
) : RecyclerView.Adapter<FiltersViewAdapterIndustry.ViewHolder>() {
    private var industries = mutableListOf<IndustryModel>()
    private var selectedPosition: Int = -1

    fun setList(list: List<IndustryModel>) {
        this.industries.clear()
        this.industries.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LittleViewIndustryRoundBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(
            binding = binding,
            clickListener = clickListener,
        )
    }

    override fun getItemCount(): Int = industries.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(industries[position])
    }

    inner class ViewHolder(
        private val binding: LittleViewIndustryRoundBinding,
        private val clickListener: OnClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(industryModel: IndustryModel) {
            binding.industry.text = industryModel.name
            // Событие нажатия кнопки
            itemView.setOnClickListener {
                selectedPosition = adapterPosition
                clickListener.onIndustryClick(industryModel)
                notifyDataSetChanged()
            }
            if (selectedPosition == adapterPosition) {
                binding.industrySrc.isChecked = true
            } else {
                binding.industrySrc.isChecked = false
            }
        }
    }

    fun interface OnClickListener {
        fun onIndustryClick(industry: IndustryModel)
    }
}


