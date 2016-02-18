package com.robinpowered.react.ScreenBrightness;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.WindowManager;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;


public class ScreenBrightnessModule extends ReactContextBaseJavaModule {
    public static final int REQUEST_WRITE_SETTINGS_PERMISSION = 2525;
    private static final String PERMISSION_EVENT_NAME = "screenBrightnessPermission";
    private static final int BRIGHTNESS_RANGE = 255;

    private Activity mActivity;

    public ScreenBrightnessModule(ReactApplicationContext reactApplicationContext, Activity activity) {
        super(reactApplicationContext);
        mActivity = activity;
    }

    @Override
    public String getName() {
        return "ScreenBrightness";
    }

    public void onPermissionResult() {
        WritableMap payload = new WritableNativeMap();
        boolean hasPermission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                Settings.System.canWrite(getReactApplicationContext());

        payload.putBoolean("hasPermission", hasPermission);

        getReactApplicationContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(PERMISSION_EVENT_NAME, payload);
    }

    public boolean hasSettingsPermission() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                Settings.System.canWrite(getReactApplicationContext()));
    }

    private void requestSettingsPermission() {
        Context context = getReactApplicationContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(context)) {
            Intent intent = new Intent(
                    Settings.ACTION_MANAGE_WRITE_SETTINGS,
                    Uri.parse("package:" + context.getPackageName())
            );
            mActivity.startActivityForResult(intent, REQUEST_WRITE_SETTINGS_PERMISSION);
        }
    }

    private int getSystemBrightness() {
        Integer brightness;
        try {
            brightness = Settings.System.getInt(
                    getReactApplicationContext().getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS
            );
        } catch (Settings.SettingNotFoundException e) {
            brightness = null;
        }
        return brightness;
    }

    private boolean setSystemBrightness(int brightness) {
        if (hasSettingsPermission()) {
            Settings.System.putInt(
                    getReactApplicationContext().getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS,
                    brightness
            );
            return true;
        }
        return false;
    }

    @ReactMethod
    public void hasPermission(final Promise promise) {
        promise.resolve(hasSettingsPermission());
    }

    @ReactMethod
    public void requestPermission() {
        requestSettingsPermission();
    }

    @ReactMethod
    public void setBrightness(float brightness, final Promise promise) {
        if (brightness > 1) {
            brightness = 1f;
        } else if (brightness < 0) {
            brightness = 0f;
        }
        if (setSystemBrightness((int) brightness * BRIGHTNESS_RANGE)) {
            promise.resolve(brightness);
        } else {
            promise.reject(null);
        }
    }

    @ReactMethod
    public void getBrightness(final Promise promise) {
        promise.resolve(getSystemBrightness());
    }

    @ReactMethod
    public void getAppBrightness(Promise promise) {
        float brightness = mActivity.getWindow().getAttributes().screenBrightness;
        promise.resolve(brightness);
    }

    @ReactMethod
    public void setAppBrightness(final float brightness, final Promise promise) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
                lp.screenBrightness=brightness;
                mActivity.getWindow().setAttributes(lp);
                promise.resolve(brightness);
            }
        });
    }
}