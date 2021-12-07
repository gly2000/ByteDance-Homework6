package com.domker.study.androidstudy

import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.media.session.MediaController
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import kotlinx.android.synthetic.main.layout_media_player.*
import java.io.IOException

class MediaPlayerActivity : AppCompatActivity() {
    private val player: MediaPlayer by lazy {
        MediaPlayer()
    }
    private var holder: SurfaceHolder? = null
   
    @RequiresApi(api = Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "MediaPlayer"
        setContentView(R.layout.layout_media_player)
        try {
            player.setDataSource(resources.openRawResourceFd(R.raw.big_buck_bunny))
            holder = surfaceView.holder
            holder?.setFormat(PixelFormat.TRANSPARENT)
            holder?.addCallback(PlayerCallBack())
            player.prepare()
            player.setOnPreparedListener { // 自动播放
                player.start()
                player.isLooping = true
            }
            player.setOnBufferingUpdateListener { mp, percent -> println(percent) }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        buttonPlay.setOnClickListener { player.start() }
        buttonPause.setOnClickListener { player.pause() }
    }

    override fun onPause() {
        super.onPause()
        player.stop()
        player.release()
    }

    private inner class PlayerCallBack : SurfaceHolder.Callback {
        override fun surfaceCreated(holder: SurfaceHolder) {
            player.setDisplay(holder)
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
        override fun surfaceDestroyed(holder: SurfaceHolder) {}
    }
}