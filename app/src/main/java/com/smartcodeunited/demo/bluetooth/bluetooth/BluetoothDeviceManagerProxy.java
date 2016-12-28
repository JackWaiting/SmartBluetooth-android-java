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
package com.smartcodeunited.demo.bluetooth.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;

import com.smartcodeunited.lib.bluetooth.managers.BLEDeviceManager;
import com.smartcodeunited.lib.bluetooth.managers.BluetoothDeviceManager;

/**
 * Created by JackWaiting on 2016/12/27.
 */
public class BluetoothDeviceManagerProxy  {

    public static final String TAG = "BluetoothDeviceManagerProxy";
    public static final String MAC_ADDRESS_FILTER_PREFIX = "";

    private boolean modifieds = false;
    private Context context;
    private static int currentDeviceMode;  //The current connection mode
    private boolean bluzManReady = false;
    private boolean scanning = false;

    /**
     * Bluetooth control class library
     */
    public static BluetoothDeviceManagerProxy proxy;
    /**
     * Whether the connected
     */
    private boolean connected;
    /**
     * Bluetooth device management class
     */
    private static BluetoothDeviceManager bluzDeviceMan;
    /**
     * The connected bluetooth devices
     */
    private BluetoothDevice connectedDevice;
    private BluetoothDevice targetDevice;
    private String targetDeviceName;

    /**
     * Access to search to the target device
     *
     * @return
     */
    public BluetoothDevice getTargetDevice() {
        return targetDevice;
    }

    /**
     * Access to search to the target name
     *
     * @return
     */
    public String getTargetDeviceName() {
        return targetDeviceName;
    }


    /**
     * Get the bluetooth management class
     *
     * @return
     */
    public BluetoothDeviceManager getBluetoothDeviceManager() {
        if (bluzDeviceMan == null) {
            Log.d("", "getBluetoothDeviceManager");
            bluzManReady = false;
            bluzDeviceMan = BluetoothDeviceManager.getInstance(context);
            bluzDeviceMan.setDeviceType(BluetoothDeviceManager.BluetoothType.BLE);
        }
        return bluzDeviceMan;
    }

    private BluetoothDeviceManagerProxy(Context context) {
        this.context = context.getApplicationContext();
        getBluetoothDeviceManager();
    }

    public static BluetoothDeviceManagerProxy getInstance(Context context) {
        if (proxy == null) {
            proxy = new BluetoothDeviceManagerProxy(context.getApplicationContext());
        }
        return proxy;
    }

    public Context getContext() {
        return context;
    }

    /**
     * For instance objects already in existence, if there is no it returns null
     *
     * @return
     */
    public static BluetoothDeviceManagerProxy getInstance() {
        return proxy;
    }

    /**
     * Scan bluetooth devices
     *
     *
     */
    public void startScanning() {
        Log.i("startDiscoverys", "startDiscoverys");
        bluzDeviceMan = getBluetoothDeviceManager();
        bluzDeviceMan.setOnBluetoothDeviceBluetoothScanningListener(onScanBLEListener);
        bluzDeviceMan.startScan();
    }

    private BLEDeviceManager.OnDiscoveryBLEListener onBluetoothDeviceDiscoveryListener;

    public void setScanningListener(
            BLEDeviceManager.OnDiscoveryBLEListener discoveryListener) {
        this.onBluetoothDeviceDiscoveryListener = discoveryListener;
    }

    public void removeScanningListener(BLEDeviceManager.OnDiscoveryBLEListener discoveryListener) {
        if (discoveryListener == onBluetoothDeviceDiscoveryListener) {
            onBluetoothDeviceDiscoveryListener = null;
        }
    }

    private BLEDeviceManager.OnDiscoveryBLEListener onScanBLEListener = new BLEDeviceManager.OnDiscoveryBLEListener() {
        @Override
        public void onDiscoveryStarted() {
            scanning = true;
            targetDevice = null;
            if (onBluetoothDeviceDiscoveryListener != null) {
                onBluetoothDeviceDiscoveryListener.onDiscoveryStarted();
            }
        }

        @Override
        public void onDiscoveryFinished() {
            scanning = false;
            if (onBluetoothDeviceDiscoveryListener != null) {
                onBluetoothDeviceDiscoveryListener.onDiscoveryFinished();
            }
        }

        @Override
        public void onBluetoothDeviceBluetoothScanClassicReceived(BluetoothDevice bluetoothDevice) {
            String address = bluetoothDevice.getAddress();

            if (address.startsWith(MAC_ADDRESS_FILTER_PREFIX)) {
                targetDevice = bluetoothDevice;
                if (onBluetoothDeviceDiscoveryListener != null) {
                    onBluetoothDeviceDiscoveryListener.onBluetoothDeviceBluetoothScanClassicReceived(bluetoothDevice);
                }
            }
        }

        @Override
        public void onBluetoothDeviceBluetoothScanBLEReceived(BluetoothDevice bluetoothDevice, int rssi, byte[] scanRecord) {
            String address = bluetoothDevice.getAddress();

            if (address.startsWith(MAC_ADDRESS_FILTER_PREFIX)) {
                targetDevice = bluetoothDevice;
                if (onBluetoothDeviceDiscoveryListener != null) {
                    onBluetoothDeviceDiscoveryListener.onBluetoothDeviceBluetoothScanBLEReceived(bluetoothDevice,rssi,scanRecord);
                }
            }
        }
    };

    /**
     * Release the bluetooth, players such as resources
     * Note: need to be at the end of the exit the program execution, after or in the code will not perform
     */
    public void destory() {
        //disconnected();
        bluzManReady = false;
        proxy = null;
        onBluetoothDeviceDiscoveryListener = null;
        if (bluzDeviceMan != null) {
            if (bluzDeviceMan.isScanning()) {
                bluzDeviceMan.stopScan();
            }
            Log.d("ManagerProxy", "bluzDeviceMan.release();");
            //Last, the release () method will be called System. Exist (), in the code following it cannot perform
            //bluzDeviceMan.release();
            System.exit(0);
        }
    }
}
