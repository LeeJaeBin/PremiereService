package com.project.premiereservice

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.project.premiereservice.model.MovieModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_movie_countdown_item.view.*

class MovieCountdownItemHolder (view: View, listener: MovieCountdownAdapter.OnMovieListsClickListener?) : RecyclerView.ViewHolder(view) {
    private val posterView: ImageView = view.image_movie_poster
    private val leftView: TextView = view.text_movie_time

    init {
        view.setOnClickListener {
            listener?.onItemClick(adapterPosition)
        }
    }

    fun bind(model: MovieModel) {
        model.run {

            Picasso.get().isLoggingEnabled = true
            Picasso.get().load(moviePoster).into(posterView);

            leftView.text = availableNum.toString() + "명 남음!"

            val imageRadius = getDrawable(posterView.context, R.drawable.image_radius)
            posterView.background = imageRadius;
            posterView.clipToOutline = true;
        }
    }
}