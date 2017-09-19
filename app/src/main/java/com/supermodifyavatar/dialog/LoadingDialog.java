package com.supermodifyavatar.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.supermodifyavatar.R;
import com.supermodifyavatar.utils.StringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by MMM on 2017/9/18.
 * 加载对话框
 */
public class LoadingDialog extends Dialog {

    @Bind(R.id.tv_loading)
    TextView mTvLoading;

    private Context mContext;

    public LoadingDialog(Context context) {
        this(context, 0);
        this.mContext = context;
    }

    public LoadingDialog(Context context, int themeResId) {
        super(context, R.style.LoadingDialogStyle);
        this.mContext = context;

        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_loading, null);
        setContentView(view);
        ButterKnife.bind(this, view);

        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
//        dialogWindow.setGravity(Gravity.CENTER | Gravity.BOTTOM);
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽高
        params.width = (int) (d.widthPixels * 1.0); // 高度设置为屏幕的1.0
//        params.height = (int) (d.heightPixels * 0.6); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(params);

        LoadingDialog.this.setCanceledOnTouchOutside(false);
        LoadingDialog.this.setCancelable(true);
    }

    public LoadingDialog setTvLoading(String tvLoading) {
        if (!StringUtils.isNull(tvLoading)) {
            mTvLoading.setText(tvLoading);
        }
        return this;
    }
}
