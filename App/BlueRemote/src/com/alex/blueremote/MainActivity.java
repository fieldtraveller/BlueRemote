package com.alex.blueremote;

//import java.nio.charset.Charset;
import java.util.ArrayList;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {
	
	BlueRemote global_variables_object;
	
	final int new_device_list_activity_request_code=3;
	final int hexboard_activity_request_code=4;
	
//	String BT_mac_address="20:13:05:27:09:64";	//ALEXBT
//	String BT_mac_address="98:D3:31:80:18:29";	//BURZO
	
	boolean new_BT_device_available=false;
	boolean in_program_mode=false;
	
	EditText et_1;
	TextView tv_1;
	
	file_data data_from_file;
	boolean file_data_changed=false;
	
	Button send;
	bluetooth_button_data button_data[]=new bluetooth_button_data[5];
	bluetooth_button buttons[]=new bluetooth_button[5]; 
	
	bluetooth_switch_data switch_data[]=new bluetooth_switch_data[2];
	bluetooth_switch switchs[]=new bluetooth_switch[2]; 
	
	Handler hex_board_handler;
	Runnable hex_board_runnable;
	
	final String filename="preferences.br";
	final int program_mode_menu_option_position=3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
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
					bluetooth_button.getButton_delay(),HexBoard.getHex_board_call_delay_factor(),HexBoard.getHex_board_backspace_delay());
			
			file_data_changed=true;
//			file_operations.save_to_file(getApplicationContext(), filename,data_from_file);
		}
		
		//channel_up
		buttons[0]=new bluetooth_button(this, null, (Button)findViewById(R.id.button1),button_data[0]);
		//channel_down
		buttons[1]=new bluetooth_button(this, null, (Button)findViewById(R.id.button5),button_data[1]);
		//volume_up
		buttons[2]=new bluetooth_button(this, null, (Button)findViewById(R.id.button4),button_data[2]);
		//volume_down
		buttons[3]=new bluetooth_button(this, null, (Button)findViewById(R.id.button2),button_data[3]);
		//select
		buttons[4]=new bluetooth_button(this, null, (Button)findViewById(R.id.button3),button_data[4]);
		
		//power
		switchs[0]=new bluetooth_switch(this, null, (Switch)findViewById(R.id.switch1),switch_data[0]);
		//mute
		switchs[1]=new bluetooth_switch(this, null, (Switch)findViewById(R.id.switch2),switch_data[1]);
															 
//		Intent programming_activity_intent = new Intent(this,programming_activity.class);
		Intent programming_activity_intent = new Intent(".programming_activity");
		
		for(int count=0;count<buttons.length;count++)
		{
			buttons[count].setProgramming_parameters(programming_activity_intent,100+count);
		}
		
		for(int count=0;count<switchs.length;count++)
		{
			switchs[count].setProgramming_parameters(programming_activity_intent,200+count);
		}

		send=(Button)findViewById(R.id.button6);
		et_1=(EditText)findViewById(R.id.editText1_preferences);
		tv_1=(TextView)findViewById(R.id.textView1);
		
		tv_1.setVisibility(View.GONE);
		
		hex_board_handler=new Handler();
		
		global_variables_object = (BlueRemote)getApplicationContext();
		global_variables_object.setConnected_device_list(new ArrayList<BT_spp>());
		
		BT_spp.ui_handler=new Handler();
		
		switchs[0].setSet_task_on_switch_off(new set_task(){

			@Override
			public void perform_task() 
			{
				switchs[1].switcher.setChecked(false);
			}
		});
		
//	    send.setOnClickListener(new View.OnClickListener() {
//	    	
//	    	@Override
//			public void onClick(View v) {
//	    		
//	    		BT_serial_device.write(data_input.getText().toString().getBytes(Charset.forName("ISO-8859-1")));
//	    	}
//		});
	    	    
	    hex_board_runnable=new Runnable(){

			@Override
			public void run() {
				
				Intent launch_hex_board= new Intent(MainActivity.this,HexBoard.class);
				launch_hex_board.putExtra(HexBoard.initial_text, et_1.getText().toString());
			    startActivityForResult(launch_hex_board, hexboard_activity_request_code);
			}			
		};
	    		
	    et_1.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				switch(event.getAction())
				{
					case MotionEvent.ACTION_DOWN:
						hex_board_handler.postDelayed(hex_board_runnable,HexBoard.hex_board_call_time_out);
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

	}
	
	//*
	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  
    {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode==new_device_list_activity_request_code)
		{
			if(data.getBooleanExtra(new_device_list_activity.new_bt_device_selected_extra_name,false))
			{
				BT_spp BT_serial_device=data.getParcelableExtra(new_device_list_activity.new_bt_device_extra_name);
				new_BT_device_available=true;
				
				for(int count=0;count<buttons.length;count++)
				{
					buttons[count].add_BT_serial_device(BT_serial_device);
				}
				
				for(int count=0;count<switchs.length;count++)
				{
					switchs[count].add_BT_serial_device(BT_serial_device);
				}
				
				global_variables_object.add_to_Connected_device_list(BT_serial_device);
				
				BT_serial_device.connect();
			}
		}
		else if(requestCode==hexboard_activity_request_code)
		{
			et_1.setText(data.getStringExtra(HexBoard.stringed_data));
			et_1.setSelection(et_1.getText().length());
		}
		else if(requestCode==preferences_activity.preferences_activity_request_code)
		{
			int button_repetition_period=data.getIntExtra(preferences_activity.button_repetition_period_extra_name, 200);
			int hex_board_call_time_out_factor=data.getIntExtra(preferences_activity.hex_board_call_time_out_factor_extra_name, 3);
			int hex_board_backspace_repetition_period=data.getIntExtra(preferences_activity.hex_board_backspace_repetition_period_extra_name, 200);
			
			bluetooth_button.setButton_delay(button_repetition_period);
			HexBoard.setHex_board_call_delay(hex_board_call_time_out_factor);
			HexBoard.setHex_board_backspace_delay(hex_board_backspace_repetition_period);
			
			data_from_file.setButton_repetition_period(button_repetition_period);
			data_from_file.set_hex_board_call_time_out_factor(hex_board_call_time_out_factor);
			data_from_file.set_hex_board_backspace_repetition_period(hex_board_backspace_repetition_period);
			file_data_changed=true;
		}
		else
		{
			for(int count=0;count<buttons.length;count++)
			{
				buttons[count].update_button_onActivityResult(requestCode,resultCode,data);
			}
			
			for(int count=0;count<switchs.length;count++)
			{
				switchs[count].update_switch_onActivityResult(requestCode,resultCode,data);
			}
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
					
					tv_1.setVisibility(View.GONE);
					send.setVisibility(View.VISIBLE);
					send.invalidate();
					et_1.setVisibility(View.VISIBLE);
					et_1.invalidate();
					
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
					invalidateOptionsMenu();
					
//					item.setTitle(R.string.normal_mode);
					in_program_mode=true;
					
					tv_1.setVisibility(View.VISIBLE);
					send.setVisibility(View.GONE);
					send.invalidate();
					et_1.setVisibility(View.GONE);
					et_1.invalidate();
					
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
				
			case R.id.menu_item_5:
				Intent preferences_intent=new Intent(this,preferences_activity.class);
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
//		if(new_BT_device_available == true )
//		{
//			BT_serial_device.disconnect();
//		}
		
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
		
		super.onDestroy();
	}
}
