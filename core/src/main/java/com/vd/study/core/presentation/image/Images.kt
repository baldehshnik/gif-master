package com.vd.study.core.presentation.image

import android.content.Context
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.material.imageview.ShapeableImageView
import com.vd.study.core.R
import com.vd.study.core.presentation.utils.IMAGE_LOADING_TIMEOUT

fun ShapeableImageView.loadGif(url: String, @DrawableRes placeholder: Int) {
    val requestOptions: RequestOptions = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .skipMemoryCache(false)
        .format(DecodeFormat.PREFER_RGB_565)
        .override(Target.SIZE_ORIGINAL)
        .timeout(IMAGE_LOADING_TIMEOUT)
        .placeholder(placeholder)
        .error(R.drawable.image_error)
        .centerCrop()

    Glide.with(this.context)
        .asGif()
        .load(url)
        .apply(requestOptions)
        .into(this)
}

fun Context.getDefaultAccountDrawableUrl(): String {
    return "android.resource://" + this.packageName + "/" + R.drawable.default_account_icon
}
