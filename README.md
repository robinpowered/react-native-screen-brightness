# react-native-screen-brightness

Access and update the system brightness on a device.

## React Native compatibility
|         React Native version          |         Compatible `react-native-screen-brightness` version   |
| :------------------: | :------------------: |
| v0.60+ | v2.x |
| v0.27 - v0.59 | v1.x |

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
