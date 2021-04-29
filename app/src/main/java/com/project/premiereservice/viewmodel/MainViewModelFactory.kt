package com.project.premiereservice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.premiereservice.network.MovieLists

class MainViewModelFactory(private val movieLists: MovieLists) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(MovieLists::class.java).newInstance(movieLists)
    }
}