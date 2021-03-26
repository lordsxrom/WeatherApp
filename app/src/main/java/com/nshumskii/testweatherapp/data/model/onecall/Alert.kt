package com.nshumskii.testweatherapp.data.model.onecall

data class Alert(
    val description: String,
    val end: Int,
    val event: String,
    val sender_name: String,
    val start: Int
)