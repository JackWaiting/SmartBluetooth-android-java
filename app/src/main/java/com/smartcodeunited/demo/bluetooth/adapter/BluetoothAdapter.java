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
package com.smartcodeunited.demo.bluetooth.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.smartcodeunited.demo.bluetooth.adapter.adapteritemview.BluetoothItemView;

import java.util.ArrayList;
import java.util.List;

public class BluetoothAdapter extends BaseAdapter {
	private Context context;
	private List<BluetoothDevice> bluetoothDevices = new ArrayList<BluetoothDevice>();
	public BluetoothAdapter(Context context){
		this.context = context;
	}

	public void setList(List<BluetoothDevice> bluetoothDevices){
		this.bluetoothDevices = bluetoothDevices;
		notifyDataSetChanged();
	}
	

	@Override
	public int getCount() {
		return bluetoothDevices.size();
	}

	@Override
	public BluetoothDevice getItem(int position) {
		return bluetoothDevices.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		BluetoothItemView itemView = null;
		if(convertView == null){
			itemView = new BluetoothItemView(context);
		}else{
			itemView = (BluetoothItemView) convertView;
		}
		itemView.render(getItem(position));
		itemView.setTag(getItem(position));

		return itemView;
	}

	private View.OnClickListener onItemClickListener;
	public void setOnItemClickListener(View.OnClickListener listener){
		onItemClickListener = listener;
	}
	

}
