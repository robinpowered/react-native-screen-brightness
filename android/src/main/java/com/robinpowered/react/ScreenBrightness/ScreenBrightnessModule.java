package com.robinpowered.react.ScreenBrightness;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.os.BatteryManager;
import android.view.WindowManager;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.Promise;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.LifecycleEventListener;

import javax.annotation.Nullable;

public class ScreenBrightnessModule extends ReactContextBaseJavaModule {
    private Activity mActivity;

    public ScreenBrightnessModule(ReactApplicationContext reactApplicationContext, Activity activity) {
        super(reactApplicationContext);
        mActivity = activity;
    }

    @Override
    public String getName() {
        return "ScreenBrightness";
    }

    @ReactMethod
    public void getBrightness(Promise promise) {
        float brightness = mActivity.getWindow().getAttributes().screenBrightness;
        promise.resolve(brightness);
    }

    @ReactMethod
    public void setBrightness(final float brightness, final Promise promise) {
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