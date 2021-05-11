package com.project.premiereservice

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.title_bar_nologin.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main)

        /*val pref = this.getPreferences(0)

        if(!pref.getString("id", "null").equals("null")) {
            window.setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar)
        }
        else {
            window.setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar_nologin)
        }*/

        if(getSharedPreference("premiere", "id") == "null") {
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
}