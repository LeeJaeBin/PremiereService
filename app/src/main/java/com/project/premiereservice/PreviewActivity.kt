package com.project.premiereservice

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.premiereservice.model.MovieInfoModel
import com.project.premiereservice.network.NetworkRequest
import com.project.premiereservice.viewmodel.MainViewModel
import com.project.premiereservice.viewmodel.MainViewModelFactory
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_preview.*
import kotlinx.android.synthetic.main.dialog_register_movie.*
import kotlinx.android.synthetic.main.title_bar_register.*
import java.text.SimpleDateFormat
import java.util.*

class PreviewActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var viewModelFactory: MainViewModelFactory

    private var movieId: Long = 0
    private var isRegistered: Boolean = false
    private var isLiked = false
    private var isShowTime = false

    private var movieUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_preview)

        movieId = intent.getLongExtra("movieId", 0)
        if(intent.hasExtra("isRegistered")) {
            isRegistered = true
        }

        initViewModel()
        viewModel.requestMovieInfo(movieId)
        viewModel.isMovieRegistered(movieId, getIntSharedPreference("premiere", "id").toLong())

        window.setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar_register)

        button_like.setOnClickListener {
            if(isLiked){
                button_like.setImageResource(R.drawable.no_like)
                isLiked = false
            }
            else{
                isLiked = true
                button_like.setImageResource(R.drawable.like)
            }
        }

        button_register_preview.setOnClickListener {
            dialogSetting()
        }

        button_unregister_preview.setOnClickListener {
            dialogSetting()
        }

        button_play_preview.setOnClickListener {
            if(!isShowTime) {
                Toast.makeText(this, "?????? 30??? ????????? ????????? ???????????????", Toast.LENGTH_SHORT).show()
            }
            else{
                val intent = Intent(this, PlayerActivity::class.java)
                intent.putExtra("movieUrl", movieUrl)
                startActivity(intent)
            }
        }
    }

    private fun dialogSetting() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.dialog_register_movie, null)
        val alertDialog = AlertDialog.Builder(this).create()

        val txtDialogInfo = view.findViewById<TextView>(R.id.info_dialog_register)
        val btnRegister = view.findViewById<Button>(R.id.button_register_dialog_yes)
        val btnNoRegister = view.findViewById<Button>(R.id.button_register_dialog_no)
        if(!isRegistered) {
            btnRegister.setOnClickListener {
                viewModel.requestRegisterMovie(movieId, getIntSharedPreference("premiere", "id").toLong(), true)
                alertDialog.dismiss()
            }
            btnNoRegister.setOnClickListener {
                alertDialog.dismiss()
            }
        }
        else{
            txtDialogInfo.text = "????????? ????????? ?????????????????????????"
            btnRegister.setOnClickListener {
                viewModel.requestRegisterMovie(movieId, getIntSharedPreference("premiere", "id").toLong(), false)
                alertDialog.dismiss()
            }
            btnNoRegister.setOnClickListener {
                alertDialog.dismiss()
            }
        }

        alertDialog.setView(view)
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
    }

    private fun initViewModel() {
        viewModelFactory = MainViewModelFactory(NetworkRequest())
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        viewModel.movieInfo.observe(this) {
            setMovieInfo(it)
        }
        viewModel.registerMovieSuccess.observe(this) {
            if(it) {
                if(!isRegistered) {
                    Toast.makeText(this, "????????? ????????? ?????????????????????.", Toast.LENGTH_SHORT).show()
                    isRegistered = true
                }
                else{
                    Toast.makeText(this, "????????? ??????????????? ?????????????????????.", Toast.LENGTH_SHORT).show()
                    isRegistered = false
                }
                setLayout(isRegistered)
            }
            else{
                Toast.makeText(this, "????????? ????????? ?????????????????????.", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.checkMovieRegistered.observe(this) {
            setLayout(it)
        }
    }

    private fun setMovieInfo(movieInfo: MovieInfoModel) {
        Picasso.get().isLoggingEnabled = true
        Picasso.get().load(movieInfo.poster).into(image_poster_preview);

        val imageRadius = getDrawable(R.drawable.image_radius)
        image_poster_preview.background = imageRadius;
        image_poster_preview.clipToOutline = true;

        text_category_preview.text = movieInfo.categories
        text_director_preview.text = "?????? : ${movieInfo.director}"
        text_actor_preview.text = "????????? : ${movieInfo.actor}"
        text_movie_title_preview.text = movieInfo.movieTitle
        text_date_premiere_preview.text = movieInfo.showDate
        text_date_theater_preview.text = movieInfo.limitDate
        text_story_preview.text = movieInfo.descriptionTxt
        text_like_preview.text = movieInfo.likes.toString()
        text_limit_preview.text = movieInfo.available.toString() + "???"

        val dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm")
        val curTime = dateFormat.parse(dateFormat.format(Date()))
        val showTime = dateFormat.parse(movieInfo.showDate)

        val remainingTime = (showTime.time - curTime.time) / 1000
        if(remainingTime > 1800 || remainingTime <0) {
            button_play_preview.setBackgroundColor(Color.parseColor("#888888"))
        }
        else{
            movieUrl = movieInfo.movieUrl
            isShowTime = true
        }
    }

    private fun setLayout(isMovieRegistered: Boolean){
        isRegistered = isMovieRegistered
        if(isMovieRegistered) {
            button_register_preview.visibility = View.GONE
            layout_button_preview.visibility = View.VISIBLE
        }
        else{
            button_register_preview.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_500))
            button_register_preview.text = "????????? ????????????"
        }
        button_register_preview.invalidate()
    }

    private fun getIntSharedPreference(prefsName: String, key: String): Int {
        this.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
                .getInt(key, 0).let { return it }
    }
}