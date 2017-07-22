package com.supermodifyavatar;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.supermodifyavatar.dialog.BaseIOSDialog;
import com.supermodifyavatar.dialog.BottomDialog;
import com.supermodifyavatar.model.UpLoadBean;
import com.supermodifyavatar.utils.PhotoUtils;
import com.supermodifyavatar.widget.CircleImageView;
import com.supermodifyavatar.widget.RoundImageView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import okhttp3.Call;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.civ_header)
    CircleImageView mCivHeader;
    @Bind(R.id.riv_header)
    RoundImageView mRivHeader;

    private boolean isPermission;
    private PhotoUtils mPhotoUtils;
    private String UPLOAD_URL = "http://120.77.251.141:39080/youe/commons/uploadFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setAvatarChangeListener();
        // 申请权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getPermissions();
        }
    }

    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermissionGen.with(this)
                    .addRequestCode(120)
                    .permissions(
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .request();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @PermissionSuccess(requestCode = 120)
    public void doPermissionSuccess() {
        isPermission = true;
        Toast.makeText(this, "权限请求成功", Toast.LENGTH_SHORT).show();
    }

    @PermissionFail(requestCode = 120)
    public void doPermissionFail() {
        isPermission = false;
        Toast.makeText(this, "权限获取失败", Toast.LENGTH_SHORT).show();
    }

    private void setAvatarChangeListener() {
        mPhotoUtils = new PhotoUtils(new PhotoUtils.OnPhotoResultListener() {
            @Override
            public void onPhotoResult(Uri uri) {
                if (uri != null && !TextUtils.isEmpty(uri.getPath())) {
                    mCivHeader.setImageURI(uri);
                    mRivHeader.setImageURI(uri);
//                    upLoadAvatar(new File(uri.getPath()));
                }
            }

            @Override
            public void onPhotoCancel() {
                Toast.makeText(MainActivity.this, "取消操作", Toast.LENGTH_SHORT).show();
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
                        if (isPermission) {
                            mPhotoUtils.takePicture(MainActivity.this);
                        } else {
                            getPermissions();
                        }
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
                if (isPermission) {
                    mPhotoUtils.takePicture(MainActivity.this);
                } else {
                    getPermissions();
                }
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

    /**
     * 上传
     *
     * @param file
     */
    private void upLoadAvatar(File file) {
        OkHttpUtils.post()
                .addFile("file", file.getPath(), file)
                .url(UPLOAD_URL)
                .addParams("toType", "0")
                .addParams("toid", "6")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        Toast.makeText(MainActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        Gson gson = new Gson();
                        UpLoadBean bean = gson.fromJson(response, UpLoadBean.class);
                        if (bean.isOk()) {
                            String imgUrl = "http://120.77.251.141:8081" + bean.getRes();
                            Glide.with(MainActivity.this).load(imgUrl).into(mCivHeader);
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(MainActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
