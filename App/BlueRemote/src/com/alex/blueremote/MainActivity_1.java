package com.alex.blueremote;

import java.io.IOException;
import java.nio.charset.Charset;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
//import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity_1 extends AppCompatActivity {
	
	BlueRemote_global_variables global_variables_object;
	
	final int BT_turn_on_request_code=2;
	final int BT_device_list_activity_request_code=3;
	final int hexboard_activity_request_code=4;
	
	BroadcastReceiver bt_device_connection_status;
	
//	String BT_mac_address="20:13:05:27:09:64";	//ALEXBT
//	String BT_mac_address="98:D3:31:80:18:29";	//BURZO
	
	BT_spp BT_serial_device;
	boolean BT_discovered_device_available=false;
	boolean in_program_mode=false;
	
	EditText data_input;
	TextView tv;
	
	saved_data data_from_file;
	
	Button send;
	bluetooth_button_data button_data[]=new bluetooth_button_data[5];
	bluetooth_button buttons[]=new bluetooth_button[5]; 
	
	bluetooth_switch_data switch_data[]=new bluetooth_switch_data[2];
	bluetooth_switch switchs[]=new bluetooth_switch[2]; 
	
	Handler hex_board_handler;
	Runnable hex_board_runnable;
	
	String filename="preferences.br";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if(file_operations.file_exists(getApplicationContext(),filename))
		{
			Log.e("Where?", "YEs FILE");

			data_from_file=(saved_data)file_operations.read_from_file(getApplicationContext(), filename);
			
			button_data=data_from_file.getButton_data();
			switch_data=data_from_file.getSwitch_data();
		}
		else
		{
			Log.e("Where?", "No File");
			
			button_data[0]=new bluetooth_button_data(getResources().getString(R.string.channel_up),new byte[]{0x01},true,new byte[]{0x02},new byte[]{0x03});
			button_data[1]=new bluetooth_button_data(getResources().getString(R.string.channel_down),new byte[]{0x11},true,new byte[]{0x12},new byte[]{0x13});
			button_data[2]=new bluetooth_button_data(getResources().getString(R.string.volume_up),new byte[]{0x21},true,new byte[]{0x22},new byte[]{0x23});
			button_data[3]=new bluetooth_button_data(getResources().getString(R.string.volume_down),new byte[]{0x31},true,new byte[]{0x32},new byte[]{0x33});
			button_data[4]=new bluetooth_button_data(getResources().getString(R.string.select),new byte[]{0x41},true,new byte[]{0x42},new byte[]{0x43});
			
			switch_data[0]=new bluetooth_switch_data(getResources().getString(R.string.power_switch),new byte[]{(byte)0xA1},new byte[]{(byte)0xA2},new byte[]{(byte)0xA3});
			switch_data[1]=new bluetooth_switch_data(getResources().getString(R.string.mute_switch),new byte[]{(byte) 0xB1},new byte[]{(byte)0xB2},new byte[]{(byte)0xB3});
			
			data_from_file=new saved_data(button_data,switch_data);
			file_operations.save_to_file(getApplicationContext(), filename,data_from_file);
		}
		
		//channel_up
		buttons[0]=new bluetooth_button(this, BT_serial_device, (Button)findViewById(R.id.button1),button_data[0]);
		//channel_down
		buttons[1]=new bluetooth_button(this, BT_serial_device, (Button)findViewById(R.id.button5),button_data[1]);
		//volume_up
		buttons[2]=new bluetooth_button(this, BT_serial_device, (Button)findViewById(R.id.button4),button_data[2]);
		//volume_down
		buttons[3]=new bluetooth_button(this, BT_serial_device, (Button)findViewById(R.id.button2),button_data[3]);
		//select
		buttons[4]=new bluetooth_button(this, BT_serial_device, (Button)findViewById(R.id.button3),button_data[4]);
		
		//power
		switchs[0]=new bluetooth_switch(this, BT_serial_device, (Switch)findViewById(R.id.switch1),switch_data[0]);
		//mute
		switchs[1]=new bluetooth_switch(this, BT_serial_device, (Switch)findViewById(R.id.switch2),switch_data[1]);
		
		Intent programming_activity_intent = new Intent(this,programming_activity.class);
		
		for(int count=0;count<buttons.length;count++)
		{
			buttons[count].setProgramming_parameters(programming_activity_intent,100+count);
		}
		
		for(int count=0;count<switchs.length;count++)
		{
			switchs[count].setProgramming_parameters(programming_activity_intent,200+count);
		}

		send=(Button)findViewById(R.id.button6);
		data_input=(EditText)findViewById(R.id.editText1);
		tv=(TextView)findViewById(R.id.textView1);
		
		tv.setVisibility(View.GONE);
		
		hex_board_handler=new Handler();
		global_variables_object = (BlueRemote_global_variables)getApplicationContext();
		
		switchs[0].setSet_task_on_switch_off(new set_task(){

			@Override
			public void perform_task() 
			{
				switchs[1].switcher.setChecked(false);
			}
		});
		
	    bt_device_connection_status=new BroadcastReceiver() {
	        
	    	@Override
	        public void onReceive(Context context, Intent intent) {
	            
	    		String action = intent.getAction();
	            
	    		if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) 
	    		{
	    			Log.e(BLUETOOTH_SERVICE, "BT Device Connected");
	            }
	            else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) 
	            {
	            	Log.e(BLUETOOTH_SERVICE, "BT Device about to Disconnect");
	            }
	            else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) 
	            {
	            	Log.e(BLUETOOTH_SERVICE, "BT Device Disconnected");
	            }           
	        }
	    };
	    
	    registerReceiver(bt_device_connection_status,new IntentFilter(android.bluetooth.BluetoothDevice.ACTION_ACL_DISCONNECTED));
	    registerReceiver(bt_device_connection_status,new IntentFilter(android.bluetooth.BluetoothDevice.ACTION_ACL_CONNECTED));
	    registerReceiver(bt_device_connection_status,new IntentFilter(android.bluetooth.BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED));   

	    send.setOnClickListener(new View.OnClickListener() {
	    	
	    	@Override
			public void onClick(View v) {
	    		
	    		BT_serial_device.write(data_input.getText().toString().getBytes(Charset.forName("ISO-8859-1")));
	    	}
		});
	    	    
	    hex_board_runnable=new Runnable(){

			@Override
			public void run() {
				
				Intent launch_hex_board= new Intent(MainActivity_1.this,HexBoard.class);
				launch_hex_board.putExtra(HexBoard.initial_text, data_input.getText().toString());
			    startActivityForResult(launch_hex_board, hexboard_activity_request_code);
			}			
		};
	    		
	    data_input.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				switch(event.getAction())
				{
					case MotionEvent.ACTION_DOWN:
						hex_board_handler.postDelayed(hex_board_runnable,HexBoard.hex_board_call_delay);
						break;
							
					case MotionEvent.ACTION_UP:
						hex_board_handler.removeCallbacks(hex_board_runnable);
						v.performClick();
						break;					
				}
				
				return false;
			}	    	
	    });
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		Log.e("Where?", "OnResume");
		
		if((BT_discovered_device_available == true )&&(in_program_mode==false))
		{
			Log.e("Where?", "New BT Device");
			if(BT_serial_device.BT_socket == null)
			{
				ProgressDialog progressDialog=ProgressDialog.show(MainActivity_1.this, "", "Connecting to Device...");
			    
			    try 
			    {
					BT_serial_device.connect();
				}
			    catch (IOException e) 
			    {
			    	Toast.makeText(getApplicationContext(), "Unable To Connect To Device.", Toast.LENGTH_LONG).show();
				}
			    
			    progressDialog.cancel();
			}	
		}
		else if(in_program_mode==true)
		{
			
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
				
				for(int count=0;count<buttons.length;count++)
				{
					buttons[count].setBT_serial_device(BT_serial_device);
				}
				
				for(int count=0;count<switchs.length;count++)
				{
					switchs[count].setBT_serial_device(BT_serial_device);
				}				
			}	
		}
		
		if(requestCode==hexboard_activity_request_code)
		{
			data_input.setText(data.getStringExtra(HexBoard.stringed_data));
			data_input.setSelection(data_input.getText().length());
		}
		
		for(int count=0;count<buttons.length;count++)
		{
			buttons[count].update_button_onActivityResult(requestCode,resultCode,data);
		}
		
		for(int count=0;count<switchs.length;count++)
		{
			switchs[count].update_switch_onActivityResult(requestCode,resultCode,data);
		}
    }  
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		int id = item.getItemId();
		
		switch(id)
		{
			case R.id.menu_item_1:
				return true;
				
			case R.id.menu_item_1_1:
				return true;
				
			case R.id.menu_item_1_2:
				return true;
				
			case R.id.menu_item_2:
				
				if(in_program_mode==true)
				{
					file_operations.save_to_file(getApplicationContext(), filename,data_from_file);
					
					item.setTitle(R.string.program_mode);
					in_program_mode=false;
					
					tv.setVisibility(View.GONE);
					send.setVisibility(View.VISIBLE);
					send.invalidate();
					data_input.setVisibility(View.VISIBLE);
					data_input.invalidate();
					
					for(int count=0;count<buttons.length;count++)
					{
						buttons[count].setMode(bluetooth_button.NORMAL_MODE);
					}
					
					for(int count=0;count<switchs.length;count++)
					{
						switchs[count].setMode(bluetooth_switch.NORMAL_MODE);
					}					
				}
				else
				{
					item.setTitle(R.string.normal_mode);
					in_program_mode=true;
					
					tv.setVisibility(View.VISIBLE);
					send.setVisibility(View.GONE);
					send.invalidate();
					data_input.setVisibility(View.GONE);
					data_input.invalidate();
					
					for(int count=0;count<buttons.length;count++)
					{
						buttons[count].setMode(bluetooth_button.PROGRAM_MODE);
					}
					
					for(int count=0;count<switchs.length;count++)
					{
						switchs[count].setMode(bluetooth_switch.PROGRAM_MODE);
					}					
				}
				
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
