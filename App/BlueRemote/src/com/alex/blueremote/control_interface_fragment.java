package com.alex.blueremote;

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
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

public class control_interface_fragment extends Fragment {
 
    Activity container_activity;
	
	TextView tv_1;
	
	ArrayList<bluetooth_button<Fragment>> buttons; 
	ArrayList<bluetooth_compound_button<Fragment>> compound_buttons;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		Log.e("","Fragment:onCreate "+this);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        
		View v =inflater.inflate(R.layout.control_interface_fragment_layout,container,false);
//        Log.e("","Fragment:onCreateView "+this);
        
        return v;
    }
	
	@Override
	public void onActivityCreated (Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
//		Log.e("","Fragment:onActivityCreated "+this);
		this.container_activity=(MainActivity) getActivity();
		this.container_activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        
		buttons=new ArrayList<bluetooth_button<Fragment>>(); 
		compound_buttons=new ArrayList<bluetooth_compound_button<Fragment>>();
		
		//channel_up
  		buttons.add(
  				new bluetooth_button<Fragment>(this, null, (Button)this.getView().findViewById(R.id.button1_cif),((MainActivity)this.container_activity).button_data.get(0))
  				);
  		//channel_down
  		buttons.add(
  				new bluetooth_button<Fragment>(this, null, (Button)this.getView().findViewById(R.id.button5_cif),((MainActivity)this.container_activity).button_data.get(1))
  				);
  		//volume_up
  		buttons.add(
   				new bluetooth_button<Fragment>(this, null, (Button)this.getView().findViewById(R.id.button4_cif),((MainActivity)this.container_activity).button_data.get(2))
  				);
  		//volume_down
  		buttons.add(
  		    	new bluetooth_button<Fragment>(this, null, (Button)this.getView().findViewById(R.id.button2_cif),((MainActivity)this.container_activity).button_data.get(3))
  				);
  		//select
  		buttons.add(
   				new bluetooth_button<Fragment>(this, null, (Button)this.getView().findViewById(R.id.button3_cif),((MainActivity)this.container_activity).button_data.get(4))
  				);
  		
  		//A
  		buttons.add(
   				new bluetooth_button<Fragment>(this, null, (Button)this.getView().findViewById(R.id.button6_cif),((MainActivity)this.container_activity).button_data.get(5))
  				);
  		//B
  		buttons.add(
   				new bluetooth_button<Fragment>(this, null, (Button)this.getView().findViewById(R.id.button7_cif),((MainActivity)this.container_activity).button_data.get(6))
  				);
  		//C
  		buttons.add(
   				new bluetooth_button<Fragment>(this, null, (Button)this.getView().findViewById(R.id.button8_cif),((MainActivity)this.container_activity).button_data.get(7))
  				);
  		
  		//power
  		compound_buttons.add(
   				new bluetooth_compound_button<Fragment>(this, null, (CompoundButton)this.getView().findViewById(R.id.switch1_cif),((MainActivity)this.container_activity).compound_button_data.get(0))
  			);
  		//mute
  		compound_buttons.add(
   				new bluetooth_compound_button<Fragment>(this, null, (CompoundButton)this.getView().findViewById(R.id.switch2_cif),((MainActivity)this.container_activity).compound_button_data.get(1))
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

  		tv_1=(TextView)this.getView().findViewById(R.id.textView1_cif);
  		
  		tv_1.setVisibility(View.GONE);
  		
  		compound_buttons.get(0).set_on_compound_button_off(new call_this_method_interface() {

			@Override
			public void call_this_method(Object object) {
				((CompoundButton) compound_buttons.get(1).get_view()).setChecked(false);
			}

		});		
  		
	    
	}
	
	@Override
	public void onPause ()
	{
		super.onPause();
//		Log.e("", "Fragment:onPause "+this);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)  
    {
		super.onActivityResult(requestCode, resultCode, data);
		
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
    }  
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
//		Log.e("", "Fragment:onDestroy "+this);
	}
}
