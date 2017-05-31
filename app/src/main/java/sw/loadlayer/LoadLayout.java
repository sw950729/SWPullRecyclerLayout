package sw.loadlayer;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

/**
 * Created by QZhu on 16-7-22.
 */
public class LoadLayout extends FrameLayout implements NestedScrollingParent,NestedScrollingChild{
    public final static int BounceTime = 500;
    private final static float HeightFactor = 1.5f;
    private RecyclerView mTarget = null;
    private int offSet_Y = 0;
    private boolean refreshEnabled = false;
    private boolean loadEnabled = false;

    private boolean useCoverMode = true;

    /**
     * 是否位于Recyler上方作为头部
     * 否则被Recyler覆盖
     */
    private static boolean LoadUnderFrame = false;
    /**
     * 是否位于Recyler下方作为底部
     * 否则被Recyler覆盖
     */
    private static boolean RefreshUnderFrame = false;
    public final static String LEFT = "left";
    public final static String RIGHT = "right";
    public final static String TOP = "header";
    public final static String BOTTOM = "footer";
    public final static String CENTER = "center";

    private View sleft,sright,sheader,sfooter,scenter;

    public boolean isRefreshEnabled() {
        return refreshEnabled;
    }

    public void setRefreshEnabled(boolean refreshEnabled) {
        if(sheader != null)
            this.refreshEnabled = refreshEnabled;
    }

    public boolean isLoadEnabled() {
        return loadEnabled;
    }

    public void setLoadEnabled(boolean loadEnabled) {
        if(sfooter != null)
            this.loadEnabled = loadEnabled;
    }

    public void setListEnabled(boolean listEnabled){
        setRefreshEnabled(listEnabled);
        setLoadEnabled(listEnabled);
    }



    private final NestedScrollingParentHelper mNestedScrollingParentHelper;
    private final NestedScrollingChildHelper childHelper;

    public LoadLayout(Context context) {
        this(context,null);
    }

    public LoadLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        childHelper = new NestedScrollingChildHelper(this);
//        width = getResources().getDisplayMetrics().widthPixels;
//        height = getResources().getDisplayMetrics().heightPixels;
        addChild();
    }

    private void addChild(){
        enableRecycleView();
    }

    private void enableRecycleView(){
        this.mTarget = new RecyclerView(getContext());
        this.mTarget.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(this.mTarget, 0,new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return childHelper.startNestedScroll(axes);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        if(objectAnimator!=null && objectAnimator.isStarted())
        {
            objectAnimator.cancel();
            objectAnimator.removeAllListeners();
            objectAnimator = null;
        }
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        // Reset the counter of how much leftover scroll needs to be consumed.
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
        startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
        // Dispatch up to the nested parent
        //startNestedScroll(nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL);
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable @Size(value = 2) int[] offsetInWindow) {
        return childHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
                offsetInWindow);
    }

    //dyUnconsumed < 0 means finger move to down else up
    //dyUnconsumed < 0
    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if(refreshEnabled)
        {
            if(dyUnconsumed < 0)
            {
                offSet_Y -= dyUnconsumed;
                offSet_Y = changeOffsetY(offSet_Y);
                if(!RefreshUnderFrame)
                {
                    moveView(mTarget,sheader,Math.abs(offSet_Y));
                }
                notifyRefreshProcess(offSet_Y);
            }
        }
        if(loadEnabled)
        {
            if(dyUnconsumed > 0)
            {
                offSet_Y -= dyUnconsumed;
                offSet_Y = changeOffsetY(offSet_Y);
                if(!LoadUnderFrame)
                {
                    moveView(mTarget,sfooter,-Math.abs(offSet_Y));
                }
                notifyLoadProcess(offSet_Y);
            }
        }
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,null);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable @Size(value = 2) int[] consumed, @Nullable @Size(value = 2) int[] offsetInWindow) {
        return childHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    //dy < 0 means finger move to down else up
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (refreshEnabled) {
            //up movement
            if (dy > 0) {
                if (offSet_Y > 0) {
                    if (offSet_Y > dy) {
                        offSet_Y -= dy;
                        consumed[1] = dy;
                    } else {
                        consumed[1] = offSet_Y;
                        offSet_Y = 0;
                    }
                    if(!RefreshUnderFrame){
                        moveView(mTarget,sheader,Math.abs(offSet_Y));
                    }
                }
            }
        }
        if (loadEnabled) {
            if (dy < 0) {
                    if (offSet_Y < 0) {
                        if (offSet_Y < dy) {
                            offSet_Y -= dy;
                            consumed[1] = dy;
                        } else {
                            consumed[1] = -offSet_Y;
                            offSet_Y = 0;
                        }
                        if(!LoadUnderFrame)
                        {
                            moveView(mTarget,sfooter,-Math.abs(offSet_Y));
                        }
                }
            }
        }
        dispatchNestedPreScroll(dx,dy,consumed,null);
    }

    private void moveView(View target,View prefix,float translates){
        if(!useCoverMode)
        {
            target.setTranslationY(translates);
        }
        prefix.setTranslationY(translates);
    }

    private int changeOffsetY(int disy){
        float maxheaderHeight = headerHeight * HeightFactor;
        float maxfooterHeight = footerHeight * HeightFactor;
        if(disy > maxheaderHeight)
        {
            return (int)maxheaderHeight;
        }else if(-disy > maxfooterHeight){
            return (int) -maxfooterHeight;
        }
        return disy;
    }
    ValueAnimator objectAnimator = null;

    @Override
    public void onStopNestedScroll(View target) {
        mNestedScrollingParentHelper.onStopNestedScroll(target);
        float refreshHeight = headerHeight ;
        float loadingHeight = footerHeight ;
        if((offSet_Y > 0 && offSet_Y < refreshHeight) || (offSet_Y < 0 && -offSet_Y< loadingHeight))
        {
            startAnim();
        }
        if(offSet_Y > refreshHeight && precessChangeListener != null)
        {
            precessChangeListener.onRefresh(sheader);
        }
        if(-offSet_Y > loadingHeight && precessChangeListener != null)
        {
            precessChangeListener.onLoad(sfooter);
        }
    }

    private void startAnim(){
        objectAnimator = ValueAnimator.ofFloat(offSet_Y,0).setDuration(BounceTime);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                offSet_Y = (int) value;
                //mTarget.setY(RAWY + offSet_Y);
                if(offSet_Y > 0)
                {
                    if(refreshEnabled)
                    {
                        if(!RefreshUnderFrame)
                        {
                            moveView(mTarget,sheader,offSet_Y);
                        }
                    }
                }else {
                    if(loadEnabled) {
                        if(!LoadUnderFrame)
                        {
                            moveView(mTarget,sfooter,offSet_Y);
                        }
                    }
                }
            }
        });
        objectAnimator.start();
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return dispatchNestedFling(velocityX,velocityY,super.onNestedFling(target, velocityX, velocityY, consumed));
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
        if(dispatchNestedPreFling(velocityX,velocityY)){
            return true;
        }else{
            return super.onNestedPreFling(target, velocityX, velocityY);
        }
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return childHelper.hasNestedScrollingParent();
    }

    @Override
    public int getNestedScrollAxes() {
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //if recycler == null then load from child
        //else it will add automatic
        if (mTarget == null) {
            ensureTarget();
        }
        if (mTarget == null) {
            return;
        }
        initialChild();
        mTarget.measure(MeasureSpec.makeMeasureSpec(
                getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
                MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(
                getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY));
    }

    /**
     * query the recycleView
     */
    private void ensureTarget() {
        // Don't bother getting the parent height if the parent hasn't been laid
        // out yet.
        if (mTarget == null) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child instanceof RecyclerView) {
                    mTarget = (RecyclerView) child;
                    break;
                }
            }
        }
    }

    /**
     * achieve child footer/header
     */
    private void initialChild(){
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if(child.getContentDescription() == null)
            {
                continue;
            }
            String contentDes = child.getContentDescription().toString();

            if(contentDes.equals(LEFT)) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                lp.gravity = Gravity.START|Gravity.CENTER_HORIZONTAL;
                lp.rightMargin = child.getMeasuredWidth();
                child.setLayoutParams(lp);
                sleft = child;
            }
            if(contentDes.equals(RIGHT)){
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                lp.gravity = Gravity.END|Gravity.CENTER_HORIZONTAL;
                lp.leftMargin = child.getMeasuredWidth();
                child.setLayoutParams(lp);
                sright = child;
            }
            if(contentDes.equals(TOP)){
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                lp.gravity = Gravity.TOP|Gravity.CENTER_HORIZONTAL;
                lp.topMargin = -child.getMeasuredHeight();
                child.setLayoutParams(lp);
                sheader = child;
            }
            if(contentDes.equals(BOTTOM)){
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                lp.gravity = Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL;
                lp.bottomMargin = -child.getMeasuredHeight();
                child.setLayoutParams(lp);
                sfooter = child;
            }
            if(contentDes.equals(CENTER)){
                LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                lp.gravity = Gravity.CENTER;
                child.setLayoutParams(lp);
                scenter = child;
            }
        }
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(sheader != null)
        {
            setHeaderHeight(sheader.getHeight());
            setRefreshEnabled(true);
        }
        if(sfooter != null)
        {
            setFooterHeight(sfooter.getHeight());
            setLoadEnabled(true);
        }
    }

    private int headerHeight = 100;
    private int footerHeight = 100;

    private void setHeaderHeight(int headerHeight) {
        this.headerHeight = headerHeight;
//        Log.d("header height = ",String.valueOf(headerHeight));
    }

    private void setFooterHeight(int footerHeight) {
        this.footerHeight = footerHeight;
//        Log.d("footer height = ",String.valueOf(footerHeight));
    }

    /**
     * this interface is to notify
     */
    public interface onPrecessChangeListener{
        /**
         * load status
         * @param footer footer view
         * @param process value -100~0
         */
        void onLoadProcessChange(View footer ,int process);

        /**
         * refresh status
         * @param header header view
         * @param process value 0~100
         */
        void onRefreshProcessChange(View header ,int process);

        void onLoad(View footer);

        void onRefresh(View header);
    }

    onPrecessChangeListener precessChangeListener;

    public void setPrecessChangeListener(onPrecessChangeListener precessChangeListener) {
        this.precessChangeListener = precessChangeListener;
    }

    private void notifyRefreshProcess(int offsety){
        if(precessChangeListener != null)
        {
            int percent = (int) ((offsety*1.0f/headerHeight)*100);
            precessChangeListener.onRefreshProcessChange(sheader,percent > 100 ? 100:percent);
        }
    }

    private void notifyLoadProcess(int offsety){
        if(precessChangeListener != null)
        {
            int percent = (int) ((offsety*1.0f/footerHeight)*100);
            precessChangeListener.onLoadProcessChange(sfooter,percent > 100 ? 100:percent);
        }

    }

    /**
     * get the recycleView in this view group
     * @return
     */
    public RecyclerView getRecycleView(){
        if (mTarget == null) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child instanceof RecyclerView) {
                    return (RecyclerView) child;
                }
            }
        }
        return mTarget;
    }

    public boolean setHeadView(View view,LoadLayout.LayoutParams layoutParams){
        if(view== null)
        {
            return false;
        }else{
            if(layoutParams == null)
                layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.TOP;
            sheader = view;
            view.setContentDescription(TOP);
            addView(this.sheader, layoutParams);
            refreshEnabled = true;
        }
        return true;
    }

    public boolean setFooterView(View view,LoadLayout.LayoutParams layoutParams){
        if(view==null)
        {
            return false;
        }else{
            if(layoutParams == null)
                layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.BOTTOM;
            sfooter = view;
            view.setContentDescription(BOTTOM);
            addView(sfooter, layoutParams);
            loadEnabled = true;
        }
        return true;
    }
}
