package com.alex.blueremote;

import java.util.ArrayList;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;

public class BlueRemote extends Application{
	
	BluetoothAdapter BtAdapter;
	ArrayList<BT_spp> connected_device_list;
	private static Context app_context;

    @Override
    public void onCreate() 
    {
        super.onCreate();
        app_context = getApplicationContext();
    }
    
	public BluetoothAdapter getBtAdapter() {
		return BtAdapter;
	}

	public void setBtAdapter(BluetoothAdapter btAdapter) {
		BtAdapter = btAdapter;
	}
	
	public ArrayList<BT_spp> getConnected_device_list() {
		return connected_device_list;
	}
	
	public BT_spp getConnected_device(int index) {
		return connected_device_list.get(index);
	}
	
	public void setConnected_device_list(ArrayList<BT_spp> connected_device_list) {
		this.connected_device_list = connected_device_list;
	}
	
	public void add_to_Connected_device_list(BT_spp connected_device) {
		this.connected_device_list.add(connected_device);
	}
	
	public static Context getContext() {
	    return app_context;
	}
}
