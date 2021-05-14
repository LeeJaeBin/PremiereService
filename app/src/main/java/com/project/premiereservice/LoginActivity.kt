package com.project.premiereservice

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.premiereservice.model.UserModel
import com.project.premiereservice.network.NetworkRequest
import com.project.premiereservice.viewmodel.MainViewModel
import com.project.premiereservice.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.runBlocking

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var viewModelFactory: MainViewModelFactory
    private lateinit var context: Context

    private lateinit var strId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_login)

        context = this

        initViewModel()

        button_login.setOnClickListener {
            strId = id_login.text.toString()
            val strPw = pw_login.text.toString()

            viewModel.requestUserLogin(strId, strPw)
        }
    }

    private fun initViewModel() {
        viewModelFactory = MainViewModelFactory(NetworkRequest())
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        viewModel.loginSuccess.observe(this) {
            loginProcess(it)
        }
        viewModel.userInfo.observe(this) {
            getUserInfoProcess(it)
        }
    }

    private fun loginProcess(isSuccess: Boolean) {
        if(!isSuccess){
            Toast.makeText(this, "로그인 정보가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
        }
        else{
            /*val pref = this.getPreferences(0)
            val editor = pref.edit()

            editor.putString("id", strId).apply()*/
            setSharedPreference("premiere", "userid", strId)
            viewModel.requestUserInfo(strId)
        }
    }

    private fun getUserInfoProcess(userInfo: UserModel) {
        setIntSharedPreference("premiere", "id", userInfo.id)
        setSharedPreference("premiere", "nickname", userInfo.nickname)
        setSharedPreference("premiere", "phonenum", userInfo.phoneNum)
        setSharedPreference("premiere", "imei", userInfo.imei)
        setSharedPreference("premiere", "category", userInfo.category)
        setSharedPreference("premiere", "image", userInfo.image)

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setSharedPreference(prefsName: String, key: String, value: String) {
        getSharedPreferences(prefsName, Context.MODE_PRIVATE)
                .edit().apply { putString(key, value); apply() }
    }

    private fun setIntSharedPreference(prefsName: String, key: String, value: Int) {
        getSharedPreferences(prefsName, Context.MODE_PRIVATE)
                .edit().apply { putInt(key, value); apply() }
    }
}