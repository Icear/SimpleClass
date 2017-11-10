package com.github.Icear.NEFU.SimpleClass;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;


/**
 * Created by icear on 2017/11/10.
 * 用于申请权限的Activity，没有界面
 */

public class PermissionQuestActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private OnRequestPermissionResultCallback callback;

    public PermissionQuestActivity(OnRequestPermissionResultCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        callback.onRequestPermissionResult(requestCode, permissions, grantResults);
    }


    public interface OnRequestPermissionResultCallback {
        void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
    }
}
