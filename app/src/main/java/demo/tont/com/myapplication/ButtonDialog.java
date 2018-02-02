package demo.tont.com.myapplication;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.Serializable;

public class ButtonDialog extends DialogFragment implements View.OnClickListener {
    private static final String TAG = "ButtonDialog";
    private static int DEFAULT_HEIGHT = 190;
    public static final int DEFAULT_TYPE = 0;
    public static final int SINGLE_TYPE = 1;
    private static final int CONFIRM_TAG = 0;
    private static final int REFUSE_TAG = 1;
    private Params mParams;

    public ButtonDialog() {
        super();
    }

    @SuppressLint("ValidFragment")
    public ButtonDialog(Params params) {
        super();
        mParams = params;
    }

    @Override
    public void onAttach(Context context) {
        Log.i(TAG, "----onAttach: " + mParams);
        super.onAttach(context);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "----onSaveInstanceState: ");
        //保存数据
        outState.putSerializable("mParam", mParams);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "----onViewStateRestored: ");
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "----onCreate: " + mParams);
        super.onCreate(savedInstanceState);
        //还原数据
        if (mParams == null) {
            mParams = (Params) savedInstanceState.getSerializable("mParam");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.i(TAG, "----onCreateDialog: ");
        Dialog dialog = buildDialog();
        setDialogDimens(dialog);
        addAnimation(dialog);
        return dialog;
    }

    //设置弹窗的动画
    private void addAnimation(Dialog dialog) {
        if (mParams.animationStyle > 0) {
            Window window = dialog.getWindow();
            window.setWindowAnimations(mParams.animationStyle);
        }
    }

    //构建dialog
    @NonNull
    private Dialog buildDialog() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(mParams.mLayoutId, null);
        Dialog dialog = new Dialog(getActivity(), mParams.dialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(mParams.canceledOnTouchOutside);
        TextView title = view.findViewById(R.id.title);
        TextView content = view.findViewById(R.id.content);
        View refuse = null;
        View confirm = null;
        if (mParams.mType == DEFAULT_TYPE) {
            confirm = view.findViewById(R.id.confirm);
            confirm.setTag(CONFIRM_TAG);
            refuse = view.findViewById(R.id.refuse);
            refuse.setTag(REFUSE_TAG);
        } else if (mParams.mType == SINGLE_TYPE) {
            confirm = view.findViewById(R.id.confirm);
            confirm.setTag(CONFIRM_TAG);
        }
        if (title != null) {
            title.setText(mParams.mTitle);
        }
        if (content != null) {
            content.setText(mParams.mContent);
        }
        if (confirm != null) {
            confirm.setOnClickListener(this);
        }
        if (refuse != null) {
            refuse.setOnClickListener(this);
        }
        Log.i(TAG, "buildDialog:title/" + title + "content/" + content + "/" + mParams.mContent + "/" + mParams.mType);
        return dialog;
    }

    //设置弹窗的尺寸
    private void setDialogDimens(Dialog dialog) {
        // 设置宽度为屏宽、位置靠近屏幕底部
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.color.transparent);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        wlp.width = (int) (displayMetrics.widthPixels * (1 - mParams.marginSideRadio));
        wlp.height = (int) (mParams.mHeight * displayMetrics.density);
        window.setAttributes(wlp);
    }

    @Override
    public void onClick(View v) {
        int tag = (Integer) v.getTag();
        Log.i(TAG, "onClick:" + tag);
        switch (tag) {
            case CONFIRM_TAG:
                int mType = mParams.mType;
                if (mType == DEFAULT_TYPE) {
                    if (mParams.onTwoButtonListener != null) {
                        mParams.onTwoButtonListener.onConfirm();
                        dismiss();
                    }
                } else if (mType == SINGLE_TYPE) {
                    if (mParams.onSingleButtonListener != null) {
                        mParams.onSingleButtonListener.onConfirm();
                        dismiss();
                    }
                }
                break;
            case REFUSE_TAG:
                if (mParams.onTwoButtonListener != null) {
                    mParams.onTwoButtonListener.onRefuse();
                    dismiss();
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mParams.onSingleButtonListener = null;
        mParams.onTwoButtonListener = null;
        mParams = null;
    }

    public static class Builder {
        private Params mParams;

        public Builder() {
            mParams = new Params();
        }

        public Builder setTitle(String title) {
            mParams.mTitle = title;
            return this;
        }

        public Builder setContent(String content) {
            mParams.mContent = content;
            return this;
        }

        public Builder setType(int type) {
            mParams.mType = type;
            return this;
        }

        public Builder setLayoutId(int layoutId) {
            mParams.mLayoutId = layoutId;
            return this;
        }

        public Builder setWidth(int width) {
            mParams.mWidth = width;
            return this;
        }

        public Builder setHeight(int height) {
            mParams.mHeight = height;
            return this;
        }

        public Builder setBackground(int background) {
            mParams.mBackground = background;
            return this;
        }

        public Builder setStyle(int style) {
            mParams.dialogStyle = style;
            return this;
        }

        public Builder setMarginSide(float radio) {
            mParams.marginSideRadio = radio;
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean outside) {
            mParams.canceledOnTouchOutside = outside;
            return this;
        }

        public Builder setOnSelectListener(OnTwoButtonListener listener) {
            mParams.onTwoButtonListener = listener;
            return this;
        }

        public Builder setOnConfirmListener(OnSingleButtonListener listener) {
            mParams.onSingleButtonListener = listener;
            return this;
        }

        public ButtonDialog create() {
            return new ButtonDialog(mParams);
        }
    }


    static class Params implements Serializable {
        int dialogStyle = R.style.Theme_AppCompat_Dialog;
        String mContent = null;
        String mTitle = null;
        int mType = 0;
        int mLayoutId = 0;
        int mWidth = 0;
        int mHeight = DEFAULT_HEIGHT;
        int mBackground = 0;
        int animationStyle = 0;
        float marginSideRadio = 0.05f;
        boolean canceledOnTouchOutside = false;
        OnTwoButtonListener onTwoButtonListener = null;
        OnSingleButtonListener onSingleButtonListener = null;
    }

    public interface OnTwoButtonListener {
        void onRefuse();

        void onConfirm();
    }

    public interface OnSingleButtonListener {
        void onConfirm();
    }
}
