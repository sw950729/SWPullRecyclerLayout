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
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import sw.interf.MeasureSpaceCallBack;


/**
 * Created by Administrator on 2017/5/24 0024.
 */
public class LoadingProcess extends View {
    private int circleWidth = 10;
    private final int swipeValue = 130;

    private int startValue = -90;
    private int endValue = startValue + startValue;
    private final static int circleRadius = 300;

    private int mswipeAngel = 60;
    private int mcurrentStartValue = -90;
    private final static int duration = 1000;
    private RectF rect = null;
    private Paint mPaint;

    private int[] COLORS = new int[]{Color.RED,Color.BLACK,Color.BLUE,Color.GREEN};

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
            int radius = (center-circleWidth/2); //圆环的半径
            rect= new RectF(center - radius, center - radius, center + radius, center + radius);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(rect != null && mswipeAngel != 0)
            canvas.drawArc(rect, mcurrentStartValue, mswipeAngel, false, mPaint);
    }

    public void setProgress(int value){
        mswipeAngel=value;
        invalidate();
    }

    private int getViewWitdh(){
        return 200;
    }

    private int getViewHeight(){
        return 200;
    }

    public void startAccAnim(){
        initial();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0,swipeValue);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setEvaluator(addEvaluator);
        valueAnimator.setDuration(duration);
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
        ValueAnimator valueAnimator = ValueAnimator.ofInt(swipeValue,0);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setEvaluator(reduceEvaluator);
        valueAnimator.setDuration(duration);
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setOnce(mcurrentStartValue);
                startAccAnim();
                super.onAnimationEnd(animation);
            }
        });
        valueAnimator.start();
    }

    private TypeEvaluator addEvaluator = new TypeEvaluator<Integer>() {
        @Override
        public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
            int currentValue = (int) ((endValue-startValue)*fraction);
            setProgress(currentValue);
            return currentValue;
        }
    };

    private TypeEvaluator reduceEvaluator = new TypeEvaluator<Integer>() {
        @Override
        public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
            int currentValue = (int) ((startValue-endValue)*(1-fraction));
            mcurrentStartValue = (int) (fraction * circleRadius) + LoadingProcess.this.startValue;
            setProgress(currentValue);
            return currentValue;
        }
    };

    private void setOnce(int mcurrentStartValue){
        startValue = mcurrentStartValue;
        endValue = startValue + swipeValue;
        mPaint.setColor(COLORS[(int)(Math.random()*4)]);
    }

    private void initial(){
        mcurrentStartValue =startValue;
    }
}
