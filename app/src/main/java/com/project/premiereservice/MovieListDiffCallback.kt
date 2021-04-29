package com.project.premiereservice

import androidx.recyclerview.widget.DiffUtil
import com.project.premiereservice.model.MovieModel

class MovieListDiffCallback(private val oldList: List<MovieModel>, private val newList: List<MovieModel>) : DiffUtil.Callback() {
    override fun getNewListSize(): Int = newList.size
    override fun getOldListSize(): Int = oldList.size
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldList[oldItemPosition] == newList[newItemPosition]
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldList[oldItemPosition].id == newList[newItemPosition].id
}