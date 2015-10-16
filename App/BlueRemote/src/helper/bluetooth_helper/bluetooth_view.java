package helper.bluetooth_helper;

import java.util.ArrayList;
import java.util.HashMap;

import helper.call_this_method_interface;
import helper.call_this_method_to_return_boolean_interface;
import helper.bluetooth_helper.BT_spp;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnLongClickListener;

public class bluetooth_view<T> implements OnLongClickListener
{
	T calling_activity_or_fragment;
	ArrayList<BT_spp> BT_serial_devices;
	
	View view;
	
	private boolean mode=NORMAL_MODE;
	
	Intent programming_activity_intent;
	int programming_activity_request_code;
	
	call_this_method_interface before_starting_programming_activity;
	call_this_method_interface after_finishing_programming_activity;
	call_this_method_to_return_boolean_interface on_long_click_when_in_normal_mode;
	
	call_this_method_interface put_more_extras=null; 
	call_this_method_interface get_more_extras=null; 
	
	call_this_method_interface on_trying_to_tx_when_device_list_empty=null; 

	public static final boolean PROGRAM_MODE=true;
	public static final boolean NORMAL_MODE=false;
	
	public static final String passed_bluetooth_view="PASSED_BLUETOOTH_VIEW";
	public static final String passed_intent="PASSED_INTENT";
	
	public bluetooth_view()
	{
		
	}
	
	public bluetooth_view(T calling_activity_or_fragment,ArrayList<BT_spp> BT_serial_devices,View view) 
	{
		super();
		
		this.calling_activity_or_fragment=calling_activity_or_fragment;
		
		if(BT_serial_devices==null)
		{
			this.BT_serial_devices=new ArrayList<BT_spp>();
		}
		else
		{
			this.BT_serial_devices=BT_serial_devices;
		}
		
		this.view = view;
		
		this.view.setOnLongClickListener(this);
	}
		
	public T get_calling_activity_or_fragment() 
	{
		return calling_activity_or_fragment;
	}

	public void set_calling_activity_or_fragment(T calling_activity_or_fragment) 
	{
		this.calling_activity_or_fragment = calling_activity_or_fragment;
	}

	public ArrayList<BT_spp> get_BT_serial_devices() 
	{
		return BT_serial_devices;
	}

	public void set_BT_serial_devices(ArrayList<BT_spp> bT_serial_devices) 
	{
		this.BT_serial_devices = bT_serial_devices;
	}

	public void add_BT_serial_device(BT_spp bT_serial_device) 
	{
		this.BT_serial_devices.add(bT_serial_device);
	}
	
	public void remove_BT_serial_device(int index) 
	{
		this.BT_serial_devices.remove(index);
	}
	
	public View get_view() 
	{
		return view;
	}

	public void set_view(View view) 
	{
		this.view = view;
	}
	
	public boolean is_mode() 
	{
		return mode;
	}

	public void set_mode(boolean mode) 
	{
		this.mode = mode;
	}
	
	public Intent get_programming_activity_intent()
	{
		return programming_activity_intent;
	}

	public void set_programming_activity_intent(Intent programming_activity_intent) 
	{
		this.programming_activity_intent = programming_activity_intent;
	}
	
	public void set_programming_activity_intent(String input) 
	{
		this.programming_activity_intent = new Intent(input);
	}
	
	public int get_programming_activity_request_code() 
	{
		return programming_activity_request_code;
	}

	public void set_programming_activity_request_code(int programming_activity_request_code) 
	{
		this.programming_activity_request_code = programming_activity_request_code;
	}
	
	public void set_programming_parameters(Intent programming_activity_intent,int programming_activity_request_code) 
	{
		this.programming_activity_intent = programming_activity_intent;
		this.programming_activity_request_code = programming_activity_request_code;
	}
	
	public call_this_method_interface get_before_starting_programming_activity() {
		return before_starting_programming_activity;
	}

	public void set_before_starting_programming_activity(
			call_this_method_interface before_starting_programming_activity) {
		this.before_starting_programming_activity = before_starting_programming_activity;
	}

	public call_this_method_interface get_after_finishing_programming_activity() {
		return after_finishing_programming_activity;
	}

	public void set_after_finishing_programming_activity(
			call_this_method_interface after_finishing_programming_activity) {
		this.after_finishing_programming_activity = after_finishing_programming_activity;
	}
	
	public call_this_method_to_return_boolean_interface get_on_long_click_when_in_normal_mode() {
		return on_long_click_when_in_normal_mode;
	}

	public void set_on_long_click_when_in_normal_mode(
			call_this_method_to_return_boolean_interface on_long_click_when_in_normal_mode) {
		this.on_long_click_when_in_normal_mode = on_long_click_when_in_normal_mode;
	}
	
	public call_this_method_interface get_put_more_extras() {
		return put_more_extras;
	}

	public void set_put_more_extras(call_this_method_interface put_more_extras) {
		this.put_more_extras = put_more_extras;
	}

	public call_this_method_interface get_more_extras() {
		return get_more_extras;
	}

	public void set_get_more_extras(call_this_method_interface get_more_extras) {
		this.get_more_extras = get_more_extras;
	}
	
	public call_this_method_interface get_on_trying_to_tx_when_device_list_empty() {
		return on_trying_to_tx_when_device_list_empty;
	}

	public void set_on_trying_to_tx_when_device_list_empty(
			call_this_method_interface on_trying_to_tx_when_device_list_empty) {
		this.on_trying_to_tx_when_device_list_empty = on_trying_to_tx_when_device_list_empty;
	}
	
	@Override
	public boolean onLongClick(View v) 
	{
		if(this.is_mode()==bluetooth_view.PROGRAM_MODE)
		{
			if((this.before_starting_programming_activity==null)||(this.after_finishing_programming_activity==null))
			{
				throw new NullPointerException();
			}
			
			before_starting_programming_activity.call_this_method(null);
			if(get_put_more_extras()!=null)
			{
				get_put_more_extras().call_this_method(this);
			}
			
			if(calling_activity_or_fragment instanceof Activity)
			{
				((Activity)calling_activity_or_fragment).startActivityForResult(this.programming_activity_intent,this.programming_activity_request_code);
			}
			else if(calling_activity_or_fragment instanceof Fragment)
			{
				((Fragment)calling_activity_or_fragment).startActivityForResult(this.programming_activity_intent,this.programming_activity_request_code);
			}
			else
			{
				
			}
			
			return true;
		}
		else
		{
			if(this.on_long_click_when_in_normal_mode!=null)
			{
				return this.on_long_click_when_in_normal_mode.call_this_method();
			}
			return false;
		}
	}
	
	public void update_view_onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(requestCode==this.programming_activity_request_code)
		{	
			after_finishing_programming_activity.call_this_method(data);
			
			if(get_more_extras()!=null)
			{
				HashMap<String, Object> hp=new HashMap<String, Object>();
				hp.put(passed_bluetooth_view, this);
				hp.put(passed_intent, data);
				
				get_more_extras().call_this_method(hp);
			}
		}
	}
}
