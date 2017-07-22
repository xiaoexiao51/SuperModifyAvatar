# SuperModifyAvatar
修改用户头像（拍照和从相册选择）

![](https://github.com/xiaoexiao51/SuperModifyAvatar/blob/master/screenshot/img01.png)

![](https://github.com/xiaoexiao51/SuperModifyAvatar/blob/master/screenshot/img02.png)

代码实现：

```java
  private void setAvatarChangeListener() {
        mPhotoUtils = new PhotoUtils(new PhotoUtils.OnPhotoResultListener() {
            @Override
            public void onPhotoResult(Uri uri) {
                if (uri != null && !TextUtils.isEmpty(uri.getPath())) {
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
