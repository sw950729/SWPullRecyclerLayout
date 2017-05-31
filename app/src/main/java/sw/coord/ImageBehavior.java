package sw.coord;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import sw.angel.swpullrecyclerlayout.R;
import sw.interf.LoadListener;


/**
 * Created by Administrator on 2017/5/22 0022.
 */

public class ImageBehavior extends CoordinatorLayout.Behavior<ImageView>{
    private LoadListener listener;

    public void setListener(LoadListener listener) {
        this.listener = listener;
    }

    private final static float FACTOR = 1.5f;

    public ImageBehavior() {
    }

    public ImageBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, ImageView child, View dependency) {
        return dependency instanceof RecyclerView;
    }

    //do nothing size and position not changed
//    @Override
//    public boolean onDependentViewChanged(CoordinatorLayout parent, ImageView child, View dependency) {
//        return super.onDependentViewChanged(parent, child, dependency);
//    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, ImageView child, View directTargetChild, View target, int nestedScrollAxes) {
        return target instanceof RecyclerView;
    }

    @Override
    public void onNestedScrollAccepted(CoordinatorLayout coordinatorLayout, ImageView child, View directTargetChild, View target, int nestedScrollAxes) {
        super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, ImageView child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        float maxValue = child.getHeight()*FACTOR;
        Log.i("dyConsumed",String.valueOf(dyConsumed));
        if(child.getId() == R.id.top)
        {
            //up to down dyUnconsumed<0
            if(dyUnconsumed < 0)
            {
                float transY = Math.min(child.getTranslationY()-dyUnconsumed,maxValue);
                child.setTranslationY(transY);
                if(listener != null)
                    listener.onRefreshProcess(Math.abs(transY)/maxValue,this);
            }
            return ;
        }

        if(child.getId() == R.id.bottom)
        {
            //up to down dyUnconsumed<0
            if(dyUnconsumed > 0)
            {
                float transY = Math.max(child.getTranslationY()-dyUnconsumed,-child.getHeight()*FACTOR);
                child.setTranslationY(transY);
                if(listener != null)
                    listener.onLoadingProcess(Math.abs(transY)/maxValue,this);
            }

        }
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, ImageView child, View target) {
        if(child.getTranslationY()!= 0) {
            float current = Math.abs(child.getTranslationY());
            float maxed = child.getHeight()*FACTOR;
            if (maxed<= current) {
                if(child.getId() == R.id.top)
                {
                    if(listener != null)
                        listener.onRefresh(this,child);
                }else if(child.getId() == R.id.bottom){
                    if(listener != null)
                        listener.onLoading(this,child);
                }
                ObjectAnimator rotate = getRotateAnimator(child);
                rotate.start();

                //start loading animator
            } else {
                ObjectAnimator translate = getTranlateAnimator(child);
                translate.start();
            }
        }
        super.onStopNestedScroll(coordinatorLayout, child, target);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, ImageView child, View target, int dx, int dy, int[] consumed) {
        Log.i("dyPreConsumed",String.valueOf(consumed[1]));
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        Log.i("dyPreConsumed1",String.valueOf(consumed[1]));
    }


    //get Translate animator for current to 0
    public ObjectAnimator getTranlateAnimator(View view){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view,View.TRANSLATION_Y,view.getTranslationY(),0);
        objectAnimator.setDuration(800);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        view.setTag(objectAnimator);
        return objectAnimator;
    }

    private void reFresh() {

    }
    //get rotate animator indeterminate
    public ObjectAnimator getRotateAnimator(View view){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view,View.ROTATION,0,360);
        objectAnimator.setDuration(800);
        objectAnimator.setRepeatCount(-1);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        view.setTag(objectAnimator);
        return objectAnimator;
    }

    public void onComplete(View view){
        try {
            ObjectAnimator objectAnimator = (ObjectAnimator) view.getTag();
            if (objectAnimator.isRunning()) {
                objectAnimator.cancel();
            }
        }catch (Exception e)
        {
            view.clearAnimation();
        }
        view.setTranslationY(0f);
    }

    /**
     * A utility function to get the {@link ImageBehavior} associated with the {@code view}.
     *
     * @param view The {@link View} with {@link ImageBehavior}.
     * @return The {@link ImageBehavior} associated with the {@code view}.
     */
    @SuppressWarnings("unchecked")
    public static ImageBehavior from(ImageView view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (!(params instanceof CoordinatorLayout.LayoutParams)) {
            throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
        }
        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) params)
                .getBehavior();
        if (!(behavior instanceof ImageBehavior)) {
            throw new IllegalArgumentException(
                    "The view is not associated with BottomSheetBehavior");
        }
        return (ImageBehavior) behavior;
    }

}
