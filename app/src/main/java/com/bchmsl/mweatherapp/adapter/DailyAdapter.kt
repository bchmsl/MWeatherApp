package com.bchmsl.mweatherapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bchmsl.mweatherapp.databinding.LayoutDailyItemBinding
import com.bchmsl.mweatherapp.model.DailyData
import com.bchmsl.mweatherapp.setImage

class DailyAdapter :
    ListAdapter<DailyData.DailyTEMP, DailyAdapter.ItemViewHolder>(ListItemCallback()) {
    inner class ItemViewHolder(private val binding: LayoutDailyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind() {
            val currentData = currentList[adapterPosition]
            binding.apply {
                tvMinimumTemp.text = currentData.main?.tempMin?.toInt().toString() + "°C"
                tvAverageTemp.text = currentData.main?.temp?.toInt().toString() + "°C"
                tvMaximumTemp.text = currentData.main?.tempMax?.toInt().toString() + "°C"
                val date = currentData.dtTxt?.split(" ")?.get(0)
                tvDate.text = date.toString()
                tvMain.text = currentData.weather?.get(0)?.main
                currentData.weather?.get(0)?.icon?.let { ivIcon.setImage(it) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemViewHolder(
            LayoutDailyItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind()
    }

    class ListItemCallback : DiffUtil.ItemCallback<DailyData.DailyTEMP>() {
        override fun areItemsTheSame(
            oldItem: DailyData.DailyTEMP,
            newItem: DailyData.DailyTEMP
        ) =
            oldItem.dtTxt == newItem.dtTxt

        override fun areContentsTheSame(
            oldItem: DailyData.DailyTEMP,
            newItem: DailyData.DailyTEMP
        ) =
            oldItem == newItem

    }
}