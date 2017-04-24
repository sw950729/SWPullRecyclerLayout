package com.angel.layout;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import com.angel.interfaces.OnSwipeStatusListener;

public class SWSlipLayout extends LinearLayout {

    private View hiddenView;
    private View itemView;
    private int hiddenViewWidth;
    private ViewDragHelper helper;
    private OnSwipeStatusListener listener;
    private Status status = Status.Close;
    private Status changeStatus = Status.Close;

    /**
     * status
     */
    public enum Status {
        Open, Close
    }

    /**
     * set slide status
     */
    public void setStatus(Status status) {
        this.status = status;
        if (status == Status.Open) {
            open();
        } else {
            close();
        }
    }

    public void setOnSwipeStatusListener(OnSwipeStatusListener listener) {
        this.listener = listener;
    }


    public SWSlipLayout(Context context) {
        super(context);
    }

    public SWSlipLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        inital();
    }

    private void inital() {
        helper = ViewDragHelper.create(this, callback);
        setOrientation(HORIZONTAL);
    }

    ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {

        public boolean tryCaptureView(View view, int arg1) {
            return view == itemView;
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
            if (itemView == changedView) {
                hiddenView.offsetLeftAndRight(dx);
            } else {
                itemView.offsetLeftAndRight(dx);
            }
            invalidate();
        }

        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (releasedChild == itemView) {
                if (xvel == 0 && Math.abs(itemView.getLeft()) > hiddenViewWidth / 2.0f) {
                    open();
                } else if (xvel < 0) {
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
    private void close() {
        changeStatus = status;
        status = Status.Close;
        if (helper.smoothSlideViewTo(itemView, 0, 0)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
        if (listener != null && changeStatus == Status.Open) {
            listener.onStatusChanged(status);
        }
    }

    /**
     * slide open
     */
    private void open() {
        changeStatus = status;
        status = Status.Open;
        if (helper.smoothSlideViewTo(itemView, -hiddenViewWidth, 0)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
        if (listener != null && changeStatus == Status.Close) {
            listener.onStatusChanged(status);
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
        return helper.shouldInterceptTouchEvent(event);
    }

    public boolean onTouchEvent(MotionEvent event) {
        requestDisallowInterceptTouchEvent(true);
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