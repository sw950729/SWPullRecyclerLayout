package com.kotlin.adapter

import android.support.v7.util.DiffUtil

/**
 * Created by Angel on 2017/5/19.
 */
class SWDiffCallBack<T>(private val olddatas: List<T>, private val newDatas: List<T>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return olddatas.size
    }

    override fun getNewListSize(): Int {
        return newDatas.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return olddatas[oldItemPosition] == newDatas[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return olddatas[oldItemPosition] == newDatas[newItemPosition]
    }
}
