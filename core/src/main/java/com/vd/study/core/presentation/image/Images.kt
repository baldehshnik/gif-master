package com.vd.study.core.presentation.image

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.material.imageview.ShapeableImageView
import com.vd.study.core.R
import com.vd.study.core.presentation.utils.IMAGE_LOADING_TIMEOUT
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

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

fun Context.saveImageToInternalStorage(
    imageUri: Uri,
    outputFileName: String,
    resultHandler: (path: String) -> Unit,
    errorHandler: (() -> Unit)? = null
) {
    val imageFile = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), outputFileName)
    try {
        val inputStream = contentResolver.openInputStream(imageUri)
        if (inputStream == null) {
            errorHandler?.invoke()
            return
        }

        val outputStream: OutputStream = FileOutputStream(imageFile)
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) {
            outputStream.write(buffer, 0, length)
        }

        inputStream.close()
        outputStream.close()

        resultHandler(imageFile.absolutePath)
    } catch (e: IOException) {
        errorHandler?.invoke()
    }
}
