package com.vd.study.account.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.vd.study.account.databinding.FragmentViewedGifsBinding
import com.vd.study.account.domain.entities.GifEntity

class GifsAdapter : PagingDataAdapter<GifEntity, GifsAdapter.GifsViewHolder>(GifDiffUtil()) {

    class GifsViewHolder(
        private val binding: FragmentViewedGifsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(gif: GifEntity) = with(binding) {

        }

    }

    override fun onBindViewHolder(holder: GifsViewHolder, position: Int) {
        val gif = getItem(position)
        if (gif != null) holder.bind(gif)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentViewedGifsBinding.inflate(inflater, parent, false)
        return GifsViewHolder(binding)
    }

    class GifDiffUtil : DiffUtil.ItemCallback<GifEntity>() {

        override fun areItemsTheSame(oldItem: GifEntity, newItem: GifEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GifEntity, newItem: GifEntity): Boolean {
            return oldItem == newItem
        }
    }

}