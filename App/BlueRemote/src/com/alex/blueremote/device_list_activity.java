package com.alex.blueremote;

import java.util.ArrayList;
import java.util.HashMap;
//import java.util.HashSet;
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
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup;

public class device_list_activity extends AppCompatActivity {

	BlueRemote_global_variables global_variables_object;
	BroadcastReceiver bt_discovery_status;
	Set<BluetoothDevice> pairedDevices;
	    
    ArrayList<HashMap<String, String>> group_list=new ArrayList<HashMap<String, String>>();
    ArrayList<ArrayList<HashMap<String, BT_spp>>> group_child_list=new ArrayList<ArrayList<HashMap<String, BT_spp>>>(); 
    ArrayList<HashMap<String, BT_spp>> paired_device_list = new ArrayList<HashMap<String, BT_spp>>();
    ArrayList<HashMap<String, BT_spp>> unpaired_device_list = new ArrayList<HashMap<String, BT_spp>>();
    
    String[] mGroupFrom;
    int[] mGroupTo;
    
    String[] mChildFrom;
    int[] mChildTo;
    
    ExpandableListView elv_1;
    SimpleExpandableListAdapter elv_adapter;
    Button refresh_button; 
	
    Intent close_intent = new Intent();
    boolean quit_warning=false;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dl_layout);
		
		elv_1=(ExpandableListView)findViewById(R.id.eList_1);
		refresh_button=(Button)findViewById(R.id.buttondl);
		
//		elv_1.setSelector(R.color.list_selector);
		refresh_button.setEnabled(false);
		
		global_variables_object = (BlueRemote_global_variables)getApplicationContext();
		
		bt_discovery_status = new BroadcastReceiver() {
	        
			@Override
			public void onReceive(Context context, Intent intent) {
			    
				String action = intent.getAction();

				if (BluetoothDevice.ACTION_FOUND.equals(action)) 
				{	
	            	Log.d(BLUETOOTH_SERVICE,"New BT Device Found");

	            	BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	                
	                if(pairedDevices.contains(device)==true)
	                {	                	
	                	for(int count_0=0;count_0<pairedDevices.size();count_0++)
	                	{
	                		if(group_child_list.get(0).get(count_0).get("group_child_item").equals(new BT_spp(device,false)))
	                		{
	                			group_child_list.get(0).get(count_0).get("group_child_item").setDiscovered(true);
	                			break;
	                		}
	                	}
	    				elv_adapter.notifyDataSetChanged();
	                }
	                else
	                {
	                	HashMap<String, BT_spp> temp = new HashMap<String, BT_spp>();
			        	temp.put("group_child_item", new BT_spp(device,true));
			        	unpaired_device_list.add(temp);
			        	
			        	elv_adapter.notifyDataSetChanged();
	                }
	            }
				else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) 
				{	
//					Log.d(BLUETOOTH_SERVICE,"BT Discovery Started");
					
					Toast.makeText(getApplicationContext(),"BT Discovery Started",Toast.LENGTH_SHORT).show();
	            }
				else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) 
				{
	            	
//					Log.d(BLUETOOTH_SERVICE,"BT Discovery Finished");
					
					Toast.makeText(getApplicationContext(),"BT Discovery Finished",Toast.LENGTH_SHORT).show();
					refresh_button.setEnabled(true);
					
//					Log.d(BLUETOOTH_SERVICE,"Discovered Devices: "+discoveredDevices.size());
				    
//					if (discoveredDevices.size() > 0) 
//				    {
//					    for (BluetoothDevice device : discoveredDevices) 
//		    	    	{
//		    	    		Log.d(BLUETOOTH_SERVICE,device.getName()+" "+device.getAddress());
//		    	    	}
//				    }
				}	
			}
	    };
	    
	    registerReceiver(bt_discovery_status, new IntentFilter(BluetoothDevice.ACTION_FOUND)); 
	    registerReceiver(bt_discovery_status, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED));
	    registerReceiver(bt_discovery_status, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
	    
	    global_variables_object.getBtAdapter().startDiscovery();

	    pairedDevices = global_variables_object.getBtAdapter().getBondedDevices();
	    
//	    Log.d(BLUETOOTH_SERVICE,"Paired Devices: "+pairedDevices.size());
	    	    
	    if (pairedDevices.size() > 0) 
	    {
	    	for (BluetoothDevice device : pairedDevices) 
	    	{
//	    		Log.d(BLUETOOTH_SERVICE,device.getName()+" "+device.getAddress());

	    		HashMap<String, BT_spp> temp = new HashMap<String, BT_spp>();
	        	temp.put("group_child_item", new BT_spp(device,false));
	        	paired_device_list.add(temp);
	    	}
	    }

	    HashMap<String, String> group_heading_1 = new HashMap<String, String>();
	    group_heading_1.put("group_item","Paired Devices");
		group_list.add(group_heading_1);
		    
	    HashMap<String, String> group_heading_2 = new HashMap<String, String>();
	    group_heading_2.put("group_item","Un-Paired Devices");
		group_list.add(group_heading_2);
	    
	    group_child_list.add(paired_device_list);
	    group_child_list.add(unpaired_device_list);
	    
	    mGroupFrom=new String[] {"group_item"};
	    mGroupTo=new int[] { R.id.elv_group };
	    
	    mChildFrom=new String[] {"group_child_item"};
	    mChildTo=new int[] { R.id.elv_group_child};
	    
	    elv_adapter = new SimpleExpandableListAdapter(
	                    this,	
	                    
	                    (List<HashMap<String, String>>)group_list,
	                    R.layout.elv_textview_group,             
	                    mGroupFrom,  			
	                    mGroupTo,    		
	                    
	                    (List<ArrayList<HashMap<String, BT_spp>>>)group_child_list,
	                    R.layout.elv_textview_group_child,        
	                    mChildFrom,      	
	                    mChildTo     		
	                	)
	    {
		    
            @Override
            public View getChildView(int groupPosition, int childPosition,
                    boolean isLastChild, View convertView, ViewGroup parent) {

            	View v;
            	
                if (convertView == null)
                {
                    v = newChildView(isLastChild, parent);
                }
                else 
                {
                    v = convertView;
                }
                
                int len = mChildTo.length;

                for (int count = 0; count < len; count++) 
                {
                    TextView tv = (TextView)v.findViewById(mChildTo[count]);
                    
                    if (tv != null) 
                    {
                        tv.setText(group_child_list.get(groupPosition).get(childPosition).get(mChildFrom[count]).toString());
                                                
                        if(group_child_list.get(groupPosition).get(childPosition).get("group_child_item").isDiscovered == true)
                        {
                        	tv.setBackgroundColor(Color.rgb(0x8F,0xE6,0x5B));
                        }
                        else
                        {
                        	tv.setBackgroundColor(Color.rgb(0xFF,0xFF,0xFF));                        	
                        }
                    }
                }
                return v;
            }

//            @Override
//            public View getGroupView(int groupPosition, boolean isExpanded,
//                    View convertView, ViewGroup parent) {
//               
//                TextView tv = (TextView) super.getGroupView(groupPosition, isExpanded, convertView, parent);
//                
//                return tv;
//            }
	    };
	    
	    elv_1.setAdapter(elv_adapter);
	    
	    elv_1.expandGroup(0,true);
//	    elv_1.expandGroup(1,true);
	    	    
	    elv_1.setOnChildClickListener(new OnChildClickListener() {

	    	@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
	    			
	    		if(group_child_list.get(groupPosition).get(childPosition).get("group_child_item").isDiscovered == true)
	    		{	    			
	    			close_intent.putExtra("BT_device_selected",((BT_spp)group_child_list.get(groupPosition).get(childPosition).get("group_child_item")));
	    			close_intent.putExtra("closeApp", false);
	    			setResult(RESULT_OK, close_intent);        
	    			finish();
	    		}
	    		else
	    		{
	    			Toast.makeText(getApplicationContext(),"Selected Device Not Discovered.\nChoose A Highligthed Device.",Toast.LENGTH_SHORT).show();
	    		}
	            return true;
			}
         });
	    
	    refresh_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				refresh_button.setEnabled(false);
				
				int childCount=elv_adapter.getChildrenCount(0);
//				int childCount=elv_adapter.getChildrenCount(0);
				
				for(int count_0=0;count_0<childCount;count_0++)
            	{
					group_child_list.get(0).get(count_0).get("group_child_item").setDiscovered(false);
            	}
				
//				childCount=elv_adapter.getChildrenCount(1);
				childCount=unpaired_device_list.size();
				
				for(int count_0=0;count_0<childCount;count_0++)
            	{
					unpaired_device_list.remove(count_0);
            	}
				
//				Log.e("What?", "discovery?"+global_variables_object.getBtAdapter().isDiscovering());
				global_variables_object.getBtAdapter().startDiscovery();
				
				elv_adapter.notifyDataSetChanged();	
			}
	    });
	}
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return false;
	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		
//		return super.onOptionsItemSelected(item);
//	}
	
	private void quit()
	{
		if(quit_warning==false)
		{
			quit_warning=true;
			
			Toast.makeText(getApplicationContext(),"Press Again To Quit.",Toast.LENGTH_SHORT).show();
		}
		else
		{
			close_intent.putExtra("closeApp", true);
			setResult(RESULT_OK, close_intent);        
			finish();
		}
	}
	
	@Override
	public boolean onSupportNavigateUp()
	{
//		Log.e("SupportNavigateUp","I Was Pressed.");
		
		this.quit();
		
		return true;
	}
	
	@Override
	public void onBackPressed ()
	{
//		Log.e("Back Button","I Was Pressed.");
		
		this.quit();
	}
	
	@Override
	protected void onDestroy ()
	{
		super.onDestroy();
		
		if(global_variables_object.getBtAdapter().isDiscovering())
		{
			Log.d(BLUETOOTH_SERVICE,"BT Discovery Cancelled:"+global_variables_object.getBtAdapter().cancelDiscovery());
		}
		
		//unregister receivers
		unregisterReceiver(bt_discovery_status);
	}	
}
