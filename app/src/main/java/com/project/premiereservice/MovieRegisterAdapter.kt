package com.project.premiereservice

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.project.premiereservice.model.MovieModel
import com.project.premiereservice.model.RegisteredMovieModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_movie_rank_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MovieRegisterAdapter(private var movieList: List<RegisteredMovieModel>) : RecyclerView.Adapter<MovieRegisterItemHolder> () {


    interface OnMovieListsClickListener {
        fun onItemClick(position: Int)
    }

    var listener: OnMovieListsClickListener? = null

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): MovieRegisterItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_movie_register_item, parent, false)

        return MovieRegisterItemHolder(view, listener)
    }

    override fun onBindViewHolder(holder: MovieRegisterItemHolder, position: Int) {
        holder.bind(movieList[position])
    }

    fun update(updated: List<RegisteredMovieModel>) {
        CoroutineScope(Dispatchers.Main).launch {
            val diffResult = async(Dispatchers.IO) {
                getDiffResult(updated)
            }
            movieList = updated
            diffResult.await().dispatchUpdatesTo(this@MovieRegisterAdapter)
        }
    }

    private fun getDiffResult(updated: List<RegisteredMovieModel>): DiffUtil.DiffResult {
        val diffCallback = RegisteredMovieListDiffCallback(movieList, updated)
        return DiffUtil.calculateDiff(diffCallback)
    }

    fun getItem(position: Int) = movieList[position]

    override fun getItemCount(): Int = movieList.size
}