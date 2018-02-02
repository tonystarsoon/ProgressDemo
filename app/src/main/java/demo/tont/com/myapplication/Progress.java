package demo.tont.com.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * 仿华为天气进度圈
 */
public class Progress extends View {
    private static final String TAG = Progress.class.getName();
    private int mDefaultBgColor = 0X00ffffff;
    private int mDefaultRingColor = 0X44FFFFFF;
    private int mDefaultProgressColor = 0XFFFFFFFF;
    private int mDefaultStrokeWidth = 14;
    private int mAxValue = 100;
    private Paint mPaint;
    private int mProgress = 0;
    private boolean start = false;
    private float mFooterTextSize = 12.0f;
    private float mCenterTextSize = 20.0f;
    private RectF recrF;
    private int progressType;

    public Progress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Progress);
        mDefaultStrokeWidth = (int) typedArray.getDimension(R.styleable.Progress_ringStroke, 14);
        mDefaultBgColor = typedArray.getColor(R.styleable.Progress_backgroundColor, 0X00ffffff);
        mDefaultRingColor = typedArray.getColor(R.styleable.Progress_ringBackground, 0X44FFFFFF);
        mDefaultProgressColor = typedArray.getColor(R.styleable.Progress_progressColor, 0XFFFFFFFF);
        mAxValue = typedArray.getInteger(R.styleable.Progress_maxValue, 100);
        mFooterTextSize = typedArray.getDimension(R.styleable.Progress_footerTextSize, 12.0f);
        mCenterTextSize = typedArray.getDimension(R.styleable.Progress_centerTextSize, 20.0f);
        progressType = typedArray.getInt(R.styleable.Progress_progressType, 0);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mDefaultStrokeWidth);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        recrF = getRecrF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        drawBg(canvas);//画背景
        drawCircle(canvas);//画总进度
        drawProgress(canvas);//画实际进度
        drawStartAndEndValue(canvas);//绘制开始和结束的指标值
        drawContentType(canvas);
    }

    private void drawBg(Canvas canvas) {
        canvas.drawColor(mDefaultBgColor);
    }

    private void drawCircle(Canvas canvas) {
        mPaint.setColor(mDefaultRingColor);
        mPaint.setStrokeWidth(mDefaultStrokeWidth);
        canvas.drawArc(recrF, -225, 270, false, mPaint);
    }

    private void drawProgress(Canvas canvas) {
        mPaint.setColor(mDefaultProgressColor);
        mPaint.setStrokeWidth(mDefaultStrokeWidth);
        canvas.drawArc(recrF, -225, 270 * mProgress / mAxValue, false, mPaint);
    }

    private void drawStartAndEndValue(Canvas canvas) {
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(mFooterTextSize);
        Log.i(TAG, "drawStartAndEndValue: ----mFooterTextSize: " + mFooterTextSize);
        String start = "0";
        String end = mAxValue + "";

        Rect rect = new Rect();
        mPaint.getTextBounds(start, 0, 1, rect);
        int padding = (int) (rect.height() * 0.5);

        canvas.drawText(start, padding, getMeasuredHeight() - padding, mPaint);
        mPaint.getTextBounds(end, 0, end.length(), rect);
        canvas.drawText(end, getMeasuredWidth() - padding - rect.width(), getMeasuredHeight() - padding, mPaint);
    }

    //绘制中间部分的内容
    private void drawContentType(Canvas canvas) {
        String text = null;
        switch (progressType) {
            case 0:
                text = 100 * mProgress / mAxValue + "%";
                break;
            case 1:
                text = mProgress + "";
                break;
        }
        if (TextUtils.isEmpty(text)) {
            return;
        }
        Log.i(TAG, "drawContentType: " + progressType);
        Rect rect = new Rect();
        mPaint.setTextSize(mCenterTextSize);
        mPaint.getTextBounds(text, 0, text.length(), rect);
        int left = getMeasuredWidth() / 2 - rect.width() / 2;
        int bottom = getMeasuredHeight() / 2;
        canvas.drawText(text, left, bottom, mPaint);
    }

    private RectF getRecrF() {
        int mViewHeight = getMeasuredHeight();
        int mViewWidth = getMeasuredWidth();
        int startX;
        int startY;
        int width;
        if (mViewWidth >= mViewHeight) {
            startX = (mViewWidth - mViewHeight) / 2;
            startY = 0;
            width = mViewHeight;
        } else {
            startX = 0;
            startY = (mViewHeight - mViewWidth) / 2;
            width = mViewWidth;
        }
        int padding = 8 + mDefaultStrokeWidth;
        return new RectF(startX + padding, startY + padding, startX + width - padding, startY + width - padding);
    }

    public void updateProgress(int progress) {
        if (start) {
            return;
        }
        if (progress > mAxValue) {
            try {
                throw new RuntimeException("the progress value should not be greater than " + mAxValue);
            } catch (Exception e) {
            } finally {
                return;
            }
        }
        start = true;
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new IntEvaluator(), 0, progress);
        valueAnimator.setDuration(1500);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer animatedValue = (Integer) animation.getAnimatedValue();
                update(animatedValue);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                start = false;

            }
        });
        valueAnimator.start();
    }

    private void update(int progress) {
        mProgress = progress;
        invalidate();
    }
}



