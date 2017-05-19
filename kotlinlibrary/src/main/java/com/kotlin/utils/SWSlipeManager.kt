package com.kotlin.utils


import com.kotlin.layout.SWSlipeLayout

class SWSlipeManager {

    private var swSlipeLayout: SWSlipeLayout? = null

    fun setSwSlipeLayout(swSlipeLayout: SWSlipeLayout) {
        this.swSlipeLayout = swSlipeLayout
    }

    fun clear() {
        swSlipeLayout = null
    }

    fun close() {
        if (swSlipeLayout != null) {
            swSlipeLayout!!.close()
        }
    }

    /**
     * if s==null means no item is open

     * @return ture means open else close
     */
    fun haveOpened(): Boolean {
        return swSlipeLayout != null
    }

    /**
     * if s==null means no item is open

     * @return true means two item is not the same one and one item is open
     */
    fun haveOpened(s: SWSlipeLayout): Boolean {
        return swSlipeLayout != null && swSlipeLayout == s
    }

    companion object {
        val instance = SWSlipeManager()
    }
}