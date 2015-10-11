package com.alex.blueremote;

import java.util.ArrayList;

import helper.bluetooth_helper.BT_spp;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;

public class BlueRemote extends Application{
	
	BluetoothAdapter BtAdapter;
	BluetoothServerSocket Bt_server_socket;
	boolean discoverability_status;

	byte device_assignment;
	
	ArrayList<BT_spp> connected_device_list;
	ArrayList<ArrayList<BT_spp>> list_of_devices_assigned_to_components;

	public static final byte assign_all_devices=1;
	public static final byte assign_latest_device_if_list_is_empty=2;
	public static final byte do_not_assign=3;
    
    @Override
    public void onCreate() 
    {
        super.onCreate();
        
        list_of_devices_assigned_to_components=new ArrayList<ArrayList<BT_spp>>(); 
        set_discoverability_status(false);
    }
    
	public BluetoothAdapter get_BtAdapter() 
	{
		return BtAdapter;
	}

	public void set_BtAdapter(BluetoothAdapter btAdapter) 
	{
		BtAdapter = btAdapter;
	}
	
	public BluetoothServerSocket get_Bt_server_socket() {
		return Bt_server_socket;
	}

	public void set_Bt_server_socket(BluetoothServerSocket bt_server_socket) {
		Bt_server_socket = bt_server_socket;
	}
	
	public boolean is_discoverability_status() {
		return discoverability_status;
	}

	public void set_discoverability_status(boolean discoverability_status) {
		this.discoverability_status = discoverability_status;
	}
	
	public byte get_device_assignment() 
	{
		return device_assignment;
	}

	public void set_device_assignment(byte device_assignment) 
	{
		this.device_assignment = device_assignment;
	}

	public ArrayList<BT_spp> get_connected_device_list() 
	{
		return connected_device_list;
	}
	
	public BT_spp get_connected_device(int index) 
	{
		return connected_device_list.get(index);
	}
	
	public void set_connected_device_list(ArrayList<BT_spp> connected_device_list) 
	{
		this.connected_device_list = connected_device_list;
	}
	
	public void add_to_connected_device_list(BT_spp connected_device) 
	{
		this.connected_device_list.add(connected_device);
	}
}
