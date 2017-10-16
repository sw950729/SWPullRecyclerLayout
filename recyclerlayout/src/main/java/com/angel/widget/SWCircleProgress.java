package com.angel.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Property;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import sw.angel.recyclerlayout.R;

public class SWCircleProgress extends View {

    /**
     * 绘制圆弧起始位置角度的动画，这样该圆弧是打圈转的动画
     */
    private static final Interpolator ANGLE_INTERPOLATOR = new LinearInterpolator();
    /**
     * 绘制圆弧臂长的动画，该动画受 mModeAppearing 控制，
     * 当 mModeAppearing 为 false 的时候，圆弧的起始点在增加，圆弧的终止点不变，弧长在逐渐减少；
     * 当 mModeAppearing 为 true 的时候， 圆弧的起始点不变，圆弧的终止点变大，弧长在逐渐增加
     */
    private static final Interpolator SWEEP_INTERPOLATOR = new AccelerateDecelerateInterpolator();
    /**
     * 圆弧起始位置动画的间隔，也就是多少毫秒圆弧转一圈，可以把该值扩大10倍来查看动画的慢动作
     */
    private static final int ANGLE_ANIMATOR_DURATION = 2000;
    /**
     * 圆弧臂长的动画间隔，也就是臂长从最小到最大值的变化时间，也可以把该值扩大10倍来查看动画的慢动作
     */
    private static final int SWEEP_ANIMATOR_DURATION = 900;
    /**
     * 圆弧的最下臂长是多少度
     */
    private static final int MIN_SWEEP_ANGLE = 30;
    private static final int DEFAULT_BORDER_WIDTH = 3;
    private final RectF bounds = new RectF();
    /**
     * 起始位置的动画对象
     */
    private ObjectAnimator animatorSweep;
    /**
     * 臂长的动画对象
     */
    private ObjectAnimator animatorAngle;
    /**
     * 控制臂长是逐渐增加还是逐渐减少
     */
    private boolean mModeAppearing = true;
    private Paint paint;
    /**
     * 每次臂长增加 、减少 转换的时候， 圆弧起始位置的偏移量会增加 2 倍的最小臂长
     */
    private float currentGlobalAngleOffset;
    private float currentGlobalAngle;
    private float currentSweepAngle;
    private float borderWidth;
    private boolean isRunning;
    private int[] mColors;
    private int currentColorIndex;
    private int nextColorIndex;
    private int width;
    private int height;
    private boolean isLoad = false;

    public SWCircleProgress(Context context) {
        this(context, null);
    }

    public SWCircleProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SWCircleProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        float density = context.getResources().getDisplayMetrics().density;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SWCircleProgress, defStyleAttr, 0);
        borderWidth = a.getDimension(R.styleable.SWCircleProgress_borderWidth,
                DEFAULT_BORDER_WIDTH * density);
        a.recycle();
        mColors = new int[4];
        mColors[0] = context.getResources().getColor(R.color.red);
        mColors[1] = context.getResources().getColor(R.color.yellow);
        mColors[2] = context.getResources().getColor(R.color.green);
        mColors[3] = context.getResources().getColor(R.color.blue);
        currentColorIndex = 0;
        nextColorIndex = 1;

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Cap.ROUND);
        paint.setStrokeWidth(borderWidth);
        paint.setColor(mColors[currentColorIndex]);

        setupAnimations();
    }

    public void setLoad(boolean isLoad) {
        this.isLoad = isLoad;
        if (!isLoad) {
            stop();
        } else {
            start();
        }
    }

    private void start() {
        if (isRunning) {
            return;
        }
        isRunning = true;
        animatorAngle.start();
        animatorSweep.start();
        invalidate();
    }

    private void stop() {
        if (!isRunning) {
            return;
        }
        isRunning = false;
        animatorAngle.cancel();
        animatorSweep.cancel();
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bounds.left = width / 2 - height / 2;
        bounds.right = width / 2 + height / 2;
        bounds.top = borderWidth / 2f;
        bounds.bottom = height - borderWidth / 2f;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float startAngle = currentGlobalAngle - currentGlobalAngleOffset;
        float sweepAngle = currentSweepAngle;
        if (mModeAppearing) {
            paint.setColor(gradient(mColors[currentColorIndex], mColors[nextColorIndex],
                    currentSweepAngle / (360 - MIN_SWEEP_ANGLE * 2)));
            sweepAngle += MIN_SWEEP_ANGLE;
        } else {
            startAngle = startAngle + sweepAngle;
            sweepAngle = 360 - sweepAngle - MIN_SWEEP_ANGLE;
        }
        canvas.drawArc(bounds, startAngle, sweepAngle, false, paint);
    }

    private static int gradient(int color1, int color2, float p) {
        int r1 = (color1 & 0xff0000) >> 16;
        int g1 = (color1 & 0xff00) >> 8;
        int b1 = color1 & 0xff;
        int r2 = (color2 & 0xff0000) >> 16;
        int g2 = (color2 & 0xff00) >> 8;
        int b2 = color2 & 0xff;
        int newr = (int) (r2 * p + r1 * (1 - p));
        int newg = (int) (g2 * p + g1 * (1 - p));
        int newb = (int) (b2 * p + b1 * (1 - p));
        return Color.argb(255, newr, newg, newb);
    }

    private void toggleAppearingMode() {
        mModeAppearing = !mModeAppearing;
        if (mModeAppearing) {
            currentColorIndex = ++currentColorIndex % 4;
            nextColorIndex = ++nextColorIndex % 4;
            currentGlobalAngleOffset = (currentGlobalAngleOffset + MIN_SWEEP_ANGLE * 2) % 360;
        }
    }

    private Property<SWCircleProgress, Float> mAngleProperty = new Property<SWCircleProgress, Float>(Float.class, "angle") {
        @Override
        public Float get(SWCircleProgress object) {
            return object.getCurrentGlobalAngle();
        }

        @Override
        public void set(SWCircleProgress object, Float value) {
            object.setCurrentGlobalAngle(value);
        }
    };

    private Property<SWCircleProgress, Float> mSweepProperty = new Property<SWCircleProgress, Float>(Float.class, "arc") {
        @Override
        public Float get(SWCircleProgress object) {
            return object.getCurrentSweepAngle();
        }

        @Override
        public void set(SWCircleProgress object, Float value) {
            object.setCurrentSweepAngle(value);
        }
    };

    private void setupAnimations() {
        animatorAngle = ObjectAnimator.ofFloat(this, mAngleProperty, 360f);
        animatorAngle.setInterpolator(ANGLE_INTERPOLATOR);
        animatorAngle.setDuration(ANGLE_ANIMATOR_DURATION);
        animatorAngle.setRepeatMode(ValueAnimator.RESTART);
        animatorAngle.setRepeatCount(ValueAnimator.INFINITE);

        animatorSweep = ObjectAnimator.ofFloat(this, mSweepProperty, 360f - MIN_SWEEP_ANGLE * 2);
        animatorSweep.setInterpolator(SWEEP_INTERPOLATOR);
        animatorSweep.setDuration(SWEEP_ANIMATOR_DURATION);
        animatorSweep.setRepeatMode(ValueAnimator.RESTART);
        animatorSweep.setRepeatCount(ValueAnimator.INFINITE);
        animatorSweep.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                toggleAppearingMode();
            }
        });
    }

    public void setCurrentGlobalAngle(float currentGlobalAngle) {
        this.currentGlobalAngle = currentGlobalAngle;
        invalidate();
    }

    public float getCurrentGlobalAngle() {
        return currentGlobalAngle;
    }

    public void setCurrentSweepAngle(float currentSweepAngle) {
        this.currentSweepAngle = currentSweepAngle;
        invalidate();
    }

    public float getCurrentSweepAngle() {
        return currentSweepAngle;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = widthMeasureSpec - MeasureSpec.getMode(widthMeasureSpec);
        height = heightMeasureSpec - MeasureSpec.getMode(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }
}