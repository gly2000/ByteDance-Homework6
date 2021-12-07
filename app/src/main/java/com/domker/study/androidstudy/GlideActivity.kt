package com.domker.study.androidstudy

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_image.*
import java.util.*

class GlideActivity : AppCompatActivity() {

    private val pages: MutableList<View> = ArrayList()
    var confirmBtn: Button? = null
    var editText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        confirmBtn = findViewById(R.id.add)
        editText = findViewById(R.id.url)

        confirmBtn?.setOnClickListener{load()}

        addImage(R.drawable.drawableimage)
        addImage(R.drawable.ic_markunread)
        addImage("file:///android_asset/assetsimage.jpg")
        addImage(R.raw.rawimage)
        addImage("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF")
        val adapter = ViewAdapter()
        adapter.setDatas(pages)
        view_pager.adapter = adapter
    }

    private fun load(){
        val url = editText?.text.toString()
        addImage(url)
        val adapter = ViewAdapter()
        adapter.setDatas(pages)
        view_pager.adapter = adapter
    }

    private fun addImage(resId: Int) {
        val imageView = layoutInflater.inflate(R.layout.activity_image_item, null) as ImageView
        Glide.with(this)
                .load(resId)
                .error(R.drawable.error)
                .into(imageView)
        pages.add(imageView)
    }

    private fun addImage(path: String) {
        val imageView = layoutInflater.inflate(R.layout.activity_image_item, null) as ImageView
        Glide.with(this)
                .load(path)
                .apply(RequestOptions().circleCrop().diskCacheStrategy(DiskCacheStrategy.ALL))
                .error(R.drawable.error) //.transition(withCrossFade(4000))
                //.override(100, 100)
                .into(imageView)
        pages.add(imageView)
    }
}