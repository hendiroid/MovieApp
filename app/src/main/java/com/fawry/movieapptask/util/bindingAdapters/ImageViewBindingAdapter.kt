package com.fawry.movieapptask.util.bindingAdapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

import java.io.File

@BindingAdapter(value = ["imageUrl"])
fun loadImage(view: ImageView, imageUrl: Int) {

    Glide.with(view.context)
        .load(imageUrl)
        .centerInside()
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .into(view)
}

@BindingAdapter(value = ["url"])
fun loadImage(view: ImageView, imageUrl: String) {

    Glide.with(view.context)
        .load("https://image.tmdb.org/t/p/w500$imageUrl")
        .centerInside()
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .skipMemoryCache(true)
        .dontAnimate()
        .into(view)
}

@BindingAdapter(value = ["file"])
fun loadImageFromFile(view: ImageView, filePath: String) {
    Glide.with(view.context)
        .load(File(filePath))
        .centerCrop()
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .into(view)
}