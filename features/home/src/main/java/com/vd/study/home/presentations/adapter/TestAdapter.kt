package com.vd.study.home.presentations.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.DrawableRes
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.vd.study.home.R
import com.vd.study.home.databinding.TestItemBinding
import com.vd.study.home.domain.entities.FullGifEntity

// change name
class TestAdapter(
    private val listener: OnGifItemClickListener
) : PagingDataAdapter<FullGifEntity, TestAdapter.TestViewHolder>(GifDiffUtil()) {

    private val gradients = listOf(
        R.drawable.placeholder_voilet_gradient,
        R.drawable.placeholder_red_gradient,
        R.drawable.placeholder_blue_gradient
    )

    // change name
    class TestViewHolder(private val binding: TestItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            gif: FullGifEntity, listener: OnGifItemClickListener, @DrawableRes placeholder: Int
        ) = with(binding) {
            val requestOptions: RequestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(false)
                .format(DecodeFormat.PREFER_RGB_565)
                .override(Target.SIZE_ORIGINAL)
                .placeholder(placeholder)
                .error(R.drawable.giphy)
                .centerCrop()

            val requestBuilder: RequestBuilder<GifDrawable> = Glide.with(this.gif.context)
                .asGif()
                .load(gif.url)
                .apply(requestOptions)

            requestBuilder.into(this.gif)

            binding.root.setOnClickListener {
                listener.onItemClick(gif)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TestItemBinding.inflate(inflater, parent, false)
        return TestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        val account: FullGifEntity? = getItem(position)
        if (account != null) {
            holder.bind(account, listener, gradients.random())
            startAnimation(holder.itemView)
        }
    }

    private fun startAnimation(view: View) {
        val animation = AnimationUtils.loadAnimation(view.context, R.anim.list_gif_item)
        view.startAnimation(animation)
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