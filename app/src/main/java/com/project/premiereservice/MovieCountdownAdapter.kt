package com.project.premiereservice

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.project.premiereservice.model.MovieModel
import kotlinx.android.synthetic.main.layout_movie_countdown_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MovieCountdownAdapter(private var movieList: List<MovieModel>) : RecyclerView.Adapter<MovieCountdownItemHolder> (), Filterable {

    private var tempList: List<MovieModel>? = null

    interface OnMovieListsClickListener {
        fun onItemClick(position: Int)
    }

    var listener: OnMovieListsClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) : MovieCountdownItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_movie_countdown_item, parent, false)

        return MovieCountdownItemHolder(view, listener)
    }

    override fun onBindViewHolder(holder: MovieCountdownItemHolder, position: Int) {
            holder?.bind(movieList[position])
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val filteredList = ArrayList<MovieModel>()
                for (row in movieList) {
                    if (row.availableNum<100) {
                        filteredList.add(row)
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

    fun update(updated: List<MovieModel>) {
        CoroutineScope(Dispatchers.Main).launch {
            val diffResult = async(Dispatchers.IO) {
                getDiffResult(updated)
            }
            movieList = updated
            diffResult.await().dispatchUpdatesTo(this@MovieCountdownAdapter)
        }
    }

    private fun getDiffResult(updated: List<MovieModel>): DiffUtil.DiffResult {
        val diffCallback = MovieListDiffCallback(movieList, updated)
        return DiffUtil.calculateDiff(diffCallback)
    }

    fun getItem(position: Int) = movieList[position]

    override fun getItemCount(): Int = movieList.size
}