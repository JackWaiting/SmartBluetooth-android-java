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
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.util.Log;

import com.smartcodeunited.lib.bluetooth.managers.BLEDeviceManager;
import com.smartcodeunited.lib.bluetooth.managers.BluetoothDeviceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JackWaiting on 2016/12/27.
 */
public class BluetoothDeviceManagerProxy  {

    public static final String TAG = "DeviceManagerProxy";
    public static final String MAC_ADDRESS_FILTER_PREFIX = "";

    private boolean modifieds = false;
    private Context context;
    private static int currentDeviceMode;  //The current connection mode
    private boolean bluzManReady = false;
    private boolean scanning = false;
    private boolean connecting = false;

    /**
     * Connection status monitoring collection
     */
    private List<BLEDeviceManager.OnConnectionBLEListener> conStateListener;

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
            bluzDeviceMan.setOnDiscoveryServiceBLEListener(onServiceBLEListener);
            bluzDeviceMan.setOnBluetoothDeviceDataReceived(onReceivedDataListener);
            bluzDeviceMan.setOnBluetoothDeviceBluetoothStatusListener(onConnectionBLEListener);
        }
        return bluzDeviceMan;
    }

    private BluetoothDeviceManagerProxy(Context context) {
        this.context = context.getApplicationContext();
        conStateListener = new ArrayList<>();
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
     */
    public void startScanning() {
        Log.i("startDiscoverys", "startDiscoverys");
        bluzDeviceMan = getBluetoothDeviceManager();
        bluzDeviceMan.setOnBluetoothDeviceBluetoothScanningListener(onScanBLEListener);
        bluzDeviceMan.startScan();
    }

    public void sendData(BluetoothGatt bluetoothGatt,
                         String qppData){
        if(bluzDeviceMan != null){
            bluzDeviceMan.sendData(bluetoothGatt,qppData);
        }
    }

    public void sendData(BluetoothGatt bluetoothGatt, byte[] bytes){
        if(bluzDeviceMan != null){
            bluzDeviceMan.sendData(bluetoothGatt,bytes);
        }
    }

    public void sendDebugData(byte[] debugData){
        if(bluzDeviceMan != null){
            bluzDeviceMan.sendDebugData(debugData);
        }
    }

    /**
     * Stop scan bluetooth devices
     */
    public void stopScanning() {
        bluzDeviceMan.stopScan();
    }

    /**
     * Disconnected bluetooth
     */
    public void disconnected() {
        if (bluzDeviceMan != null && connected) {
            bluzDeviceMan.disconnect();
        }
    }

    /**
     * Connect bluetooth
     *
     * @param device
     * @return
     */
    public boolean connectDevice(BluetoothDevice device) {
        Log.i("connectDevice", "connectDevice+device");
        connecting = true;
        bluzDeviceMan = getBluetoothDeviceManager();
        Log.i("connectDevice", "connectDevice+else");
        stopScanning();
        disconnected();
        bluzDeviceMan.connect(device);
        return false;
    }

    public void addOnBluetoothDeviceConnectionStateChangedListener(
            BLEDeviceManager.OnConnectionBLEListener listener) {
        conStateListener.add(listener);
    }

    public void removeOnBluetoothDeviceConnectionStateChangedListener(
            BLEDeviceManager.OnConnectionBLEListener listener) {
        conStateListener.remove(listener);
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

    private BLEDeviceManager.OnReceivedDataListener onBluetoothDeviceReceivedDataListener;

    public void setReceivedDataListener(
            BLEDeviceManager.OnReceivedDataListener discoveryListener) {
        this.onBluetoothDeviceReceivedDataListener = discoveryListener;
    }

    private BLEDeviceManager.OnReceivedDataListener onReceivedDataListener = new BLEDeviceManager.OnReceivedDataListener() {
        @Override
        public void onRecivedData(byte[] data) {
            if(onBluetoothDeviceReceivedDataListener != null){
                onBluetoothDeviceReceivedDataListener.onRecivedData(data);
            }
        }
    };

    private BLEDeviceManager.OnDiscoveryServiceBLEListener onBluetoothDeviceDiscoveryServiceBLEListener;

    public void setDiscoveryServiceBLEListener(
            BLEDeviceManager.OnDiscoveryServiceBLEListener discoveryListener) {
        this.onBluetoothDeviceDiscoveryServiceBLEListener = discoveryListener;
    }


    private BLEDeviceManager.OnDiscoveryServiceBLEListener onServiceBLEListener = new BLEDeviceManager.OnDiscoveryServiceBLEListener() {
        @Override
        public void onDiscoveryServiceChar(String UUIDService, BluetoothGattCharacteristic gattCharacteristic) {
            if(onBluetoothDeviceDiscoveryServiceBLEListener != null){
                onBluetoothDeviceDiscoveryServiceBLEListener.onDiscoveryServiceChar(UUIDService , gattCharacteristic);
            }
        }
    };

    private BLEDeviceManager.OnConnectionBLEListener onConnectionBLEListener = new BLEDeviceManager.OnConnectionBLEListener() {
        @Override
        public void onConnectionStateChanged(BluetoothGatt mBluetoothGatt, int state) {

            Log.i("onConnectionState",mBluetoothGatt.getDevice()+ "     state= "+state);
            switch (state) {
                case BluetoothDeviceManager.BluetoothDeviceConnectionStatus.CONNECTED:
                    connected = true;
                    break;
                case BluetoothDeviceManager.BluetoothDeviceConnectionStatus.DISCONNECTED:
                    connected = false;
                    break;
            }
            connecting = false;
            notifyConntectionStateChanged(mBluetoothGatt, state);
        }
    };

    /**
     * Notify the listener, bluetooth connection state changes
     *
     * @param mBluetoothGatt
     * @param state
     */
    private void notifyConntectionStateChanged(BluetoothGatt mBluetoothGatt, int state) {

        int sizes = conStateListener.size();
        for (int i = 0; i < sizes; i++) {
            conStateListener.get(i).onConnectionStateChanged( mBluetoothGatt, state);
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
            Log.i(TAG,"bluetoothDeviceName= "+ bluetoothDevice.getName() +"       bluetoothDeviceAddress="+bluetoothDevice.getAddress());
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
        onBluetoothDeviceDiscoveryServiceBLEListener = null;
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
