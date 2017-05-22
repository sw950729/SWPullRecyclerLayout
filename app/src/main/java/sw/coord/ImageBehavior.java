package sw.coord;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import sw.angel.swpullrecyclerlayout.R;


/**
 * Created by Administrator on 2017/5/22 0022.
 */

public class ImageBehavior extends CoordinatorLayout.Behavior<ImageView>{

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
        Log.i("unconsumed",dxUnconsumed+":"+dyUnconsumed);
        if(child.getId() == R.id.top)
        {
            //up to down dyUnconsumed<0
            if(dyUnconsumed < 0)
            {
                float transY = Math.min(child.getTranslationY()-dyUnconsumed,child.getHeight()*FACTOR);
                child.setTranslationY(transY);
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
            }

        }
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, ImageView child, View target) {
        if(child.getTranslationY()!= 0) {
            if (child.getHeight() <= Math.abs(child.getTranslationY()*FACTOR)) {
                ObjectAnimator rotate = getRotateAnimator(child);
                rotate.start();
            } else {
                ObjectAnimator translate = getTranlateAnimator(child);
                translate.start();
            }
        }
        super.onStopNestedScroll(coordinatorLayout, child, target);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, ImageView child, View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
    }


    //get Translate animator for current to 0
    public ObjectAnimator getTranlateAnimator(View view){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view,View.TRANSLATION_Y,view.getTranslationY(),0);
        objectAnimator.setDuration(800);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        view.setTag(objectAnimator);
        return objectAnimator;
    }

    //get rotate animator indeterminate
    public ObjectAnimator getRotateAnimator(View view){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view,View.ROTATION,0,359);
        objectAnimator.setDuration(800);
        objectAnimator.setRepeatCount(-1);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        view.setTag(objectAnimator);
        return objectAnimator;
    }
}
