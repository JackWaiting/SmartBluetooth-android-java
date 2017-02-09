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

import android.bluetooth.BluetoothGattCharacteristic;
import android.widget.TextView;

import com.smartcodeunited.demo.bluetooth.R;
import com.smartcodeunited.demo.bluetooth.bluetooth.BluetoothDeviceManagerProxy;
import com.smartcodeunited.lib.bluetooth.managers.BLEDeviceManager;

public class DeviceUUIDActivity extends BaseActivity {

    private BluetoothDeviceManagerProxy blzMan;
    private String mStrBluetoothGattName;
    private TextView tvServiceUUID,tvCharacteristicUUID;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_device_uuid;
    }

    @Override
    protected void initBase() {
        initBluetoothManager();
        mStrBluetoothGattName = getIntent().getExtras().getString("mStrBluetoothGattName");
    }

    @Override
    protected void initUI() {
        setTitleText(mStrBluetoothGattName);
        setLeftBtn(R.string.back,0);

        tvServiceUUID = (TextView) findViewById(R.id.tv_service_uuid);
        tvCharacteristicUUID = (TextView) findViewById(R.id.tv_characteristic_uuid);
    }

    private void initBluetoothManager() {
        blzMan = BluetoothDeviceManagerProxy
                .getInstance(getApplicationContext());
        blzMan.setDiscoveryServiceBLEListener(onServiceBLEListener);
    }

    private BLEDeviceManager.OnDiscoveryServiceBLEListener onServiceBLEListener = new BLEDeviceManager.OnDiscoveryServiceBLEListener() {
        @Override
        public void onDiscoveryServiceChar(String UUIDService, BluetoothGattCharacteristic gattCharacteristic) {
        }
    };
}
