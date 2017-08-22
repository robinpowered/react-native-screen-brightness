# react-native-screen-brightness

Access and update the system brightness on a device.

## Installation instructions for Android

These are instructions for the current `npm` release, 0.1.0.

### Install the npm package

Run:

`npm install --save react-native-screen-brightness`

### Modify android/app/build.gradle

Add `compile project(":react-native-screen-brightness")` to the dependencies section:

```
dependencies {
     compile fileTree(dir: "libs", include: ["*.jar"])
     compile "com.android.support:appcompat-v7:23.0.1"
     compile "com.facebook.react:react-native:0.20.+"
     compile project(":react-native-screen-brightness")
 }
```

### Modify android/settings.gradle

Add

```
include ':react-native-screen-brightness'
project(':react-native-screen-brightness').projectDir = new File(settingsDir, '../node_modules/react-native-screen-brightness/android')
```

After the `include ':app'` line.

### Modify android/app/src/main/java/com/magicmirror/MainActivity.java

Add the following after the other `import` lines at the top of the file:

```java
import com.robinpowered.react.ScreenBrightness.ScreenBrightnessPackage;
```

Then, add `new ScreenBrightnessPackage(this)` to the `getPackages()` method (note
the comma after `new MainReactPackage()`):

```java
public class MainActivity extends ReactActivity {
     @Override
     protected List<ReactPackage> getPackages() {
       return Arrays.<ReactPackage>asList(
         new MainReactPackage(),
         new ScreenBrightnessPackage(this)
       );
     }
 }
```

### Add to your JavaScript:

Before any use of the code:

```js
+import { NativeModules } from "react-native";
+const { ScreenBrightness } = NativeModules;
```

See the Example section below.

Now you can build as normal.

## Example
```js
import {NativeModules} from 'react-native';
var {SystemBrightness} = NativeModules;

SystemBrightness.setBrightness(0.5) // between 0 and 1

SystemBrightness.getBrightness().then(brightness => {
  console.log('brightness', brightness);
});
```
