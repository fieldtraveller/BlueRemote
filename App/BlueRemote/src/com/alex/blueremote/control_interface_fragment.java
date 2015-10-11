package com.alex.blueremote;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

import helper.call_this_method_interface;
import helper.bluetooth_helper.BT_spp;
import helper.bluetooth_helper.bluetooth_button;
import helper.bluetooth_helper.bluetooth_compound_button;
import helper.bluetooth_helper.bluetooth_view;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

public class control_interface_fragment extends Fragment {
 
    Activity container_activity;
	
	EditText et_1;
	TextView tv_1;
	
	Button send;
//	bluetooth_button<Fragment> buttons[]=new bluetooth_button[5]; 
//	bluetooth_compound_button<Fragment> compound_buttons[]=new bluetooth_compound_button[2]; 
	
	ArrayList<bluetooth_button<Fragment>> buttons; 
	ArrayList<bluetooth_compound_button<Fragment>> compound_buttons;
	
	ArrayList<BT_spp> send_device_list;
	
	final int hexboard_activity_request_code=4;
	Handler hex_board_handler;
	Runnable hex_board_runnable;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("","Fragment:onCreate");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        
		View v =inflater.inflate(R.layout.control_interface_fragment_layout,container,false);
        Log.e("","Fragment:onCreateView");
        
        return v;
    }
	
	@Override
	public void onActivityCreated (Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		Log.e("","Fragment:onActivityCreated");
		this.container_activity=(MainActivity) getActivity();
		this.container_activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        
		buttons=new ArrayList<bluetooth_button<Fragment>>(); 
		compound_buttons=new ArrayList<bluetooth_compound_button<Fragment>>();
		
		//channel_up
  		buttons.add(
  				new bluetooth_button<Fragment>(this, null, (Button)this.getView().findViewById(R.id.button1_cif),((MainActivity)this.container_activity).button_data[0])
  				);
  		//channel_down
  		buttons.add(
  				new bluetooth_button<Fragment>(this, null, (Button)this.getView().findViewById(R.id.button5_cif),((MainActivity)this.container_activity).button_data[1])
  				);
  		//volume_up
  		buttons.add(
   				new bluetooth_button<Fragment>(this, null, (Button)this.getView().findViewById(R.id.button4_cif),((MainActivity)this.container_activity).button_data[2])
  				);
  		//volume_down
  		buttons.add(
  		    	new bluetooth_button<Fragment>(this, null, (Button)this.getView().findViewById(R.id.button2_cif),((MainActivity)this.container_activity).button_data[3])
  				);
  		//select
  		buttons.add(
   				new bluetooth_button<Fragment>(this, null, (Button)this.getView().findViewById(R.id.button3_cif),((MainActivity)this.container_activity).button_data[4])
  				);
  		
  		//power
  		compound_buttons.add(
   				new bluetooth_compound_button<Fragment>(this, null, (CompoundButton)this.getView().findViewById(R.id.switch1_cif),((MainActivity)this.container_activity).compound_button_data[0])
  			);
  		//mute
  		compound_buttons.add(
   				new bluetooth_compound_button<Fragment>(this, null, (CompoundButton)this.getView().findViewById(R.id.switch2_cif),((MainActivity)this.container_activity).compound_button_data[1])
  			);
  															 
  		Intent programming_activity_intent = new Intent(container_activity,programming_activity.class);

  		call_this_method_interface put_more_extras=new call_this_method_interface(){

			@SuppressWarnings("unchecked")
			@Override
			public void call_this_method(Object object) 
			{
				bluetooth_view<Fragment> passed_bluetooth_view=(bluetooth_view<Fragment>)object;
				
				if(passed_bluetooth_view instanceof bluetooth_button<?>)
				{
					passed_bluetooth_view.get_programming_activity_intent().putExtra(programming_activity.input_type,bluetooth_button.input_type_button);
				}
				else if(passed_bluetooth_view instanceof bluetooth_compound_button<?>)
				{
					passed_bluetooth_view.get_programming_activity_intent().putExtra(programming_activity.input_type,bluetooth_compound_button.input_type_compound_button);
				}
				else
				{
					
				}
				
				passed_bluetooth_view.get_programming_activity_intent().putIntegerArrayListExtra(connected_device_list_activity.selected_devices_list_indices_extra_name,BT_spp.get_indices(
							((BlueRemote)(((Fragment)passed_bluetooth_view.get_calling_activity_or_fragment()).getActivity()).getApplicationContext())
							.get_connected_device_list(),passed_bluetooth_view.get_BT_serial_devices())
							);
			}
  			
  		}; 
  		call_this_method_interface get_more_extras=new call_this_method_interface(){

			@SuppressWarnings("unchecked")
			@Override
			public void call_this_method(Object object) {
				
				HashMap<String, Object> hp=(HashMap<String, Object>)object;
				bluetooth_view<Fragment> passed_bluetooth_view=(bluetooth_view<Fragment>) hp.get(bluetooth_view.passed_bluetooth_view);
				Intent passed_intent=(Intent) hp.get(bluetooth_view.passed_intent);
				
				
				BT_spp.update_list_based_on_indices(
							((BlueRemote)(((Fragment)passed_bluetooth_view.get_calling_activity_or_fragment()).getActivity()).getApplicationContext())
							.get_connected_device_list()
							,passed_bluetooth_view.get_BT_serial_devices()
							,passed_intent.getIntegerArrayListExtra(connected_device_list_activity.selected_devices_list_indices_extra_name));
			}
  		}; 

  		int number_of_iterations=buttons.size();
  		for(int count=0;count<number_of_iterations;count++)
  		{
  			buttons.get(count).set_programming_parameters(programming_activity_intent,100+count);
  			
  			buttons.get(count).set_put_more_extras(put_more_extras);
  			buttons.get(count).set_get_more_extras(get_more_extras);
  			
  			((MainActivity)this.container_activity)
  				.global_variables_object.list_of_devices_assigned_to_components
  					.add(buttons.get(count).get_BT_serial_devices());
  		}
  		
  		number_of_iterations=compound_buttons.size();
  		for(int count=0;count<number_of_iterations;count++)
  		{
  			compound_buttons.get(count).set_programming_parameters(programming_activity_intent,200+count);
  			
  			compound_buttons.get(count).set_put_more_extras(put_more_extras);
  			compound_buttons.get(count).set_get_more_extras(get_more_extras);
  			
  			((MainActivity)this.container_activity)
				.global_variables_object.list_of_devices_assigned_to_components
					.add(compound_buttons.get(count).get_BT_serial_devices());

  		}

  		send=(Button)this.getView().findViewById(R.id.button6_cif);
  		et_1=(EditText)this.getView().findViewById(R.id.editText1_cif);
  		tv_1=(TextView)this.getView().findViewById(R.id.textView1_cif);
  		
  		tv_1.setVisibility(View.GONE);
  		
  		hex_board_handler=new Handler();
  		
  		send_device_list=((MainActivity)this.container_activity).terminal_device_list;
  		
  		compound_buttons.get(0).set_on_compound_button_off(new call_this_method_interface() {

			@Override
			public void call_this_method(Object object) {
				((CompoundButton) compound_buttons.get(1).get_view()).setChecked(false);
			}

		});		
  		
	    send.setOnClickListener(new View.OnClickListener() {
	    	
	    	@Override
			public void onClick(View v) {
	    		
	    		int device_count=send_device_list.size();
				for(int count=0;count<device_count;count++)
				{
					send_device_list.get(count).write(et_1.getText().toString().getBytes(Charset.forName("ISO-8859-1")));
				}	    		
	    	}
		});
	    	    
	    hex_board_runnable=new Runnable(){

			@Override
			public void run() {
				
				Intent launch_hex_board= new Intent(container_activity,HexBoard.class);
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
	public void onPause ()
	{
		super.onPause();
		Log.e("", "Fragment:onPause");
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)  
    {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode==hexboard_activity_request_code)
		{
			et_1.setText(data.getStringExtra(HexBoard.stringed_data));
			et_1.setSelection(et_1.getText().length());
		}
		else
		{
			int number_of_iterations=buttons.size();
			for(int count=0;count<number_of_iterations;count++)
			{
				buttons.get(count).update_view_onActivityResult(requestCode,resultCode,data);
			}
			
			number_of_iterations=compound_buttons.size();
			for(int count=0;count<number_of_iterations;count++)
			{
				compound_buttons.get(count).update_view_onActivityResult(requestCode,resultCode,data);
			}
			
			Log.e("","InFragment requestCode:"+requestCode+"\n data:"+data);
		}
    }  
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}
}
