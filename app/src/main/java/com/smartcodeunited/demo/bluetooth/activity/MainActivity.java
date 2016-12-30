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
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.smartcodeunited.demo.bluetooth.R;
import com.smartcodeunited.demo.bluetooth.adapter.BluetoothAdapter;
import com.smartcodeunited.demo.bluetooth.bluetooth.BluetoothDeviceManagerProxy;

import java.util.List;


public class MainActivity extends BluetoothActivity implements View.OnClickListener{

    private BluetoothAdapter bluetoothAdapter;
    private ListView bluetoothListView;
    private BluetoothDeviceManagerProxy proxy;


    @Override
    public void scanCallback(List<BluetoothDevice> mListBluetoothDevices) {

        if(bluetoothAdapter != null){
            bluetoothAdapter.setList(getBluetoothDeviceList());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBase();
        initView();
    }

    private void initBase() {
        proxy = BluetoothDeviceManagerProxy.getInstance();
    }

    private void initView() {
        bluetoothListView = (ListView) findViewById(R.id.lv_bluetooth);
        bluetoothAdapter = new BluetoothAdapter(this);
        bluetoothListView.setAdapter(bluetoothAdapter);

        findViewById(R.id.btn_start_scanning).setOnClickListener(this);
        findViewById(R.id.btn_stop_scanning).setOnClickListener(this);
        findViewById(R.id.btn_disconnected).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btn_start_scanning:
                if(proxy != null){
                    proxy.startScanning();
                }
                break;

            case R.id.btn_stop_scanning:
                break;

            case R.id.btn_disconnected:
                break;
        }
    }
}
