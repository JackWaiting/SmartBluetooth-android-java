package com.smartcodeunited.lib.bluetooth.entitys;

import android.bluetooth.BluetoothDevice;

import java.io.Serializable;

/**
 * Created by misparking on 16/12/27.
 */
public class BleDeviceInfo implements Serializable {
    private  BluetoothDevice device;
    private int rssi;
    private  String name;
    private  String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public BleDeviceInfo(BluetoothDevice device) {
        this.device = device;
    }

    public BleDeviceInfo(){}
    public BleDeviceInfo(String address,String name){
        this.address=address;
        this.name=name;
    }
}
