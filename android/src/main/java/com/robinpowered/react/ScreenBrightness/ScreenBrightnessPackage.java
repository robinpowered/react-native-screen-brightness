package com.robinpowered.react.ScreenBrightness;

import android.app.Activity;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ScreenBrightnessPackage implements ReactPackage {
  private Activity mActivity = null;
  private final int mWriteSettingsRequestCode;

  public ScreenBrightnessPackage(Activity activity, final int writeSettingsRequestCode) {
    mActivity = activity;
    mWriteSettingsRequestCode = writeSettingsRequestCode;
  }

  @Override
  public List<NativeModule> createNativeModules(ReactApplicationContext reactApplicationContext) {
    List<NativeModule> modules = new ArrayList<NativeModule>();
    modules.add(new ScreenBrightnessModule(
            reactApplicationContext,
            mActivity,
            mWriteSettingsRequestCode
    ));
    return modules;
  }

  @Override
  public List<Class<? extends JavaScriptModule>> createJSModules() {
    return Collections.emptyList();
  }

  @Override
  public List<ViewManager> createViewManagers(ReactApplicationContext reactApplicationContext) {
    return Arrays.<ViewManager>asList();
  }
}
