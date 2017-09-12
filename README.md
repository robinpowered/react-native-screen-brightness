# react-native-screen-brightness

Access and update the system brightness on a device.

## Install

Install with [yarn](https://yarnpkg.com) or [npm](https://www.npmjs.com).

```shell
yarn add react-native-screen-brightness
```

```shell
npm i --save react-native-screen-brightness
```

## Example

```js
import ScreenBrightness from 'react-native-screen-brightness';

ScreenBrightness.setBrightness(0.5); // between 0 and 1

ScreenBrightness.getBrightness().then(brightness => {
  console.log('brightness', brightness);
});
```
