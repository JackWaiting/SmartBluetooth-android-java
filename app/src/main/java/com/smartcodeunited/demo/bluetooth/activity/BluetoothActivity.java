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
package com.smartcodeunited.demo.bluetooth.activity;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.smartcodeunited.demo.bluetooth.bluetooth.BluetoothDeviceManagerProxy;
import com.smartcodeunited.lib.bluetooth.managers.BLEDeviceManager;

import java.util.ArrayList;
import java.util.List;

public abstract class BluetoothActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "BluetoothActivity";
    private BluetoothDeviceManagerProxy blzMan;

    private List<BluetoothDevice> mListBluetoothDevices = new ArrayList<>(); //New search list of bluetooth
    public abstract void scanCallback(List<BluetoothDevice> mListBluetoothDevices);
    public abstract void connectCallback(BluetoothGatt mBluetoothGatt, int state);
    public List<BluetoothDevice> getBluetoothDeviceList(){
        return mListBluetoothDevices;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBluetoothManager();

    }

    private void initBluetoothManager() {
        blzMan = BluetoothDeviceManagerProxy
                .getInstance(getApplicationContext());
        blzMan.setScanningListener(onDiscoveryBLEListener);
        blzMan.setReceivedDataListener(onReceivedDataListener);
        blzMan.addOnBluetoothDeviceConnectionStateChangedListener(onConnectionBLEListener);
    }

    private BLEDeviceManager.OnReceivedDataListener onReceivedDataListener = new BLEDeviceManager.OnReceivedDataListener() {
        @Override
        public void onRecivedData(byte[] data) {
        }
    };



    public void connectDevice(BluetoothDevice bluetoothDevice){
        if(blzMan != null){
            blzMan.connectDevice(bluetoothDevice);
        }
    }

    private BLEDeviceManager.OnConnectionBLEListener onConnectionBLEListener = new BLEDeviceManager.OnConnectionBLEListener() {
        @Override
        public void onConnectionStateChanged(BluetoothGatt mBluetoothGatt, int state) {
            connectCallback(mBluetoothGatt,state);
        }
    };

    private BLEDeviceManager.OnDiscoveryBLEListener onDiscoveryBLEListener = new BLEDeviceManager.OnDiscoveryBLEListener() {
        @Override
        public void onDiscoveryStarted() {
            mListBluetoothDevices.clear();
        }

        @Override
        public void onDiscoveryFinished() {

        }

        @Override
        public void onBluetoothDeviceBluetoothScanClassicReceived(BluetoothDevice bluetoothDevice) {

        }

        @Override
        public void onBluetoothDeviceBluetoothScanBLEReceived(BluetoothDevice bluetoothDevice, int rssi, byte[] scanRecord) {

            Log.i(TAG,"bluetoothDeviceName= "+ bluetoothDevice.getName() +"       bluetoothDeviceAddress="+bluetoothDevice.getAddress());
            mListBluetoothDevices.add(bluetoothDevice);
            scanCallback(mListBluetoothDevices);

        }

    };

    @Override
    public void onClick(View v) {

    }


}
