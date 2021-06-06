package com.yuncommunity.lizard.base;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.oldfeel.base.BaseActivity;

import java.util.List;

import easypermissions.EasyPermissions;

/**
 * Created by oldfeel on 16-10-17.
 */

public class MyActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    public DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = DBHelper.getInstance(this);
    }

    @Override
    public void onPermissionsGranted(List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(List<String> perms) {

    }

    @Override
    public void onRequestDenied() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
