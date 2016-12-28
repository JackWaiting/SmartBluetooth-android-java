package com.smartcodeunited.demo.bluetooth.adapter.adapteritemview;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.smartcodeunited.demo.bluetooth.R;

public class BluetoothItemView extends FrameLayout {

	private Context context;
	private TextView tvDeviceName;
	private TextView tvDeviceAddress;

	public BluetoothItemView(Context context) {
		super(context);
		init(context);
		this.context = context;
	}

	private void init(final Context context) {
		LayoutInflater.from(context).inflate(R.layout.item_bluetooth_list, this);
		tvDeviceName = (TextView) findViewById(R.id.tv_device_name_item);
		tvDeviceAddress = (TextView) findViewById(R.id.tv_device_address);
	}


	public void render(BluetoothDevice bluetoothDevice) {
		if(bluetoothDevice == null){
			return;
		}
		tvDeviceName.setText(bluetoothDevice.getName());
		tvDeviceAddress.setText(bluetoothDevice.getAddress());
	}

}
