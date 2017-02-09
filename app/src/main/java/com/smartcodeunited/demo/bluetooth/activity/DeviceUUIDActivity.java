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

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.smartcodeunited.demo.bluetooth.R;
import com.smartcodeunited.demo.bluetooth.bluetooth.BluetoothDeviceManagerProxy;
import com.smartcodeunited.lib.bluetooth.managers.BLEDeviceManager;

public class DeviceUUIDActivity extends BaseActivity implements View.OnClickListener {

    private BluetoothDeviceManagerProxy blzMan;
    private String mStrBluetoothGattName;
    private TextView tvServiceUUID,tvCharacteristicUUID;
    private String strUUIDService;
    private String strUUIDCharacteristic;
    private TextView tvReceivedData,btnSend;
    private EditText ediWriteData;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_device_uuid;
    }

    @Override
    protected void initBase() {
        initBluetoothManager();
        mStrBluetoothGattName = getIntent().getExtras().getString("mBluetoothGattName");
        strUUIDService = getIntent().getExtras().getString("strUUIDService");
        strUUIDCharacteristic = getIntent().getExtras().getString("strUUIDCharacteristic");
    }

    @Override
    protected void initUI() {
        setTitleText(mStrBluetoothGattName);
        setLeftBtn(R.string.back,0);

        tvServiceUUID = (TextView) findViewById(R.id.tv_service_uuid);
        tvCharacteristicUUID = (TextView) findViewById(R.id.tv_characteristic_uuid);
        tvReceivedData = (TextView) findViewById(R.id.tv_received_data);
        ediWriteData = (EditText) findViewById(R.id.edi_write_data);
        btnSend = (TextView) findViewById(R.id.btn_send);

        tvServiceUUID.setText(strUUIDService);
        tvCharacteristicUUID.setText(strUUIDCharacteristic);
        btnSend.setOnClickListener(this);

    }

    private void initBluetoothManager() {
        blzMan = BluetoothDeviceManagerProxy
                .getInstance(this);
        blzMan.setReceivedDataListener(onReceivedDataListenerer);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btn_send:
                blzMan.sendDebugData(ediWriteData.getText().toString());
                break;
        }
    }

    private BLEDeviceManager.OnReceivedDataListener onReceivedDataListenerer = new BLEDeviceManager.OnReceivedDataListener() {
        @Override
        public void onRecivedData(byte[] data) {
            for (int i = 0; i < data.length; i ++){
                Log.i("onRecivedData","data = " + data[i]);
            }
        }
    };

}
