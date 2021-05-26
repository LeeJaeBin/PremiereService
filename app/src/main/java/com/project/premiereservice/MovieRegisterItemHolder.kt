package com.project.premiereservice

import android.graphics.Color
import android.text.Layout
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.project.premiereservice.model.MovieModel
import com.project.premiereservice.model.RegisteredMovieModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_movie_register_item_test.view.*
import java.text.SimpleDateFormat
import java.util.*

class MovieRegisterItemHolder (view: View, listener: MovieRegisterAdapter.OnMovieListsClickListener?) : RecyclerView.ViewHolder(view) {
    private val posterView: ImageView = view.image_movie_poster
    private val layoutCover: View = view.layout_cover_register_test
    private val imageFailed: ImageView = view.image_failed_register_test
    private val imageTimer: ImageView = view.image_timer_register_test
    /*private val posterView: ImageView = view.image_movie_poster
    private val titleText: TextView = view.text_register_title
    private val showDateText: TextView = view.text_register_date
    private val imageFailed: ImageView = view.image_failed_register
    private val layoutRegisterBackground: ConstraintLayout = view.layout_register_background
    private val layoutCover: ConstraintLayout = view.layout_cover_register*/

    init {
        view.setOnClickListener {
            listener?.onItemClick(adapterPosition)
        }
    }

    fun bind(model: RegisteredMovieModel) {
        model.run {

            /*if(isSuccess) {
                layoutRegisterBackground.setBackgroundResource(R.drawable.goldticket)
            }
            else{
                var dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm")
                try {
                    var limdate = dateFormat.parse(limitDate)
                    if (Date().after(limdate)){
                        layoutCover.alpha = 0.2F
                        imageFailed.visibility = View.VISIBLE
                    }
                } catch (e: Exception){

                }
            }*/

            var dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm")
            try {
                var limdate = dateFormat.parse(limitDate)
                var showdate = dateFormat.parse(showDate)
                if (Date().after(limdate)){
                    if(isSuccess && Date().after(showdate)) {
                        layoutCover.visibility = View.VISIBLE
                        imageFailed.setImageResource(R.drawable.closed)
                        imageFailed.visibility = View.VISIBLE
                    }
                    else if(!isSuccess) {
                        layoutCover.visibility = View.VISIBLE
                        imageFailed.visibility = View.VISIBLE
                    }
                }
                else if(!isSuccess){
                    layoutCover.visibility = View.VISIBLE
                    imageTimer.visibility = View.VISIBLE
                }
            } catch (e: Exception){

            }

            Picasso.get().isLoggingEnabled = true
            Picasso.get().load(moviePoster).into(posterView)

            //titleText.text = title

            /*val builder = SpannableStringBuilder("$showDate 상영")
            builder.setSpan(ForegroundColorSpan(Color.parseColor("#FF6200EE")), 0, showDate.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            showDateText.append(builder)*/

            val imageRadius = getDrawable(posterView.context, R.drawable.image_radius)
            posterView.background = imageRadius;
            posterView.clipToOutline = true;
        }
    }
}