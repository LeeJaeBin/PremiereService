package com.project.premiereservice

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_movie_rank_item.view.*

class MovieRegisterAdapter(private val context: Context, private var movieList: ArrayList<MovieList>) : RecyclerView.Adapter<MovieRegisterAdapter.MovieRegisterViewHolder> () {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = run {
        MovieRegisterViewHolder(parent)
    }

    override fun onBindViewHolder(holder: MovieRegisterViewHolder, position: Int) {

        holder?.bind(movieList[position])

        holder?.movieItemLayout.setOnClickListener {
            val intent = Intent(context, PreviewCancelActivity::class.java)
            intent.putExtra("id", movieList[position].id)
            context.startActivity(intent)
        }
    }

    inner class MovieRegisterViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.layout_movie_register_item, parent, false)) {

        val movieItemLayout: View = itemView.layout_movie
        val mMoviePoster: ImageView = itemView.image_movie_poster

        fun bind(movie: MovieList) {
            //mMovieTitle.text = movie.movieTitle
            Picasso.get().isLoggingEnabled = true
            Picasso.get().load(movie.moviePoster).into(mMoviePoster);

            val imageRadius = context.getDrawable(R.drawable.image_radius)
            mMoviePoster.background = imageRadius;
            mMoviePoster.clipToOutline = true;
        }
    }

    override fun getItemCount(): Int = movieList.size
}