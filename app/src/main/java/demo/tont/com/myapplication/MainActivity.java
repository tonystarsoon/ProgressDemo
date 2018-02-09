package demo.tont.com.myapplication;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RadioButton;

import demo.tont.com.myapplication.dialog.ButtonDialog;
import demo.tont.com.myapplication.progress.HoodleProgress;
import demo.tont.com.myapplication.progress.LinearProgress;
import demo.tont.com.myapplication.progress.RingProgress;
import demo.tont.com.myapplication.radiogroup.MyRadioGroup;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testLinearProgress();
        testRingProgress();

        testHoodleProgress();
    }
    private String[] names = {"我的世界","世界","世界","世界","世界","我的世界"};

    public void dialog1(View view1) {
        MyRadioGroup myRadioGroup = findViewById(R.id.select_dialog);

        for (int i = 0; i < names.length; i++) {
            LayoutInflater layoutInflater = getLayoutInflater();
            RadioButton view = (RadioButton) layoutInflater.inflate(R.layout.child, null, false);
            view.setTag("" + i);
            view.setText(names[i]);
            myRadioGroup.addView(view);
        }
        myRadioGroup.setOnCheckedChangeListener(new MyRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(int position) {
                Log.i(TAG, "onCheckedChanged: " + position);
            }
        });
    }

    private void testHoodleProgress() {
        HoodleProgress hoodleProgress = findViewById(R.id.hoodleProgress);
//        hoodleProgress.start();
    }

    private void testRingProgress() {
        RingProgress viewById = findViewById(R.id.progress);
        viewById.updateProgress(800);
    }

    private void testLinearProgress() {
        final LinearProgress linearProgress = findViewById(R.id.linearProgress);

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





