package com.vd.study.home.presentations.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.vd.study.core.presentation.utils.GIPHY_USERNAME
import com.vd.study.home.databinding.GifItemBinding
import com.vd.study.home.domain.entities.FullGifEntity
import com.vd.study.home.domain.entities.GifAuthorEntity

class GifAdapter : PagingDataAdapter<FullGifEntity, GifAdapter.GifViewHolder>(GifDiffUtil()) {

    class GifViewHolder(val binding: GifItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(account: FullGifEntity) = with(binding) {
            setAuthorInfo(account.author)
            setLikeAndSaveStatus(account.isLiked, account.isSaved)
            //load gif with coil
        }

        private fun setAuthorInfo(author: GifAuthorEntity?) = with(binding) {
            if (author == null) {
                textAccount.text = GIPHY_USERNAME
                // load default image with coil
            } else {
                textAccount.text = author.username
                // load image with coil
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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = GifItemBinding.inflate(inflater, parent, false)
        return GifViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
        val account: FullGifEntity? = getItem(position)
        if (account != null) holder.bind(account)
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