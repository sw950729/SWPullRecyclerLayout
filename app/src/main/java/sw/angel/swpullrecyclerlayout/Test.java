package sw.angel.swpullrecyclerlayout;

import android.support.v4.view.NestedScrollingChild;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Administrator on 2017/5/20 0020.
 */

public class Test extends AppCompatActivity implements NestedScrollingChild{
    @Override
    public void setNestedScrollingEnabled(boolean enabled) {

    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return false;
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return false;
    }

    @Override
    public void stopNestedScroll() {

    }

    @Override
    public boolean hasNestedScrollingParent() {
        return false;
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return false;
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return false;
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return false;
    }
}
