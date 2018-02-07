package demo.tont.com.myapplication;

import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;


public class HoodleProgress extends View {
    private static final String TAG = HoodleProgress.class.getName();
    private int mDefaultColor = 0XFF000000;
    private Paint mPaint = null;
    private Cricle circle = null;
    private int mHoodleRadius = 15;//小弹珠的半径
    private double mHoodleAngle = -Math.PI / 2;
    private boolean startEd = false;
    private ValueAnimator valueAnimator;

    public HoodleProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mDefaultColor);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.i(TAG, "onSizeChanged: ");
        int measuredHeight = getMeasuredHeight();
        int measuredWidth = getMeasuredWidth();
        if (measuredHeight > 0 && measuredWidth > 0) {
            circle = new Cricle();
            int width = measuredWidth > measuredHeight ? measuredHeight : measuredWidth;
            circle.x = measuredWidth / 2;
            circle.y = measuredHeight / 2;
            circle.radius = width / 2 - mHoodleRadius;//大圆的半径
        }
        initAnimation();
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        Log.i(TAG, "onVisibilityChanged: " + visibility);
        if (visibility == View.VISIBLE) {
            startAnimation();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        drawCircle(canvas);

        mPaint.setStyle(Paint.Style.FILL);
        drawHoodle(canvas);
    }

    private void drawCircle(Canvas canvas) {
        RectF rectF = getShadowCircle();
        canvas.drawArc(rectF, -90, 360, false, mPaint);
    }

    private void drawHoodle(Canvas canvas) {
        RectF child = getHoodle();
        canvas.drawArc(child, 0, 360, false, mPaint);
    }

    public RectF getHoodle() {
        RectF rectF = new RectF();
        float xCenter = (float) (circle.x + Math.cos(mHoodleAngle) * circle.radius);
        float yCenter = (float) (circle.y + Math.sin(mHoodleAngle) * circle.radius);

        rectF.left = xCenter - mHoodleRadius;
        rectF.top = yCenter - mHoodleRadius;
        rectF.right = xCenter + mHoodleRadius;
        rectF.bottom = yCenter + mHoodleRadius;
        return rectF;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.i(TAG, "onDetachedFromWindow: ");
        stopAnimation();
    }
    
    class Cricle {
        int x;
        int y;
        int radius;
    }

    public RectF getShadowCircle() {
        int left = 0;
        int top = 0;
        int right = 0;
        int bottom = 0;
        if (circle != null) {
            left = circle.x - circle.radius;
            right = circle.x + circle.radius;
            top = circle.y - circle.radius;
            bottom = circle.y + circle.radius;
        }
        return new RectF(left, top, right, bottom);
    }

    private void initAnimation() {
        if (!startEd) {
            startEd = true;
            valueAnimator = ValueAnimator.ofObject(new FloatEvaluator(), -Math.PI / 2, Math.PI * 3 / 2);
            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            valueAnimator.setRepeatCount(10000);
            valueAnimator.setDuration(1500);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mHoodleAngle = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            startAnimation();
        }
    }

    private void startAnimation() {
        if (valueAnimator != null && !valueAnimator.isRunning()) {
            valueAnimator.start();
        }
    }

    private void stopAnimation() {
        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.cancel();
            valueAnimator.end();
        }
    }
}




