package com.project.premiereservice

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.title_bar_nologin.*

class MainActivity : AppCompatActivity() {

    private var backKeyPressedTime: Long = 0

    private val requiredPermissions = arrayOf(
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.INTERNET)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main)

        checkPermissions()

        if(getIntSharedPreference("premiere", "id") == 0) {
            window.setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar_nologin)

            button_title_login.setOnClickListener {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }

        }
        else {
            window.setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar)
        }

        replaceFragment(HomeFragment())

        navigation_menu.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.action_home -> {
                    replaceFragment(HomeFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_movie -> {
                    replaceFragment(RegisterFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_rating -> {
                    replaceFragment(RatingFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_profile -> {
                    replaceFragment(MyPageFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
        }
    }

    private fun checkPermissions() {
        var rejectedPermissionList = ArrayList<String>()

        for(permission in requiredPermissions){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                rejectedPermissionList.add(permission)
            }
        }
        if(rejectedPermissionList.isNotEmpty()){
            val array = arrayOfNulls<String>(rejectedPermissionList.size)
            ActivityCompat.requestPermissions(this, rejectedPermissionList.toArray(array), 100)
        }
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis()
            Toast.makeText(this, "뒤로 버튼을 한번 더 누르시면 종료됩니다", Toast.LENGTH_SHORT).show()

        }
        else if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.navigation_frame_layout, fragment)
        fragmentTransaction.commit()
    }

    private fun getSharedPreference(prefsName: String, key: String): String {
        getSharedPreferences(prefsName, Context.MODE_PRIVATE)
                ?.getString(key, "null")?.let { return it }
        return "null"
    }

    private fun getIntSharedPreference(prefsName: String, key: String): Int {
        getSharedPreferences(prefsName, Context.MODE_PRIVATE)
                ?.getInt(key, 0)?.let { return it }
        return 0
    }
}