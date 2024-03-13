package com.vd.study.search.presentation.adapter

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
import com.vd.study.search.databinding.ListItemBinding
import com.vd.study.search.domain.entities.NetworkGifEntity
import com.vd.study.core.R as CoreResources

class SearchGifAdapter :
    PagingDataAdapter<NetworkGifEntity, SearchGifAdapter.SearchViewHolder>(NetworkGifDiffUtil()) {

    private val gradients = listOf(
        CoreResources.drawable.placeholder_voilet_gradient,
        CoreResources.drawable.placeholder_red_gradient,
        CoreResources.drawable.placeholder_blue_gradient
    )

    class SearchViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(gif: NetworkGifEntity, @DrawableRes placeholder: Int) = with(binding) {
            val requestOptions: RequestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(false)
                .format(DecodeFormat.PREFER_RGB_565)
                .override(Target.SIZE_ORIGINAL)
                .placeholder(placeholder)
                .error(CoreResources.drawable.giphy)
                .centerCrop()

            val requestBuilder: RequestBuilder<GifDrawable> = Glide.with(this.gif.context)
                .asGif()
                .load(gif.url)
                .apply(requestOptions)

            requestBuilder.into(this.gif)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater, parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val account: NetworkGifEntity? = getItem(position)
        if (account != null) {
            holder.bind(account, gradients.random())
            holder.itemView.loadAnimation(CoreResources.anim.list_gif_item)
        }
    }

    class NetworkGifDiffUtil : DiffUtil.ItemCallback<NetworkGifEntity>() {

        override fun areItemsTheSame(oldItem: NetworkGifEntity, newItem: NetworkGifEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NetworkGifEntity, newItem: NetworkGifEntity): Boolean {
            return oldItem == newItem
        }
    }
}
