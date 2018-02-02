package demo.tont.com.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Progress viewById = findViewById(R.id.progress);
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





