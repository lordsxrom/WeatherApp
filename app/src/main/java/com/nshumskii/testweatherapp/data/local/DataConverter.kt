package com.nshumskii.testweatherapp.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nshumskii.testweatherapp.data.model.common.Weather
import com.nshumskii.testweatherapp.data.model.onecall.Alert
import com.nshumskii.testweatherapp.data.model.onecall.Daily
import com.nshumskii.testweatherapp.data.model.onecall.Hourly
import com.nshumskii.testweatherapp.data.model.onecall.Minutely
import java.lang.reflect.Type


class DataConverter {

    @TypeConverter
    fun fromWeatherList(list: List<Weather?>?): String? {
        if (list == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Weather?>?>() {}.type
        return gson.toJson(list, type)
    }

    @TypeConverter
    fun toWeatherList(string: String?): List<Weather>? {
        if (string == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Weather?>?>() {}.type
        return gson.fromJson<List<Weather>>(string, type)
    }

    @TypeConverter
    fun fromAlertList(list: List<Alert?>?): String? {
        if (list == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Alert?>?>() {}.type
        return gson.toJson(list, type)
    }

    @TypeConverter
    fun toAlertList(string: String?): List<Alert>? {
        if (string == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Alert?>?>() {}.type
        return gson.fromJson<List<Alert>>(string, type)
    }

    @TypeConverter
    fun fromDailyList(list: List<Daily?>?): String? {
        if (list == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Daily?>?>() {}.type
        return gson.toJson(list, type)
    }

    @TypeConverter
    fun toDailyList(string: String?): List<Daily>? {
        if (string == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Daily?>?>() {}.type
        return gson.fromJson<List<Daily>>(string, type)
    }

    @TypeConverter
    fun fromHourlyList(list: List<Hourly?>?): String? {
        if (list == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Hourly?>?>() {}.type
        return gson.toJson(list, type)
    }

    @TypeConverter
    fun toHourlyList(string: String?): List<Hourly>? {
        if (string == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Hourly?>?>() {}.type
        return gson.fromJson<List<Hourly>>(string, type)
    }

    @TypeConverter
    fun fromMinutelyList(list: List<Minutely?>?): String? {
        if (list == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Minutely?>?>() {}.type
        return gson.toJson(list, type)
    }

    @TypeConverter
    fun toMinutelyList(string: String?): List<Minutely>? {
        if (string == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Minutely?>?>() {}.type
        return gson.fromJson<List<Minutely>>(string, type)
    }

}