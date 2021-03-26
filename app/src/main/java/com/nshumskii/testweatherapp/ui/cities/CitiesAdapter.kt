package com.nshumskii.testweatherapp.ui.cities

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nshumskii.testweatherapp.data.local.entities.CurrentWeatherEntity
import com.nshumskii.testweatherapp.data.model.common.Coord
import com.nshumskii.testweatherapp.databinding.ItemCityBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class CitiesAdapter(
    private val clickListener: (coord: Coord) -> Unit,
    private val longClickListener: (CurrentWeatherEntity) -> Unit
) : RecyclerView.Adapter<CitiesAdapter.CityHolder>() {

    private val cities = mutableListOf<CurrentWeatherEntity>()

    class CityHolder(private val itemHitBinding: ItemCityBinding) :
        RecyclerView.ViewHolder(itemHitBinding.root) {
        fun bind(
            weather: CurrentWeatherEntity,
            listener: (coord: Coord) -> Unit,
            longClickListener: (CurrentWeatherEntity) -> Unit
        ) {
            itemHitBinding.root.setOnClickListener { listener.invoke(weather.coord) }
            itemHitBinding.root.setOnLongClickListener {
                longClickListener.invoke(weather)
                true
            }
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

    fun setData(cities: List<CurrentWeatherEntity>) {
        this.cities.clear()
        this.cities.addAll(cities)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder {
        val itemHitBinding =
            ItemCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CityHolder(itemHitBinding)
    }

    override fun onBindViewHolder(holder: CityHolder, position: Int) {
        val city = cities[position]
        holder.bind(city, clickListener, longClickListener)
    }

    override fun getItemCount(): Int = cities.size

}