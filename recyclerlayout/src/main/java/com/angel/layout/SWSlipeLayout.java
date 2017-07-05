package com.angel.layout;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import com.angel.utils.SWSlipeManager;

/**
 * Created by Angel on 2017/4/10.
 */
public class SWSlipeLayout extends LinearLayout {

    private View hiddenView;
    private View itemView;
    private int hiddenViewWidth;
    private ViewDragHelper helper;
    private Status changeStatus = Status.Close;
    private float downX;
    private float downY;
    private float moveX;
    private float moveY;
    private float downIX;
    private float downIY;
    private int mPointerId = 0;

    /**
     * status
     */
    public enum Status {
        Open, Close
    }

    public SWSlipeLayout(Context context) {
        super(context);
        initial();
    }

    public SWSlipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initial();
    }

    private void initial() {
        helper = ViewDragHelper.create(this, callback);
        setOrientation(HORIZONTAL);
    }

    ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {

        public boolean tryCaptureView(View view, int pointerId) {
            if (mPointerId == pointerId) {
                return view == itemView;
            } else {
                return false;
            }
        }

        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child == itemView) {
                if (left > 0) {
                    return 0;
                } else {
                    left = Math.max(left, -hiddenViewWidth);
                    return left;
                }
            }
            return 0;
        }

        public int getViewHorizontalDragRange(View child) {
            return hiddenViewWidth;
        }

        public void onViewPositionChanged(View changedView, int left, int top,
                                          int dx, int dy) {
            if (dx != 0) {
                getParent().requestDisallowInterceptTouchEvent(true);
            }
            if (itemView == changedView) {
                hiddenView.offsetLeftAndRight(dx);
            } else {
                itemView.offsetLeftAndRight(dx);
            }
            if (itemView.getLeft() != 0) {
                SWSlipeManager.getInstance().setSwSlipeLayout(SWSlipeLayout.this);
            } else {
                SWSlipeManager.getInstance().clear();
            }
            if (itemView.getLeft() == 0 && changeStatus != Status.Close) {
                changeStatus = Status.Close;
            } else if (itemView.getLeft() == -hiddenViewWidth && changeStatus != Status.Open) {
                changeStatus = Status.Open;
            }
            invalidate();
        }

        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (releasedChild == itemView) {
                if (xvel == 0 && Math.abs(itemView.getLeft()) > hiddenViewWidth / 2.0f || xvel < 0) {
                    open();
                } else {
                    close();
                }
            }
        }

    };

    /**
     * slide close
     */
    public void close() {
        if (helper.smoothSlideViewTo(itemView, 0, 0)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
        SWSlipeManager.getInstance().clear();
    }

    /**
     * slide open
     */
    public void open() {
        SWSlipeManager.getInstance().setSwSlipeLayout(this);
        if (helper.smoothSlideViewTo(itemView, -hiddenViewWidth, 0)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void computeScroll() {
        super.computeScroll();
        // start animation
        if (helper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean value = helper.shouldInterceptTouchEvent(event);
        //if you open is not the current item,close
        if (!SWSlipeManager.getInstance().haveOpened(this)) {
            SWSlipeManager.getInstance().close();
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downIX = event.getX();
                downIY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = event.getX();
                moveY = event.getY();
                if (Math.abs(moveX - downIX) > 1 || Math.abs(moveY - downIY) > 1) {
                    value = true;
                }
                break;
        }
        return value;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (SWSlipeManager.getInstance().haveOpened(this)) {
            getParent().requestDisallowInterceptTouchEvent(true);
        } else if (SWSlipeManager.getInstance().haveOpened()) {
            getParent().requestDisallowInterceptTouchEvent(true);
            return true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();
                float dx = Math.abs(moveX - downX);
                float dy = Math.abs(moveY - downY);
                if (dx > dy) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                downX = moveX;
                downY = moveY;
                break;
        }
        helper.processTouchEvent(event);
        return true;
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 2) {
            throw new NullPointerException("you only need two child view!");
        }
        itemView = getChildAt(0);
        hiddenView = getChildAt(1);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        hiddenViewWidth = hiddenView.getMeasuredWidth();
    }
}