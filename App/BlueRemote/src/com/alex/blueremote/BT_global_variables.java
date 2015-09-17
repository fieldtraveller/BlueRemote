package com.alex.blueremote;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;

public class BT_global_variables extends Application{
	
	BluetoothAdapter BtAdapter;

	public BluetoothAdapter getBtAdapter() {
		return BtAdapter;
	}

	public void setBtAdapter(BluetoothAdapter btAdapter) {
		BtAdapter = btAdapter;
	}
	
}
