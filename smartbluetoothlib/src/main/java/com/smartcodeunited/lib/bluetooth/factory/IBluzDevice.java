/*
 * Copyright (C) 2016 SmartCodeUnited http://www.smartcodeunited.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.smartcodeunited.lib.bluetooth.factory;

import android.bluetooth.BluetoothDevice;

/**
 * Created by misparking on 16/12/27.
 */
public interface IBluzDevice {


    public  void setOnConnectionListener(OnConnectionListener paramOnConnectionListener);

    public  void setOnDiscoveryListener(OnDiscoveryListener paramOnDiscoveryListener);

    public static  interface OnConnectionListener
    {
        public  void onConnected(BluetoothDevice paramBluetoothDevice);

        public  void onDisconnected(BluetoothDevice paramBluetoothDevice);
    }

    public static  interface OnDiscoveryListener
    {
        public  void onConnectionStateChanged(BluetoothDevice paramBluetoothDevice, int paramInt);

        public  void onDiscoveryStarted();

        public  void onDiscoveryFinished();

        public  void onFound(BluetoothDevice paramBluetoothDevice);
    }
}
