package ru.practicum.android.diploma.ui.searchfilters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.LittleViewForFilterBinding
import ru.practicum.android.diploma.domain.models.CityModel

class FiltersViewAdapterCity(
    private val clickListener: OnClickListener,
) : RecyclerView.Adapter<FiltersViewAdapterCity.ViewHolder>() {
    private var areas = mutableListOf<CityModel>()

    fun setList(list: List<CityModel>) {
        this.areas.clear()
        this.areas.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LittleViewForFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(
            binding = binding,
            clickListener = clickListener,
        )
    }

    override fun getItemCount(): Int = areas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(areas[position])
    }

    class ViewHolder(
        private val binding: LittleViewForFilterBinding,
        private val clickListener: OnClickListener,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(cityModel: CityModel) {
            binding.country.text = cityModel.name
            // Событие нажатия кнопки
            itemView.setOnClickListener { clickListener.onAreaClick(cityModel) }
        }
    }

    fun interface OnClickListener {
        fun onAreaClick(cityModel: CityModel)
    }
}
