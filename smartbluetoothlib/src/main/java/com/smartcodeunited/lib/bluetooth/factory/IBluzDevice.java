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
