package com.project.premiereservice

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment() {

    private val movieRegisterList = ArrayList<MovieList>()
    private var mRegisterAdapter: MovieRegisterAdapter ?= null

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

        bindMovieRanks()
        bindMovieRankAdapter()
    }

    private fun bindMovieRankAdapter(){
        val mLayoutManager = GridLayoutManager(mContext, 3)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL

        mRegisterAdapter = MovieRegisterAdapter(mContext, movieRegisterList)
        recycler_movie_registered.adapter = mRegisterAdapter
        recycler_movie_registered.layoutManager = mLayoutManager
    }

    private fun bindMovieRanks(){
        val temp = MovieList(1, "타이타닉", "http://img.movist.com/?img=/x00/00/10/15_p1.jpg", 1, 10000)
        val temp2 = MovieList(2, "어벤져스:엔드게임", "https://upload.wikimedia.org/wikipedia/ko/f/f2/%EC%96%B4%EB%B2%A4%EC%A0%B8%EC%8A%A4-_%EC%97%94%EB%93%9C%EA%B2%8C%EC%9E%84_%ED%8F%AC%EC%8A%A4%ED%84%B0.jpg", 2, 10000)
        val temp3 = MovieList(3, "자전차왕 엄복동", "https://upload.wikimedia.org/wikipedia/ko/thumb/4/42/%EC%9E%90%EC%A0%84%EC%B0%A8%EC%99%95_%EC%97%84%EB%B3%B5%EB%8F%99_%ED%8F%AC%EC%8A%A4%ED%84%B0.jpg/220px-%EC%9E%90%EC%A0%84%EC%B0%A8%EC%99%95_%EC%97%84%EB%B3%B5%EB%8F%99_%ED%8F%AC%EC%8A%A4%ED%84%B0.jpg", 3, 0)
        val temp4 = MovieList(4, "닥터스트레인지", "https://img1.daumcdn.net/thumb/R800x0/?scode=mtistory2&fname=https%3A%2F%2Ft1.daumcdn.net%2Fcfile%2Ftistory%2F2647A44257FF15DD34", 4, 0)
        val temp5 = MovieList(5, "어바웃타임", "http://www.artinsight.co.kr/data/tmp/1802/b37b789c931b794edc69054a6dba61fc_UhTzC8aAR.jpg", 5, 0)
        movieRegisterList.add(temp)
        movieRegisterList.add(temp2)
        movieRegisterList.add(temp3)
        movieRegisterList.add(temp4)
        movieRegisterList.add(temp5)
    }
}