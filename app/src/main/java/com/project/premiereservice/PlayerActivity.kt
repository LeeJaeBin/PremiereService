package com.project.premiereservice

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Point
import android.media.AudioManager
import android.media.MediaDrm
import android.net.Uri
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.StrictMode
import android.telephony.TelephonyManager
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.TrackSelection
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.project.premiereservice.common.DRMINFO
import com.teruten.tms4encrypt.TMS4Encrypt
import com.teruten.tms4webserver.TMS4WebServer
import kotlinx.android.synthetic.main.activity_player.*
import java.util.*
import kotlin.math.abs
import kotlin.math.floor


class PlayerActivity : AppCompatActivity() {

    private lateinit var player: SimpleExoPlayer
    private var screenWidth: Int = 0
    private var screenHeight: Int = 0
    private var isControlHidden: Boolean = true
    private var _webServer: TMS4WebServer ?= null
    private val _info: DRMINFO = DRMINFO.instance

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;

        val intent = intent
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        var strMediaPath = intent.getStringExtra("movieUrl")
        //strMediaPath = "http://122.199.199.20/servertest/test/sample.mp4.MS4"

        // 1. Create a default TrackSelector

        // 1. Create a default TrackSelector
        val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter()
        val videoTrackSelectionFactory: TrackSelection.Factory = AdaptiveTrackSelection.Factory(
            bandwidthMeter
        )


        player = SimpleExoPlayer.Builder(this).build()
        player.playWhenReady = true

        // 초기화
        if (DRM_Init() != TMS4Encrypt.TMS4E_OK) return
        var nReturn = _webServer?.OpenRemoteMedia(strMediaPath, "wmc001")
        if (nReturn != TMS4Encrypt.TMS4E_OK) {
            return
        }

        // DRM 용 파일 경로 가져오기.
        strMediaPath = _webServer?.getFilePath(strMediaPath)


        // Produces DataSource instances through which media data is loaded.
        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
            this,
            Util.getUserAgent(this, "TPlayerTestApp_TEA_AS"), null
        )
        val mp4VideoUri = Uri.parse(strMediaPath)

        val videoUriList = arrayOfNulls<Uri>(3)

        videoUriList[0] = Uri.parse("http://122.199.199.20/servertest/test/ad1.mp4")
        videoUriList[1] = Uri.parse("http://122.199.199.20/servertest/test/ad2.mp4")
        videoUriList[2] = mp4VideoUri

        val videoSource = arrayOfNulls<MediaSource>(3)
        for(i in 0..2) {
            videoSource[i] = ProgressiveMediaSource.Factory(dataSourceFactory).setTag(i).createMediaSource(
                videoUriList[i]
            )
        }

        val mediaSource: MediaSource = ConcatenatingMediaSource(*videoSource)

        /*
        val videoSource: MediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
            .createMediaSource(mp4VideoUri)*/

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        var isLeftGesture = true
        var x = 0F
        var y = 0F

        var brightness = 0F

        val audioManager: AudioManager = this.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        getScreenSize()
        makeFullScreen()

        val uri = Uri.parse("url.toString()")

        val lp = window.attributes

        playerView.useController = false
        playerView.player = player
        player.prepare(mediaSource)

        playerView.setOnTouchListener { _, motionEvent ->

            when (motionEvent.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    isControlHidden = if (isControlHidden) {
                        playerView.showController()
                        false
                    } else {
                        playerView.hideController()
                        true
                    }
                    x = motionEvent.x
                    y = motionEvent.y
                    isLeftGesture = x < (screenWidth / 2)
                    true
                }

                MotionEvent.ACTION_UP -> {
                    gestureImage.visibility = View.GONE
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    val x2: Double = motionEvent.x.toDouble()
                    val y2: Double = motionEvent.y.toDouble()

                    val diffX = (floor(x2 - x)).toInt()
                    var diffY = (floor(y2 - y)).toInt()

                    if (abs(diffY) > abs(diffX) && abs(diffY) >= 70) {
                        if (isLeftGesture) {
                            brightness = if (y < y2) {
                                (brightness - 0.1F)
                            } else {
                                (brightness + 0.1F)
                            }
                            if (brightness < 0) {
                                brightness = 0F
                            } else if (brightness > 1.0) {
                                brightness = 1F
                            }
                            lp.screenBrightness = brightness
                            window.attributes = lp
                            gestureImage.setBackgroundResource(R.drawable.brightness)
                            gestureImage.visibility = View.VISIBLE
                        } else {
                            if (y < y2) {
                                audioManager.adjustVolume(
                                    AudioManager.ADJUST_LOWER,
                                    AudioManager.FLAG_PLAY_SOUND
                                )
                            } else {
                                audioManager.adjustVolume(
                                    AudioManager.ADJUST_RAISE,
                                    AudioManager.FLAG_PLAY_SOUND
                                )
                            }
                            val curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
                            if (curVolume >= 8) {
                                gestureImage.setBackgroundResource(R.drawable.full)
                            } else if (curVolume < 8) {
                                gestureImage.setBackgroundResource(R.drawable.low)
                            }
                            if (curVolume == 0) {
                                gestureImage.setBackgroundResource(R.drawable.mute)
                            }
                            gestureImage.visibility = View.VISIBLE
                        }
                        y = y2.toFloat()
                    }
                    true
                }
                else -> super.onTouchEvent(motionEvent)
            }
        }
    }

    private fun DRM_Init(): Int {
        var nReturn = -1
        if (_webServer == null) {
            _webServer = TMS4WebServer()
            //TMS4WebServer.setTerutenCertificate();
            setDeviceData()
            val info: DRMINFO = DRMINFO.instance
            // _webServer.InitInstance(this, STREAMING_KEY)
            nReturn = _webServer?.InitInstance(info.IMEI, info.MACADDRESS, this)!!
            if (nReturn != TMS4Encrypt.TMS4E_OK) {
                return nReturn
            }
            _webServer?.setBufferSize(1024)
            _webServer?.StartServer(50000)
        }
        return nReturn
    }

    @SuppressLint("HardwareIds")
    private fun setDeviceData() {
        val tpman = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        @SuppressLint("WifiManagerLeak") val wifiManager =
            getSystemService(WIFI_SERVICE) as WifiManager
        var wifiinfo: WifiInfo? = null
        if (wifiManager != null) {
            wifiinfo = wifiManager.connectionInfo
        }

        if (tpman == null && wifiinfo == null) return
        _info.HINT = TMS4Encrypt.TMS4_HINT_PREPACK
        /*if (tpman != null) {
            if (tpman.deviceId != null && tpman.deviceId.length > 0) {
                _info.HINT = _info.HINT or TMS4Encrypt.TMS4_HINT_IMEI
                _info.IMEI = tpman.deviceId
            }
        }*/
        val deviceUuid = UUID(-0x121074568629b532L, -0x5c37d8232ae2de13L)
        var deviceId = ""
        try {
            val mediaDrm = MediaDrm(deviceUuid)
            deviceId = android.util.Base64.encodeToString(
                mediaDrm.getPropertyByteArray(MediaDrm.PROPERTY_DEVICE_UNIQUE_ID),
                0
            ).trim()
            _info.IMEI = deviceId
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (wifiinfo != null) {
            if (wifiinfo.macAddress != null && wifiinfo.macAddress.length > 0) {
                _info.HINT = _info.HINT or TMS4Encrypt.TMS4_HINT_MAC_ADDRESS
                _info.MACADDRESS = wifiinfo.macAddress
            }
        }
    }



    private fun makeFullScreen() {
        val decorView = window.decorView
        var uiOption = window.decorView.systemUiVisibility
        uiOption =
                uiOption or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        uiOption =
                uiOption or View.SYSTEM_UI_FLAG_FULLSCREEN
        uiOption =
                uiOption or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        decorView.systemUiVisibility = uiOption
    }

    private fun getScreenSize() {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        screenWidth = size.x
        screenHeight = size.y
    }

    override fun onPause() {
        super.onPause()
        player.playWhenReady = false
    }

    override fun onDestroy() {
        super.onDestroy()
        player.stop()
        player.release()
    }
}