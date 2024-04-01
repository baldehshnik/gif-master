package com.vd.study.search.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.vd.study.core.presentation.animation.loadAnimation
import com.vd.study.core.presentation.image.loadGif
import com.vd.study.core.presentation.utils.getGradientsArray
import com.vd.study.search.databinding.ListItemBinding
import com.vd.study.search.domain.entities.GifEntity
import com.vd.study.core.R as CoreResources

interface OnGifItemClickListener {

    fun onClick(gif: GifEntity)

}

class SearchGifAdapter(
    private val listener: OnGifItemClickListener
) : PagingDataAdapter<GifEntity, SearchGifAdapter.SearchViewHolder>(GifEntityDiffUtil()) {

    private val gradients = getGradientsArray()

    class SearchViewHolder(
        private val binding: ListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(gif: GifEntity, @DrawableRes placeholder: Int, listener: OnGifItemClickListener) =
            with(binding) {
                binding.gif.loadGif(gif.url, placeholder)
                binding.gif.setOnClickListener { listener.onClick(gif) }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater, parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val account: GifEntity? = getItem(position)
        if (account != null) {
            holder.bind(account, gradients.random(), listener)
            holder.itemView.loadAnimation(CoreResources.anim.list_gif_item)
        }
    }

    class GifEntityDiffUtil : DiffUtil.ItemCallback<GifEntity>() {

        override fun areItemsTheSame(oldItem: GifEntity, newItem: GifEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GifEntity, newItem: GifEntity): Boolean {
            return oldItem == newItem
        }
    }
}
