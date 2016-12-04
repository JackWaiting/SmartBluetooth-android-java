package com.smartcodeunited.lib.bluetooth.managers;

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
    // Characteristic
    private static String uuidQppService = "0000fee9-0000-1000-8000-00805f9b34fb";
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

    public interface OnConnectionListener {
        /**
         * @param mBluetoothGatt
         * @param state          1为连接，2为未连接
         */
        public void onConnectionStateChanged(BluetoothGatt mBluetoothGatt, int state);
    }

    private static BLEDeviceManager sBLEDeviceManager = new BLEDeviceManager();

    private static OnConnectionListener sOnConnectionListener;

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

    /**
     * 连接状态监听
     *
     * @param onConnectionListener
     */
    public void setOnConnectionListener(OnConnectionListener onConnectionListener) {
        sOnConnectionListener = onConnectionListener;
        Log.d(TAG, "setOnConnectionListener：" + (sOnConnectionListener == null));
    }

    public void connectBLEDevice(Context context, BluetoothDevice device) {
        closeGatt();
        mContext = context;
        mDevice = device;
        mBluetoothGatt = device.connectGatt(mContext, false, mGattCallback);
        Log.i("tag", "connect " + mDevice.getName());
    }

    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            Log.i(TAG, "onConnectionStateChange : " + status + "  newState : "
                    + newState);

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                mBluetoothGatt.discoverServices();//扫描设备所支持的服务
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                closeGatt();
            }
            sOnConnectionListener.onConnectionStateChanged(gatt, newState);
        }

        /**
         * 设备服务的回调
         * @param gatt
         * @param status
         */
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            isServicesDiscovered = setEnable(gatt, uuidQppService, uuidQppCharWrite);
        }

        /**
         * 写入设备的回调
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
         * 发送特征值到设备后，设备反馈应用端
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
     * 初始化一些必须的UUID
     *
     * @param bluetoothGatt
     * @param qppServiceUUID 可修改的ServiceUUID
     * @param writeCharUUID  可修改的writeCharUUID
     *                       可混淆，后续修改
     * @return
     */
    private   boolean setEnable(BluetoothGatt bluetoothGatt,
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

        if (!setCharacteristicNotification(bluetoothGatt,arrayNtfCharList.get(0), true))
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
     * 接收设备端数据
     *
     * @param bluetoothGatt
     * @param characteristic
     */
    private  void updateValueForNotification(BluetoothGatt bluetoothGatt,
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


    private  void parse(byte[] commands) {
        // 切取命令：去掉 头+尾+长度位+设备识别位
        int length = commands.length - 2 - 1 - 1;
        final byte[] subCommand = new byte[length];

        for (int i = 0; i < length; i++) {
            subCommand[i] = commands[3 + i];
        }
        handler.post(new Runnable() {

            @Override
            public void run() {
                switch (subCommand[0]) {
                    case CommandProtocol.Type.FEEDBACK_INQUIRY: {
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

    public void disconnect() {
        if (mBluetoothGatt == null) {
            Log.w("Qn Dbg", "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
    }
}
