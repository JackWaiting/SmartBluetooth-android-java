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
package com.smartcodeunited.lib.bluetooth.managers;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.smartcodeunited.lib.bluetooth.commands.CommandManager;
import com.smartcodeunited.lib.bluetooth.commands.CommandProtocol;
import com.smartcodeunited.lib.bluetooth.tools.LogManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by misparking on 16/12/4.
 */
public class BLEDeviceManager {


    // / send data
    private static BluetoothGattCharacteristic writeCharacteristic; // / write
    // Characteristic；uuid for test.
    private static String uuidQppService = "0000fee9-0000-1000-8000-00805f9b34fb";
    // Characteristic；uuid for test.
    private static String uuidQppCharWrite = "d44bc439-abfd-45a2-b575-925416129600";
    public static final int qppServerBufferSize = 20;
    // / receive data
    private static BluetoothGattCharacteristic notifyCharacteristic;

    private static ArrayList<BluetoothGattCharacteristic> arrayNtfCharList = new ArrayList<BluetoothGattCharacteristic>();
    /**
     * notify Characteristic
     */
    private static byte notifyCharaIndex = 0;
    private static boolean NotifyEnabled = false;
    private static final String UUIDDes = "00002902-0000-1000-8000-00805f9b34fb";


    private static BLEDeviceManager sBLEDeviceManager = new BLEDeviceManager();

    private static BluetoothGatt mBluetoothGatt = null;

    private Context mContext;
    private BluetoothDevice mDevice;

    private static Handler handler = new Handler();

    protected static final String TAG = BLEDeviceManager.class.getSimpleName();

    private BLEDeviceManager() {

    }

    public static BLEDeviceManager getInstance() {
        if (sBLEDeviceManager != null)
            sBLEDeviceManager = new BLEDeviceManager();
        return sBLEDeviceManager;
    }

    public static BluetoothGatt getBluetoothGatt() {
        return mBluetoothGatt;
    }

    public static String getAddress() {
        String address = null;
        if (mBluetoothGatt != null)
            address = mBluetoothGatt.getDevice().getAddress();
        return address;
    }

    public static String getName() {
        String name = null;
        if (mBluetoothGatt != null)
            name = mBluetoothGatt.getDevice().getName();
        return name;
    }

    public static boolean isServicesDiscovered;

    public interface OnReceivedDataListener {
        public void onRecivedData(byte[] data);
    }

    private static OnReceivedDataListener sOnReceivedDataListener;

    public void setOnReceivedDataListener(OnReceivedDataListener onReceivedDataListener) {
        sOnReceivedDataListener = onReceivedDataListener;
    }


    public interface OnConnectionBLEListener {
        /**
         * @param mBluetoothGatt
         * @param state          connected:1,disconnected:2.
         */
        public void onConnectionStateChanged(BluetoothGatt mBluetoothGatt, int state);
    }

    private static OnConnectionBLEListener sOnConnectionListener;

    public interface OnDiscoveryBLEListener {

        public void onDiscoveryStarted();

        public void onDiscoveryFinished();

        public void onBluetoothDeviceBluetoothScanClassicReceived(BluetoothDevice bluetoothDevice);

        public void onBluetoothDeviceBluetoothScanBLEReceived(BluetoothDevice bluetoothDevice, int rssi, byte[] scanRecord);

    }

    private static OnDiscoveryBLEListener sOnDiscoveryBLEListener;

    public interface OnDiscoveryServiceBLEListener{
        public void onDiscoveryServiceChar(String UUIDService, BluetoothGattCharacteristic gattCharacteristic);
    }
    private  static OnDiscoveryServiceBLEListener sOnDiscoveryServiceBLEListener;


    /**
     * Connection status Listener
     *
     * @param onConnectionListener
     */
    public void setOnConnectionListener(OnConnectionBLEListener onConnectionListener) {
        sOnConnectionListener = onConnectionListener;
    }

    public void setOnDiscoveryBLEListener(OnDiscoveryBLEListener onDiscoveryBLEListener) {
        sOnDiscoveryBLEListener = onDiscoveryBLEListener;
    }
    public void setOnDiscoveryServiceBLEListener(OnDiscoveryServiceBLEListener onDiscoveryServiceBLEListener){
        sOnDiscoveryServiceBLEListener=onDiscoveryServiceBLEListener;
    }


    public void connectBLEDevice(Context context, BluetoothDevice device) {
        closeGatt();
        mContext = context;
        mDevice = device;
        mBluetoothGatt = device.connectGatt(mContext, false, mGattCallback);
        Log.i("tag", "connect " + mDevice.getName());
    }

    /**
     * Implements callback methods for GATT events that the app cares about.  For example,
     * connection change and services discovered.
     */
    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            Log.i(TAG, "onConnectionStateChange : " + status + "  newState : "
                    + newState);

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                mBluetoothGatt.discoverServices();//Scanning equipment supported by the service
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                isServicesDiscovered=false;
                closeGatt();
            }
            sOnConnectionListener.onConnectionStateChanged(gatt, newState);
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            final List<BluetoothGattService> services = gatt.getServices();
            for (BluetoothGattService service : services) {
                List<BluetoothGattCharacteristic> gattCharacteristics = service
                        .getCharacteristics();
                for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                    int charaProp = gattCharacteristic.getProperties();
//                    if ((charaProp | BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
                        if (sOnDiscoveryServiceBLEListener != null)
                            sOnDiscoveryServiceBLEListener.onDiscoveryServiceChar(service.getUuid().toString(), gattCharacteristic);
                        Log.e(TAG, "gattCharacteristic UUID-->" + gattCharacteristic.getUuid());

                        if (!isServicesDiscovered){
                            isServicesDiscovered = setEnable(gatt, service.getUuid().toString(), gattCharacteristic.getUuid().toString());
                        }
//                    }
                }
            }



        }

        public void setUUID(String uuidService, BluetoothGattCharacteristic gattCharacteristic) {
            uuidQppService = uuidService;
            writeCharacteristic = gattCharacteristic;
        }
        /**
         * Write callback
         * @param gatt
         * @param characteristic
         * @param status
         */
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
//            Log.e(TAG, status==BluetoothGatt.GATT_SUCCESS?"Send success!!":"Send failed!!");
            Log.d(TAG, status + "--" + Arrays.toString(characteristic.getValue()));

        }

        /**
         * After the feature value is sent to the device, the device feedback application
         * @param gatt
         * @param characteristic
         */
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            updateValueForNotification(gatt, characteristic);
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            setLENextNotify(gatt, true);
        }
    };


    /**
     * Initialize some of the necessary UUID
     *
     * @param bluetoothGatt
     * @param qppServiceUUID Modified of ServiceUUID
     * @param writeCharUUID  Modified of writeCharUUID
     * @return
     */
    //TODO Can be confused
    private boolean setEnable(BluetoothGatt bluetoothGatt,
                              String qppServiceUUID, String writeCharUUID) {
        resetQppField();
        if (qppServiceUUID != null)
            uuidQppService = qppServiceUUID;
        if (writeCharUUID != null)
            uuidQppCharWrite = writeCharUUID;
        if (bluetoothGatt == null) {//|| qppServiceUUID.isEmpty()|| writeCharUUID.isEmpty()
            Log.e(TAG, "invalid arguments");
            return false;
        }
        BluetoothGattService qppService = bluetoothGatt.getService(UUID
                .fromString(uuidQppService));
        if (qppService == null) {
            Log.e(TAG, "Qpp service not found");
            return false;
        }
        //
        List<BluetoothGattCharacteristic> gattCharacteristics = qppService
                .getCharacteristics();
        for (int j = 0; j < gattCharacteristics.size(); j++) {
            BluetoothGattCharacteristic chara = gattCharacteristics.get(j);
            if (chara.getUuid().toString().equals(uuidQppCharWrite)) {
                // Log.i(TAG,"Wr char is "+chara.getUuid().toString());
                writeCharacteristic = chara;
            } else if (chara.getProperties() == BluetoothGattCharacteristic.PROPERTY_NOTIFY) {
                // Log.i(TAG,"NotiChar UUID is : "+chara.getUuid().toString());
                notifyCharacteristic = chara;
                arrayNtfCharList.add(chara);
            }
        }

        if (!setCharacteristicNotification(bluetoothGatt, arrayNtfCharList.get(0), true))
            return false;
        notifyCharaIndex++;

        return true;
    }

    private static void resetQppField() {
        writeCharacteristic = null;
        notifyCharacteristic = null;

        arrayNtfCharList.clear();
        // NotifyEnabled=false;
        notifyCharaIndex = 0;
    }

    private boolean setLENextNotify(BluetoothGatt bluetoothGatt,
                                    boolean EnableNotifyChara) {
        if (notifyCharaIndex == arrayNtfCharList.size()) {
            NotifyEnabled = true;
            return true;
        }
        return setCharacteristicNotification(bluetoothGatt,
                arrayNtfCharList.get(notifyCharaIndex++), EnableNotifyChara);
    }

    private boolean setCharacteristicNotification(
            BluetoothGatt bluetoothGatt,
            BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (bluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return false;
        }

        bluetoothGatt.setCharacteristicNotification(characteristic, enabled);

        try {
            BluetoothGattDescriptor descriptor = characteristic
                    .getDescriptor(UUID.fromString(UUIDDes));
            if (descriptor != null) {
                descriptor
                        .setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                return (bluetoothGatt.writeDescriptor(descriptor));
            } else {
                Log.e(TAG, "descriptor is null");
                return false;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return true;
    }


    /**
     * Receiving device data
     *
     * @param bluetoothGatt
     * @param characteristic
     */
    private void updateValueForNotification(BluetoothGatt bluetoothGatt,
                                            BluetoothGattCharacteristic characteristic) {
        if (bluetoothGatt == null || characteristic == null) {
            Log.e(TAG, "invalid arguments");
            return;
        }
        if (!NotifyEnabled) {
            Log.e(TAG, "The notifyCharacteristic not enabled");
            return;
        }
        String strUUIDForNotifyChar = characteristic.getUuid().toString();
        final byte[] receiveData = characteristic.getValue();

        // if (qppData != null && qppData.length > 0)
        // OnVoltageListener.onQppReceiveData(bluetoothGatt, strUUIDForNotifyChar,
        // qppData);
        LogManager.d(TAG, "receive-->" + receiveData);
//        printLog(receiveData);
        if (CommandManager.isCommandValid(receiveData)) {
            parse(receiveData);
        }
    }


    /**
     * send data to device
     *
     * @param bluetoothGatt
     * @param qppData
     * @return
     */
    private boolean sendData(BluetoothGatt bluetoothGatt,
                             String qppData) {
        boolean ret = false;
        if (bluetoothGatt == null) {
            Log.e(TAG, "BluetoothAdapter not initialized !");
            return false;
        }

        if (qppData == null) {
            Log.e(TAG, "qppData = null !");
            return false;
        }
        writeCharacteristic.setValue(qppData);
        return bluetoothGatt.writeCharacteristic(writeCharacteristic);
    }


    /**
     * send data to device
     *
     * @param bluetoothGatt
     * @param bytes
     * @return
     */
    private boolean sendData(BluetoothGatt bluetoothGatt, byte[] bytes) {
        boolean ret = false;
        if (bluetoothGatt == null) {
            Log.e(TAG, "BluetoothAdapter not initialized !");
            return false;
        }

        if (bytes == null) {
            Log.e(TAG, "qppData = null !");
            return false;
        }
        writeCharacteristic.setValue(bytes);
        return bluetoothGatt.writeCharacteristic(writeCharacteristic);
    }

    /**
     * parse the receive command
     *
     * @param commands
     */
    private void parse(final byte[] commands) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                if (sOnReceivedDataListener != null) {
                    sOnReceivedDataListener.onRecivedData(commands);
                }
                switch (commands[0]) {
                    case CommandProtocol.Type.FEEDBACK_CONTROL: {

                    }
                    break;
                }
            }
        });
    }

    public void closeGatt() {

        if (mBluetoothGatt != null) {
            mBluetoothGatt.close();
            mBluetoothGatt = null;
        }
    }

    public void disConnectBLEDevice() {
        if (mBluetoothGatt == null) {
            Log.w("Qn Dbg", "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
        mBluetoothGatt = null;
    }

    private boolean mScanning;
    private Handler mHandler = new Handler();
    // Stops scanning after 10 seconds.


    public void scanBLE() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScanning = false;
                if (sOnDiscoveryBLEListener != null){
                    if (mBluetoothDevicesFound != null) {
                        mBluetoothDevicesFound.clear();
                    }
                    sOnDiscoveryBLEListener.onDiscoveryFinished();
                }
                BluetoothDeviceManager.getBluetoothAdapter().stopLeScan(mLeScanCallback);
            }
        }, CommandProtocol.SCAN_TIMEOUT);
        if (sOnDiscoveryBLEListener != null)
            sOnDiscoveryBLEListener.onDiscoveryStarted();
        mScanning = true;
        BluetoothDeviceManager.getBluetoothAdapter().startLeScan(mLeScanCallback);
    }

    public void stopScan() {
        if (mScanning){
            if (mBluetoothDevicesFound != null) {
                mBluetoothDevicesFound.clear();
            }
            BluetoothDeviceManager.getBluetoothAdapter().stopLeScan(mLeScanCallback);
        }
    }


    /**
     * <p>
     * Result device for scanning once.
     * </p>
     *
     * @reset The variable must be reset at the proper time!
     *
     * @since 1.0.0
     */
    private  List<BluetoothDevice> mBluetoothDevicesFound = new ArrayList<BluetoothDevice>();

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            if (sOnDiscoveryBLEListener != null){
                for (BluetoothDevice bluetoothDevice:mBluetoothDevicesFound){
                    if (!bluetoothDevice.getAddress().equalsIgnoreCase(device.getAddress())){

                        mBluetoothDevicesFound.add(device);
                        sOnDiscoveryBLEListener.onBluetoothDeviceBluetoothScanBLEReceived(device, rssi, scanRecord);

                    }
                }


            }
        }
    };

    /**
     * send commands to device for test
     *
     * @param testByte
     */
    public void sendDebugData(byte[] testByte) {
        sendData(mBluetoothGatt, testByte);
    }

    /**
     * send commands to device for test
     *
     * @param testStrings
     */
    public void sendDebugData(String testStrings) {
        sendData(mBluetoothGatt, testStrings);
    }


}
