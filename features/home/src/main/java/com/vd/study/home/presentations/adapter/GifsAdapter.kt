package com.vd.study.home.presentations.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
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
import com.vd.study.core.presentation.animation.loadAnimation
import com.vd.study.home.databinding.TestItemBinding
import com.vd.study.home.domain.entities.FullGifEntity
import com.vd.study.core.R as CoreResources

class GifsAdapter(
    private val listener: OnGifItemClickListener
) : PagingDataAdapter<FullGifEntity, GifsAdapter.GifsViewHolder>(GifDiffUtil()) {

    private val gradients = listOf(
        CoreResources.drawable.placeholder_voilet_gradient,
        CoreResources.drawable.placeholder_red_gradient,
        CoreResources.drawable.placeholder_blue_gradient
    )

    class GifsViewHolder(private val binding: TestItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            gif: FullGifEntity, listener: OnGifItemClickListener, @DrawableRes placeholder: Int
        ) = with(binding) {
            val requestOptions: RequestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(false)
                .format(DecodeFormat.PREFER_RGB_565)
                .override(Target.SIZE_ORIGINAL)
                .timeout(10000)
                .placeholder(placeholder)
                .error(CoreResources.drawable.giphy)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TestItemBinding.inflate(inflater, parent, false)
        return GifsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GifsViewHolder, position: Int) {
        val account: FullGifEntity? = getItem(position)
        if (account != null) {
            holder.bind(account, listener, gradients.random())
            holder.itemView.loadAnimation(CoreResources.anim.list_gif_item)
        }
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