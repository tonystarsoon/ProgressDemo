package demo.tont.com.myapplication.progress;

import android.util.Log;
import android.view.animation.Interpolator;

/**
 * Created by admin on 2018/2/9.
 */

public class MyInterpolator implements Interpolator {
    private static final String TAG = "MyInterpolator";

    @Override
    public float getInterpolation(float input) {
        Log.i(TAG, "getInterpolation: " + input);
        return input;
    }
}
