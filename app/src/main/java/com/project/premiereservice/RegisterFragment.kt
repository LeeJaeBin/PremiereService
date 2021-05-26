package com.project.premiereservice

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.premiereservice.model.MovieModel
import com.project.premiereservice.model.RegisteredMovieModel
import com.project.premiereservice.network.NetworkRequest
import com.project.premiereservice.viewmodel.MainViewModel
import com.project.premiereservice.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_preview.*
import kotlinx.android.synthetic.main.dialog_register_movie.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var viewModelFactory: MainViewModelFactory
    private lateinit var mMovieRegisterAdapter: MovieRegisterAdapter

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
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = getIntSharedPreference("premiere", "id")
        if(userId == 0) {
            layout_register_beforelogin.visibility = View.VISIBLE
            layout_register_afterlogin.visibility = View.GONE
        }
        else{
            layout_register_beforelogin.visibility = View.GONE
            layout_register_afterlogin.visibility = View.VISIBLE

            initViewModel()
            viewModel.requestRegisteredMovieLists(userId)
        }
    }

    private fun initViewModel() {
        viewModelFactory = MainViewModelFactory(NetworkRequest())
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        viewModel.registeredMovieList.observe(this) {
            updateMovies(it)
        }
    }

    private fun updateMovies(movies: List<RegisteredMovieModel>) {
        if(::mMovieRegisterAdapter.isInitialized) {
            mMovieRegisterAdapter.update(movies)
        } else {

            mMovieRegisterAdapter = MovieRegisterAdapter(movies).apply {
                listener = object : MovieRegisterAdapter.OnMovieListsClickListener {
                    override fun onItemClick(position: Int) {
                        mMovieRegisterAdapter.getItem(position).run {
                            openMovieInfo(id)
                        }
                    }
                }
            }

            mMovieRegisterAdapter.filter.filter("")
            recycler_movie_registered.run {
                layoutManager = GridLayoutManager(mContext, 3)
                adapter = mMovieRegisterAdapter

            }

            recycler_movie_registered.isVerticalScrollBarEnabled = false
        }
    }

    private fun openMovieInfo(id: Long) {
        try {
            val intent = Intent(context, PreviewActivity::class.java)
            intent.putExtra("movieId", id)
            intent.putExtra("isRegistered", true);
            mContext.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getIntSharedPreference(prefsName: String, key: String): Int {
        context?.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
                ?.getInt(key, 0)?.let { return it }
        return 0
    }
}