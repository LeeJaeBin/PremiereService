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
import kotlinx.android.synthetic.main.layout_movie_favorite_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MovieFavoriteAdapter(private var movieList: List<MovieModel>) : RecyclerView.Adapter<MovieFavoriteItemHolder> (), Filterable {

    private var tempList: List<MovieModel>? = null

    //private val pref =

    interface OnMovieListsClickListener {
        fun onItemClick(position: Int)
    }

    init {
        tempList = movieList
    }

    var listener: OnMovieListsClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieFavoriteItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_movie_favorite_item, parent, false)

        return MovieFavoriteItemHolder(view, listener)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {

                val myType = charSequence.toString()

                val filteredList = ArrayList<MovieModel>()
                for (row in movieList) {
                    val movieTypeList = row.movieCategory.split("/")
                    for(category in movieTypeList) {
                        if(category == myType) {
                            filteredList.add(row)
                            break
                        }
                    }
                }
                tempList = filteredList
                val filterResults = FilterResults()
                filterResults.values = tempList
                return filterResults
            }
            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                movieList = filterResults.values as List<MovieModel>
                notifyDataSetChanged()
            }
        }
    }

    override fun onBindViewHolder(holder: MovieFavoriteItemHolder, position: Int) {
        holder.bind(movieList[position])
    }

    fun update(updated: List<MovieModel>) {
        CoroutineScope(Dispatchers.Main).launch {
            val diffResult = async(Dispatchers.IO) {
                getDiffResult(updated)
            }
            movieList = updated
            diffResult.await().dispatchUpdatesTo(this@MovieFavoriteAdapter)
        }
    }

    private fun getDiffResult(updated: List<MovieModel>): DiffUtil.DiffResult {
        val diffCallback = MovieListDiffCallback(movieList, updated)
        return DiffUtil.calculateDiff(diffCallback)
    }

    fun getItem(position: Int) = movieList[position]

    override fun getItemCount(): Int = movieList.size
}