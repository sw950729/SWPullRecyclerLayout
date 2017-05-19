package com.kotlin.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * Created by Angel on 2017/5/19.
 */
abstract class SWRecyclerAdapter<T>(context: Context, private val list: MutableList<T>?) : RecyclerView.Adapter<SWViewHolder>() {
    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, layoutID: Int): SWViewHolder {
        val holder = SWViewHolder(
                inflater.inflate(getItemLayoutId(layoutID), parent, false))
        return holder
    }

    override fun onBindViewHolder(holder: SWViewHolder, position: Int) {
        bindData(holder, position, list!![position])
    }

    abstract fun getItemLayoutId(layoutID: Int): Int

    abstract fun bindData(holder: SWViewHolder, position: Int, item: T)

    override fun getItemCount(): Int {
        return list!!.size
    }

    fun add(position: Int, item: T) {
        list!!.add(position, item)
        notifyItemInserted(position)
    }

    fun delete(position: Int) {
        list!!.removeAt(position)
        notifyItemRemoved(position)
    }

    fun clear() {
        if (list != null && list.size > 0) {
            list.clear()
        }
        notifyDataSetChanged()
    }

    fun setTop(position: Int) {
        list!!.add(0, list[position])
        list.removeAt(position + 1)
        notifyDataSetChanged()
    }

}
