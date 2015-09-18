package com.alex.blueremote;

import java.util.HashSet;
import java.util.Set;

import android.support.v7.app.AppCompatActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
	
	BluetoothAdapter BtAdapter;
	BT_global_variables BT_global_variables_1;
	
	final int BT_turn_on_fragment_request_code=2;
	final int BT_device_list_request_code=3;
	
	BroadcastReceiver bt_device_found_receiver;
	BroadcastReceiver bt_discovery_started_receiver;
	BroadcastReceiver bt_discovery_finished_receiver;
	
	Set<BluetoothDevice> pairedDevices;
	Set<BluetoothDevice> unpairedDevices = new HashSet<BluetoothDevice>();
	
//	String BT_mac_address="20:13:05:27:09:64";	//ALEXBT
//	String BT_mac_address="98:D3:31:80:18:29";	//BURZO
	String BT_mac_address;
	
	BT_spp BT_serial;
	
	Button channel_up,channel_down,volume_up,volume_down,select,send,discovery;
	Switch power,mute;
	EditText data;
	
	byte channel_up_command=0;
	byte channel_down_command=1;
	byte volume_up_command=2;
	byte volume_down_command=4;
	byte select_command=8;
	byte power_command=32;
	byte mute_command=64;
	
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
		discovery=(Button)findViewById(R.id.button7);
		
		power=(Switch)findViewById(R.id.switch1);
		mute=(Switch)findViewById(R.id.switch2);
		
		data=(EditText)findViewById(R.id.editText1);
		
		BT_global_variables_1 = (BT_global_variables)getApplicationContext();
		
		BtAdapter = BluetoothAdapter.getDefaultAdapter();
		
		 BT_global_variables_1.setBtAdapter(BtAdapter);
		
		if (BtAdapter == null) 
		{
		    Toast.makeText(getApplicationContext(), "No BlueTooth Hardware Detected.\nApp Exited", Toast.LENGTH_LONG).show();
		    this.finish();
		}
		
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
	    
	    Intent device_list= new Intent(this, device_list_activity.class);
	    startActivityForResult(device_list, BT_device_list_request_code);
	    	    
//	    BT_serial=new BT_spp(BtAdapter, BT_mac_address);
//	    BT_serial.connect();
	    	    
	    channel_up.setOnClickListener(new View.OnClickListener() {
	    	
	    	@Override
			public void onClick(View v) {	    		
	    		BT_serial.write(channel_up_command);
	    	}
	    	
		});
	    
	    channel_down.setOnClickListener(new View.OnClickListener() {
	    	
	    	@Override
			public void onClick(View v) {
	    		
	    		BT_serial.write(channel_down_command);
	    		
	    	}
	    	
		});
	    
	    volume_up.setOnClickListener(new View.OnClickListener() {
	    	
	    	@Override
			public void onClick(View v) {
	    		
	    		BT_serial.write(volume_down_command);
	    		
	    	}
	    	
		});
	    
	    volume_down.setOnClickListener(new View.OnClickListener() {
	    	
	    	@Override
			public void onClick(View v) {
	    		
	    		BT_serial.write(volume_down_command);
	    		
	    	}
	    	
		});
	    
	    select.setOnClickListener(new View.OnClickListener() {
	    	
	    	@Override
			public void onClick(View v) {
	    		
	    		BT_serial.write(select_command);
	    		
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
			public void onCheckedChanged(CompoundButton buttonView,	boolean isChecked) {
		    	
		    	BT_serial.write(power_command);
				
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
			public void onCheckedChanged(CompoundButton buttonView,	boolean isChecked) {
		    	
		    	BT_serial.write(mute_command);
		    	
		    	if(isChecked)
				{
					
				}
				else
				{
					
				}
		    	
			}
	    });
	    	    
	}
	
	@Override
	protected void onRestart ()
	{
		Log.e("Where?", "OnRestart");
	}
	
	@Override
	protected void onStart ()
	{
		Log.e("Where?", "OnStart");
	}
	
	@Override
	protected void onResume ()
	{
		Log.e("Where?", "OnResume");
	}
	
	@Override
	protected void onPause ()
	{
		Log.e("Where?", "OnPause");
	}
	
	@Override
	protected void onStop ()
	{
		Log.e("Where?", "OnStop");
	}
	
	
	//*
	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  
    {
		super.onActivityResult(requestCode, resultCode, data);
		
//		if(requestCode==BT_turn_on_fragment_request_code)
//		{
//			Log.d(BLUETOOTH_SERVICE,"resultCode:"+resultCode);
//			
//			if(resultCode==RESULT_OK)
//			{
//				Log.d(BLUETOOTH_SERVICE, "Bluetooth is On.");
//			}
//			else if(resultCode==RESULT_CANCELED)
//			{
//				Log.d(BLUETOOTH_SERVICE, "Bluetooth is Off.");
//			}
//			
//		}
		
		if(requestCode==BT_device_list_request_code)
		{
			if(data.getBooleanExtra("closeApp",false) == true)
			{
				this.finish();
				
				Log.e("Quit", "Bye-Bye");
			}
			else
			{
				BT_mac_address=data.getStringExtra("mac_address");
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
	    
//		BT_serial.disconnect();
		
		//Direct Turn Off BT by App 
		if (BtAdapter.isEnabled()) 
		{
			BtAdapter.disable();
		}
			
		Log.d(BLUETOOTH_SERVICE, "Bluetooth turned Off.");
	}
}
