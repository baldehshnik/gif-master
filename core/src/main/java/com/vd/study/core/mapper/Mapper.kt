package com.vd.study.core.mapper

interface Mapper<I, O> {

    fun map(input: I): O

}
