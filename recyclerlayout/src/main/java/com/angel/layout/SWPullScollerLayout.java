package com.angel.layout;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import com.angel.interfaces.OnTouchUpListener;

/**
 * Created by Angel on 2017/5/24.
 */
public class SWPullScollerLayout extends LinearLayout implements NestedScrollingParent {

    private Context context;
    private NestedScrollingParentHelper helper;
    private LinearLayout headerLayout = null;
    private LinearLayout footerLayout = null;
    private SWScrollView swScrollView = null;
    private OnTouchUpListener onTouchUpListener = null;
    private boolean IsRefresh = true;
    private boolean IsLoad = true;
    private boolean isfling = false;
    //move total
    private int totalY = 0;
    private int headerHeight = 0;
    private int footerHeight = 0;

    public SWPullScollerLayout(Context context) {
        super(context);
        this.context = context;
        initial();
    }

    public SWPullScollerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initial();
    }

    private void initial() {
        helper = new NestedScrollingParentHelper(this);
        headerLayout = new LinearLayout(context);
        footerLayout = new LinearLayout(context);
        swScrollView = new SWScrollView(context);
        setOrientation(VERTICAL);
        headerLayout.setOrientation(VERTICAL);
        footerLayout.setOrientation(VERTICAL);
        addView(headerLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        addView(swScrollView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(footerLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    /**
     * add headerview
     */
    public void addHeaderView(View headerView, int headerHeight) {
        this.headerHeight = headerHeight;
        this.headerLayout.removeAllViews();
        this.headerLayout.addView(headerView);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, headerHeight);
        layoutParams.topMargin = -headerHeight;
        this.headerLayout.setLayoutParams(layoutParams);
    }

    /**
     * add contentview
     */
    public void addContentView(View contentView) {
        this.swScrollView.removeAllViews();
        this.swScrollView.addView(contentView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    /**
     * add footerview
     */
    public void addFooterView(View footerView, int footerHeight) {
        this.footerHeight = footerHeight;
        this.footerLayout.removeAllViews();
        this.footerLayout.addView(footerView);
        this.footerLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, footerHeight));
    }

    public int getTotal() {
        return -totalY / 2;
    }

    public void addOnTouchUpListener(OnTouchUpListener onTouchUpListener) {
        this.onTouchUpListener = onTouchUpListener;
    }

    public void setScrollTo(int fromY, int toY) {
        smoothScrollTo((float) fromY, (float) toY);
    }

    public void setIsScrollLoad(boolean isScrollLoad) {
        swScrollView.isScrollLoad = isScrollLoad;
    }

    public boolean isScrollLoad() {
        return swScrollView.isScrollLoad;
    }

    public void setIsScrollRefresh(boolean isScrollRefresh) {
        swScrollView.isScrollRefresh = isScrollRefresh;
    }

    public boolean isScrollRefresh() {
        return swScrollView.isScrollRefresh;
    }

    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    public void onNestedScrollAccepted(View child, View target, int axes) {
        helper.onNestedScrollAccepted(child, target, axes);
        startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
    }

    /**
     * parent
     * parent view intercept child view scroll
     */
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (totalY < 0 && swScrollView.isOrientation(0) || totalY > 0 && swScrollView.isOrientation(1)) {
            isfling = true;
        }
        if (IsRefresh) {
            if (dy < 0) {
                if (swScrollView.isOrientation(1)) {
                    totalY += dy;
                    if ((totalY / 2) >= 0) {
                        scrollTo(0, totalY / 2);
                        consumed[1] = dy;
                    } else {
                        scrollTo(0, 0);
                        consumed[1] = 0;
                    }
                }
                return;
            }
        }
        if (IsLoad) {
            if (dy > 0) {
                if (swScrollView.isOrientation(0)) {
                    totalY += dy;
                    if ((totalY / 2) <= 0) {
                        scrollTo(0, totalY / 2);
                        consumed[1] = dy;
                    } else {
                        scrollTo(0, 0);
                        consumed[1] = 0;
                    }
                }
                return;
            }
        }
    }

    /**
     * parent
     * while child view move finish
     * dyUnconsumed less than 0,move down
     * dyUnconsumed  more than 0,move up
     */
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if (dyUnconsumed != 0) {
            totalY += dyUnconsumed;
            scrollTo(0, totalY / 2);
        }
    }

    public void onStopNestedScroll(View child) {
        helper.onStopNestedScroll(child);
        if (onTouchUpListener != null) {
            isfling = false;
            if (this.getTotal() >= headerHeight) {
                this.setScrollTo(this.getTotal(), headerHeight);
                if (!this.isScrollRefresh()) {
                    this.setIsScrollRefresh(true);
                    onTouchUpListener.OnRefreshing();
                }
            } else if (-this.getTotal() >= footerHeight) {
                this.setScrollTo(this.getTotal(), -footerHeight);
                if (!this.isScrollLoad()) {
                    this.setIsScrollLoad(true);
                    onTouchUpListener.OnLoading();
                }
            } else {
                this.setScrollTo(0, 0);
            }
        }
    }

    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return isfling;
    }

    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return isfling;
    }

    public int getNestedScrollAxes() {
        return helper.getNestedScrollAxes();
    }

    private void smoothScrollTo(float fromY, float toY) {
        ValueAnimator animator = ValueAnimator.ofFloat(new float[]{fromY, toY});
        if (fromY == toY) {
            animator.setDuration(0);
        } else {
            animator.setDuration(300);
        }
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                int to = (int) (-((Float) animation.getAnimatedValue()).floatValue());
                scrollTo(0, to);
                totalY = to * 2;
            }
        });
        animator.start();
    }

    public class SWScrollView extends NestedScrollView {
        private boolean isScrollLoad = false;
        private boolean isScrollRefresh = false;

        public SWScrollView(Context context) {
            super(context);
            setVerticalFadingEdgeEnabled(false);
            setHorizontalFadingEdgeEnabled(false);
            setVerticalScrollBarEnabled(false);
            setHorizontalScrollBarEnabled(false);
            setOverScrollMode(OVER_SCROLL_NEVER);
        }

        /**
         * orientation
         * 0 menas down
         * 1 means up
         */
        private boolean isOrientation(int orientation) {
            if (orientation == 0) {
                return isCanPullDown();
            } else if (orientation == 1) {
                return isCanPullUp();
            }
            return false;
        }

        private boolean isCanPullDown() {
            return !canScrollVertically(-1);
        }

        private boolean isCanPullUp() {
            return !canScrollVertically(1);
        }

    }
}
