package com.alex.blueremote;

import java.io.IOException;
import java.util.ArrayList;

import helper.call_this_method_interface;
import helper.view_pager_helper.*;
import helper.bluetooth_helper.BT_spp;
import helper.bluetooth_helper.bluetooth_button;
import helper.bluetooth_helper.bluetooth_button_data;
import helper.bluetooth_helper.bluetooth_compound_button;
import helper.bluetooth_helper.bluetooth_compound_button_data;

import com.google.samples.apps.iosched.ui.widget.SlidingTabLayout;

import file_operations.file_operations;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
//import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
//import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
//import android.content.BroadcastReceiver;
//import android.content.Context;
import android.content.Intent;
//import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements view_pager_adapter.fragment_initialization_interface
															  ,BT_spp.device_connection_status_interface
															  ,BT_spp.device_write_interface
															  ,BT_spp.device_read_interface{
	
//	BroadcastReceiver bt_device_connection_status;

	ViewPager v_pager;
    view_pager_adapter vpa;
    SlidingTabLayout tabs;
    
    CharSequence fragment_titles[]={"Interface","Terminal"};
   
    control_interface_fragment fragment_1;
	terminal_fragment fragment_2;
	
	file_data data_from_file;
	boolean file_data_changed=false;
	
//	public bluetooth_button_data button_data[]=new bluetooth_button_data[8];
//	public bluetooth_compound_button_data compound_button_data[]=new bluetooth_compound_button_data[2];

	public ArrayList<bluetooth_button_data> button_data=new ArrayList<bluetooth_button_data>();
	public ArrayList<bluetooth_compound_button_data> compound_button_data=new ArrayList<bluetooth_compound_button_data>();

	boolean in_program_mode=false;
	
	final String filename="preferences.br";
	
	final int program_mode_menu_option_position=3;
	final int discoverability_menu_option_position=2;
	final int new_device_list_activity_request_code=3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
//		Log.e("","Activity:onCreate "+this);
		
		if(file_operations.file_exists(getApplicationContext(),filename))
		{
			data_from_file=(file_data)file_operations.read_from_file(getApplicationContext(), filename);
			
			button_data=data_from_file.get_button_data();
			compound_button_data=data_from_file.get_compound_button_data();
			
			bluetooth_button.set_button_repetition_period(data_from_file.get_button_repetition_period());
			HexBoard.set_hex_board_call_time_out(data_from_file.get_hex_board_call_time_out_factor());
			HexBoard.set_hex_board_backspace_repetition_period(data_from_file.get_hex_board_backspace_repetition_period());
			
			terminal_fragment.set_colors(data_from_file.get_colors());
			terminal_fragment.set_clear_input_on_send(data_from_file.is_clear_input_on_send());
			
		}
		else
		{
			button_data.add(new bluetooth_button_data(getResources().getString(R.string.channel_up),	new byte[]{0x01},true,new byte[]{0x02},new byte[]{0x03}));
			button_data.add(new bluetooth_button_data(getResources().getString(R.string.channel_down),	new byte[]{0x11},true,new byte[]{0x12},new byte[]{0x13}));
			button_data.add(new bluetooth_button_data(getResources().getString(R.string.volume_up),		new byte[]{0x21},true,new byte[]{0x22},new byte[]{0x23}));
			button_data.add(new bluetooth_button_data(getResources().getString(R.string.volume_down),	new byte[]{0x31},true,new byte[]{0x32},new byte[]{0x33}));
			button_data.add(new bluetooth_button_data(getResources().getString(R.string.select),		new byte[]{0x41},true,new byte[]{0x42},new byte[]{0x43}));
			
			button_data.add(new bluetooth_button_data(getResources().getString(R.string.aaa),			new byte[]{0x51},true,new byte[]{0x52},new byte[]{0x53}));
			button_data.add(new bluetooth_button_data(getResources().getString(R.string.bbb),			new byte[]{0x61},true,new byte[]{0x62},new byte[]{0x63}));
			button_data.add(new bluetooth_button_data(getResources().getString(R.string.ccc),			new byte[]{0x71},true,new byte[]{0x72},new byte[]{0x73}));
			
			compound_button_data.add(new bluetooth_compound_button_data(getResources().getString(R.string.power_switch),	new byte[]{(byte)0xA1},new byte[]{(byte)0xA2},new byte[]{(byte)0xA3}));
			compound_button_data.add(new bluetooth_compound_button_data(getResources().getString(R.string.mute_switch),		new byte[]{(byte)0xB1},new byte[]{(byte)0xB2},new byte[]{(byte)0xB3}));
			
			bluetooth_button.set_button_repetition_period(200);
			HexBoard.set_hex_board_call_time_out(3);
			HexBoard.set_hex_board_backspace_repetition_period(200);
			
			int colors[]={	 Color.rgb(250,250,250)
							,Color.rgb(0x00, 0x00, 0x00)
							,Color.rgb(0x00, 0x00, 0x00)
						 };
			
			terminal_fragment.set_colors(colors);
			terminal_fragment.set_clear_input_on_send(true);
			
			data_from_file=new file_data(button_data,compound_button_data,
					bluetooth_button.get_button_repetition_period(),
					HexBoard.get_hex_board_call_delay_factor(),
					HexBoard.get_hex_board_backspace_repetition_period(),
					BlueRemote.assign_latest_device_if_list_is_empty,colors,1,true);
			
			file_data_changed=true;
		}
		
		BlueRemote.set_connected_device_list(new ArrayList<BT_spp>());
		BlueRemote.set_foreground_colors(new ArrayList<int[]>());
		BlueRemote.list_of_devices_assigned_to_components=new ArrayList<ArrayList<BT_spp>>(); 
        
		BlueRemote.set_device_assignment(data_from_file.get_device_assignment());
		
		vpa = new view_pager_adapter(getSupportFragmentManager(),fragment_titles,this);
		 
        v_pager = (ViewPager) findViewById(R.id.pager);
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        
        v_pager.setAdapter(vpa);
        
        tabs.setDistributeEvenly(true); 
        tabs.setViewPager(v_pager);
        
        fragment_1=(control_interface_fragment) vpa.getItem(0);
		fragment_2=(terminal_fragment) vpa.getItem(1);
		
		fragment_2.set_number_of_active_terminals(data_from_file.get_number_of_active_terminals());
		
//		v_pager.addOnPageChangeListener(new OnPageChangeListener(){
//
//			@Override
//			public void onPageScrollStateChanged(int arg0) {
//				
//				switch(arg0)
//				{
//				case 0:
//					break;
//					
//				case 1:
//					
//					int number_of_iterations=fragment_1.buttons.size();
//					for(int count=0;count<number_of_iterations;count++)
//					{
//						if(fragment_1.buttons.get(count).get_view().isPressed())
//						{
//							fragment_1.buttons.get(count).button_pressed_response(false);
//							fragment_1.buttons.get(count).get_view().setPressed(false);
//						}
//					}
//					break;
//					
//				case 2:
//					break;
//				}
//			}
//
//			@Override
//			public void onPageScrolled(int arg0, float arg1, int arg2) {
//			}
//
//			@Override
//			public void onPageSelected(int arg0) {
//			}
//			
//		});
				
//		bt_device_connection_status=new BroadcastReceiver() {
// 	        
// 	    	@Override
// 	        public void onReceive(Context context, final Intent intent) {
// 	            
// 	    		String action = intent.getAction();
// 	            
// 	    		if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) 
// 	    		{
//// 	    			Log.e(BLUETOOTH_SERVICE, "BT Device Connected");
// 	            }
// 	            else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) 
// 	            {
//// 	            	Log.e(BLUETOOTH_SERVICE, "BT Device about to Disconnect");
// 	            }
// 	            else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) 
// 	            {
//// 	            	Log.e(BLUETOOTH_SERVICE, "BT Device Disconnected");
// 	            }           
// 	        }
// 	    };
// 	    
// 	    registerReceiver(bt_device_connection_status,new IntentFilter(android.bluetooth.BluetoothDevice.ACTION_ACL_DISCONNECTED));
// 	    registerReceiver(bt_device_connection_status,new IntentFilter(android.bluetooth.BluetoothDevice.ACTION_ACL_CONNECTED));
// 	    registerReceiver(bt_device_connection_status,new IntentFilter(android.bluetooth.BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED));   
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
//		Log.e("","Activity:onResume "+this);
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
				BT_serial_device.set_dcsi(this);
				BT_serial_device.set_dwi(this);
				BT_serial_device.set_dri(this);
				
				BT_serial_device.connect();
			}
		}
		else if(requestCode==preferences_activity.preferences_activity_request_code)
		{
			BlueRemote.set_device_assignment(data.getByteExtra(preferences_activity.device_assignment_extra_name,BlueRemote.do_not_assign));
			int button_repetition_period=data.getIntExtra(preferences_activity.button_repetition_period_extra_name, 200);
			int hex_board_call_time_out_factor=data.getIntExtra(preferences_activity.hex_board_call_time_out_factor_extra_name, 3);
			int hex_board_backspace_repetition_period=data.getIntExtra(preferences_activity.hex_board_backspace_repetition_period_extra_name, 200);
			
			bluetooth_button.set_button_repetition_period(button_repetition_period);
			HexBoard.set_hex_board_call_time_out(hex_board_call_time_out_factor);
			HexBoard.set_hex_board_backspace_repetition_period(hex_board_backspace_repetition_period);
			
			int colors[]=new int[]{
					 data.getIntExtra(terminal_fragment.terminal_background_color_extra_name,0)
					,data.getIntExtra(terminal_fragment.terminal_default_incoming_foreground_color_extra_name,0)
					,data.getIntExtra(terminal_fragment.terminal_default_outgoing_foreground_color_extra_name,0)
			};
			
			terminal_fragment.set_colors(colors);
			fragment_2.set_background_color();
			
			terminal_fragment.set_clear_input_on_send(data.getBooleanExtra(terminal_fragment.terminal_clear_input_on_send_extra_name,false));
			fragment_2.set_number_of_active_terminals(data.getIntExtra(terminal_fragment.terminal_number_of_active_terminals_extra_name,0));
			
			fragment_2.update_terminal_view();
			
			data_from_file.set_device_assignment(BlueRemote.get_device_assignment());
			data_from_file.set_button_repetition_period(button_repetition_period);
			data_from_file.set_hex_board_call_time_out_factor(hex_board_call_time_out_factor);
			data_from_file.set_hex_board_backspace_repetition_period(hex_board_backspace_repetition_period);
			data_from_file.set_colors(colors);
			data_from_file.set_clear_input_on_send(terminal_fragment.is_clear_input_on_send());
			data_from_file.set_number_of_active_terminals(fragment_2.get_number_of_active_terminals());
			
			file_data_changed=true;
		}
		else
		{
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
		
		if(BlueRemote.is_discoverability_status()==true)
		{
			menu.getItem(discoverability_menu_option_position).setTitle(R.string.disable_discoverability);
		}
		else
		{
			menu.getItem(discoverability_menu_option_position).setTitle(R.string.enable_discoverability);
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
				if(BlueRemote.get_connected_device_list().size()>0)
				{
					Intent disconnect_device_activity_intent=new Intent(this,disconnect_device_activity.class);
					this.startActivity(disconnect_device_activity_intent);
				}
				else
				{
					Toast.makeText(getApplicationContext(), "No Connected Devices Available", Toast.LENGTH_SHORT).show();
				}
				return true;
				
			case R.id.menu_item_3:
				if(BlueRemote.is_discoverability_status()==false)
				{
					BlueRemote.set_discoverability_status(true);
					invalidateOptionsMenu();
					
					Thread discoverability_thread=new Thread(){
						
						public void run()
						{
							try 
							{
								BlueRemote.set_Bt_server_socket(BlueRemote.get_BtAdapter()
										.listenUsingRfcommWithServiceRecord("BlueRemote",BT_spp.get_Bt_spp_uuid())
										);
								
								while(BlueRemote.is_discoverability_status()==true)
								{
									BluetoothSocket socket= BlueRemote.get_Bt_server_socket().accept();
									
									if (socket != null)
									{
										BT_spp new_device=new BT_spp(socket.getRemoteDevice(),false);
										new_device.set_BT_socket(socket);
										
										new_device.connect();
									}
								}
							}
							catch (IOException e) 
							{
//								Log.e("",""+e.toString());
//								e.printStackTrace();
							}
						}
					};
					
					discoverability_thread.start();
				}
				else
				{
					BlueRemote.set_discoverability_status(false);
					try 
					{
						BlueRemote.get_Bt_server_socket().close();
					}
					catch (IOException e) 
					{
						e.printStackTrace();
					}
					invalidateOptionsMenu();
				}
				return true;
			
			case R.id.menu_item_4:
				
				if(in_program_mode==true)
				{
					file_data_changed=true;

					invalidateOptionsMenu();
					in_program_mode=false;
					
					fragment_1.tv_1.setVisibility(View.GONE);
					
					int number_of_iterations=fragment_1.buttons.size();
					for(int count=0;count<number_of_iterations;count++)
					{
						fragment_1.buttons.get(count).set_mode(bluetooth_button.NORMAL_MODE);
					}
					
					number_of_iterations=fragment_1.compound_buttons.size();
					for(int count=0;count<number_of_iterations;count++)
					{
						fragment_1.compound_buttons.get(count).set_mode(bluetooth_compound_button.NORMAL_MODE);
					}					
				}
				else
				{
					invalidateOptionsMenu();
					
					in_program_mode=true;
					
					fragment_1.tv_1.setVisibility(View.VISIBLE);
					
					int number_of_iterations=fragment_1.buttons.size();
					for(int count=0;count<number_of_iterations;count++)
					{
						fragment_1.buttons.get(count).set_mode(bluetooth_button.PROGRAM_MODE);
					}
					
					number_of_iterations=fragment_1.compound_buttons.size();
					for(int count=0;count<number_of_iterations;count++)
					{
						fragment_1.compound_buttons.get(count).set_mode(bluetooth_compound_button.PROGRAM_MODE);
					}					
				}
				return true;
				
			case R.id.menu_item_5:
				Intent preferences_intent=new Intent(this,preferences_activity.class);
				preferences_intent.putExtra(preferences_activity.device_assignment_extra_name,						BlueRemote.get_device_assignment());
				preferences_intent.putExtra(preferences_activity.button_repetition_period_extra_name,				bluetooth_button.get_button_repetition_period());
				preferences_intent.putExtra(preferences_activity.hex_board_call_time_out_factor_extra_name,			HexBoard.get_hex_board_call_delay_factor());
				preferences_intent.putExtra(preferences_activity.hex_board_backspace_repetition_period_extra_name,	HexBoard.get_hex_board_backspace_repetition_period());
				
				preferences_intent.putExtra(terminal_fragment.terminal_background_color_extra_name,			terminal_fragment.get_colors()[0]);
				preferences_intent.putExtra(terminal_fragment.terminal_default_incoming_foreground_color_extra_name,terminal_fragment.get_colors()[1]);
				preferences_intent.putExtra(terminal_fragment.terminal_default_outgoing_foreground_color_extra_name,terminal_fragment.get_colors()[2]);
				
				preferences_intent.putExtra(terminal_fragment.terminal_clear_input_on_send_extra_name,terminal_fragment.is_clear_input_on_send());
				preferences_intent.putExtra(terminal_fragment.terminal_number_of_active_terminals_extra_name,fragment_2.get_number_of_active_terminals());
				
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
				if(BlueRemote.is_discoverability_status()==true)
				{
					try 
					{
						BlueRemote.get_Bt_server_socket().close();
						BlueRemote.set_Bt_server_socket(null);
						BlueRemote.set_discoverability_status(false);
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
				}
				
				int number_of_connected_devices=BlueRemote.get_connected_device_list().size();
				
				for(int count=0;count<number_of_connected_devices;count++)
				{
					BlueRemote.get_connected_device(count).disconnect();	
				}
				BlueRemote.set_connected_device_list(null);
				BlueRemote.list_of_devices_assigned_to_components=null;
				
				//Direct Turn Off BT by App 
				if (BlueRemote.get_BtAdapter().isEnabled()) 
				{
					BlueRemote.get_BtAdapter().disable();
				}
				BlueRemote.set_BtAdapter(null);
					
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
		
//		unregisterReceiver(bt_device_connection_status);
		super.onDestroy();
	}

	@Override
	public void on_device_connection_pass() 
	{
		this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), "Device Connected", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void on_device_connection_pass(final BT_spp passed_device) 
	{
		Thread device_assigning_thread=new Thread(){
			
			public void run()
			{
				BlueRemote.add_to_connected_device_list(passed_device);
				BlueRemote.get_foreground_colors().add(new int[]{
						 terminal_fragment.get_default_incoming_foreground_color()
						,terminal_fragment.get_default_outgoing_foreground_color()
				});
				
   				int list_size=BlueRemote.list_of_devices_assigned_to_components.size();
   				
				if(BlueRemote.get_device_assignment()==BlueRemote.assign_all_devices)
    			{
    				for(int count_1=0;count_1<list_size;count_1++)
    				{
    					BlueRemote.list_of_devices_assigned_to_components.get(count_1)
   							.add(passed_device);
    					
    					passed_device.used_by_new_component();
					}
   				}
   				else if(BlueRemote.get_device_assignment()==BlueRemote.assign_latest_device_if_list_is_empty)
   				{
    				for(int count_1=0;count_1<list_size;count_1++)
   					{
   						if(BlueRemote.list_of_devices_assigned_to_components.get(count_1).size()==0)
   						{
   							BlueRemote.list_of_devices_assigned_to_components.get(count_1)
    							.add(passed_device);
   							
   							passed_device.used_by_new_component();
   						}
   					}
   				}
   				else if(BlueRemote.get_device_assignment()==BlueRemote.do_not_assign)
   				{				
    			}
				
				Thread device_input_reader_thread=new Thread(){
					
					public void run()
					{
						try 
						{
							while(passed_device.get_BT_socket().isConnected())
	    						{
	    							if(passed_device.get_BT_inputStream().available()>0)
								{
	    								passed_device.read(new byte[1024]);
								}
								else
								{
									Thread.sleep(100);
								}
	    						}
						}
						catch (IOException e) 
						{
							e.printStackTrace();
						} 
						catch (InterruptedException e) 
						{
							e.printStackTrace();
						}
					}
				};
				
				device_input_reader_thread.start();
			}
		};
			
		device_assigning_thread.start();
	}
	
	@Override
	public void on_device_connection_fail() 
	{
		this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), "Unable To Connect To Device.", Toast.LENGTH_LONG).show();
			}
		});
	}
	
	@Override
	public void on_device_connection_fail(BT_spp failed_device) 
	{
		
	}

	@Override
	public void on_device_write() {
		
	}

	@Override
	public void on_device_write(final BT_spp device_written_to, final byte written_byte) {
		
		fragment_2.terminal_handler.post(new Runnable(){

			@Override
			public void run() {
				Spannable text = new SpannableString(device_written_to.get_BT_Device().getName()+">:"+written_byte+"\n");        

				int position_of_device=BlueRemote.get_connected_device_list().indexOf(device_written_to);
				
				text.setSpan(new ForegroundColorSpan(BlueRemote.get_foreground_colors().get(position_of_device)[1]),
						0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

				fragment_2.tv_1.append(text);
				fragment_2.sv.fullScroll(View.FOCUS_DOWN);
			}
		});
	}

	@Override
	public void on_device_write(final BT_spp device_written_to, final byte[] written_bytes) {
		
		fragment_2.terminal_handler.post(new Runnable(){

			@Override
			public void run() {
				
				Spannable text = new SpannableString(device_written_to.get_BT_Device().getName()+">:"+(new String(written_bytes))+"\n");        
				
				int position_of_device=BlueRemote.get_connected_device_list().indexOf(device_written_to);
				
				text.setSpan(new ForegroundColorSpan(BlueRemote.get_foreground_colors().get(position_of_device)[1]),
						0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

				fragment_2.tv_1.append(text);
				fragment_2.sv.fullScroll(View.FOCUS_DOWN);
			}
		});
	}

	@Override
	public void on_device_read() {
		
	}

	@Override
	public void on_device_read(final BT_spp device_read_from, final byte read_byte) {
		
		fragment_2.terminal_handler.post(new Runnable(){

			@Override
			public void run() {
				
				Spannable text = new SpannableString(device_read_from.get_BT_Device().getName()+"<:"+(read_byte)+"\n");        
				
				int position_of_device=BlueRemote.get_connected_device_list().indexOf(device_read_from);
				
				text.setSpan(new ForegroundColorSpan(BlueRemote.get_foreground_colors().get(position_of_device)[0]),
						0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

				fragment_2.tv_1.append(text);
				fragment_2.sv.fullScroll(View.FOCUS_DOWN);
			}
		});
	}

	@Override
	public void on_device_read(final BT_spp device_read_from, final byte[] read_bytes,
			int number_of_bytes_read) {
		
		fragment_2.terminal_handler.post(new Runnable(){

			@Override
			public void run() {
				Spannable text = new SpannableString(device_read_from.get_BT_Device().getName()+"<:"+(new String(read_bytes))+"\n");
				
				int position_of_device=BlueRemote.get_connected_device_list().indexOf(device_read_from);
				
				text.setSpan(new ForegroundColorSpan(BlueRemote.get_foreground_colors().get(position_of_device)[0]),
						0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

				fragment_2.tv_1.append(text);
				fragment_2.sv.fullScroll(View.FOCUS_DOWN);
			}
		});
	}

	@Override
	public Fragment initialize_fragment(int order_of_fragment_in_view_pager) 
	{
		switch(order_of_fragment_in_view_pager)
		{
		case 0:
			return new control_interface_fragment();
			
		case 1:
			return new terminal_fragment();
		}
		
		return null;
	}
}
