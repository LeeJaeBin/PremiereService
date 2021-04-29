package com.project.premiereservice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_preview.*
import kotlinx.android.synthetic.main.title_bar_register.*

class PreviewCancelActivity : AppCompatActivity() {

    private var id: Int = 0
    private var isLiked = false
    private val temp = MovieListForRegister(1, "타이타닉", "http://img.movist.com/?img=/x00/00/10/15_p1.jpg", "2021.04.07 18:00", "2021.04.08",
        "우연한 기회로 티켓을 구해 타이타닉호에 올라탄 자유로운 영혼을 가진 화가 잭(레오나르도 디카프리오)은 막강한 재력의 약혼자와 함께 1등실에 승선한 로즈(케이트 윈슬렛)에게 한 눈에 반한다. 진실한 사랑을 꿈꾸던 로즈 또한 생애 처음 황홀한 감정에 휩싸이고, 둘은 운명 같은 사랑에 빠지는데…", 1000, 5)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_preview_cancel)

        if(intent.hasExtra("id")) {
            id = intent.getIntExtra("id", 0)
        }

        window.setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar_register)

        setLayout()

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
    }

    private fun setLayout(){
        Picasso.get().isLoggingEnabled = true
        Picasso.get().load(temp.moviePoster).into(image_poster_preview);

        val imageRadius = getDrawable(R.drawable.image_radius)
        image_poster_preview.background = imageRadius;
        image_poster_preview.clipToOutline = true;

        text_movie_title_preview.text = temp.movieTitle
        text_date_premiere_preview.text = temp.premiereDate
        text_date_theater_preview.text = temp.theaterDate
        text_story_preview.text = temp.movieStory
        text_like_preview.text = temp.likes.toString()
        text_limit_preview.text = temp.limitApplicant.toString() + "명"
    }
}