package com.vd.study.account.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vd.study.account.databinding.LikedGifItemBinding
import com.vd.study.account.domain.entities.GifEntity

interface OnLikedGifItemClickListener {
    fun onClick(gif: GifEntity)
}

class LikedGifsAdapter(
    private val items: List<GifEntity>,
    private val listener: OnLikedGifItemClickListener
) : ListAdapter<GifEntity, LikedGifsAdapter.LikedGifsViewHolder>(LikedGifDiffUtil()) {

    class LikedGifsViewHolder(private val binding: LikedGifItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(gif: GifEntity, listener: OnLikedGifItemClickListener) = with(binding) {
            root.setOnClickListener { listener.onClick(gif) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikedGifsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LikedGifItemBinding.inflate(inflater, parent, false)
        return LikedGifsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LikedGifsViewHolder, position: Int) {
        holder.bind(items[position], listener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class LikedGifDiffUtil : DiffUtil.ItemCallback<GifEntity>() {
        override fun areItemsTheSame(oldItem: GifEntity, newItem: GifEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GifEntity, newItem: GifEntity): Boolean {
            return oldItem == newItem
        }
    }
}