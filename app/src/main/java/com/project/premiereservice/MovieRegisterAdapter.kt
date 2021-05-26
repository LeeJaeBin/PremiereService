package com.project.premiereservice

import android.R.attr.data
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.project.premiereservice.model.RegisteredMovieModel
import kotlinx.android.synthetic.main.layout_movie_rank_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MovieRegisterAdapter(private var movieList: List<RegisteredMovieModel>) : RecyclerView.Adapter<MovieRegisterItemHolder> (), Filterable {

    private var tempList: List<RegisteredMovieModel>? = null

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
    ): MovieRegisterItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.layout_movie_register_item_test,
            parent,
            false
        )

        return MovieRegisterItemHolder(view, listener)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val filteredList = ArrayList<RegisteredMovieModel>()
                for (row in movieList) {
                    filteredList.add(row)
                }
                tempList = filteredList
                val filterResults = FilterResults()
                filterResults.values = tempList
                return filterResults
            }
            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                movieList = filterResults.values as List<RegisteredMovieModel>

                /*Collections.sort(
                    movieList
                ) { lhs, rhs ->
                    if ((!lhs.isSuccess && rhs.isSuccess) || !lhs.isSuccess && !rhs.isSuccess && (Date().after(SimpleDateFormat("yyyy.MM.dd HH:mm").parse(lhs.limitDate)))) 1
                    else if (!lhs.isSuccess && (Date().after(SimpleDateFormat("yyyy.MM.dd HH:mm").parse(lhs.limitDate)))) -1
                    else 0
                }*/

                notifyDataSetChanged()
            }
        }
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