package com.supermodifyavatar.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.supermodifyavatar.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by MMM on 2017/9/18.
 * 精仿IOS单选对话框
 */
public class BaseIOSDialog extends Dialog {

    @Bind(R.id.txt_title)
    TextView mTxtTitle;
    @Bind(R.id.ll_content)
    LinearLayout mLlContent;
    @Bind(R.id.sv_content)
    ScrollView mSvContent;
    @Bind(R.id.txt_cancel)
    TextView mTxtCancel;

    private Context mContext;
    private boolean showTitle;
    private List<String> mItems = new ArrayList<>();

    public BaseIOSDialog(Context context) {
        this(context, 0);
        this.mContext = context;
    }

    public BaseIOSDialog(Context context, int themeResId) {
        super(context, R.style.IOSDialogStyle);
        this.mContext = context;

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_base_ios, null);
        setContentView(view);
        ButterKnife.bind(this, view);

        Window dialogWindow = getWindow();
//        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setGravity(Gravity.CENTER | Gravity.BOTTOM);
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽高
        params.width = (int) (d.widthPixels * 1.0); // 高度设置为屏幕的1.0
//        params.height = (int) (d.heightPixels * 0.6); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(params);

        BaseIOSDialog.this.setCanceledOnTouchOutside(true);
        BaseIOSDialog.this.setCancelable(true);
    }

    public BaseIOSDialog setTitle(String title) {
        showTitle = true;
        mTxtTitle.setVisibility(View.VISIBLE);
        mTxtTitle.setText(title);
        return this;
    }

    public BaseIOSDialog addSheetItem(List<String> strItems, OnItemClickListener listener) {
        mItems = strItems;
        mListener = listener;
        return this;
    }

    private void setSheetItems() {
        if (mItems == null || mItems.size() <= 0) {
            return;
        }

        if (mItems.size() >= 7) {
            LayoutParams params = (LayoutParams) mSvContent.getLayoutParams();
            DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
            int heightPixels = metrics.heightPixels;
            params.height = heightPixels / 2;
            mSvContent.setLayoutParams(params);
        }

        for (int i = 0; i < mItems.size(); i++) {
            final int position = i;
            String strItem = mItems.get(i);

            TextView textView = new TextView(mContext);
            textView.setText(strItem);
            textView.setTextSize(18);
            textView.setGravity(Gravity.CENTER);

            if (mItems.size() == 1) {
                if (showTitle) {
                    textView.setBackgroundResource(R.drawable.ios_bottom_selector);
                } else {
                    textView.setBackgroundResource(R.drawable.ios_single_selector);
                }
            } else {
                if (showTitle) {
                    if (i >= 0 && i < mItems.size() - 1) {
                        textView.setBackgroundResource(R.drawable.ios_middle_selector);
                    } else {
                        textView.setBackgroundResource(R.drawable.ios_bottom_selector);
                    }
                } else {
                    if (i == 0) {
                        textView.setBackgroundResource(R.drawable.ios_top_selector);
                    } else if (i < mItems.size() - 1) {
                        textView.setBackgroundResource(R.drawable.ios_middle_selector);
                    } else {
                        textView.setBackgroundResource(R.drawable.ios_bottom_selector);
                    }
                }
            }

            textView.setTextColor(mContext.getResources().getColor(R.color.color_blue));

            float scale = mContext.getResources().getDisplayMetrics().density;
            int height = (int) (45 * scale + 0.5f);
            textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, height));

            mLlContent.addView(textView);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onClick(position);
                    }
                    dismiss();
                }
            });

            mTxtCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

        }
    }

    @Override
    public void show() {
        setSheetItems();
        super.show();
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

}
