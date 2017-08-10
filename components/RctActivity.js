'use strict'; 
/**
 * This exposes the native CallLogAndroid module as a JS module. Allows you
 * to add JS methods here for your native module
 */

import { NativeModules } from 'react-native';
module.exports = NativeModules.CallLogAndroid;