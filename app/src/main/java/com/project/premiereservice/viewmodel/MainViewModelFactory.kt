package com.project.premiereservice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.premiereservice.network.NetworkRequest

class MainViewModelFactory(private val movieLists: NetworkRequest) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(NetworkRequest::class.java).newInstance(movieLists)
    }
}