package demo.tont.com.myapplication.radiogroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;


public class MyRadioGroup extends ViewGroup implements View.OnClickListener {
    private static final String TAG = MyRadioGroup.class.getName();
    private int defaultMargin = 30;
    private int maxChildHeight;
    private OnCheckedChangeListener listener;
    private int mLineCount = 3;
    private int[] attrs = null;

    public MyRadioGroup(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initAttrs(context);
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, attrs);
        mLineCount = typedArray.getInt(0, mLineCount);
    }

    private void initAttrs(Context context) {
        int lineCount = context.getResources().getIdentifier("lineCount", "attr", context.getPackageName());
        attrs = new int[]{lineCount};
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.i(TAG, "onMeasure: ");

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int childCount = getChildCount();
        maxChildHeight = 0;

        int hMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST);
        int wMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize / mLineCount, MeasureSpec.AT_MOST);

        for (int index = 0; index < childCount; index++) {
            View childAt = getChildAt(index);
            measureChild(childAt, wMeasureSpec, hMeasureSpec);
            int childAtMeasuredHeight = childAt.getMeasuredHeight();
            maxChildHeight = childAtMeasuredHeight > maxChildHeight ? childAtMeasuredHeight : maxChildHeight;
        }

        int count = (int) Math.ceil(childCount * 1.0f / mLineCount);
        Log.i(TAG, "onMeasure: " + count + "---childCount: " + childCount);
        int heightTotal = count * (maxChildHeight + defaultMargin) + defaultMargin;

        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(heightTotal, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.i(TAG, "onLayout: ");
        int childCount = getChildCount();
        int eachMeasuredWidth = getMeasuredWidth() / mLineCount;

        int left = 0;
        int top = 0;
        int right = 0;
        int bottom = 0;

        for (int index = 0; index < childCount; index++) {
            View childAt = getChildAt(index);
            int measuredWidth = childAt.getMeasuredWidth();
            int measuredHeight = childAt.getMeasuredHeight();
            left = (index % mLineCount) * eachMeasuredWidth + (eachMeasuredWidth - measuredWidth) / 2;
            top = (index / mLineCount) * (defaultMargin + maxChildHeight) + defaultMargin + (maxChildHeight - measuredHeight) / 2;
            right = left + measuredWidth;
            bottom = top + measuredHeight;
            childAt.layout(left, top, right, bottom);
            childAt.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        int childCount = getChildCount();
        for (int index = 0; index < childCount; index++) {
            RadioButton childAt = (RadioButton) getChildAt(index);
            if (childAt == v) {
                childAt.setChecked(true);
                if (listener != null) {
                    listener.onCheckedChanged(index);
                }
            } else {
                childAt.setChecked(false);
            }
        }
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.listener = listener;
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(int position);
    }
}
