package sw.coord;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017/5/23 0023.
 */
public class SWCoordinatorLayout extends CoordinatorLayout implements NestedScrollingChild {

    private NestedScrollingChildHelper childHelper;
    private float mTotalUnconsumed;
    private final int[] mParentOffsetInWindow = new int[2];

    public SWCoordinatorLayout(Context context) {
        this(context, null);
    }

    public SWCoordinatorLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SWCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        childHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return childHelper.startNestedScroll(axes);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return super.onStartNestedScroll(child, target, nestedScrollAxes);
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        super.onNestedScrollAccepted(child, target, nestedScrollAxes);
        mTotalUnconsumed = 0;
        startNestedScroll(nestedScrollAxes);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable @Size(value = 2) int[] consumed, @Nullable @Size(value = 2) int[] offsetInWindow) {
        return childHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (dy > 0 && mTotalUnconsumed > 0) {
            if (dy > mTotalUnconsumed) {
                consumed[1] = dy - (int) mTotalUnconsumed;
                mTotalUnconsumed = 0;
            } else {
                mTotalUnconsumed -= dy;
                consumed[1] = dy;
            }
            dispatchNestedPreScroll(dx, dy, consumed, null);
        }
        super.onNestedPreScroll(target, dx, dy, consumed);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, mParentOffsetInWindow);
        final int dy = dyUnconsumed + mParentOffsetInWindow[1];
        if (dy < 0) {
            mTotalUnconsumed += Math.abs(dy);
        }
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable @Size(value = 2) int[] offsetInWindow) {
        return childHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return dispatchNestedFling(velocityX, velocityY, super.onNestedFling(target, velocityX, velocityY, consumed));
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return childHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return childHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        if (dispatchNestedPreFling(velocityX, velocityY)) {
            return true;
        } else {
            return super.onNestedPreFling(target, velocityX, velocityY);
        }
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        childHelper.setNestedScrollingEnabled(enabled);
        super.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return super.isNestedScrollingEnabled();
    }

    @Override
    public void stopNestedScroll() {
        childHelper.stopNestedScroll();
    }

    @Override
    public void onStopNestedScroll(View target) {
        super.onStopNestedScroll(target);
        if (mTotalUnconsumed > 0) {
            mTotalUnconsumed = 0;
        }
        stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return childHelper.hasNestedScrollingParent();
    }

}
