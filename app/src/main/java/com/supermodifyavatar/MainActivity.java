package com.supermodifyavatar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.supermodifyavatar.dialog.BaseIOSDialog;
import com.supermodifyavatar.dialog.BottomDialog;
import com.supermodifyavatar.utils.PhotoUtils;
import com.supermodifyavatar.widget.CircleImageView;
import com.supermodifyavatar.widget.RoundImageView;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.civ_header)
    CircleImageView mCivHeader;
    @Bind(R.id.riv_header)
    RoundImageView mRivHeader;

    private PhotoUtils mPhotoUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setAvatarChangeListener();
    }

    private void setAvatarChangeListener() {
        mPhotoUtils = new PhotoUtils(new PhotoUtils.OnPhotoResultListener() {
            @Override
            public void onPhotoResult(Uri uri) {
                if (uri != null && !TextUtils.isEmpty(uri.getPath())) {
                    mCivHeader.setImageURI(uri);
                    mRivHeader.setImageURI(uri);
                }
            }

            @Override
            public void onPhotoCancel() {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PhotoUtils.INTENT_CROP:
            case PhotoUtils.INTENT_TAKE:
            case PhotoUtils.INTENT_SELECT:
                mPhotoUtils.onActivityResult(MainActivity.this, requestCode, resultCode, data);
                break;
        }
    }

    @OnClick({R.id.civ_header, R.id.riv_header})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.civ_header:
                showPhotoDialog1();
                break;
            case R.id.riv_header:
                showPhotoDialog2();
                break;
        }
    }

    private void showPhotoDialog1() {
        BaseIOSDialog dialog = new BaseIOSDialog(this);
        dialog.addSheetItem(Arrays.asList("拍照", "从相册选择"), new BaseIOSDialog.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                switch (position) {
                    case 0:
                        mPhotoUtils.takePicture(MainActivity.this);
                        break;
                    case 1:
                        mPhotoUtils.selectPicture(MainActivity.this);
                        break;
                }
            }
        }).show();
    }

    private void showPhotoDialog2() {
        // 判断6.0权限问题
        final BottomDialog dialog = new BottomDialog(this, "拍照", "从相册选择");
        dialog.setConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoUtils.takePicture(MainActivity.this);
            }
        });
        dialog.setMiddleListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoUtils.selectPicture(MainActivity.this);
            }
        });
        dialog.show();
    }

}
