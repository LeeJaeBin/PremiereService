package com.project.premiereservice

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.premiereservice.model.MovieModel
import com.project.premiereservice.network.NetworkRequest
import com.project.premiereservice.viewmodel.MainViewModel
import com.project.premiereservice.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_mypage.*
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var viewModelFactory: MainViewModelFactory
    private lateinit var mMovieRankAdapter: MovieRankAdapter
    private lateinit var mMovieCountdownAdapter: MovieCountdownAdapter
    private lateinit var mMovieFavoriteAdapter: MovieFavoriteAdapter
    private lateinit var mMovieAllAdapter: MovieAllAdapter

    private var isLogined = false

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

        val nicknameText = getSharedPreference("premiere", "nickname")

        //val boldFont = Typeface.createFromAsset(context?.assets, "bold.ttf")
        //val typefaceSpan = TypefaceSpan("bold")

        val builder = SpannableStringBuilder("$nicknameText 님 취향저격 영화들")
        builder.setSpan(ForegroundColorSpan(resources.getColor(R.color.purple_500)), 0, nicknameText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        title_favorite_home.append(builder)
    }

    private fun initViewModel() {
        viewModelFactory = MainViewModelFactory(NetworkRequest())
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

            val myType = getSharedPreference("premiere", "category")
            if(myType == "null") {
                layout_movie_favorite.visibility = View.GONE
                isLogined = false
            }
            else{
                layout_movie_favorite.visibility = View.VISIBLE
                isLogined = true
            }

            mMovieRankAdapter.filter.filter("")
            mMovieCountdownAdapter.filter.filter("")

            if(isLogined) {
                mMovieFavoriteAdapter.filter.filter(myType)
                //mMovieFavoriteAdapter.filter.filter("로맨스")
            }

            recycler_movie_rank.run {
                layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
                adapter = mMovieRankAdapter
            }

            recycler_movie_countdown.run {
                layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
                adapter = mMovieCountdownAdapter
            }

            if(isLogined) {
                recycler_movie_favorite.run {
                    layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
                    adapter = mMovieFavoriteAdapter
                }
            }

            recycler_movie_all.run {
                layoutManager = GridLayoutManager(mContext, 3)
                adapter = mMovieAllAdapter
            }

            //recycler_movie_all.isNestedScrollingEnabled = false
        }
    }

    private fun openMovieInfo(id: Long) {
        try {
            val intent = Intent(context, PreviewActivity::class.java)
            intent.putExtra("movieId", id)
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setSharedPreference(prefsName: String, key: String, value: String) {
        context?.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
                ?.edit()?.apply { putString(key, value); apply() }
    }

    private fun getSharedPreference(prefsName: String, key: String): String {
        context?.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
                ?.getString(key, "null")?.let { return it }
        return "null"
    }
}