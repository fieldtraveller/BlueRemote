package com.alex.blueremote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.support.v7.app.AppCompatActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.view.ViewGroup;

public class device_list_activity_2 extends AppCompatActivity {

	BluetoothAdapter BtAdapter;
	
	BT_global_variables BT_global_variables_1;
	
	BroadcastReceiver bt_device_found_receiver;
	BroadcastReceiver bt_discovery_started_receiver;
	BroadcastReceiver bt_discovery_finished_receiver;
	
	Set<BluetoothDevice> pairedDevices;
	Set<BluetoothDevice> unpairedDevices = new HashSet<BluetoothDevice>();
	
	ExpandableListView elv_1;
	    
    ArrayList<HashMap<String, String>> group_list=new ArrayList<HashMap<String, String>>();
    
    ArrayList<ArrayList<HashMap<String, String>>> group_child_list=new ArrayList<ArrayList<HashMap<String, String>>>(); 
    ArrayList<HashMap<String, String>> paired_device_list = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> unpaired_device_list = new ArrayList<HashMap<String, String>>();
    
    SimpleExpandableListAdapter elv_adapter;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dl_layout_2);
		
		elv_1=(ExpandableListView)findViewById(R.id.eList_1);
		
		BT_global_variables_1 = (BT_global_variables)getApplicationContext();
		BtAdapter=BT_global_variables_1.getBtAdapter();
		
//		Log.d(BLUETOOTH_SERVICE, "Bluetooth On? "+(BtAdapter.getState()==BluetoothAdapter.STATE_ON));
		
		//*
	    pairedDevices = BtAdapter.getBondedDevices();
	    
	    Log.d(BLUETOOTH_SERVICE,"Paired Devices: "+pairedDevices.size());
	    	    
	    if (pairedDevices.size() > 0) 
	    {
	    	for (BluetoothDevice device : pairedDevices) 
	    	{
	    		Log.d(BLUETOOTH_SERVICE,device.getName()+" "+device.getAddress());

	    		HashMap<String, String> temp = new HashMap<String, String>();
	        	temp.put("group_child_item", device.getName()+"\n"+device.getAddress());
	        	paired_device_list.add(temp);
	    	}
	    }
	    //*/
	    
	    
//	    HashMap<String, String> temp = new HashMap<String, String>();
//    	temp.put("group_item_1","Paired Devices");
//    	temp.put("group_item_2","Un-Paired Devices");
//	    group_list.add(temp);
	    
	    {
	    	HashMap<String, String> temp = new HashMap<String, String>();
	    	temp.put("group_item","Paired Devices");
		    group_list.add(temp);
		    
	    }
	    {
	    	HashMap<String, String> temp = new HashMap<String, String>();
			temp.put("group_item","Un-Paired Devices");
		    group_list.add(temp);
	    }

	    group_child_list.add(paired_device_list);
	    group_child_list.add(unpaired_device_list);
	    
	    elv_adapter = new SimpleExpandableListAdapter(
	                    this,			
	                    (List<HashMap<String, String>>)group_list,
	                    R.layout.elv_textview_group,             
	                    new String[] {"group_item"},  			
//	                    new String[] {"group_item_1","group_item_2"},
	                    new int[] { R.id.elv_group },    		
	                    (List<ArrayList<HashMap<String, String>>>)group_child_list,
	                    R.layout.elv_textview_group_child,        
	                    new String[] {"group_child_item"},      	
	                    new int[] { R.id.elv_group_child}     		
	                	){
	    	
            @Override
            public View getChildView(int groupPosition, int childPosition,
                    boolean isLastChild, View convertView, ViewGroup parent) {

                TextView tv = (TextView)super.getChildView(groupPosition, childPosition, isLastChild,convertView, parent);
                
//                tv.setBackgroundColor(Color.GREEN);
//                
//                String mac_address=tv.getText().toString();
//				Log.e("text",mac_address);
//	    		
                return tv;
            }

            @Override
            public View getGroupView(int groupPosition, boolean isExpanded,
                    View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub
                TextView tv = (TextView) super.getGroupView(groupPosition, isExpanded, convertView, parent);
                
                return tv;
            }

	    	};
	    
	    elv_1.setAdapter(elv_adapter);
	        
//	    Log.e("GroupCount",elv_1.getCount()+"");
//	    elv_1.expandGroup(0,true);
//	    elv_1.expandGroup(1,true);
	    
	    elv_1.setOnGroupClickListener(new OnGroupClickListener() {

	    	@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
	    		
	    		if(elv_1.isGroupExpanded(groupPosition))
	    		{
	    			elv_1.collapseGroup(groupPosition);
	    			
	    			if(groupPosition==1)
		    		{
	    				if(BtAdapter.isDiscovering())
	    				{
//		    				BtAdapter.cancelDiscovery();
		    				Log.d(BLUETOOTH_SERVICE,"BT Discovery Cancelled:"+BtAdapter.cancelDiscovery());		
		    			}
		    		}
	    			else
	    			{
	    				
	    			}
	    		}
	    		else
	    		{
	    			elv_1.expandGroup(groupPosition);
	    			
	    			if(groupPosition==1)
		    		{
	    				elv_adapter.notifyDataSetInvalidated();
	    				Log.d("Success?",""+unpaired_device_list.retainAll(paired_device_list));
	    				elv_adapter.notifyDataSetChanged();
	    				
		    			BtAdapter.startDiscovery();
//		    			Log.d(BLUETOOTH_SERVICE,"BT Discovery Started:"+BtAdapter.startDiscovery());
		    			Log.d("Click","Yes");
		    		}
		    		else
		    		{
		    			
		    		}
	    		}
	    		
				return true;
			}

         });
	    
	    elv_1.setOnChildClickListener(new OnChildClickListener() {

	    	@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {

				String mac_address=((TextView)v.findViewById(R.id.elv_group_child)).getText().toString();
				mac_address=mac_address.substring(mac_address.length()-17);
				
	    		Log.e("MAC Address",mac_address);
	    		
	            return true;
			}

         });

	    //*
	    bt_device_found_receiver = new BroadcastReceiver() {
	        
			@Override
			public void onReceive(Context context, Intent intent) {
			    
				String action = intent.getAction();

				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	            	
	            	Log.d(BLUETOOTH_SERVICE,"New BT Device Found");

	            	BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	                
	                unpairedDevices.add(device);
	                //Log.d(BLUETOOTH_SERVICE,device.getName()+" "+device.getAddress());
	                
		    		HashMap<String, String> temp = new HashMap<String, String>();
		        	temp.put("group_child_item", device.getName()+"\n"+device.getAddress());
		        	unpaired_device_list.add(temp);
		        	
		        	elv_adapter.notifyDataSetChanged();
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
					
//					if(BtAdapter.isDiscovering());
//					{
//						Log.d(BLUETOOTH_SERVICE,"BT Discovery Cancelled:"+BtAdapter.cancelDiscovery());
//					}
					
				}
	        	
			}
	    };
	    
	    IntentFilter bt_device_found_intent_filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
	    registerReceiver(bt_device_found_receiver, bt_device_found_intent_filter); 
	    
	    IntentFilter bt_discovery_started_intent_filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
	    registerReceiver(bt_discovery_started_receiver, bt_discovery_started_intent_filter);
	    
	    IntentFilter bt_discovery_finished_intent_filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
	    registerReceiver(bt_discovery_finished_receiver, bt_discovery_finished_intent_filter);
	    
//	    Log.d(BLUETOOTH_SERVICE,"BT Discovery Started:"+BtAdapter.startDiscovery());
	    //Log.d(BLUETOOTH_SERVICE,"BT isDiscovering:"+BtAdapter.isDiscovering());
	    //*/
	    
	}
		
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
		
		if(BtAdapter.isDiscovering())
		{
			Log.d(BLUETOOTH_SERVICE,"BT Discovery Cancelled:"+BtAdapter.cancelDiscovery());
		}
		
		//unregister receivers
		unregisterReceiver(bt_device_found_receiver);
		unregisterReceiver(bt_discovery_started_receiver);
		unregisterReceiver(bt_discovery_finished_receiver);

	}	
}
