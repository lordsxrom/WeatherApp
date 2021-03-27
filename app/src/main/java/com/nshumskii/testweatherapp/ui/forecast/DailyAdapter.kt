package com.nshumskii.testweatherapp.ui.forecast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nshumskii.testweatherapp.data.model.onecall.Daily
import com.nshumskii.testweatherapp.databinding.ItemDailyBinding
import java.text.SimpleDateFormat
import java.util.*

class DailyAdapter : ListAdapter<Daily, DailyAdapter.DailyHolder>(DiffCallback()) {

    class DailyHolder(private val itemBinding: ItemDailyBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(daily: Daily) {
            val formatter = SimpleDateFormat("EEE, d MMM hh:mm aaa")
            val dateString = formatter.format(Date(daily.dt * 1000L))
            itemBinding.date.text = dateString
            itemBinding.humidityValue.text = "${daily.humidity.toString()} %"
            itemBinding.pressureValue.text = "${daily.pressure.toString()} mm Hg"
            itemBinding.sunriseValue.text = SimpleDateFormat("hh:mm aaa").format(Date(daily.sunrise * 1000L))
            itemBinding.sunsetValue.text = SimpleDateFormat("hh:mm aaa").format(Date(daily.sunset * 1000L))
            itemBinding.tempValue.text = "${daily.temp.day.toString()} ℃"
            itemBinding.feelsLikeValue.text = "${daily.feels_like.day.toString()} ℃"
            Glide.with(itemView)
                .load("http://openweathermap.org/img/wn/${daily.weather[0].icon}@2x.png")
                .into(itemBinding.icon)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyHolder {
        val itemBinding =
            ItemDailyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DailyHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: DailyHolder, position: Int) {
        val daily = getItem(position)
        holder.bind(daily)
    }

    class DiffCallback : DiffUtil.ItemCallback<Daily>() {
        override fun areItemsTheSame(oldItem: Daily, newItem: Daily) =
            oldItem.dt == newItem.dt

        override fun areContentsTheSame(oldItem: Daily, newItem: Daily) =
            oldItem == newItem
    }

}