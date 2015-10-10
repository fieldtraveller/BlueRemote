package com.alex.blueremote;

import java.util.ArrayList;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;

public class BlueRemote extends Application{
	
	BluetoothAdapter BtAdapter;
	byte device_assignment;
	
	ArrayList<BT_spp> connected_device_list;
	ArrayList<ArrayList<BT_spp>> list_of_devices_assigned_to_components;
	private static Context app_context;

	public static final byte assign_all_devices=1;
	public static final byte assign_latest_device_if_list_is_empty=2;
	public static final byte do_not_assign=3;
    
    @Override
    public void onCreate() 
    {
        super.onCreate();
        app_context = getApplicationContext();
        
        list_of_devices_assigned_to_components=new ArrayList<ArrayList<BT_spp>>(); 
    }
    
	public BluetoothAdapter getBtAdapter() {
		return BtAdapter;
	}

	public void setBtAdapter(BluetoothAdapter btAdapter) {
		BtAdapter = btAdapter;
	}
	
	public byte getDevice_assignment() {
		return device_assignment;
	}

	public void setDevice_assignment(byte device_assignment) {
		this.device_assignment = device_assignment;
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
