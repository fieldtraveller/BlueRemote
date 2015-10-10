package com.alex.blueremote;

import java.util.ArrayList;

import com.google.samples.apps.iosched.ui.widget.SlidingTabLayout;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
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
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements BT_spp.device_connection_status_interface
															  ,BT_spp.device_write_interface
															  ,BT_spp.device_read_interface{
	
	BlueRemote global_variables_object;
	BroadcastReceiver bt_device_connection_status;

	ArrayList<BT_spp> awaiting_connection_list;
	ArrayList<BT_spp> terminal_device_list;
	
	control_interface_fragment fragment_1;
	terminal_fragment fragment_2;
	
	ViewPager v_pager;
    view_pager_adapter vpa;
    SlidingTabLayout tabs;
    
    CharSequence titles[]={"Interface","Terminal"};
    int tab_count=titles.length;
	
//	String BT_mac_address="20:13:05:27:09:64";	//ALEXBT
//	String BT_mac_address="98:D3:31:80:18:29";	//BURZO
	
	boolean new_BT_device_available=false;
	boolean in_program_mode=false;
	
	file_data data_from_file;
	boolean file_data_changed=false;
	
	public bluetooth_button_data button_data[]=new bluetooth_button_data[5];
	public bluetooth_switch_data switch_data[]=new bluetooth_switch_data[2];
	
	final String filename="preferences.br";
	final int program_mode_menu_option_position=3;
	final int new_device_list_activity_request_code=3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Log.e("","Activity:onCreate");
		
		if(file_operations.file_exists(getApplicationContext(),filename))
		{
			Log.e("Where?", "Yes FILE");

			data_from_file=(file_data)file_operations.read_from_file(getApplicationContext(), filename);
			
			button_data=data_from_file.getButton_data();
			switch_data=data_from_file.getSwitch_data();
			
			bluetooth_button.setButton_delay(data_from_file.getButton_repetition_period());
			HexBoard.setHex_board_call_delay(data_from_file.get_hex_board_call_time_out_factor());
			HexBoard.setHex_board_backspace_delay(data_from_file.get_hex_board_backspace_repetition_period());
			
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
			
			bluetooth_button.setButton_delay(200);
			HexBoard.setHex_board_call_delay(3);
			HexBoard.setHex_board_backspace_delay(200);
			
			data_from_file=new file_data(button_data,switch_data,
					bluetooth_button.getButton_delay(),
					HexBoard.getHex_board_call_delay_factor(),
					HexBoard.getHex_board_backspace_delay(),
					BlueRemote.assign_latest_device_if_list_is_empty);
			
			file_data_changed=true;
//			file_operations.save_to_file(getApplicationContext(), filename,data_from_file);
		}
		
		global_variables_object = (BlueRemote)getApplicationContext();
		global_variables_object.setConnected_device_list(new ArrayList<BT_spp>());
		global_variables_object.setDevice_assignment(data_from_file.getDevice_assignment());
		
		vpa =  new view_pager_adapter(getSupportFragmentManager(),tab_count,titles);
		 
        v_pager = (ViewPager) findViewById(R.id.pager);
        v_pager.setAdapter(vpa);
 
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); 
        tabs.setViewPager(v_pager);
		
		BT_spp.ui_handler=new Handler();  
		
		v_pager.addOnPageChangeListener(new OnPageChangeListener(){

			@Override
			public void onPageScrollStateChanged(int arg0) {
				
				switch(arg0)
				{
				case 0:
					break;
					
				case 1:
					
					int number_of_iterations=fragment_1.buttons.length;
					for(int count=0;count<number_of_iterations;count++)
					{
						if(fragment_1.buttons[count].button.isPressed())
						{
							fragment_1.buttons[count].button_pressed_response(false);
							fragment_1.buttons[count].button.setPressed(false);
						}
					}
					break;
					
				case 2:
					break;
				}
				
//				Log.e("", "onPageScrollStateChanged:"+arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
//				Log.e("", "onPageScrolled:"+arg0+" "+arg1+" "+arg2);
			}

			@Override
			public void onPageSelected(int arg0) {
				
//				Log.e("", "onPageSelected:"+arg0);
			}
			
		});
		
		if(awaiting_connection_list==null)
		{
			awaiting_connection_list=new ArrayList<BT_spp>();
		}
		
		if(terminal_device_list==null)
		{
			terminal_device_list=new ArrayList<BT_spp>();
			
			this.global_variables_object.list_of_devices_assigned_to_components
				.add(terminal_device_list);
		}
		
		Thread fragment_initiation_thread=new Thread(){
			
			public void run()
			{
				while(vpa.fragment_list.size()!=vpa.getCount())
				{
					
				}
				
				fragment_1=(control_interface_fragment) vpa.fragment_list.get(0);
				fragment_2=(terminal_fragment) vpa.fragment_list.get(1);
				
				Log.e("","Fragments Initiated");
			}
		};
		
		fragment_initiation_thread.start();
		
		bt_device_connection_status=new BroadcastReceiver() {
 	        
 	    	@Override
 	        public void onReceive(Context context, final Intent intent) {
 	            
 	    		String action = intent.getAction();
 	            
 	    		if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) 
 	    		{
 	    			Thread device_assigning_thread=new Thread(){
 	    				
 	    				public void run()
 	    				{
 	    					BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
 	    					int number_of_iterations=awaiting_connection_list.size();
 	    	    			for(int count_0=0;count_0<number_of_iterations;count_0++)
 	    	    			{
 	    	    				BT_spp spp_device=awaiting_connection_list.get(count_0);
 	    	    				
 	    	    				if(spp_device.equals(device))
 	    	    				{
 	    	    					global_variables_object.add_to_Connected_device_list(spp_device);
 	    	    					awaiting_connection_list.remove(count_0);
 	    	    					
 	    	    					int list_size=global_variables_object.list_of_devices_assigned_to_components.size();
	    	    					if(global_variables_object.getDevice_assignment()==BlueRemote.assign_all_devices)
 	    	    					{
 	    	    						for(int count_1=0;count_1<list_size;count_1++)
 	    	    						{
 	    	    							global_variables_object.list_of_devices_assigned_to_components.get(count_1)
 	    	    								.add(spp_device);
 	    	    						}
 	    	    					}
 	    	    					else if(global_variables_object.getDevice_assignment()==BlueRemote.assign_latest_device_if_list_is_empty)
 	    	    					{
 	    	    						for(int count_1=0;count_1<list_size;count_1++)
 	    	    						{
 	    	    							if(global_variables_object.list_of_devices_assigned_to_components.get(count_1).size()==0)
 	    	    							{
 	    	    								global_variables_object.list_of_devices_assigned_to_components.get(count_1)
 	    	    								.add(spp_device);
 	    	    							}	
 	    	    						}
 	    	    					}
 	    	    					else
 	    	    					{
 	    	    						
 	    	    					}
 	    	    					
 	    	    					break;
 	    	    				}
 	    	    			}
 	    				}
 	    			};
 	    			
 	    			device_assigning_thread.start();
 	    			
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
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		Log.e("","Activity:onResume");
	}
	
	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  
    {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode==new_device_list_activity_request_code)
		{
			if(data.getBooleanExtra(new_device_list_activity.new_bt_device_selected_extra_name,false))
			{
				BT_spp BT_serial_device=data.getParcelableExtra(new_device_list_activity.new_bt_device_extra_name);
				BT_serial_device.dcsi=this;
				BT_serial_device.dwi=this;
				BT_serial_device.dri=this;
				
				new_BT_device_available=true;
				
				awaiting_connection_list.add(BT_serial_device);

				BT_serial_device.connect();
			}
		}
		else if(requestCode==preferences_activity.preferences_activity_request_code)
		{
			global_variables_object.setDevice_assignment(data.getByteExtra(preferences_activity.device_assignment_extra_name,BlueRemote.do_not_assign));
			int button_repetition_period=data.getIntExtra(preferences_activity.button_repetition_period_extra_name, 200);
			int hex_board_call_time_out_factor=data.getIntExtra(preferences_activity.hex_board_call_time_out_factor_extra_name, 3);
			int hex_board_backspace_repetition_period=data.getIntExtra(preferences_activity.hex_board_backspace_repetition_period_extra_name, 200);
			
			bluetooth_button.setButton_delay(button_repetition_period);
			HexBoard.setHex_board_call_delay(hex_board_call_time_out_factor);
			HexBoard.setHex_board_backspace_delay(hex_board_backspace_repetition_period);
			
			data_from_file.setDevice_assignment(global_variables_object.getDevice_assignment());
			data_from_file.setButton_repetition_period(button_repetition_period);
			data_from_file.set_hex_board_call_time_out_factor(hex_board_call_time_out_factor);
			data_from_file.set_hex_board_backspace_repetition_period(hex_board_backspace_repetition_period);
			file_data_changed=true;
		}
		else
		{
			Log.e("","requestCode:"+requestCode+"\n data:"+data);
		}
    }  
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		
		for(int count=0;count<menu.size();count++)
		{
			if(in_program_mode==true)
			{
				if(count!=program_mode_menu_option_position)
				{
					menu.getItem(count).setEnabled(false);
				}
			}
			else
			{
				menu.getItem(count).setEnabled(true);
			}			
		}
		
		if(in_program_mode==true)
		{
			menu.getItem(program_mode_menu_option_position).setTitle(R.string.normal_mode);
		}
		else
		{
			menu.getItem(program_mode_menu_option_position).setTitle(R.string.program_mode);
		}
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		int id = item.getItemId();
		
		switch(id)
		{
			case R.id.menu_item_1:
				Intent device_list= new Intent(this, new_device_list_activity.class);
			    startActivityForResult(device_list, new_device_list_activity_request_code);
				return true;
			
			case R.id.menu_item_2:
				return true;
				
			case R.id.menu_item_3:
				return true;
			
			case R.id.menu_item_4:
				
				if(in_program_mode==true)
				{
					file_data_changed=true;
//					file_operations.save_to_file(getApplicationContext(), filename,data_from_file);

					invalidateOptionsMenu();
					
//					item.setTitle(R.string.program_mode);
					in_program_mode=false;
					
					fragment_1.tv_1.setVisibility(View.GONE);
					fragment_1.send.setVisibility(View.VISIBLE);
					fragment_1.send.invalidate();
					fragment_1.et_1.setVisibility(View.VISIBLE);
					fragment_1.et_1.invalidate();
					
					for(int count=0;count<fragment_1.buttons.length;count++)
					{
						fragment_1.buttons[count].setMode(bluetooth_button.NORMAL_MODE);
					}
					
					for(int count=0;count<fragment_1.switchs.length;count++)
					{
						fragment_1.switchs[count].setMode(bluetooth_switch.NORMAL_MODE);
					}					
				}
				else
				{
					invalidateOptionsMenu();
					
//					item.setTitle(R.string.normal_mode);
					in_program_mode=true;
					
					fragment_1.tv_1.setVisibility(View.VISIBLE);
					fragment_1.send.setVisibility(View.GONE);
					fragment_1.send.invalidate();
					fragment_1.et_1.setVisibility(View.GONE);
					fragment_1.et_1.invalidate();
					
					for(int count=0;count<fragment_1.buttons.length;count++)
					{
						fragment_1.buttons[count].setMode(bluetooth_button.PROGRAM_MODE);
					}
					
					for(int count=0;count<fragment_1.switchs.length;count++)
					{
						fragment_1.switchs[count].setMode(bluetooth_switch.PROGRAM_MODE);
					}					
				}
								
				return true;
				
			case R.id.menu_item_5:
				Intent preferences_intent=new Intent(this,preferences_activity.class);
				preferences_intent.putExtra(preferences_activity.device_assignment_extra_name,global_variables_object.getDevice_assignment());
				preferences_intent.putExtra(preferences_activity.button_repetition_period_extra_name,bluetooth_button.getButton_delay());
				preferences_intent.putExtra(preferences_activity.hex_board_call_time_out_factor_extra_name,HexBoard.getHex_board_call_delay_factor());
				preferences_intent.putExtra(preferences_activity.hex_board_backspace_repetition_period_extra_name,HexBoard.getHex_board_backspace_delay());
				startActivityForResult(preferences_intent, preferences_activity.preferences_activity_request_code);
				return true;
				
			default:
				break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed ()
	{
		if(in_program_mode==true)
		{
			Toast.makeText(getApplicationContext(),"Exit Program Mode First", Toast.LENGTH_SHORT).show();
		}
		else
		{
			this.finish();
		}
	}
	
	@Override
	protected void onDestroy ()
	{
		Thread app_closing_thread=new Thread(){
			
			public void run()
			{
				int number_of_connected_devices=global_variables_object.getConnected_device_list().size();
				
				for(int count=0;count<number_of_connected_devices;count++)
				{
					global_variables_object.getConnected_device(count).disconnect();	
				}
				
				//Direct Turn Off BT by App 
				if (global_variables_object.getBtAdapter().isEnabled()) 
				{
					global_variables_object.getBtAdapter().disable();
				}
					
				Log.d(BLUETOOTH_SERVICE, "Bluetooth turned Off.");
			}
		};
		
		app_closing_thread.start();
		
		if(file_data_changed==true)
		{
			Thread data_saving_thread=new Thread(){
				
				public void run()
				{
					file_operations.save_to_file(getApplicationContext(), filename,data_from_file);
				}
			};
			
			data_saving_thread.start();
		}
		
		unregisterReceiver(bt_device_connection_status);
		super.onDestroy();
	}

	@Override
	public void on_device_connection_pass() 
	{
		
	}

	@Override
	public void on_device_connection_fail(BT_spp failed_device) 
	{
		int number_of_iterations=awaiting_connection_list.size();
		for(int count_0=0;count_0<number_of_iterations;count_0++)
		{
			BT_spp spp_device=awaiting_connection_list.get(count_0);				
			if(spp_device.equals(failed_device))
			{
				awaiting_connection_list.remove(count_0);
				Log.e("","Device Removed:"+awaiting_connection_list.size());
				break;
			}
		}
	}

	@Override
	public void on_device_connection_pass(BT_spp passed_device) {
		
	}

	@Override
	public void on_device_connection_fail() {
		
	}

	@Override
	public void on_device_write() {
		
	}

	@Override
	public void on_device_write(final String start_text, final byte written_byte,
			String end_text) {
		
		fragment_2.terminal_handler.post(new Runnable(){

			@Override
			public void run() {
//				fragment_2.tv_1.setTextColor();
				fragment_2.tv_1.append(start_text+written_byte+"\n");
				fragment_2.sv.fullScroll(View.FOCUS_DOWN);
			}
			
		});
		
	}

	@Override
	public void on_device_write(final String start_text, final byte[] written_bytes,
			String end_text) {
		
		fragment_2.terminal_handler.post(new Runnable(){

			@Override
			public void run() {
				fragment_2.tv_1.append(start_text+(new String(written_bytes))+"\n");
				fragment_2.sv.fullScroll(View.FOCUS_DOWN);
			}
			
		});
	}

	@Override
	public void on_device_read() {
		
	}

	@Override
	public void on_device_read(final String start_text, final byte read_byte,
			String end_text) {
		
		fragment_2.terminal_handler.post(new Runnable(){

			@Override
			public void run() {
				fragment_2.tv_1.append(start_text+read_byte+"\n");
				fragment_2.sv.fullScroll(View.FOCUS_DOWN);
			}
			
		});
	}

	@Override
	public void on_device_read(final String start_text, final byte[] read_bytes,
			int number_of_bytes_read, String end_text) {
		
		fragment_2.terminal_handler.post(new Runnable(){

			@Override
			public void run() {
				fragment_2.tv_1.append(start_text+(new String(read_bytes))+"\n");
				fragment_2.sv.fullScroll(View.FOCUS_DOWN);
			}
			
		});
	}
}
