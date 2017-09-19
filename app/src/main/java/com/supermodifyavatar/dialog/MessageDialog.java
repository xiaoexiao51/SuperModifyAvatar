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
import butterknife.OnClick;

/**
 * Created by MMM on 2017/9/18.
 * 提示对话框
 */
public class MessageDialog extends Dialog {

    @Bind(R.id.txt_title)
    TextView mTxtTitle;
    @Bind(R.id.txt_content)
    TextView mTxtContent;
    @Bind(R.id.txt_cancel)
    TextView mTxtCancel;
    @Bind(R.id.view_line)
    View mViewLine;
    @Bind(R.id.txt_confirm)
    TextView mTxtConfirm;

    private Context mContext;

    public MessageDialog(Context context) {
        this(context, 0);
        this.mContext = context;
    }

    public MessageDialog(Context context, int themeResId) {
        super(context, R.style.AlertDialogStyle);
        this.mContext = context;

        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_message, null);
        setContentView(view);
        ButterKnife.bind(this, view);

        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
//        dialogWindow.setGravity(Gravity.CENTER | Gravity.BOTTOM);
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽高
        params.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.8
//        params.height = (int) (d.heightPixels * 0.6); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(params);

        MessageDialog.this.setCanceledOnTouchOutside(false);
        MessageDialog.this.setCancelable(true);
    }

    public MessageDialog build(String title, String content, boolean hideCancel) {
        if (StringUtils.isNull(title)) {
            mTxtTitle.setVisibility(View.GONE);
        } else {
            mTxtTitle.setText(title);
        }
        if (StringUtils.isNull(content)) {
            mTxtContent.setVisibility(View.GONE);
        } else {
            mTxtContent.setText(content);
        }

        if (hideCancel) {
            mTxtCancel.setVisibility(View.GONE);
            mViewLine.setVisibility(View.GONE);
            mTxtConfirm.setBackgroundResource(R.drawable.shap_dialog_message_single);
        }
        return this;
    }

    @OnClick({R.id.txt_cancel, R.id.txt_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_cancel:
                if (mOnChooseResultListener != null) {
                    mOnChooseResultListener.onChooseResult(false);
                }
                MessageDialog.this.dismiss();
                break;
            case R.id.txt_confirm:
                if (mOnChooseResultListener != null) {
                    mOnChooseResultListener.onChooseResult(true);
                }
                MessageDialog.this.dismiss();
                break;
        }
    }

    private OnChooseResultListener mOnChooseResultListener;

    public interface OnChooseResultListener {
        void onChooseResult(boolean confirm);
    }

    public MessageDialog setOnChooseResultListener(OnChooseResultListener listener) {
        mOnChooseResultListener = listener;
        return this;
    }
}