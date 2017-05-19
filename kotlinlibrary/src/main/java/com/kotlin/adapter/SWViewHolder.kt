package com.kotlin.adapter

import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.kotlin.layout.SWSlipeLayout

/**
 * Created by Angel on 2017/5/19.
 */
class SWViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val viewSparseArray: SparseArray<View>

    init {
        viewSparseArray = SparseArray<View>()
    }

    private fun <T : View> findViewById(viewId: Int): T {
        var view: View? = viewSparseArray.get(viewId)
        if (view == null) {
            view = itemView.findViewById(viewId)
            viewSparseArray.put(viewId, view)
        }
        return (view as T?)!!
    }

    fun getView(viewId: Int): View {
        return findViewById(viewId)
    }

    fun getTextView(viewId: Int): TextView {
        return getView(viewId) as TextView
    }

    fun getButton(viewId: Int): Button {
        return getView(viewId) as Button
    }

    fun getImageView(viewId: Int): ImageView {
        return getView(viewId) as ImageView
    }

    fun getSWSlipLayout(viewId: Int): SWSlipeLayout {
        return getView(viewId) as SWSlipeLayout
    }
}
