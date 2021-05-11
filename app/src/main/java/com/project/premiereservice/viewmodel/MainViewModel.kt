package com.project.premiereservice.viewmodel

import android.provider.Settings
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.premiereservice.model.MovieModel
import com.project.premiereservice.model.UserModel
import com.project.premiereservice.network.NetworkRequest
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainViewModel(private val networkRequest: NetworkRequest) : ViewModel() {
    private val _movieList = MutableLiveData<List<MovieModel>>()
    private val _userInfo = MutableLiveData<UserModel>()
    private val _loginSuccess = MutableLiveData<Boolean>()
    val movieList = _movieList
    val loginSuccess = _loginSuccess
    val userInfo = _userInfo

    fun requestMovieLists(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            networkRequest.getMovieLists(query)?.let { response ->
                if(response.isSuccessful) {
                    response.body()?.let {
                        _movieList.postValue(it.items)
                    }
                }
            }
        }
    }

    fun requestUserLogin(id: String, pw: String) {
        CoroutineScope(Dispatchers.IO).launch {
            networkRequest.isLoginSuccess(id, pw)?.let { response ->
                if(response.isSuccessful) {
                    response.body()?.let {
                        _loginSuccess.postValue(it.isSuccess)
                    }
                }
            }
        }
    }

    fun requestUserInfo(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            networkRequest.getUserInfo(id)?.let { response ->
                if(response.isSuccessful) {
                    response.body()?.let {
                        _userInfo.postValue(it.item)
                    }
                }
            }
        }
    }
}