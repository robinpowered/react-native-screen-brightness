# react-native-screen-brightness

Access and update the system brightness on a device.

_Installation guide in progress_

## Example
```js
import {NativeModules} from 'react-native';
var {SystemBrightness} = NativeModules;

SystemBrightness.setBrightness(0.5) // between 0 and 1

SystemBrightness.getBrightness().then(brightness => {
  console.log('brightness', brightness);
});
```
