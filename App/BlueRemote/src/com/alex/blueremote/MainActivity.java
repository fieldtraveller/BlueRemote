package com.alex.blueremote;

import android.support.v7.app.AppCompatActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
	
	BlueRemote_global_variables global_variables_object;
	
	final int BT_turn_on_request_code=2;
	final int BT_device_list_activity_request_code=3;
	
	BroadcastReceiver bt_device_connection_status;
	
//	String BT_mac_address="20:13:05:27:09:64";	//ALEXBT
//	String BT_mac_address="98:D3:31:80:18:29";	//BURZO
	
	BT_spp BT_serial_device;
	boolean BT_discovered_device_available=false;
	
	Button channel_up,channel_down,volume_up,volume_down,select,send;
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
		
		power=(Switch)findViewById(R.id.switch1);
		mute=(Switch)findViewById(R.id.switch2);
		
		data=(EditText)findViewById(R.id.editText1);
		
//	    power.setChecked(false);
//	    mute.setChecked(false);
		
		global_variables_object = (BlueRemote_global_variables)getApplicationContext();
		
//		global_variables_object.setBtAdapter(BluetoothAdapter.getDefaultAdapter());
		
	    bt_device_connection_status=new BroadcastReceiver() {
	        
	    	@Override
	        public void onReceive(Context context, Intent intent) {
	            
	    		String action = intent.getAction();
	            
	    		if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) 
	    		{
//	    			BT_serial_device.setDataTransferReady(true);
	    			Log.e(BLUETOOTH_SERVICE, "BT Device Connected");
	            }
	            else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) 
	            {
	            	Log.e(BLUETOOTH_SERVICE, "BT Device about to Disconnect");
	            }
	            else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) 
	            {
//	            	BT_serial_device.setDataTransferReady(false);
	            	Log.e(BLUETOOTH_SERVICE, "BT Device Disconnected");
	            }           
	        }
	    };
	    
	    registerReceiver(bt_device_connection_status,new IntentFilter(android.bluetooth.BluetoothDevice.ACTION_ACL_DISCONNECTED));
	    registerReceiver(bt_device_connection_status,new IntentFilter(android.bluetooth.BluetoothDevice.ACTION_ACL_CONNECTED));
	    registerReceiver(bt_device_connection_status,new IntentFilter(android.bluetooth.BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED));   

//	    Intent splash= new Intent(this, splash.class);
//	    startActivityForResult(splash,BT_turn_on_request_code);
	    	    	    	    
	    channel_up.setOnClickListener(new View.OnClickListener() {
	    	
	    	@Override
			public void onClick(View v) {	    		
	    		BT_serial_device.write(channel_up_command);
	    	}
	    	
		});
	    
	    channel_down.setOnClickListener(new View.OnClickListener() {
	    	
	    	@Override
			public void onClick(View v) {
	    		
	    		BT_serial_device.write(channel_down_command);
	    		
	    	}
	    	
		});
	    
	    volume_up.setOnClickListener(new View.OnClickListener() {
	    	
	    	@Override
			public void onClick(View v) {
	    		
	    		BT_serial_device.write(volume_down_command);
	    		
	    	}
	    	
		});
	    
	    volume_down.setOnClickListener(new View.OnClickListener() {
	    	
	    	@Override
			public void onClick(View v) {
	    		
	    		BT_serial_device.write(volume_down_command);
	    		
	    	}
	    	
		});
	    
	    select.setOnClickListener(new View.OnClickListener() {
	    	
	    	@Override
			public void onClick(View v) {
	    		
	    		BT_serial_device.write(select_command);
	    		
	    	}
	    	
		});
	    
	    send.setOnClickListener(new View.OnClickListener() {
	    	
	    	@Override
			public void onClick(View v) {
	    			    		
	    	}
	    	
		});
	    	    
	    power.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	   
		    @Override
			public void onCheckedChanged(CompoundButton buttonView,	boolean isChecked) {
		    	
		    	BT_serial_device.write(power_command);
				
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
		    	
		    	BT_serial_device.write(mute_command);
		    	
		    	if(isChecked)
				{
					
				}
				else
				{
					
				}
		    	
			}
	    });  
	}
	
//	@Override
//	protected void onRestart()
//	{
//		super.onRestart();
//		Log.e("Where?", "OnRestart");
//	}
//	
//	@Override
//	protected void onStart()
//	{
//		super.onStart();
//		Log.e("Where?", "OnStart");
//	}
//	
	@Override
	protected void onResume()
	{
		super.onResume();
//		Log.e("Where?", "OnResume");
		
		if(BT_discovered_device_available == true )
		{
			if(BT_serial_device.BT_socket == null)
			{
			    BT_serial_device.connect();
			}	
		}
		else
		{
			Intent device_list= new Intent(this, device_list_activity.class);
		    startActivityForResult(device_list, BT_device_list_activity_request_code);
		}
	}
//	
//	@Override
//	protected void onPause ()
//	{
//		super.onPause();
//		Log.e("Where?", "OnPause");
//	}
//	
//	@Override
//	protected void onStop ()
//	{
//		super.onStop();
//		Log.e("Where?", "OnStop");
//	}
	
	//*
	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  
    {
		super.onActivityResult(requestCode, resultCode, data);
		
//		if(requestCode==BT_turn_on_request_code)
//		{
//			if(data.getBooleanExtra("closeApp",false) == true)
//			{
//				this.finish();
//				Log.e("Quit", "Bye-Bye");
//			}
//			else
//			{
//				Intent device_list= new Intent(this, device_list_activity.class);
//			    startActivityForResult(device_list, BT_device_list_activity_request_code);
//			}
//			
//		}
		
		if(requestCode==BT_device_list_activity_request_code)
		{
			if(data.getBooleanExtra("closeApp",false) == true)
			{
				this.finish();
				Log.e("Quit", "Bye-Bye");
			}
			else
			{
				BT_serial_device=data.getParcelableExtra("BT_device_selected");
				BT_discovered_device_available=true;
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
	    
		if(BT_discovered_device_available == true )
		{
			BT_serial_device.disconnect();
		}
		
		unregisterReceiver(bt_device_connection_status);
		
		//Direct Turn Off BT by App 
		if (global_variables_object.getBtAdapter().isEnabled()) 
		{
			global_variables_object.getBtAdapter().disable();
		}
			
		Log.d(BLUETOOTH_SERVICE, "Bluetooth turned Off.");
	}
}
