package com.lyz.lib.manage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import me.iwf.photopicker.utils.PermissionsConstant;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by donglua on 2016/10/19.
 *
 */

public class PermissionsUtils {

    @SuppressLint("ObsoleteSdkInt")
    public static boolean checkReadStoragePermission(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            return true;
        }
        int readStoragePermissionState =
                ContextCompat.checkSelfPermission(activity, READ_EXTERNAL_STORAGE);

        boolean readStoragePermissionGranted = readStoragePermissionState == PackageManager.PERMISSION_GRANTED;

        if (!readStoragePermissionGranted) {
            ActivityCompat.requestPermissions(activity,
                    PermissionsConstant.PERMISSIONS_EXTERNAL_READ,
                    PermissionsConstant.REQUEST_EXTERNAL_READ);
        }
        return readStoragePermissionGranted;
    }

    public static boolean checkWriteStoragePermission(Fragment fragment) {
        if (null == fragment) return false;
        Context context = fragment.getContext();
        if (null != context) {
            int writeStoragePermissionState =
                    ContextCompat.checkSelfPermission(fragment.getContext(), WRITE_EXTERNAL_STORAGE);

            boolean writeStoragePermissionGranted = writeStoragePermissionState == PackageManager.PERMISSION_GRANTED;

            if (!writeStoragePermissionGranted) {
                fragment.requestPermissions(PermissionsConstant.PERMISSIONS_EXTERNAL_WRITE,
                        PermissionsConstant.REQUEST_EXTERNAL_WRITE);
            }
            return writeStoragePermissionGranted;
        } else {
            return false;
        }
    }

    public static boolean checkCameraPermission(Fragment fragment) {
        if (null == fragment) return false;
        Context context = fragment.getContext();
        if (null != context) {
            int cameraPermissionState = ContextCompat.checkSelfPermission(context, CAMERA);

            boolean cameraPermissionGranted = cameraPermissionState == PackageManager.PERMISSION_GRANTED;

            if (!cameraPermissionGranted) {
                fragment.requestPermissions(PermissionsConstant.PERMISSIONS_CAMERA,
                        PermissionsConstant.REQUEST_CAMERA);
            }
            return cameraPermissionGranted;
        } else {
            return false;
        }
    }

}
