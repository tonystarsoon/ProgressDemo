package demo.tont.com.myapplication.progress;

import android.animation.FloatEvaluator;
import android.util.Log;

/**
 * Created by admin on 2018/2/7.
 */

public class MyFloatEvaluator extends FloatEvaluator {
    private static final String TAG = MyFloatEvaluator.class.getName();

    public Float evaluate(float fraction, Number startValue, Number endValue) {
        float startFloat = startValue.floatValue();
        float v = startFloat + fraction * (endValue.floatValue() - startFloat);
        Log.i(TAG, "----fraction: " + fraction + "---v: " + v);
        return v;
    }
}
