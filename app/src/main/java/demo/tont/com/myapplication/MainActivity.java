package demo.tont.com.myapplication;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    private LinearProgress linearProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearProgress = findViewById(R.id.linearProgress);

        ValueAnimator valueAnimator = ValueAnimator.ofObject(new IntEvaluator(), 0, 40);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                linearProgress.notifyProgress(animatedValue);
            }
        });
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setDuration(2000);
        valueAnimator.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        RingProgress viewById = findViewById(R.id.progress);
        viewById.updateProgress(800);
    }

    public void dialog(View view) {
        ButtonDialog.Builder builder = new ButtonDialog.Builder();
        ButtonDialog dialog = builder
                .setContent("测试代码测试代码测试代码测试代码测试代码测试代码测试代码测试代码")
                .setTitle("警告警告警告")
                .setLayoutId(R.layout.custom_dialog)
                .setType(ButtonDialog.DEFAULT_TYPE)
                .setMarginSide(0.15f)
                .setOnSelectListener(new ButtonDialog.OnTwoButtonListener() {
                    @Override
                    public void onRefuse() {
                        Log.i(TAG, "onRefuse: ");
                    }

                    @Override
                    public void onConfirm() {
                        Log.i(TAG, "onConfirm: ");
                    }
                })
                .create();
        dialog.show(getSupportFragmentManager(), "tag");
    }
}





