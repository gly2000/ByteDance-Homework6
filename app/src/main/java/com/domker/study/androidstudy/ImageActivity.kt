package com.domker.study.androidstudy

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.domker.study.androidstudy.ImageActivity
import kotlinx.android.synthetic.main.activity_image.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*

class ImageActivity : AppCompatActivity () {

    var confirmBtn:Button? = null
    var editText:EditText? = null

    private val pages: MutableList<View> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        confirmBtn = findViewById(R.id.add)
        editText = findViewById(R.id.url)

        confirmBtn?.setOnClickListener{load()}

        view_pager.post {
            loadNetImage(view_pager.width, view_pager.height, "https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF")
            val adapter = ViewAdapter()
            adapter.setDatas(pages)
            view_pager.adapter = adapter
        }
    }

    private fun load(){
        val url = editText?.text.toString()
        loadNetImage(view_pager.width, view_pager.height, url)
        val adapter = ViewAdapter()
        adapter.setDatas(pages)
        view_pager.adapter = adapter
    }

    private fun loadNetImage(width: Int, height: Int, url: String) {
        val imageView = layoutInflater.inflate(R.layout.activity_image_item, null) as ImageView
        pages.add(imageView)
        Thread {
            val bitmap = decodeBitmapFromNet(url,
                    width,
                    height)
            runOnUiThread { addImageAsyn(imageView, bitmap) }
        }.start()
    }

    private fun addImageAsyn(imageView: ImageView, bitmap: Bitmap?) {
        imageView.setImageBitmap(bitmap)
    }

    private fun decodeBitmapFromNet(url: String, reqWidth: Int, reqHeight: Int): Bitmap? {
        var input: InputStream? = null
        var data: ByteArray? = null
        try {
            val imgUrl = URL(url)
            val conn = imgUrl.openConnection() as HttpURLConnection
            conn.doInput = true
            conn.connect()
            input = conn.inputStream
            data = inputStreamToByteArray(input)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            return null
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } finally {
            try {
                input?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return if (data != null) {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeByteArray(data, 0, data.size, options)
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
            options.inJustDecodeBounds = false
            BitmapFactory.decodeByteArray(data, 0, data.size, options)
        } else {
            null
        }
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        //返回2的整数次幂
        val width = options.outWidth
        val height = options.outHeight
        var inSampleSize = 1
        if(height > reqHeight || width > reqWidth){
            val halfHeight = height / 2;
            val halfWidth = width / 2;
            while (halfHeight / inSampleSize >= reqHeight || halfWidth / inSampleSize >= reqWidth){
                inSampleSize = inSampleSize * 2
            }
        }
        return inSampleSize
    }

    companion object {
        fun inputStreamToByteArray(input: InputStream?): ByteArray {
            val outputStream = ByteArrayOutputStream()
            input ?: return outputStream.toByteArray()
            val buffer = ByteArray(1024)
            var len: Int
            try {
                while (input.read(buffer).also { len = it } != -1) {
                    outputStream.write(buffer, 0, len)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    input.close()
                    outputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return outputStream.toByteArray()
        }
    }
}