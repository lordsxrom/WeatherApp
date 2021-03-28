package com.nshumskii.testweatherapp.utils.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseHolder<T>(itemBinding: ViewBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {
    abstract fun bind(data: T)
}