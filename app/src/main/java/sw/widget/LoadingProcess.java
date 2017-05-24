package sw.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import sw.interf.MeasureSpaceCallBack;


/**
 * Created by Administrator on 2017/5/24 0024.
 */
public class LoadingProcess extends View {
    private int circleWidth = 10;
    private int mcurrentEndValue = 90;
    private int mswipeAngel = 60;
    private int mcurrentStartValue = -90;
    private final static int circleRadius = 360;
    private RectF rect = null;
    private Paint mPaint;

    public LoadingProcess(Context context) {
        this(context,null);
    }

    public LoadingProcess(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingProcess(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialStyle(context,attrs,defStyleAttr,0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LoadingProcess(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialStyle(context,attrs,defStyleAttr,defStyleRes);
    }

    private void initialStyle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
//        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.customView,defStyleAttr,defStyleRes);
//        int count = a.getIndexCount();
//
//        for(int i=0;i<count;i++){
//            int attr = a.getIndex(i);
//            switch(attr){
//                case R.styleable.customView_customTitle:
//                    break;
//                case R.styleable.customView_customBackIcon:
//                    break;
//                case R.styleable.customView_customSubTitle:
//                    break;
//                case R.styleable.customView_customBackGround:
//                    break;
//            }
//        }
//        a.recycle();
        mPaint = new Paint();
        mPaint.setStrokeWidth(circleWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLUE);
    }

    private MeasureSpaceCallBack spaceCallBack = new MeasureSpaceCallBack() {
        @Override
        public int getContentWitdh() {
            return getViewWitdh();
        }

        @Override
        public int getContentHeight() {
            return getViewHeight();
        }
    };

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int[] result = MeasureSpaceCallBack.measureSize(widthMeasureSpec,heightMeasureSpec,spaceCallBack);
        setMeasuredDimension(result[0],result[1]);
        if(rect == null)
        {
            int center = getMeasuredWidth()/2; //获取圆心的x坐标
            int radius = (int) (center-circleWidth/2); //圆环的半径
            rect= new RectF(center - radius, center - radius, center + radius, center + radius);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(rect != null)
            canvas.drawArc(rect, mcurrentStartValue, mswipeAngel, false, mPaint);
//        //宽度使用-,高度使用+,向下为正数
//        canvas.drawText(customTitle,(getWidth()-textBounds.width())/2,(getHeight()+textBounds.height())/2,mPaint);
    }

    public void setProgress(int value,boolean add){
        if(add)
        {
            mswipeAngel=value;
        }else{
            mcurrentStartValue=-90+value;
            mswipeAngel=circleRadius-value;
            Log.i("setProcess","value = "+mcurrentStartValue+" angel ="+mswipeAngel);
        }
        invalidate();
    }

    private int getViewWitdh(){
        return 200;
    }

    private int getViewHeight(){
        return 200;
    }

    public void startAccAnim(){
        ValueAnimator valueAnimator = ValueAnimator.ofInt(mcurrentStartValue,mcurrentStartValue+circleRadius);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setEvaluator(addEvaluator);
        valueAnimator.setDuration(3000);
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                startDecAnim();
                super.onAnimationEnd(animation);
            }
        });
        valueAnimator.start();
    }

    private void startDecAnim(){
        ValueAnimator valueAnimator = ValueAnimator.ofInt(-90,270);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setEvaluator(reduceEvaluator);
        valueAnimator.setDuration(3000);
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                startAccAnim();
                super.onAnimationEnd(animation);
            }
        });
        valueAnimator.start();
    }

    private TypeEvaluator addEvaluator = new TypeEvaluator<Integer>() {
        @Override
        public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
            int currentValue = (int)(360*fraction);
            setProgress(currentValue,true);
            return currentValue;
        }
    };

    private TypeEvaluator reduceEvaluator = new TypeEvaluator<Integer>() {
        @Override
        public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
            int currentValue = (int)(360*fraction);
            setProgress(currentValue,false);
            return currentValue;
        }
    };
}
