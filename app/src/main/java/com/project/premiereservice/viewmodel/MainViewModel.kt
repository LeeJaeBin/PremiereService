package com.project.premiereservice.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.premiereservice.model.MovieModel
import com.project.premiereservice.network.MovieLists
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val movieLists: MovieLists) : ViewModel() {
    private val _movieList = MutableLiveData<List<MovieModel>>()
    val movieList = _movieList

    fun requestMovieLists(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            movieLists.getMovieLists(query)?.let { response ->
                if(response.isSuccessful) {
                    response.body()?.let {
                        _movieList.postValue(it.items)
                    }
                }
            }
        }
    }
}