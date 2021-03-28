package com.nshumskii.testweatherapp.ui.forecast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nshumskii.testweatherapp.data.model.onecall.Daily
import com.nshumskii.testweatherapp.data.model.onecall.Hourly
import com.nshumskii.testweatherapp.databinding.ItemDailyBinding
import com.nshumskii.testweatherapp.databinding.ItemHourlyBinding
import com.nshumskii.testweatherapp.utils.base.BaseHolder
import java.text.SimpleDateFormat
import java.util.*

class DailyAdapter : RecyclerView.Adapter<BaseHolder<*>>() {

    companion object {
        private const val TYPE_DAILY = 0
        private const val TYPE_HOURLY = 1
    }

    private var adapterDataList: MutableList<Any> = mutableListOf()

    fun submitList(list: List<Any>) {
        adapterDataList.clear()
        adapterDataList.addAll(list)
        notifyDataSetChanged()
    }

    class DailyHolder(private val itemBinding: ItemDailyBinding) :
        BaseHolder<Daily>(itemBinding) {

        override fun bind(data: Daily) {
            val formatter = SimpleDateFormat("EEE, d MMM hh:mm aaa")
            val dateString = formatter.format(Date(data.dt * 1000L))
            itemBinding.date.text = dateString
            itemBinding.humidityValue.text = "${data.humidity.toString()} %"
            itemBinding.pressureValue.text = "${data.pressure.toString()} mm Hg"
            itemBinding.sunriseValue.text = SimpleDateFormat("hh:mm aaa").format(Date(data.sunrise * 1000L))
            itemBinding.sunsetValue.text = SimpleDateFormat("hh:mm aaa").format(Date(data.sunset * 1000L))
            itemBinding.tempValue.text = "${data.temp.day.toString()} ℃"
            itemBinding.feelsLikeValue.text = "${data.feels_like.day.toString()} ℃"
            Glide.with(itemView)
                .load("http://openweathermap.org/img/wn/${data.weather[0].icon}@2x.png")
                .into(itemBinding.icon)
        }
    }

    class HourlyHolder(private val itemBinding: ItemHourlyBinding) :
        BaseHolder<Hourly>(itemBinding) {

        override fun bind(data: Hourly) {
            val formatter = SimpleDateFormat("hh:mm aaa")
            val dateString = formatter.format(Date(data.dt * 1000L))
            itemBinding.date.text = dateString
            itemBinding.humidityValue.text = "${data.humidity} %"
            itemBinding.pressureValue.text = "${data.pressure} mm Hg"
            itemBinding.tempValue.text = "${data.temp} ℃"
            itemBinding.feelsLikeValue.text = "${data.feels_like} ℃"
            Glide.with(itemView)
                .load("http://openweathermap.org/img/wn/${data.weather[0].icon}@2x.png")
                .into(itemBinding.icon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<*> {
        return when (viewType) {
            TYPE_DAILY -> {
                val itemBinding =
                    ItemDailyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return DailyHolder(itemBinding)
            }
            TYPE_HOURLY -> {
                val itemBinding =
                    ItemHourlyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return HourlyHolder(itemBinding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: BaseHolder<*>, position: Int) {
        val element = adapterDataList[position]
        when (holder) {
            is DailyHolder -> holder.bind(element as Daily)
            is HourlyHolder -> holder.bind(element as Hourly)
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        val comparable = adapterDataList[position]
        return when (comparable) {
            is Daily -> TYPE_DAILY
            is Hourly -> TYPE_HOURLY
            else -> throw IllegalArgumentException("Invalid type of data " + position)
        }
    }

    override fun getItemCount(): Int = adapterDataList.size

}