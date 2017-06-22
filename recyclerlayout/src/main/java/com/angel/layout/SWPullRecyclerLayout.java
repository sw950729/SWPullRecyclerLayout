package com.angel.layout;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.*;
import android.support.v7.widget.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import com.angel.interfaces.OnTouchUpListener;
import com.angel.utils.SWSlipeManager;

/**
 * Created by Angel on 2016/7/27.
 */
public class SWPullRecyclerLayout extends LinearLayout implements NestedScrollingParent, NestedScrollingChild {
        private final static long BOUNCE_TIME = 500;
    private Context context = null;
    private NestedScrollingParentHelper helper = null;
    private NestedScrollingChildHelper childHelper = null;
    private boolean isRefresh = true;
    private boolean isLoad = true;
    protected final int[] parentScrollConsumed = new int[2];
    private boolean isShow = true;
    //move total
    private int totalY = 0;
    private LinearLayout headerLayout = null;
    private MyRecyclerView myRecyclerView = null;
    private LinearLayout footerLayout = null;
    private OnTouchUpListener onTouchUpListener = null;
    private boolean isfling = false;
    private int headerHeight = 0;
    private int footerHeight = 0;

    public SWPullRecyclerLayout(Context context) {
        super(context);
        this.context = context;
        initial();
    }

    public SWPullRecyclerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initial();
    }

    private void initial() {
        helper = new NestedScrollingParentHelper(this);
        childHelper = new NestedScrollingChildHelper(this);
        headerLayout = new LinearLayout(context);
        myRecyclerView = new MyRecyclerView(context);
        footerLayout = new LinearLayout(context);
        setOrientation(VERTICAL);
        setNestedScrollingEnabled(true);
        headerLayout.setOrientation(VERTICAL);
        footerLayout.setOrientation(VERTICAL);
        addView(this.headerLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        addView(this.myRecyclerView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(this.footerLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    public void setMyRecyclerView(RecyclerView.LayoutManager layoutManager, RecyclerView.Adapter adapter) {
        myRecyclerView.setMyLayoutManager(layoutManager);
        myRecyclerView.setAdapter(adapter);
    }

    public void setMyRecyclerView(RecyclerView.LayoutManager layoutManager, RecyclerView.Adapter adapter, boolean fixed) {
        myRecyclerView.setMyLayoutManager(layoutManager);
        myRecyclerView.setAdapter(adapter);
        myRecyclerView.setHasFixedSize(fixed);
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
     * add footerview
     */
    public void addFooterView(View footerView, int footerHeight) {
        this.footerHeight = footerHeight;
        this.footerLayout.removeAllViews();
        this.footerLayout.addView(footerView);
        this.footerLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, footerHeight));
    }

    public void closeRefresh() {
        setIsScrollRefresh(false);
        setScrollTo(getTotal(), 0);
        SWSlipeManager.getInstance().close();
    }

    public void closeLoad() {
        setIsScrollLoad(false);
        setScrollTo(getTotal(), 0);
        SWSlipeManager.getInstance().close();
    }

    public void setScrollTo(int fromY, int toY) {
        smoothScrollTo((float) fromY, (float) toY);
    }

    public void setItemDivider(RecyclerView.ItemDecoration itemDecoration) {
        myRecyclerView.addItemDecoration(itemDecoration);
    }

    public int getTotal() {
        return -totalY / 2;
    }

    public void setIsScrollLoad(boolean isScrollLoad) {
        myRecyclerView.isScrollLoad = isScrollLoad;
    }

    public boolean isScrollLoad() {
        return myRecyclerView.isScrollLoad;
    }

    public void setIsScrollRefresh(boolean isScrollRefresh) {
        myRecyclerView.isScrollRefresh = isScrollRefresh;
    }

    public boolean isScrollRefresh() {
        return myRecyclerView.isScrollRefresh;
    }

    public void setShowHeaderAndFooter(boolean isShow) {
        this.isShow = isShow;
    }

    public void setRecyclerViewScrollToPosition(int position) {
        myRecyclerView.scrollToPosition(position);
    }

    public void addOnTouchUpListener(OnTouchUpListener onTouchUpListener) {
        this.onTouchUpListener = onTouchUpListener;
    }

    //child start nested scroll
    public boolean startNestedScroll(int axes) {
        return childHelper.startNestedScroll(axes);
    }

    //parent
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        stopAnimator();
        return isEnabled() && (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    //parent
    public void onNestedScrollAccepted(View child, View target, int axes) {
        helper.onNestedScrollAccepted(child, target, axes);
        startNestedScroll(axes & ViewCompat.SCROLL_AXIS_VERTICAL);
        //totalY = 0;
    }

    //child dispatch pre scroll
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return childHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    /**
     * parent
     * parent view intercept child view scroll
     */
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (totalY < 0 && myRecyclerView.isOrientation(0) && myRecyclerView.isFirstPosition()
                || totalY > 0 && myRecyclerView.isOrientation(1) && myRecyclerView.isLastPosition()) {
            isfling = true;
        }
        if(dispatchNestedPreScroll(dx , dy , consumed, null)){
//            dx -= consumed[0];
            dy -= consumed[1];
            return ;
        }
        if (isRefresh) {
            if (dy < 0 ) {
                if (myRecyclerView.isOrientation(0) ) {
                    totalY += dy;
                    if ((totalY / 2) <= 0) {
                        scrollTo(0, totalY / 2);
                        consumed[1] += dy;
                    }
                }
            }if(dy >0 && totalY < 0)
            {
                totalY += dy;
                if(totalY > 0)
                    totalY = 0;
                if ((totalY / 2) <= 0) {
                    scrollTo(0, totalY / 2);
                    consumed[1] += dy;
                }
                return ;
            }
        }
        if (isLoad) {
            if (dy > 0) {
                if (myRecyclerView.isOrientation(1) ) {
                    totalY += dy;
                    if ((totalY / 2) >= 0) {
                        scrollTo(0, totalY / 2);
                        consumed[1] += dy;
                    }
//                    else {
//                        scrollTo(0, 0);
//                        consumed[1] = 0;
//                    }
                }
            }if(dy < 0 && totalY > 0){
                totalY += dy;
                if(totalY < 0)
                    totalY = 0;
                if ((totalY / 2) >= 0) {
                    scrollTo(0, totalY / 2);
                    consumed[1] += dy;
                }
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
        dispatchNestedScroll(0, dyConsumed, 0, dyUnconsumed, null);
//        if (isShow) {
//            if (dyUnconsumed != 0) {
//                totalY += dyUnconsumed;
//                scrollTo(0, totalY / 2);
//            }
//        }
    }

    //child handle scroll
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return childHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    public void onStopNestedScroll(View child) {
        helper.onStopNestedScroll(child);
        if (onTouchUpListener != null) {
            isfling = false;
            if (getTotal() >= headerHeight && myRecyclerView.isFirstPosition()) {
                Log.i("-------HEADER", "onStopNestedScroll: " + getTotal());
                this.setScrollTo(this.getTotal(), headerHeight);
                if (!this.isScrollRefresh()) {
                    this.setIsScrollRefresh(true);
                    onTouchUpListener.OnRefreshing();
                }
            } else if (-getTotal() >= footerHeight && myRecyclerView.isLastPosition()) {
                Log.i("-------FOOTER", "onStopNestedScroll: " + getTotal());
                this.setScrollTo(this.getTotal(), -footerHeight);
                if (!this.isScrollLoad()) {
                    this.setIsScrollLoad(true);
                    onTouchUpListener.OnLoading();
                }
            } else {
                Log.i("-----", "onStopNestedScroll: " + getTotal());
                this.setScrollTo(getTotal(), 0);
            }
        }
        stopNestedScroll();
    }

    public void setNestedScrollingEnabled(boolean enabled) {
        childHelper.setNestedScrollingEnabled(enabled);
    }

    public boolean isNestedScrollingEnabled() {
        return childHelper.isNestedScrollingEnabled();
    }

    public void stopNestedScroll() {
        childHelper.stopNestedScroll();
    }

    public boolean hasNestedScrollingParent() {
        return childHelper.hasNestedScrollingParent();
    }

    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return childHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return childHelper.dispatchNestedPreFling(velocityX, velocityY);
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

    private ValueAnimator animator;
    private void smoothScrollTo(float fromY, float toY) {
        Log.i("smoothScrollTo",fromY +" to "+toY);
        animator = ValueAnimator.ofFloat(fromY, toY);
        if (fromY == toY) {
            animator.setDuration(0);
        } else {
            animator.setDuration(BOUNCE_TIME);
        }
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                int to = (int) (-(Float) animation.getAnimatedValue());
                scrollTo(0, to);
                totalY = to * 2;
            }
        });
        animator.start();
    }

    private void stopAnimator(){
        if(animator != null)
        {
            animator.cancel();
            animator = null;
        }
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (!enabled) {
            closeRefresh();
        }
    }

    private class MyRecyclerView extends RecyclerView {
        private StaggeredGridLayoutManager staggeredGridLayoutManager = null;
        private LinearLayoutManager linearLayoutManager = null;
        private GridLayoutManager gridLayoutManager = null;
        private boolean isScrollLoad = false;
        private boolean isScrollRefresh = false;

        public MyRecyclerView(Context context) {
            super(context);
            setVerticalFadingEdgeEnabled(false);
            setHorizontalFadingEdgeEnabled(false);
            setVerticalScrollBarEnabled(false);
            setHorizontalScrollBarEnabled(false);
            setOverScrollMode(OVER_SCROLL_NEVER);
            setItemAnimator(new DefaultItemAnimator());
        }

        private void setMyLayoutManager(LayoutManager layoutManager) {
            if (layoutManager instanceof StaggeredGridLayoutManager) {
                staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            } else if (layoutManager instanceof GridLayoutManager) {
                gridLayoutManager = (GridLayoutManager) layoutManager;
            } else if (layoutManager instanceof LinearLayoutManager) {
                linearLayoutManager = (LinearLayoutManager) layoutManager;
            }
            setLayoutManager(layoutManager);
            if (!isVertical()) {
                throw new NullPointerException("vertical!");
            }
        }

        public boolean isFirstPosition() {
            LayoutManager layoutManager = getLayoutManager();
            int firstVisibleItemPosition;
            if (layoutManager instanceof GridLayoutManager) {
                firstVisibleItemPosition = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(into);
                firstVisibleItemPosition = findMix(into);
            } else {
                firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            }
            return firstVisibleItemPosition == 0;
        }

        public boolean isLastPosition() {
            LayoutManager layoutManager = getLayoutManager();
            int lastVisibleItemPosition;
            if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                lastVisibleItemPosition = findMax(into);
            } else {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
            return lastVisibleItemPosition == layoutManager.getItemCount() - 1;
        }

        private int findMix(int[] firstPositions) {
            int min = firstPositions[0];
            for (int value : firstPositions) {
                if (value < min) {
                    min = value;
                }
            }
            return min;
        }

        private int findMax(int[] lastPositions) {
            int max = lastPositions[0];
            for (int value : lastPositions) {
                if (value > max) {
                    max = value;
                }
            }
            return max;
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

        private boolean isVertical() {
            if (staggeredGridLayoutManager != null) {
                return staggeredGridLayoutManager.getOrientation() == StaggeredGridLayoutManager.VERTICAL;
            } else if (linearLayoutManager != null) {
                return linearLayoutManager.getOrientation() == LinearLayoutManager.VERTICAL;
            } else if (gridLayoutManager != null) {
                return gridLayoutManager.getOrientation() == GridLayoutManager.VERTICAL;
            }
            return false;
        }
    }
}