package demo.tont.com.myapplication.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import demo.tont.com.myapplication.R;

/**
 * 支持水平和垂直两个方向的进度条
 */
public class LinearProgress extends View {
    private static final String TAG = LinearProgress.class.getName();
    private int mDefaultBgColor = 0X00ffffff;
    private int mDefaultProgressBackground = 0X44FFFFFF;
    private int mDefaultProgressColor = 0XFFFFFFFF;
    private float mCenterTextSize = 20.0f;
    private int mDefaultTextColor = 0XFFFFFFFF;
    private int progressType;
    private float mDefaultRadius = 0;
    private int mMaxValue = 100;
    private int mCurrentPogress = 0;
    private RectF recrF;
    private Paint mPaint;
    private int mDefaultOrientatil;
    private static final int HORIZONTAL = 0;
    private static final int VERTICAL_DOWN = 1;
    private static final int VERTICAL_UP = 2;

    public LinearProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initAttrs(context, attrs);
        initPaint();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LinearProgress);
        mDefaultBgColor = typedArray.getColor(R.styleable.LinearProgress_linearProgressBackground, 0X00ffffff);
        mDefaultProgressBackground = typedArray.getColor(R.styleable.LinearProgress_linearProgressBackgroundColor, 0X44FFFFFF);
        mDefaultProgressColor = typedArray.getColor(R.styleable.LinearProgress_linearProgressColor, 0XFFFFFFFF);
        mCenterTextSize = typedArray.getDimension(R.styleable.LinearProgress_linearProgressTextSize, 20.0f);
        progressType = typedArray.getInt(R.styleable.LinearProgress_linearProgressType, 0);
        mMaxValue = typedArray.getInteger(R.styleable.LinearProgress_linearProgressMaxValue, 100);
        mDefaultRadius = typedArray.getDimension(R.styleable.LinearProgress_linearProgressRadius, 0f);
        mDefaultTextColor = typedArray.getColor(R.styleable.LinearProgress_linearProgressTextColor, 0XFFFFFFFF);
        mDefaultOrientatil = typedArray.getInt(R.styleable.LinearProgress_linearProgressOrientational, 0);
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        recrF = getRecrF(mMaxValue);
        int measuredHeight = getMeasuredHeight();
        int measuredWidth = getMeasuredWidth();
        if (measuredHeight * 1.0f / 2.0f < mDefaultRadius) {
            mDefaultRadius = measuredHeight * 1.0f / 2.0f;
        }
        //文字大小不能过大
        if (mDefaultOrientatil == HORIZONTAL) {
            mCenterTextSize = measuredHeight < mCenterTextSize ? measuredHeight : mCenterTextSize;
        } else {
            mCenterTextSize = measuredWidth < mCenterTextSize ? measuredWidth : mCenterTextSize;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBackground(canvas);
        drawProgressBg(canvas);
        drawProgress(canvas);
        drawText(canvas);
    }

    private void drawBackground(Canvas canvas) {
        canvas.drawColor(mDefaultBgColor);
    }

    private void drawProgressBg(Canvas canvas) {
        mPaint.setColor(mDefaultProgressBackground);
        canvas.drawRoundRect(recrF, mDefaultRadius, mDefaultRadius, mPaint);
    }

    private void drawProgress(Canvas canvas) {
        mPaint.setColor(mDefaultProgressColor);
        RectF recrF = getRecrF(mCurrentPogress);
        canvas.drawRoundRect(recrF, mDefaultRadius, mDefaultRadius, mPaint);
    }

    private void drawText(Canvas canvas) {
        String text = ((int) mCurrentPogress * 100f / mMaxValue) + "%";
        Point point = getTextPosition(text);
        canvas.drawText(text, point.x, point.y, mPaint);
    }

    //计算文本位置
    public Point getTextPosition(String text) {
        Point point = new Point();
        //文本
        mPaint.setTextSize(mCenterTextSize);
        mPaint.setColor(mDefaultTextColor);

        RectF recrF = getRecrF(mCurrentPogress);
        Rect rect = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), rect);
        int rightPosition = 0;
        int bootomPosition = 0;
        switch (mDefaultOrientatil) {
            case HORIZONTAL:
                float right = recrF.right;
                rightPosition = (int) (right - rect.width() - 8);
                bootomPosition = getMeasuredHeight() / 2 + rect.height() / 2;
                break;
            case VERTICAL_DOWN:
                float bottom = recrF.bottom;
                bootomPosition = (int) (bottom - rect.height() - 8);
                rightPosition = getMeasuredWidth() / 2 - rect.width() / 2;
                break;
            case VERTICAL_UP:
                float top = recrF.top;
                bootomPosition = (int) (top + rect.height() + 18);
                rightPosition = getMeasuredWidth() / 2 - rect.width() / 2;
                break;
        }
        point.x = rightPosition;
        point.y = bootomPosition;
        return point;
    }

    private RectF getRecrF(int progress) {
        int measuredHeight = getMeasuredHeight();
        int measuredWidth = getMeasuredWidth();
        if (measuredHeight == 0 || measuredWidth == 0) {
            return new RectF(0, 0, 0, 0);
        }
        switch (mDefaultOrientatil) {
            case HORIZONTAL:
                return new RectF(0, 0, (progress * 1.0f / mMaxValue) * measuredWidth, measuredHeight);
            case VERTICAL_DOWN:
                return new RectF(0, 0, measuredWidth, (progress * 1.0f / mMaxValue) * measuredHeight);
            case VERTICAL_UP:
                return new RectF(0, ((mMaxValue - progress) * 1.0f / mMaxValue) * measuredHeight, measuredWidth, measuredHeight);
        }
        return new RectF(0, 0, 0, 0);
    }

    /**
     * 更新进度信息
     *
     * @param progress
     */
    public void notifyProgress(int progress) {
        if (progress > mMaxValue) {
            try {
                throw new RuntimeException("the progress value should not greater than " + mMaxValue);
            } catch (Exception c) {
                c.printStackTrace();
            }
        }
        mCurrentPogress = progress;
        invalidate();
    }

    /**
     * 设置最大值
     *
     * @param maxValue
     */
    public void setMaxValue(int maxValue) {
        mMaxValue = maxValue;
    }
}

