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
import android.bluetooth.BluetoothGattService;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.smartcodeunited.demo.bluetooth.R;
import com.smartcodeunited.demo.bluetooth.adapter.BluetoothAdapter;
import com.smartcodeunited.demo.bluetooth.bluetooth.BluetoothDeviceManagerProxy;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BluetoothActivity implements View.OnClickListener{

    private BluetoothAdapter bluetoothAdapter;
    private ListView bluetoothListView;
    private BluetoothDeviceManagerProxy proxy;
    private List<BluetoothDevice> bluetoothDevices = new ArrayList<>();
    private List<BluetoothGattService> bluetoothGattServices = new ArrayList<>();

    @Override
    public void scanCallback(List<BluetoothDevice> mListBluetoothDevices) {

        if(bluetoothAdapter != null){
            bluetoothDevices = mListBluetoothDevices;
            bluetoothAdapter.setList(mListBluetoothDevices);
        }
    }

    @Override
    public void connectCallback(BluetoothGatt mBluetoothGatt, int state) {

        if(mBluetoothGatt != null){
            bluetoothGattServices = mBluetoothGatt.getServices();
            Log.i("bluetoothGattServices",mBluetoothGatt.getDevice().getName()+ "   bluetoothGattServices:" + state +"getServices size=" + bluetoothGattServices.size());
            for (int i = 0 ; i <bluetoothGattServices.size(); i ++){
                //Log.i("bluetoothGattServices",bluetoothGattServices.get(i).getUuid()+"");
                //Log.i("bluetoothGattServices",bluetoothGattServices.get(i).getCharacteristics().get(i).getUuid()+"");
            }
            /*if(state == 2){
                Intent intent = new Intent(this,DeviceUUIDActivity.class);
                intent.putExtra("mBluetoothGatt",mBluetoothGatt.getDevice());
                //intent.putExtra("mBluetoothGattService",  );
                startActivity(intent);
            }*/
        }

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    private void setItemClick() {
        if(bluetoothListView != null){
            bluetoothListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(bluetoothDevices!= null && bluetoothDevices.size() > 0)
                        connectDevice(bluetoothDevices.get(position));
                }
            });
        }
    }
    @Override
    protected void initBase() {
        proxy = BluetoothDeviceManagerProxy.getInstance(this);
    }

    @Override
    protected void initUI() {
        initView();
        setItemClick();
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
