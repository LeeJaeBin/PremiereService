package com.project.premiereservice

import android.graphics.Color
import android.text.Layout
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.project.premiereservice.model.MovieModel
import com.project.premiereservice.model.RegisteredMovieModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_movie_favorite_item.view.*
import kotlinx.android.synthetic.main.layout_movie_favorite_item.view.image_movie_poster
import kotlinx.android.synthetic.main.layout_movie_register_item.view.*

class MovieRegisterItemHolder (view: View, listener: MovieRegisterAdapter.OnMovieListsClickListener?) : RecyclerView.ViewHolder(view) {
    private val posterView: ImageView = view.image_movie_poster
    private val titleText: TextView = view.text_register_title
    private val showDateText: TextView = view.text_register_date
    private val layoutRegisterBackground: ConstraintLayout = view.layout_register_background

    init {
        view.setOnClickListener {
            listener?.onItemClick(adapterPosition)
        }
    }

    fun bind(model: RegisteredMovieModel) {
        model.run {

            if(isSuccess) {
                layoutRegisterBackground.setBackgroundResource(R.drawable.goldticket)
            }

            Picasso.get().isLoggingEnabled = true
            Picasso.get().load(moviePoster).into(posterView)

            titleText.text = title
            //showDateText.text = "$showDate 상영"

            val builder = SpannableStringBuilder("$showDate 상영")
            builder.setSpan(ForegroundColorSpan(Color.parseColor("#FF6200EE")), 0, showDate.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            showDateText.append(builder)

            val imageRadius = getDrawable(posterView.context, R.drawable.image_radius)
            posterView.background = imageRadius;
            posterView.clipToOutline = true;
        }
    }
}