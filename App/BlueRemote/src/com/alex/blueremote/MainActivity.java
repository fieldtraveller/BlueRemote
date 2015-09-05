package com.alex.blueremote;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import android.support.v7.app.AppCompatActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

	BluetoothAdapter BtAdapter;
	final int BT_turn_on_fragment_request_code=2;
	
	BroadcastReceiver bt_device_found_receiver;
	BroadcastReceiver bt_discovery_started_receiver;
	BroadcastReceiver bt_discovery_finished_receiver;
	
	BluetoothDevice BT_device;
	
	Set<BluetoothDevice> pairedDevices;
	Set<BluetoothDevice> unpairedDevices = new HashSet<BluetoothDevice>();
	
	BluetoothServerSocket BT_s_socket;
    UUID BT_module_uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothSocket BT_socket;
    
	String BT_module_mac="20:13:05:27:09:64";

	Button channel_up,channel_down,volume_up,volume_down,select,send;
	Switch power,mute;
	EditText data;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		channel_up=(Button)findViewById(R.id.button1);
		channel_down=(Button)findViewById(R.id.button5);
		volume_up=(Button)findViewById(R.id.button4);
		volume_down=(Button)findViewById(R.id.button2);
		select=(Button)findViewById(R.id.button3);
		send=(Button)findViewById(R.id.button6);
		
		power=(Switch)findViewById(R.id.switch1);
		mute=(Switch)findViewById(R.id.switch2);
		
		data=(EditText)findViewById(R.id.editText1);
		
		BtAdapter = BluetoothAdapter.getDefaultAdapter();
		/*
		if (BtAdapter != null) 
		{
		    Log.d(BLUETOOTH_SERVICE, "Bluetooth HW exist.");
		}
		//*/
		
		//*
		//Direct Turn On BT by App 
		if (!BtAdapter.isEnabled()) 
		{
			BtAdapter.enable();
			
			while(BtAdapter.getState()==BluetoothAdapter.STATE_TURNING_ON)
			{
				//Log.d(BLUETOOTH_SERVICE, "Bluetooth turning On.");
			}
		}
	    Log.d(BLUETOOTH_SERVICE, "Bluetooth turned On.");
	    
	    //*/
	    
		/*
		//Request User to turn on BT 
	    //Can not turned off
	    if (!BtAdapter.isEnabled()) 
		{
			Log.d(BLUETOOTH_SERVICE, "Bluetooth is Off.");
			Log.d(BLUETOOTH_SERVICE, "Turning on Bluetoooth.");
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, BT_turn_on_fragment_request_code);
		    //startActivity(enableBtIntent);
		    
		    
		    while(BtAdapter.getState()==BluetoothAdapter.STATE_TURNING_ON)
			{
				//Log.d(BLUETOOTH_SERVICE, "Bluetooth turning On.");
			}
			
			
		}
		else
		{
			Log.d(BLUETOOTH_SERVICE, "Bluetooth is On.");
		}
		//*/
	    
	    /*
	    pairedDevices = BtAdapter.getBondedDevices();
	    Log.d(BLUETOOTH_SERVICE,"Paired Devices: "+pairedDevices.size());
	    
	    if (pairedDevices.size() > 0) 
	    {
	    	for (BluetoothDevice device : pairedDevices) 
	    	{
	    		Log.d(BLUETOOTH_SERVICE,device.getName()+" "+device.getAddress());
	    	}
	    }
	    
	    bt_device_found_receiver = new BroadcastReceiver() {
	        
			@Override
			public void onReceive(Context context, Intent intent) {
			    
				String action = intent.getAction();

				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	            	
	            	Log.d(BLUETOOTH_SERVICE,"BT Device Found");

	            	BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	                
	                unpairedDevices.add(device);
	                //Log.d(BLUETOOTH_SERVICE,device.getName()+" "+device.getAddress());
	            }
	        	
			}
	    };
	    
	    bt_discovery_started_receiver = new BroadcastReceiver() {
	        
			@Override
			public void onReceive(Context context, Intent intent) {
			    
				String action = intent.getAction();
	            
				if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
	            	
					Log.d(BLUETOOTH_SERVICE,"BT Discovery Started");
	            }
	        	
			}
	    };
	    
	    bt_discovery_finished_receiver = new BroadcastReceiver() {
	        
			@Override
			public void onReceive(Context context, Intent intent) {
			    
				String action = intent.getAction();
	            
				if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
	            	
					Log.d(BLUETOOTH_SERVICE,"BT Discovery Finished");
					Log.d(BLUETOOTH_SERVICE,"UnPaired Devices: "+unpairedDevices.size());
				    
					if (unpairedDevices.size() > 0) 
				    {
					    for (BluetoothDevice device : unpairedDevices) 
		    	    	{
		    	    		Log.d(BLUETOOTH_SERVICE,device.getName()+" "+device.getAddress());
		    	    	}
				    }
					
					if(BtAdapter.isDiscovering());
					{
						Log.d(BLUETOOTH_SERVICE,"BT Discovery Cancelled:"+BtAdapter.cancelDiscovery());
					}
					
				}
	        	
			}
	    };
	    
	    IntentFilter bt_device_found_intent_filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
	    registerReceiver(bt_device_found_receiver, bt_device_found_intent_filter); 
	    
	    IntentFilter bt_discovery_started_intent_filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
	    registerReceiver(bt_discovery_started_receiver, bt_discovery_started_intent_filter);
	    
	    IntentFilter bt_discovery_finished_intent_filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
	    registerReceiver(bt_discovery_finished_receiver, bt_discovery_finished_intent_filter);
	    
	    Log.d(BLUETOOTH_SERVICE,"BT Discovery Started:"+BtAdapter.startDiscovery());
	    //Log.d(BLUETOOTH_SERVICE,"BT isDiscovering:"+BtAdapter.isDiscovering());
	    //*/
	    
	    /*
	    Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
	    discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
	    startActivity(discoverableIntent);
	    //*/
	    
	    /*
	    try 
	    {
	    	BT_s_socket = BtAdapter.listenUsingRfcommWithServiceRecord("BlueRemote", BT_module_uuid);
	    	
	    	 BT_socket=BT_s_socket.accept();
	    	 
	    	 if(socket!=null)
	    	 {
	    		 Log.d(BLUETOOTH_SERVICE,"Connection Succesful.");
	    	 }
		}
	    catch (IOException e) 
	    {
			e.printStackTrace();
		}
	    //*/
	    
	    //*
	    BT_device = BtAdapter.getRemoteDevice(BT_module_mac);
	    
	    //*
	    if(BT_device!=null)
   	 	{
	    	Log.d(BLUETOOTH_SERVICE,"BT Device Obtained.");
   	 	}
	    else
	    {
	    	Log.d(BLUETOOTH_SERVICE,"BT Device:Null.");
	    }
	    
	    //*
	    try 
	    {
			//BT_socket = BT_device.createRfcommSocketToServiceRecord(BT_module_uuid);
			BT_socket = BT_device.createInsecureRfcommSocketToServiceRecord(BT_module_uuid); 
			BT_device.
			if(BT_socket!=null)
	   	 	{
		    	Log.d(BLUETOOTH_SERVICE,"Socket Created.");
	   	 	}
		    else
		    {
		    	Log.d(BLUETOOTH_SERVICE,"Socket:Null.");
		    }
			
			BT_socket.connect();
			
			Log.d(BLUETOOTH_SERVICE,"isConnected():"+BT_socket.isConnected());
			
			OutputStream BT_outStream = BT_socket.getOutputStream();
			BT_outStream.write(0);
			BT_outStream.write(1);
			BT_outStream.write(2);
			BT_outStream.write(4);
			BT_outStream.write(8);
			BT_outStream.write(16);
			BT_outStream.write(32);
			BT_outStream.write(64);
			BT_outStream.write(128);
			BT_outStream.write(255);
			BT_outStream.close();
			
			BT_socket.close();
			
		} 
	    catch (IOException e) 
	    {
			e.printStackTrace();
		}
	    //*/
	    
	    channel_up.setOnClickListener(new View.OnClickListener() {
	    	
	    	@Override
			public void onClick(View v) {
	    		
	    	}
	    	
		});
	    
	    channel_down.setOnClickListener(new View.OnClickListener() {
	    	
	    	@Override
			public void onClick(View v) {
	    		
	    	}
	    	
		});
	    
	    volume_up.setOnClickListener(new View.OnClickListener() {
	    	
	    	@Override
			public void onClick(View v) {
	    		
	    	}
	    	
		});
	    
	    volume_down.setOnClickListener(new View.OnClickListener() {
	    	
	    	@Override
			public void onClick(View v) {
	    		
	    	}
	    	
		});
	    
	    select.setOnClickListener(new View.OnClickListener() {
	    	
	    	@Override
			public void onClick(View v) {
	    		
	    	}
	    	
		});
	    
	    send.setOnClickListener(new View.OnClickListener() {
	    	
	    	@Override
			public void onClick(View v) {
	    		
	    	}
	    	
		});
	    
	    power.setChecked(false);
	    mute.setChecked(false);
	    
	    power.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	   
		    @Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				
		    	if(isChecked)
				{
					
				}
				else
				{
					mute.setChecked(false);
				}
				
			}
	    });
	    
	    mute.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	   
		    @Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
		    	
		    	if(isChecked)
				{
					
				}
				else
				{
					
				}
		    	
			}
	    });
	    	    
	}
	
	/*
	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  
    {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode==BT_turn_on_fragment_request_code)
		{
			Log.d(BLUETOOTH_SERVICE,"resultCode:"+resultCode);
			
			if(resultCode==RESULT_OK)
			{
				Log.d(BLUETOOTH_SERVICE, "Bluetooth is On.");
			}
			else if(resultCode==RESULT_CANCELED)
			{
				Log.d(BLUETOOTH_SERVICE, "Bluetooth is Off.");
			}
			
		}
        
    }  
	//*/
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onDestroy ()
	{
		super.onDestroy();
	    
		if(BT_socket.isConnected())
		{
			try 
			{
				BT_socket.close();
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		
		//Direct Turn Off BT by App 
		if (BtAdapter.isEnabled()) 
		{
			BtAdapter.disable();
		}
		
		//unregister receivers
//		unregisterReceiver(bt_device_found_receiver);
//		unregisterReceiver(bt_discovery_started_receiver);
//		unregisterReceiver(bt_discovery_finished_receiver);
		
			
		Log.d(BLUETOOTH_SERVICE, "Bluetooth turned Off.");
	}
}
