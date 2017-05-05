package com.angel.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import com.angel.interfaces.OnSwipeStatusListener;
import org.w3c.dom.Attr;
import sw.angel.recyclerlayout.R;

public class SWSlipeLayout extends LinearLayout {

    private View hiddenView;
    private View itemView;
    private int hiddenViewWidth;
    private ViewDragHelper helper;
    private OnSwipeStatusListener listener;
    private Status status = Status.Close;
    private Status changeStatus = Status.Close;
    private boolean isOpen = false;
    private boolean slipe_enable = true;

    /**
     * status
     */
    public enum Status {
        Open, Close
    }

    public void setOnSwipeStatusListener(OnSwipeStatusListener listener) {
        this.listener = listener;
    }

    public SWSlipeLayout(Context context) {
        super(context, null);
    }

    public SWSlipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initial(context, attrs);
    }


    private void initial(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SWSlipeLayout);
        slipe_enable = array.getBoolean(R.styleable.SWSlipeLayout_slipe_enable, true);
        if (slipe_enable) {
            helper = ViewDragHelper.create(this, callback);
        }
        setOrientation(HORIZONTAL);
        array.recycle();
    }

    ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {

        public boolean tryCaptureView(View view, int arg1) {
            return true;
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

    public boolean isOpen() {
        return isOpen;
    }

    /**
     * slide close
     */
    public void close() {
        isOpen = !isOpen;
        changeStatus = status;
        status = Status.Close;
        if (helper.smoothSlideViewTo(itemView, 0, 0)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
        if (listener != null && changeStatus == Status.Open) {
            listener.onStatusChanged(this, status);
        }
    }

    /**
     * slide open
     */
    public void open() {
        isOpen = !isOpen;
        changeStatus = status;
        status = Status.Open;
        if (helper.smoothSlideViewTo(itemView, -hiddenViewWidth, 0)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
        if (listener != null && changeStatus == Status.Close) {
            listener.onStatusChanged(this, status);
        }
    }

    public void computeScroll() {
        super.computeScroll();
        // start animation
        if (slipe_enable) {
            if (helper.continueSettling(true)) {
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (slipe_enable) {
            return helper.shouldInterceptTouchEvent(event);
        }
        return super.onInterceptTouchEvent(event);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (slipe_enable) {
            helper.processTouchEvent(event);
            return true;
        }
        return super.onTouchEvent(event);
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