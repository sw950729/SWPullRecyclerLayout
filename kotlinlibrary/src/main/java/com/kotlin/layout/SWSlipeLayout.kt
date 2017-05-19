package com.kotlin.layout

import android.content.Context
import android.support.v4.view.ViewCompat
import android.support.v4.widget.ViewDragHelper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import com.kotlin.utils.SWSlipeManager

/**
 * Created by Angel on 2017/5/19.
 */
class SWSlipeLayout : LinearLayout {

    private var hiddenView: View? = null
    private var itemView: View? = null
    private var hiddenViewWidth: Int = 0
    private var helper: ViewDragHelper? = null
    private var changeStatus = Status.Close
    private var downX: Float = 0.toFloat()
    private var downY: Float = 0.toFloat()
    private var moveX: Float = 0.toFloat()
    private var moveY: Float = 0.toFloat()
    private var downIX: Float = 0.toFloat()
    private var downIY: Float = 0.toFloat()

    /**
     * status
     */
    enum class Status {
        Open, Close
    }

    constructor(context: Context) : super(context) {
        initial()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initial()
    }

    private fun initial() {
        helper = ViewDragHelper.create(this, callback)
        orientation = LinearLayout.HORIZONTAL
    }

    internal var callback: ViewDragHelper.Callback = object : ViewDragHelper.Callback() {

        override fun tryCaptureView(view: View, arg1: Int): Boolean {
            return view === itemView
        }

        override fun clampViewPositionHorizontal(child: View?, left: Int, dx: Int): Int {
            var left = left
            if (child === itemView) {
                if (left > 0) {
                    return 0
                } else {
                    left = Math.max(left, -hiddenViewWidth)
                    return left
                }
            }
            return 0
        }

        override fun getViewHorizontalDragRange(child: View?): Int {
            return hiddenViewWidth
        }

        override fun onViewPositionChanged(changedView: View?, left: Int, top: Int,
                                           dx: Int, dy: Int) {
            if (dx != 0) {
                parent.requestDisallowInterceptTouchEvent(true)
            }
            if (itemView === changedView) {
                hiddenView!!.offsetLeftAndRight(dx)
            } else {
                itemView!!.offsetLeftAndRight(dx)
            }
            if (itemView!!.left != 0) {
                SWSlipeManager.instance.setSwSlipeLayout(this@SWSlipeLayout)
            } else {
                SWSlipeManager.instance.clear()
            }
            if (itemView!!.left == 0 && changeStatus != Status.Close) {
                changeStatus = Status.Close
            } else if (itemView!!.left == -hiddenViewWidth && changeStatus != Status.Open) {
                changeStatus = Status.Open
            }
            invalidate()
        }

        override fun onViewReleased(releasedChild: View?, xvel: Float, yvel: Float) {
            if (releasedChild === itemView) {
                if (xvel == 0f && Math.abs(itemView!!.left) > hiddenViewWidth / 2.0f || xvel < 0) {
                    open()
                } else {
                    close()
                }
            }
        }

    }

    /**
     * slide close
     */
    fun close() {
        if (helper!!.smoothSlideViewTo(itemView, 0, 0)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
        SWSlipeManager.instance.clear()
    }

    /**
     * slide open
     */
    fun open() {
        SWSlipeManager.instance.setSwSlipeLayout(this)
        if (helper!!.smoothSlideViewTo(itemView, -hiddenViewWidth, 0)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    override fun computeScroll() {
        super.computeScroll()
        // start animation
        if (helper!!.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        var value = helper!!.shouldInterceptTouchEvent(event)
        //if you open is not the current item,close
        if (!SWSlipeManager.instance.haveOpened(this)) {
            SWSlipeManager.instance.close()
        }

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downIX = event.x
                downIY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                moveX = event.x
                moveY = event.y
                if (Math.abs(moveX - downIX) > 1 || Math.abs(moveY - downIY) > 1) {
                    value = true
                }
            }
        }
        return value
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (SWSlipeManager.instance.haveOpened(this)) {
            parent.requestDisallowInterceptTouchEvent(true)
        } else if (SWSlipeManager.instance.haveOpened()) {
            parent.requestDisallowInterceptTouchEvent(true)
            return true
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                val moveX = event.x
                val moveY = event.y
                val dx = Math.abs(moveX - downX)
                val dy = Math.abs(moveY - downY)
                if (dx > dy) {
                    parent.requestDisallowInterceptTouchEvent(true)
                }
                downX = moveX
                downY = moveY
            }
        }
        helper!!.processTouchEvent(event)
        return true
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount != 2) {
            throw NullPointerException("you only need two child view!")
        }
        itemView = getChildAt(0)
        hiddenView = getChildAt(1)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        hiddenViewWidth = hiddenView!!.measuredWidth
    }
}