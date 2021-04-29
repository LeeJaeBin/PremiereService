package com.project.premiereservice

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.project.premiereservice.model.MovieModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_movie_all_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MovieAllAdapter(private var movieList: List<MovieModel>) : RecyclerView.Adapter<MovieAllItemHolder> () {

    interface OnMovieListsClickListener {
        fun onItemClick(position: Int)
    }

    var listener: OnMovieListsClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieAllItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_movie_all_item, parent, false)

        return MovieAllItemHolder(view, listener)
    }

    override fun onBindViewHolder(holder: MovieAllItemHolder, position: Int) {
        holder.bind(movieList[position])
    }

    fun update(updated: List<MovieModel>) {
        CoroutineScope(Dispatchers.Main).launch {
            val diffResult = async(Dispatchers.IO) {
                getDiffResult(updated)
            }
            movieList = updated
            diffResult.await().dispatchUpdatesTo(this@MovieAllAdapter)
        }
    }

    private fun getDiffResult(updated: List<MovieModel>): DiffUtil.DiffResult {
        val diffCallback = MovieListDiffCallback(movieList, updated)
        return DiffUtil.calculateDiff(diffCallback)
    }

    fun getItem(position: Int) = movieList[position]

    override fun getItemCount(): Int = movieList.size
}