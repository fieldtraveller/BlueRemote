package com.alex.blueremote;

import java.util.ArrayList;

import helper.bluetooth_helper.BT_spp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;

public class BlueRemote {
	
	static BluetoothAdapter BtAdapter;
	static BluetoothServerSocket Bt_server_socket;
	static boolean discoverability_status=false;

	static byte device_assignment;
	
	static ArrayList<BT_spp> connected_device_list;
	static ArrayList<int[]> foreground_colors;

	static ArrayList<ArrayList<BT_spp>> list_of_devices_assigned_to_components;

	public static final byte assign_all_devices=1;
	public static final byte assign_latest_device_if_list_is_empty=2;
	public static final byte do_not_assign=3;
    
    public static BluetoothAdapter get_BtAdapter() 
	{
		return BtAdapter;
	}

	public static void set_BtAdapter(BluetoothAdapter BtAdapter) 
	{
		BlueRemote.BtAdapter = BtAdapter;
	}
	
	public static BluetoothServerSocket get_Bt_server_socket() {
		return Bt_server_socket;
	}

	public static void set_Bt_server_socket(BluetoothServerSocket bt_server_socket) {
		Bt_server_socket = bt_server_socket;
	}
	
	public static boolean is_discoverability_status() {
		return discoverability_status;
	}

	public static void set_discoverability_status(boolean discoverability_status) {
		BlueRemote.discoverability_status = discoverability_status;
	}
	
	public static byte get_device_assignment() 
	{
		return device_assignment;
	}

	public static void set_device_assignment(byte device_assignment) 
	{
		BlueRemote.device_assignment = device_assignment;
	}

	public static ArrayList<BT_spp> get_connected_device_list() 
	{
		return connected_device_list;
	}
	
	public static BT_spp get_connected_device(int index) 
	{
		return connected_device_list.get(index);
	}
	
	public static void set_connected_device_list(ArrayList<BT_spp> connected_device_list) 
	{
		BlueRemote.connected_device_list = connected_device_list;
	}
	
	public static void add_to_connected_device_list(BT_spp connected_device) 
	{
		BlueRemote.connected_device_list.add(connected_device);
	}
	
	public static ArrayList<int[]> get_foreground_colors() {
		return foreground_colors;
	}

	public static void set_foreground_colors(ArrayList<int[]> foreground_colors) {
		BlueRemote.foreground_colors = foreground_colors;
	}

}
