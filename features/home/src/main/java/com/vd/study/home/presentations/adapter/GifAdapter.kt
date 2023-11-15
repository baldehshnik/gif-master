package com.vd.study.home.presentations.adapter

import android.content.Context
import android.graphics.Insets
import android.os.Build
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import android.view.WindowMetrics
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.imageLoader
import coil.load
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.vd.study.core.presentation.utils.GIPHY_USERNAME
import com.vd.study.home.databinding.GifItemBinding
import com.vd.study.home.domain.entities.FullGifEntity
import com.vd.study.home.domain.entities.GifAuthorEntity


class GifAdapter(
    private val listener: OnGifItemClickListener
) : PagingDataAdapter<FullGifEntity, GifAdapter.GifViewHolder>(GifDiffUtil()) {

    class GifViewHolder(private val binding: GifItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(gif: FullGifEntity, listener: OnGifItemClickListener) = with(binding) {
            setGifHeight(gif.height, gif.width)
            setAuthorInfo(gif.author)

            val request = ImageRequest.Builder(this.gif.context)
                .data(gif.url)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .crossfade(true)
                .target(this.gif)
                .build()
            this.gif.context.imageLoader.enqueue(request)

            btnLike.setOnClickListener { listener.onLikeClick(gif) }
            btnSave.setOnClickListener { listener.onSaveClick(gif) }
            btnShare.setOnClickListener { listener.onShareClick(gif) }

            setLikeAndSaveStatus(gif.isLiked, gif.isSaved)
        }

        private fun setAuthorInfo(author: GifAuthorEntity?) = with(binding) {
            if (author == null) {
                textAccount.text = GIPHY_USERNAME
            } else {
                textAccount.text = author.username
                imageAccount.load(author.avatarUrl) {
                    crossfade(true)
                }
            }
        }

        private fun setLikeAndSaveStatus(
            isLiked: Boolean, isSaved: Boolean
        ) = with(binding) {
            if (isLiked) {
                // change icon with animation
            }
            if (isSaved) {
                // change icon with animation
            }
        }

        private fun setGifHeight(height: Int, width: Int) {
            val screen = getScreenWidth(binding.gif.context)
            var newHeight = height
            if (screen - width > 0) {
                newHeight += screen - width
            }

            val layoutParams = binding.gif.layoutParams
            layoutParams.height = (newHeight / 2.6 * binding.gif.context.resources.displayMetrics.density + 0.5f).toInt()
            binding.gif.layoutParams = layoutParams
        }

        @Suppress("DEPRECATION")
        private fun getScreenWidth(context: Context): Int {
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val windowMetrics: WindowMetrics = windowManager.currentWindowMetrics
                val insets: Insets = windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
                windowMetrics.bounds.width() - insets.left - insets.right
            } else {
                val displayMetrics = DisplayMetrics()
                windowManager.defaultDisplay.getMetrics(displayMetrics)
                displayMetrics.widthPixels
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = GifItemBinding.inflate(inflater, parent, false)
        return GifViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
        val account: FullGifEntity? = getItem(position)
        if (account != null) holder.bind(account, listener)
    }

    class GifDiffUtil : DiffUtil.ItemCallback<FullGifEntity>() {

        override fun areItemsTheSame(oldItem: FullGifEntity, newItem: FullGifEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FullGifEntity, newItem: FullGifEntity): Boolean {
            return oldItem == newItem
        }
    }
}