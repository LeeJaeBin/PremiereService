package com.project.premiereservice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.premiereservice.model.MovieModel
import com.project.premiereservice.network.MovieLists
import com.project.premiereservice.viewmodel.MainViewModel
import com.project.premiereservice.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_movie_countdown_item.*

class HomeFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var viewModelFactory: MainViewModelFactory
    private lateinit var mMovieRankAdapter: MovieRankAdapter
    private lateinit var mMovieCountdownAdapter: MovieCountdownAdapter
    private lateinit var mMovieFavoriteAdapter: MovieFavoriteAdapter
    private lateinit var mMovieAllAdapter: MovieAllAdapter

    private lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        viewModel.requestMovieLists("request")
    }

    private fun initViewModel() {
        viewModelFactory = MainViewModelFactory(MovieLists())
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        viewModel.movieList.observe(this) {
            updateMovies(it)
        }
    }

    private fun updateMovies(movies: List<MovieModel>) {
        if(::mMovieRankAdapter.isInitialized
                && ::mMovieCountdownAdapter.isInitialized
                && ::mMovieFavoriteAdapter.isInitialized
                && ::mMovieAllAdapter.isInitialized) {
            mMovieRankAdapter.update(movies)
            mMovieCountdownAdapter.update(movies)
            mMovieFavoriteAdapter.update(movies)
            mMovieAllAdapter.update(movies)
        } else {
            mMovieRankAdapter = MovieRankAdapter(movies).apply {
                listener = object : MovieRankAdapter.OnMovieListsClickListener {
                    override fun onItemClick(position: Int) {
                        mMovieRankAdapter.getItem(position).run {
                            openMovieInfo(id)
                        }
                    }
                }
            }

            mMovieCountdownAdapter = MovieCountdownAdapter(movies).apply {
                listener = object : MovieCountdownAdapter.OnMovieListsClickListener {
                    override fun onItemClick(position: Int) {
                        mMovieCountdownAdapter.getItem(position).run {
                            openMovieInfo(id)
                        }
                    }
                }
            }

            mMovieFavoriteAdapter = MovieFavoriteAdapter(movies).apply {
                listener = object : MovieFavoriteAdapter.OnMovieListsClickListener {
                    override fun onItemClick(position: Int) {
                        mMovieFavoriteAdapter.getItem(position).run {
                            openMovieInfo(id)
                        }
                    }
                }
            }

            mMovieAllAdapter = MovieAllAdapter(movies).apply {
                listener = object : MovieAllAdapter.OnMovieListsClickListener {
                    override fun onItemClick(position: Int) {
                        mMovieAllAdapter.getItem(position).run {
                            openMovieInfo(id)
                        }
                    }
                }
            }

            mMovieRankAdapter.filter.filter("")
            mMovieCountdownAdapter.filter.filter("")
            mMovieFavoriteAdapter.filter.filter("로맨스")

            recycler_movie_rank.run {
                layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
                adapter = mMovieRankAdapter
            }

            recycler_movie_countdown.run {
                layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
                adapter = mMovieCountdownAdapter
            }

            recycler_movie_favorite.run {
                layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
                adapter = mMovieFavoriteAdapter
            }

            recycler_movie_all.run {
                layoutManager = GridLayoutManager(mContext, 3)
                adapter = mMovieAllAdapter
            }

            recycler_movie_all.isVerticalScrollBarEnabled = false
        }
    }

    private fun openMovieInfo(id: Long) {
        try {
            val intent = Intent(context, PreviewActivity::class.java)
            intent.putExtra("id", id)
            mContext.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}