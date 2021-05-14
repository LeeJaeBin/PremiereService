package com.project.premiereservice

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.title_bar_nologin.*

class MainActivity : AppCompatActivity() {

    private var backKeyPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main)

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