package com.project.premiereservice.viewmodel

import android.provider.Settings
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.premiereservice.model.MovieInfoModel
import com.project.premiereservice.model.MovieModel
import com.project.premiereservice.model.RegisteredMovieModel
import com.project.premiereservice.model.UserModel
import com.project.premiereservice.network.NetworkRequest
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainViewModel(private val networkRequest: NetworkRequest) : ViewModel() {
    private val _movieList = MutableLiveData<List<MovieModel>>()
    private val _registeredMovieList = MutableLiveData<List<RegisteredMovieModel>>()
    private val _userInfo = MutableLiveData<UserModel>()
    private val _movieInfo = MutableLiveData<MovieInfoModel>()
    private val _loginSuccess = MutableLiveData<Boolean>()
    private val _registerMovieSuccess = MutableLiveData<Boolean>()
    private val _checkMovieRegistered = MutableLiveData<Boolean>()
    val movieList = _movieList
    val loginSuccess = _loginSuccess
    val userInfo = _userInfo
    val movieInfo = _movieInfo
    val registeredMovieList = _registeredMovieList
    val registerMovieSuccess = _registerMovieSuccess
    val checkMovieRegistered = _checkMovieRegistered

    fun requestMovieLists(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            networkRequest.getMovieLists(id)?.let { response ->
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

    fun requestRegisteredMovieLists(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            networkRequest.getRegisteredMovieLists(id)?.let { response ->
                if(response.isSuccessful) {
                    response.body()?.let {
                        _registeredMovieList.postValue(it.items)
                    }
                }
            }
        }
    }

    fun requestMovieInfo(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            networkRequest.getMovieInfo(id)?.let { response ->
                if(response.isSuccessful) {
                    response.body()?.let {
                        _movieInfo.postValue(it.item)
                    }
                }
            }
        }
    }

    fun requestRegisterMovie(movieId: Long, userId: Long, isRegister: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            networkRequest.registerMovie(movieId, userId, isRegister)?.let { response ->
                if(response.isSuccessful) {
                    response.body()?.let {
                        _registerMovieSuccess.postValue(it.isSuccess)
                    }
                }
            }
        }
    }

    fun isMovieRegistered(movieId: Long, userId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            networkRequest.isMovieRegistered(movieId, userId)?.let { response ->
                if(response.isSuccessful) {
                    response.body()?.let {
                        _checkMovieRegistered.postValue(it.checkRegistered)
                    }
                }
            }
        }
    }
}