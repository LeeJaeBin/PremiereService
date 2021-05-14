package com.project.premiereservice

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.TypefaceSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_mypage.*
import java.util.*


class MyPageFragment : Fragment() {

    private lateinit var mContext: Context
    private val tempProfileImage = "https://d1vv4p3ixxyri0.cloudfront.net/companies_logo/56eab470db5e3e7903ced685c936ab14f824efaf8ba9f532e951b9450d114a9b_1495106556374337.png"

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

        if(getIntSharedPreference("premiere", "id") == 0) {
            button_logout_mypage.visibility = View.GONE
            layout_beforelogin_mypage.visibility = View.VISIBLE
            layout_afterlogin_mypage.visibility = View.GONE
        }
        else {
            button_logout_mypage.visibility = View.VISIBLE
            layout_beforelogin_mypage.visibility = View.GONE
            layout_afterlogin_mypage.visibility = View.VISIBLE
        }

        button_logout_mypage.setOnClickListener {
            logoutProcess()

            val intent = Intent(activity, MainActivity::class.java)
            activity?.startActivity(intent)
            activity?.finish()
        }
    }

    private fun logoutProcess() {
        setIntSharedPreference("premiere", "id", 0)
        setSharedPreference("premiere", "userid", "null")
        setSharedPreference("premiere", "nickname", "null")
        setSharedPreference("premiere", "phonenum", "null")
        setSharedPreference("premiere", "imei", "null")
        setSharedPreference("premiere", "category", "null")
        setSharedPreference("premiere", "image", "null")
    }

    private fun setProfileImage() {
        Picasso.get().isLoggingEnabled = true
        Picasso.get().load(getSharedPreference("premiere", "image")).into(image_profile_mypage)
    }

    private fun setText() {
        val nicknameText = getSharedPreference("premiere", "nickname")
        val random = Random()
        val num = random.nextInt(4)
        val welcomeMessageArr = arrayOf("안녕하세요!", "환영합니다!", "좋은하루 보내고 계신가요?", "만나서 반가워요!")
        val welcomeMessage = welcomeMessageArr[num]

        //val boldFont = Typeface.createFromAsset(context?.assets, "bold.ttf")
        //val typefaceSpan = TypefaceSpan("bold")

        val builder = SpannableStringBuilder("$nicknameText 님 $welcomeMessage")
        builder.setSpan(ForegroundColorSpan(resources.getColor(R.color.purple_500)), 0, nicknameText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder.setSpan(StyleSpan(Typeface.BOLD), 0, nicknameText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        text_nickname_mypage.append(builder)
   }

    private fun setSharedPreference(prefsName: String, key: String, value: String) {
        context?.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
                ?.edit()?.apply { putString(key, value); apply() }
    }

    private fun setIntSharedPreference(prefsName: String, key: String, value: Int) {
        context?.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
                ?.edit()?.apply { putInt(key, value); apply() }
    }

    private fun getSharedPreference(prefsName: String, key: String): String {
        context?.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
                ?.getString(key, "null")?.let { return it }
        return "null"
    }

    private fun getIntSharedPreference(prefsName: String, key: String): Int {
        context?.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
                ?.getInt(key, 0)?.let { return it }
        return 0
    }
}