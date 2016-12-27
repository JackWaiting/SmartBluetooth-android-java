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
package com.smartcodeunited.demo.bluetooth;


import android.bluetooth.BluetoothDevice;

public final class BluetoothDeviceManager
{
    private static final BluetoothDeviceManager sBluetoothDeviceManager = new BluetoothDeviceManager();
    private static OnBluetoothDeviceBluetoothStatusListener sOnBluetoothDeviceBluetoothStatusListener;

    /**
     * @reference https://en.wikipedia.org/wiki/List_of_Bluetooth_profiles
     */
    public static final class BluetoothDeviceProfile
    {
        public static final int A2DP = 1;
        public static final int ATT = 2;
        public static final int AVRCP = 3;
        public static final int BIP = 4;
        public static final int BPP = 5;
        public static final int CIP = 6;
        public static final int CTP = 7;
        public static final int DIP = 8;
        public static final int DUN = 9;
        public static final int FAX = 10;
        public static final int FTP = 11;
        public static final int GAVDP = 12;
        public static final int GAP = 13;
        public static final int GATT = 14;
        public static final int GOEP = 15;
        public static final int HCRP = 16;
        public static final int HDP = 17;
        public static final int HFP = 18;
        public static final int HID = 19;
        public static final int HSP = 20;
        public static final int ICP = 21;
        public static final int LAP = 22;
        public static final int MAP = 23;
        public static final int OBEX = 24;
        public static final int OPP = 25;
        public static final int PAN = 26;
        public static final int PBAP = 27;
        public static final int PXP = 28;
        public static final int SPP = 29;
        public static final int SDAP = 30;
        public static final int SIMAP = 31;
        public static final int SYNCH = 32;
        public static final int SYNCML = 33;
        public static final int VDP = 34;
        public static final int WAPB = 35;
        public static final int UDI = 36;
        public static final int ESDP = 37;
        public static final int VCP = 38;
    }

    public static final class BluetoothType
    {
        public static final int CLASSIC = 1;
        public static final int BLE = 2;
    }

    public static final class BluetoothStatus
    {
        public static final int TURNING_ON = 1;
        public static final int ON = 2;
        public static final int TURNING_OFF = 3;
        public static final int OFF = 4;
        public static final int SCANNING = 5;
    }

    public static final class BluetoothDeviceConnectionStatus
    {
        public static final int CONNECTING = 1;
        public static final int CONNECTED = 2;
        public static final int DISCONNECTING = 3;
        public static final int DISCONNECTED = 4;
    }

    public interface OnBluetoothDeviceBluetoothStatusListener
    {
        public void onBluetoothDeviceBluetoothStatusChanged(int bluetoothStatus);
    }

    public interface OnBluetoothDeviceBluetoothScanningListener
    {
        public void onBluetoothDeviceBluetoothScanningClassicReceived(BluetoothDevice bluetoothDevice);

        public void onBluetoothDeviceBluetoothScanningBLEReceived(BluetoothDevice bluetoothDevice, int rssi, byte[] scanRecord);
    }

    public interface OnBluetoothDeviceBluetoothConnectionStatusListener
    {
        public void onBluetoothDeviceBluetoothConnectionStatusChanged(BluetoothDeviceConnectionStatus bluetoothDeviceConnectionStatus, BluetoothDevice bluetoothDevice, BluetoothDeviceProfile bluetoothDeviceProfile);
    }

    private BluetoothDeviceManager()
    {}

    public static BluetoothDeviceManager getInstance()
    {
        return sBluetoothDeviceManager;
    }

    public void setOnBluetoothDeviceBluetoothStatusListener(OnBluetoothDeviceBluetoothStatusListener onBluetoothDeviceBluetoothStatusListener)
    {
        sOnBluetoothDeviceBluetoothStatusListener = onBluetoothDeviceBluetoothStatusListener;
    }

    public void setOnBluetoothDeviceBluetoothScanningListener(OnBluetoothDeviceBluetoothScanningListener onBluetoothDeviceBluetoothScanningListener)
    {

    }

    public void setOnBluetoothDeviceBluetoothConnectionStatusListener(OnBluetoothDeviceBluetoothConnectionStatusListener onBluetoothDeviceBluetoothConnectionStatusListener)
    {

    }

    public boolean isSupported()
    {
        return false;
    }

    public boolean isEnabled()
    {
        return false;
    }

    public boolean isMACAddressValid(String address)
    {
        return false;
    }

    public boolean isScanning(BluetoothType bluetoothType)
    {
        return false;
    }

    public boolean isConnected(BluetoothDevice bluetoothDevice, BluetoothDeviceProfile bluetoothDevicProfile)
    {
        return false;
    }

    /**
     * Note: You can only scan for Bluetooth LE devices or scan for Classic Bluetooth devices, as described in Bluetooth. You cannot scan for both Bluetooth LE and classic devices at the same time.
     *
     * @param bluetoothType
     */
    public void startScanning(BluetoothType bluetoothType)
    {
        if(bluetoothType != null){
            sBluetoothDeviceManager.startScanning(bluetoothType);
        }

    }

    /**
     * Note: You can only scan for Bluetooth LE devices or scan for Classic Bluetooth devices, as described in Bluetooth. You cannot scan for both Bluetooth LE and classic devices at the same time.
     *
     * @param bluetoothType
     */
    public void stopScanning(BluetoothType bluetoothType)
    {

    }

    public void turnOn()
    {

    }

    public void turnOff()
    {

    }

    public void connect(BluetoothDevice bluetoothDevice, BluetoothDeviceProfile bluetoothDeviceProfile)
    {

    }

    public void disconnect(BluetoothDevice bluetoothDevice, BluetoothDeviceProfile bluetoothDeviceProfile)
    {

    }

}
