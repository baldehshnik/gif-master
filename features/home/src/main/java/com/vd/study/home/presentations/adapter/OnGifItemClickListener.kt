package com.vd.study.home.presentations.adapter

import com.vd.study.home.domain.entities.FullGifEntity

interface OnGifItemClickListener {

    fun onLikeClick(gif: FullGifEntity)

    fun onSaveClick(gif: FullGifEntity)

    fun onShareClick(gif: FullGifEntity)

}