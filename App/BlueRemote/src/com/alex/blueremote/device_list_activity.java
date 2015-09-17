package com.alex.blueremote;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import android.support.v7.app.AppCompatActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

public class device_list_activity extends AppCompatActivity {

	BluetoothAdapter BtAdapter;
	
	BT_global_variables BT_global_variables_1;
	
	BroadcastReceiver bt_device_found_receiver;
	BroadcastReceiver bt_discovery_started_receiver;
	BroadcastReceiver bt_discovery_finished_receiver;
	
	Set<BluetoothDevice> pairedDevices;
	Set<BluetoothDevice> unpairedDevices = new HashSet<BluetoothDevice>();
	
	ListView lv_1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dl_layout_1);
		
		lv_1=(ListView)findViewById(R.id.List_1);
		
//		Log.e("Where am I?", "Device List Activity");
		
		BT_global_variables_1 = (BT_global_variables)getApplicationContext();
		
		BtAdapter=BT_global_variables_1.getBtAdapter();
		
		Log.d(BLUETOOTH_SERVICE, "Bluetooth On? "+(BtAdapter.getState()==BluetoothAdapter.STATE_ON));
		
		//*
	    pairedDevices = BtAdapter.getBondedDevices();
	    
	    Log.d(BLUETOOTH_SERVICE,"Paired Devices: "+pairedDevices.size());
	    
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.lv_textview);

	    if (pairedDevices.size() > 0) 
	    {
	    	for (BluetoothDevice device : pairedDevices) 
	    	{
	    		Log.d(BLUETOOTH_SERVICE,device.getName()+" "+device.getAddress());
	    		adapter.add(device.getName()+"\n"+device.getAddress());
	    	}
	    }
	    	    
	    lv_1.setAdapter(adapter);
	    
	    lv_1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
	               String mac_address=((String) lv_1.getItemAtPosition(position)).substring((((String) lv_1.getItemAtPosition(position)).length())-17);
	               
//	               Toast.makeText(getApplicationContext(),""+mac_address , Toast.LENGTH_LONG).show();
			}

         });
	    
 
	    //*/
	    
	    /*
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
		
		//unregister receivers
//		unregisterReceiver(bt_device_found_receiver);
//		unregisterReceiver(bt_discovery_started_receiver);
//		unregisterReceiver(bt_discovery_finished_receiver);

	}
}
