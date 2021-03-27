package com.nshumskii.testweatherapp.ui.cities

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nshumskii.testweatherapp.data.local.entities.CurrentWeatherEntity
import com.nshumskii.testweatherapp.databinding.ItemCityBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class CitiesAdapter(
    private val listener: OnItemClickListener,
) : ListAdapter<CurrentWeatherEntity, CitiesAdapter.CityHolder>(DiffCallback()) {

    inner class CityHolder(private val itemHitBinding: ItemCityBinding) :
        RecyclerView.ViewHolder(itemHitBinding.root) {

        init {
            itemHitBinding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val task = getItem(position)
                        listener.onItemClick(task)
                    }
                }
                root.setOnLongClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val task = getItem(position)
                        listener.onItemLongClick(task)
                        return@setOnLongClickListener true
                    }
                    return@setOnLongClickListener false
                }
            }
        }

        fun bind(weather: CurrentWeatherEntity) {
            itemHitBinding.cityName.text = weather.name
            itemHitBinding.temperature.text = "${weather.main.temp.roundToInt()} ℃"
            val formatter = SimpleDateFormat("dd/MM/yyyy")
            val dateString = formatter.format(Date(weather.dt * 1000L))
            itemHitBinding.syncTime.text = dateString
            Glide.with(itemView)
                .load("http://openweathermap.org/img/wn/${weather.weather[0].icon}@2x.png")
                .into(itemHitBinding.icon)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(weather: CurrentWeatherEntity)
        fun onItemLongClick(weather: CurrentWeatherEntity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder {
        val itemHitBinding =
            ItemCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CityHolder(itemHitBinding)
    }

    override fun onBindViewHolder(holder: CityHolder, position: Int) {
        val weather = getItem(position)
        holder.bind(weather)
    }

    class DiffCallback : DiffUtil.ItemCallback<CurrentWeatherEntity>() {
        override fun areItemsTheSame(oldItem: CurrentWeatherEntity, newItem: CurrentWeatherEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CurrentWeatherEntity, newItem: CurrentWeatherEntity) =
            oldItem == newItem
    }

}