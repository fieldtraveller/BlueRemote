package com.alex.blueremote;

import java.io.IOException;
import java.nio.charset.Charset;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
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
	final int hexboard_activity_request_code=4;
	
	BroadcastReceiver bt_device_connection_status;
	
//	String BT_mac_address="20:13:05:27:09:64";	//ALEXBT
//	String BT_mac_address="98:D3:31:80:18:29";	//BURZO
	
	BT_spp BT_serial_device;
	boolean BT_discovered_device_available=false;
	
	Button channel_up,channel_down,volume_up,volume_down,select,send;
	Switch power,mute;
	EditText data_input;
	
	bluetooth_button button_7;
	
	boolean in_program_mode=false;
	
	byte[] channel_up_command={0};
	byte[] channel_down_command={1};
	byte[] volume_up_command={2};
	byte[] volume_down_command={4};
	byte[] select_command={8};
	
	byte[] power_command={32};
	byte[] power_on_command={32};
	byte[] power_off_command={32};
	byte[] mute_command={64};
	byte[] mute_on_command={64};
	byte[] mute_off_command={64};
	
	Handler hex_board_handler;
	Runnable hex_board_runnable;
	int hex_board_call_delay=ViewConfiguration.getLongPressTimeout()*3;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
//		byte[] b = "Alex".getBytes(Charset.forName("UTF-8"));
//		Log.e("length",""+b.length+" "+b[0]+" "+b[1]+" "+b[2]+" "+b[3]);
//		Log.e("test",hexstring2string_pasrser("41424344"));
		
		channel_up=(Button)findViewById(R.id.button1);
		channel_down=(Button)findViewById(R.id.button5);
		volume_up=(Button)findViewById(R.id.button4);
		volume_down=(Button)findViewById(R.id.button2);
		select=(Button)findViewById(R.id.button3);
		send=(Button)findViewById(R.id.button6);
		
		bluetooth_button_data a=new bluetooth_button_data(null,new byte[]{12,13});
		button_7=new bluetooth_button(this, BT_serial_device, (Button)findViewById(R.id.button7), 
										false,a);
				
		power=(Switch)findViewById(R.id.switch1);
		mute=(Switch)findViewById(R.id.switch2);
		
		data_input=(EditText)findViewById(R.id.editText1);
		
//		set_value.setVisibility(View.INVISIBLE);
//		set_value.invalidate();
		
//	    power.setChecked(false);
//	    mute.setChecked(false);
		
		hex_board_handler=new Handler();
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
//	    			progressDialog.cancel();
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
	    		if(in_program_mode==true)
	    		{
	    			
	    		}
	    		else
	    		{
	    			BT_serial_device.write(channel_up_command);
	    		}
	    	}
	    	
		});
	    
	    channel_down.setOnClickListener(new View.OnClickListener() {
	    	
	    	@Override
			public void onClick(View v) {
	    		
	    		if(in_program_mode==true)
	    		{
	    			
	    		}
	    		else
	    		{
	    			BT_serial_device.write(channel_down_command);
	    		}
	    		
	    	}
	    	
		});
	    
	    volume_up.setOnClickListener(new View.OnClickListener() {
	    	
	    	@Override
			public void onClick(View v) {
	    		
	    		if(in_program_mode==true)
	    		{
	    			
	    		}
	    		else
	    		{
	    			BT_serial_device.write(volume_up_command);
	    		}
	    		
	    	}
	    	
		});
	    
	    volume_down.setOnClickListener(new View.OnClickListener() {
	    	
	    	@Override
			public void onClick(View v) {
	    		
	    		if(in_program_mode==true)
	    		{
	    			
	    		}
	    		else
	    		{
	    			BT_serial_device.write(volume_down_command);
	    		}
	    		
	    	}
	    	
		});
	    
	    select.setOnClickListener(new View.OnClickListener() {
	    	
	    	@Override
			public void onClick(View v) {
	    		
	    		if(in_program_mode==true)
	    		{
	    			
	    		}
	    		else
	    		{
	    			BT_serial_device.write(select_command);
	    		}
	    		
	    	}
	    	
		});
	    
	    send.setOnClickListener(new View.OnClickListener() {
	    	
	    	@Override
			public void onClick(View v) {
	    		
	    		if(in_program_mode==true)
	    		{
	    			
	    		}
	    		else
	    		{
//	    			BT_serial_device.write(data_input.getText().toString().getBytes(Charset.forName("UTF-8")));
	    			BT_serial_device.write(data_input.getText().toString().getBytes(Charset.forName("ISO-8859-1")));
	    		}
	    	}
		});
	    	    
	    power.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	   
		    @Override
			public void onCheckedChanged(CompoundButton buttonView,	boolean isChecked) {
		    	
		    	if(in_program_mode==true)
	    		{
	    			
	    		}
	    		else
	    		{
	    			BT_serial_device.write(power_command);
					
			    	if(isChecked)
					{
						
					}
					else
					{
						mute.setChecked(false);
					}
	    		}
		    	
				
			}
	    });
	    
	    mute.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	   
		    @Override
			public void onCheckedChanged(CompoundButton buttonView,	boolean isChecked) {
		    	
		    	if(in_program_mode==true)
	    		{
	    			
	    		}
	    		else
	    		{
	    			BT_serial_device.write(mute_command);
					
	    			if(isChecked)
					{
						
					}
					else
					{
						
					}
	    		}	    	
			}
	    });
	    
	    hex_board_runnable=new Runnable(){

			@Override
			public void run() {
				
				Intent device_list= new Intent(MainActivity.this,HexBoard.class);
			    startActivityForResult(device_list, hexboard_activity_request_code);
			}
			
		};
	    		
	    data_input.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				switch(event.getAction())
				{
					case MotionEvent.ACTION_DOWN:
						hex_board_handler.postDelayed(hex_board_runnable,hex_board_call_delay);
						break;
							
					case MotionEvent.ACTION_UP:
						hex_board_handler.removeCallbacks(hex_board_runnable);
						v.performClick();
						break;
							
//					default:				
				}
				
				return false;
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
				ProgressDialog progressDialog=ProgressDialog.show(MainActivity.this, "", "Connecting to Device...");
			    
			    try 
			    {
					BT_serial_device.connect();
				}
			    catch (IOException e) 
			    {
			    	Toast.makeText(getApplicationContext(), "Unable To Connect To Device.", Toast.LENGTH_LONG).show();
				}
			    
			    progressDialog.cancel();
			    
			    
//			    FragmentManager fm = getFragmentManager();
//			    FragmentTransaction fragmentTransaction = fm.beginTransaction();
//			    fragmentTransaction.replace(R.id.fragment1, new connecting_fragment());
//			    fragmentTransaction.commit();
			    
//			    Intent device_list= new Intent(this, connecting_dialog.class);
//			    startActivity(device_list);
			    
			}	
		}
		else
		{
			Intent device_list= new Intent(this, device_list_activity.class);
		    startActivityForResult(device_list, BT_device_list_activity_request_code);
		}
	}
	
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
				button_7.setBT_serial_device(BT_serial_device);
			}	
		}
		
		if(requestCode==hexboard_activity_request_code)
		{
			data_input.setText(data_input.getText().toString()+data.getStringExtra(HexBoard.stringed_data));
			data_input.setSelection(data_input.getText().length());
		}
		
    }  
	//*/
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
//		Log.e("Where?", "onCreateOptionsMenu");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		int id = item.getItemId();
		Log.e("Where?", "onOptionsItemSelected");
		
		switch(id)
		{
			case R.id.menu_item_1:
				Log.e("Where?", "R.id.menu_item_1");
				return true;
				
			case R.id.menu_item_1_1:
				Log.e("Where?", "R.id.menu_item_1_1");
				return true;
				
			case R.id.menu_item_1_2:
				Log.e("Where?", "R.id.menu_item_1_2");
				return true;
				
			case R.id.menu_item_2:
				Log.e("Where?", "R.id.menu_item_2");
				
				in_program_mode=true;
				send.setVisibility(View.GONE);
				send.invalidate();
				data_input.setVisibility(View.GONE);
				data_input.invalidate();
//				set_value.setVisibility(View.VISIBLE);
//				set_value.invalidate();
				return true;
				
			default:
				break;
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
