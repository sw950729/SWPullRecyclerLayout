package com.kotlin.layout

import android.animation.ValueAnimator
import android.content.Context
import android.support.v4.view.NestedScrollingParent
import android.support.v4.view.NestedScrollingParentHelper
import android.support.v7.widget.*
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.kotlin.interfaces.OnTouchUpListener

/**
 * Created by Angel on 2016/7/27.
 */
class SWPullRecyclerLayout : LinearLayout, NestedScrollingParent {

    private var helper: NestedScrollingParentHelper? = null
    private val IsRefresh = true
    private val IsLoad = true
    //move total
    private var totalY = 0
    private var headerLayout: LinearLayout? = null
    private var myRecyclerView: MyRecyclerView? = null
    private var footerLayout: LinearLayout? = null
    private var onTouchUpListener: OnTouchUpListener? = null
    private var isfling = false
    private var headerHeight = 0
    private var footerHeight = 0

    constructor(context: Context) : super(context) {
        initial()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initial()
    }

    private fun initial() {
        helper = NestedScrollingParentHelper(this)
        headerLayout = LinearLayout(context)
        myRecyclerView = MyRecyclerView(context)
        footerLayout = LinearLayout(context)
        orientation = LinearLayout.VERTICAL
        headerLayout!!.orientation = LinearLayout.VERTICAL
        footerLayout!!.orientation = LinearLayout.VERTICAL
        addView(this.headerLayout, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))
        addView(this.myRecyclerView, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT))
        addView(this.footerLayout, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))
    }

    fun setMyRecyclerView(layoutManager: RecyclerView.LayoutManager, adapter: RecyclerView.Adapter<*>) {
        myRecyclerView!!.setMyLayoutManager(layoutManager)
        myRecyclerView!!.adapter = adapter
    }

    fun setMyRecyclerView(layoutManager: RecyclerView.LayoutManager, adapter: RecyclerView.Adapter<*>, fixed: Boolean) {
        myRecyclerView!!.setMyLayoutManager(layoutManager)
        myRecyclerView!!.adapter = adapter
        myRecyclerView!!.setHasFixedSize(fixed)
    }

    /**
     * add headerview
     */
    fun addHeaderView(headerView: View, headerHeight: Int) {
        this.headerHeight = headerHeight
        this.headerLayout!!.removeAllViews()
        this.headerLayout!!.addView(headerView)
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, headerHeight)
        layoutParams.topMargin = -headerHeight
        this.headerLayout!!.layoutParams = layoutParams
    }

    /**
     * add footerview
     */
    fun addFooterView(footerView: View, footerHeight: Int) {
        this.footerHeight = footerHeight
        this.footerLayout!!.removeAllViews()
        this.footerLayout!!.addView(footerView)
        this.footerLayout!!.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, footerHeight)
    }

    fun setScrollTo(fromY: Int, toY: Int) {
        smoothScrollTo(fromY.toFloat(), toY.toFloat())
    }

    fun setItemDivider(itemDecoration: RecyclerView.ItemDecoration) {
        myRecyclerView!!.addItemDecoration(itemDecoration)
    }

    val total: Int
        get() = -totalY / 2

    var isScrollLoad: Boolean
        get() = myRecyclerView!!.isScrollLoad
        set(isScrollLoad) {
            myRecyclerView!!.isScrollLoad = isScrollLoad
        }

    var isScrollRefresh: Boolean
        get() = myRecyclerView!!.isScrollRefresh
        set(isScrollRefresh) {
            myRecyclerView!!.isScrollRefresh = isScrollRefresh
        }

    fun setRecyclerViewScrollToPosition(position: Int) {
        myRecyclerView!!.scrollToPosition(position)
    }

    fun addOnTouchUpListener(onTouchUpListener: OnTouchUpListener) {
        this.onTouchUpListener = onTouchUpListener
    }

    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        return true
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int) {
        helper!!.onNestedScrollAccepted(child, target, axes)
    }

    /**
     * parent view intercept child view scroll
     */
    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        if (totalY < 0 && myRecyclerView!!.isOrientation(0) || totalY > 0 && myRecyclerView!!.isOrientation(1)) {
            isfling = true
        }
        if (IsRefresh) {
            if (dy > 0) {
                if (myRecyclerView!!.isOrientation(0)) {
                    totalY += dy
                    if (totalY / 2 <= 0) {
                        scrollTo(0, totalY / 2)
                        consumed[1] = dy
                    } else {
                        scrollTo(0, 0)
                        consumed[1] = 0
                    }
                }
                return
            }
        }
        if (IsLoad) {
            if (dy < 0) {
                if (myRecyclerView!!.isOrientation(1)) {
                    totalY += dy
                    if (totalY / 2 >= 0) {
                        scrollTo(0, totalY / 2)
                        consumed[1] = dy
                    } else {
                        scrollTo(0, 0)
                        consumed[1] = 0
                    }
                }
                return
            }
        }
    }

    /**
     * while child view move finish
     * dyUnconsumed less than 0,move down
     * dyUnconsumed  more than 0,move up
     */
    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
        if (dyUnconsumed != 0) {
            totalY += dyUnconsumed
            scrollTo(0, totalY / 2)
        }
    }

    override fun onStopNestedScroll(child: View) {
        helper!!.onStopNestedScroll(child)
        if (onTouchUpListener != null) {
            isfling = false
            if (this.total >= headerHeight) {
                this.setScrollTo(this.total, headerHeight)
                if (!this.isScrollRefresh) {
                    this.isScrollRefresh = true
                    onTouchUpListener!!.OnRefreshing()
                }
            } else if (-this.total >= footerHeight) {
                this.setScrollTo(this.total, -footerHeight)
                if (!this.isScrollLoad) {
                    this.isScrollLoad = true
                    onTouchUpListener!!.OnLoading()
                }
            } else {
                this.setScrollTo(0, 0)
            }
        }
    }

    override fun onNestedFling(target: View, velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        return isfling
    }

    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        return isfling
    }

    override fun getNestedScrollAxes(): Int {
        return helper!!.nestedScrollAxes
    }

    private fun smoothScrollTo(fromY: Float, toY: Float) {
        val animator = ValueAnimator.ofFloat(*floatArrayOf(fromY, toY))
        if (fromY == toY) {
            animator.duration = 0
        } else {
            animator.duration = 300
        }
        animator.addUpdateListener { animation ->
            val to = (-(animation.animatedValue as Float).toFloat()).toInt()
            scrollTo(0, to)
            totalY = to * 2
        }
        animator.start()
    }

    private inner class MyRecyclerView(context: Context) : RecyclerView(context) {
        private var staggeredGridLayoutManager: StaggeredGridLayoutManager? = null
        private var linearLayoutManager: LinearLayoutManager? = null
        private var gridLayoutManager: GridLayoutManager? = null
        var isScrollLoad = false
        var isScrollRefresh = false

        init {
            isVerticalFadingEdgeEnabled = false
            isHorizontalFadingEdgeEnabled = false
            isVerticalScrollBarEnabled = false
            isHorizontalScrollBarEnabled = false
            overScrollMode = View.OVER_SCROLL_NEVER
            itemAnimator = DefaultItemAnimator()
        }

        fun setMyLayoutManager(layoutManager: RecyclerView.LayoutManager) {
            if (layoutManager is StaggeredGridLayoutManager) {
                staggeredGridLayoutManager = layoutManager
            } else if (layoutManager is GridLayoutManager) {
                gridLayoutManager = layoutManager
            } else if (layoutManager is LinearLayoutManager) {
                linearLayoutManager = layoutManager
            }
            setLayoutManager(layoutManager)
            if (!isVertical) {
                throw NullPointerException("vertical!")
            }
        }

        /**
         * orientation
         * 0 menas down
         * 1 means up
         */
        fun isOrientation(orientation: Int): Boolean {
            if (orientation == 0) {
                return isCanPullDown
            } else if (orientation == 1) {
                return isCanPullUp
            }
            return false
        }

        private val isCanPullDown: Boolean
            get() = !canScrollVertically(-1)

        private val isCanPullUp: Boolean
            get() = !canScrollVertically(1)

        private val isVertical: Boolean
            get() {
                if (staggeredGridLayoutManager != null) {
                    return staggeredGridLayoutManager!!.orientation == StaggeredGridLayoutManager.VERTICAL
                } else if (linearLayoutManager != null) {
                    return linearLayoutManager!!.orientation == LinearLayoutManager.VERTICAL
                } else if (gridLayoutManager != null) {
                    return gridLayoutManager!!.orientation == GridLayoutManager.VERTICAL
                }
                return false
            }
    }

}