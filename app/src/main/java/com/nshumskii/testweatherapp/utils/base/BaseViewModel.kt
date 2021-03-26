package com.nshumskii.testweatherapp.utils.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nshumskii.testweatherapp.utils.Event

abstract class BaseViewModel : ViewModel() {

    val progress: MutableLiveData<Event<Boolean>> = MutableLiveData()

    val error: MutableLiveData<Event<String>> = MutableLiveData()

}