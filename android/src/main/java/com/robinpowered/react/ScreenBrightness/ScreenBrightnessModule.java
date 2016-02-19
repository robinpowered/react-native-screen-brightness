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
    public static final String MODULE_NAME = "ScreenBrightness";

    private static final String PERMISSION_EVENT_NAME = "screenBrightnessPermission";
    private static final int BRIGHTNESS_MAX = 255;
    private static final int BRIGHTNESS_MIN = 0;

    private final int mWriteSettingsRequestCode;

    private Activity mActivity;

    public ScreenBrightnessModule(
            ReactApplicationContext reactApplicationContext,
            Activity activity,
            final int writeSettingsRequestCode) {
        super(reactApplicationContext);
        mActivity = activity;
        mWriteSettingsRequestCode = writeSettingsRequestCode;
    }

    @Override
    public String getName() {
        return MODULE_NAME;
    }

    /**
     * Called by the main activity when the ACTION_MANAGE_WRITE_SETTINGS result is received.
     * Emits the result into the JS context.
     */
    public void onPermissionResult() {
        WritableMap payload = new WritableNativeMap();
        boolean hasPermission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                Settings.System.canWrite(getReactApplicationContext());

        payload.putBoolean("hasPermission", hasPermission);

        getReactApplicationContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(PERMISSION_EVENT_NAME, payload);
    }

    /**
     * Returns whether the device has granted the application permission to write settings.
     *
     * @return True if WRITE_SETTINGS are granted.
     */
    public boolean hasSettingsPermission() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                Settings.System.canWrite(getReactApplicationContext()));
    }

    /**
     * Invokes the request permission activity to request access for WRITE_SETTINGS.
     */
    private void requestSettingsPermission() {
        Context context = getReactApplicationContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(context)) {
            Intent intent = new Intent(
                    Settings.ACTION_MANAGE_WRITE_SETTINGS,
                    Uri.parse("package:" + context.getPackageName())
            );
            mActivity.startActivityForResult(intent, mWriteSettingsRequestCode);
        }
    }

    /**
     * Gets the brightness level of the device settings.
     *
     * @return The brightness level.
     */
    private Integer getSystemBrightness() {
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

    /**
     * Sets the brightness level to the device settings.
     *
     * @param brightness The brightness level between 0-255.
     * @return True if the brightness has been set.
     */
    private boolean setSystemBrightness(int brightness) {
        if (hasSettingsPermission()) {
            // ensure brightness is bound between range 0-255
            brightness = Math.max(BRIGHTNESS_MIN, Math.min(brightness, BRIGHTNESS_MAX));
            Settings.System.putInt(
                    getReactApplicationContext().getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS,
                    brightness
            );
            return true;
        }
        return false;
    }

    /**
     * Determines if the application has been granted WRITE_SETTINGS permissions.
     * This method is callable from the JS context.
     *
     * @param promise A promise resolving if the application has WRITE_SETTINGS granted
     */
    @ReactMethod
    public void hasPermission(final Promise promise) {
        promise.resolve(hasSettingsPermission());
    }

    /**
     * Invokes the permission request flow.
     * This method is callable from the JS context.
     */
    @ReactMethod
    public void requestPermission() {
        requestSettingsPermission();
    }

    /**
     * Updates the device brightness.
     * This method is callable from the JS context.
     *
     * @param brightness The brightness level between 0-1.
     * @param promise A promise resolving if the brightness was updated.
     */
    @ReactMethod
    public void setBrightness(float brightness, final Promise promise) {
        if (setSystemBrightness((int) (brightness * BRIGHTNESS_MAX))) {
            promise.resolve(brightness);
        } else {
            promise.reject(null);
        }
    }

    /**
     * Gets the brightness level of the device.
     * This method is callable from the JS context.
     *
     * @param promise A promise resolving the brightness level.
     */
    @ReactMethod
    public void getBrightness(final Promise promise) {
        promise.resolve(getSystemBrightness());
    }

    /**
     * Gets the application brightness level
     * This method is callable from the JS context.
     *
     * @param promise A promise resolving the app brightness level.
     */
    @ReactMethod
    public void getAppBrightness(Promise promise) {
        float brightness = mActivity.getWindow().getAttributes().screenBrightness;
        promise.resolve(brightness);
    }

    /**
     * Sets the application brightness level.
     * This method is callable from the JS context.
     *
     * @param brightness The brightness level.
     * @param promise A promise resolving the updated brightness level.
     */
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