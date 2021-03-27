package com.nshumskii.testweatherapp.ui.forecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nshumskii.testweatherapp.data.local.entities.OnecallEntity
import com.nshumskii.testweatherapp.data.model.common.Coord
import com.nshumskii.testweatherapp.data.repository.OnecallRepository
import com.nshumskii.testweatherapp.utils.Event
import com.nshumskii.testweatherapp.utils.Result
import com.nshumskii.testweatherapp.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnecallViewModel @Inject constructor(
    private val onecallRepository: OnecallRepository
) : BaseViewModel() {

    private val _onecall: MutableLiveData<OnecallEntity> = MutableLiveData()
    val onecall: LiveData<OnecallEntity> get() = _onecall

    fun getOnecall(coord: Coord) {
        viewModelScope.launch {
            onecallRepository.getOnecall(coord).collect { result ->
                when (result.status) {
                    Result.Status.SUCCESS -> {
                        progress.value = Event(false)
                        result.data?.let { _onecall.value = it }
                    }
                    Result.Status.ERROR -> {
                        progress.value = Event(false)
                        error.value = Event(result.message ?: "Error")
                    }
                    Result.Status.LOADING -> {
                        progress.value = Event(true)
                    }
                }
            }
        }
    }

}