package com.project.premiereservice

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_mypage.*
import kotlinx.android.synthetic.main.fragment_register.*

class MyPageFragment : Fragment() {

    private lateinit var mContext: Context
    private val tempProfileImage = "https://preview.keenthemes.com/metronic-v4/theme/assets/pages/media/profile/people19.png"

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
        return inflater.inflate(R.layout.fragment_mypage, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setProfileImage()
        setText()
    }

    private fun setProfileImage() {
        Picasso.get().isLoggingEnabled = true
        Picasso.get().load(tempProfileImage).into(image_profile_mypage)
    }

    private fun setText() {
        val nicknameText = "이재빈"
        val builder = SpannableStringBuilder("$nicknameText 님 안녕하세요!")
        builder.setSpan(ForegroundColorSpan(resources.getColor(R.color.purple_500)), 0, nicknameText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        text_nickname_mypage.append(builder)
   }
}