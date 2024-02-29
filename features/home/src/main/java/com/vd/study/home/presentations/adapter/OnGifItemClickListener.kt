package com.vd.study.home.presentations.adapter

import com.vd.study.home.domain.entities.FullGifEntity

// delete three functions
interface OnGifItemClickListener {

    fun onLikeClick(gif: FullGifEntity)

    fun onSaveClick(gif: FullGifEntity)

    fun onShareClick(gif: FullGifEntity)

    fun onItemClick(gif: FullGifEntity)

}