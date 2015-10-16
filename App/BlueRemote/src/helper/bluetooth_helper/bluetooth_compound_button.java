package helper.bluetooth_helper;

import java.util.ArrayList;

import helper.call_this_method_interface;
import helper.bluetooth_helper.BT_spp;
import helper.bluetooth_helper.bluetooth_compound_button_data;
import helper.bluetooth_helper.bluetooth_view;

import android.content.Intent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class bluetooth_compound_button<T> extends bluetooth_view<T> implements OnCheckedChangeListener
{
	bluetooth_compound_button_data compound_button_data;
	
	call_this_method_interface on_compound_button_change=null; 
	call_this_method_interface on_compound_button_on=null; 
	call_this_method_interface on_compound_button_off=null; 

	public static final String input_type_compound_button="COMPOUND_BUTTON";
	
	public bluetooth_compound_button()
	{	
	}
	
	public bluetooth_compound_button(T get_calling_activity_or_fragment,ArrayList<BT_spp> get_BT_serial_devices,CompoundButton compound_buttoner) 
	{
		super(get_calling_activity_or_fragment,get_BT_serial_devices,(View)compound_buttoner);
		
		initialise_interfaces();
		
		((CompoundButton) this.get_view()).setOnCheckedChangeListener(this);
	}
		
	public bluetooth_compound_button(T get_calling_activity_or_fragment,ArrayList<BT_spp> get_BT_serial_devices,CompoundButton compound_buttoner
			,bluetooth_compound_button_data compound_button_data) 
	{
		super(get_calling_activity_or_fragment,get_BT_serial_devices,(View)compound_buttoner);
		
		this.compound_button_data=compound_button_data;
		
		initialise_interfaces();
		
		((CompoundButton) this.get_view()).setOnCheckedChangeListener(this);
	}

	void initialise_interfaces()
	{
		this.set_before_starting_programming_activity(new call_this_method_interface() {

			@Override
			public void call_this_method(Object object) {

				get_programming_activity_intent().putExtra(bluetooth_compound_button_data.compound_button_text_extra_name,get_compound_button_data().get_compound_button_text());
			
				get_programming_activity_intent().putExtra(bluetooth_compound_button_data.compound_button_code_extra_name,get_compound_button_data().get_compound_button_code());
				get_programming_activity_intent().putExtra(bluetooth_compound_button_data.compound_button_on_code_extra_name,get_compound_button_data().get_compound_button_on_code());
				get_programming_activity_intent().putExtra(bluetooth_compound_button_data.compound_button_off_code_extra_name,get_compound_button_data().get_compound_button_off_code());
			 
			}

			});
		
		this.set_after_finishing_programming_activity(new call_this_method_interface() {

			@Override
			public void call_this_method(Object object) 
			{
				Intent data=(Intent)object;
				
				compound_button_data.set_compound_button_text(data.getStringExtra(bluetooth_compound_button_data.compound_button_text_extra_name));
				((CompoundButton)get_view()).setText(get_compound_button_data().get_compound_button_text());
				
				compound_button_data.set_compound_button_code(data.getByteArrayExtra(bluetooth_compound_button_data.compound_button_code_extra_name));
				compound_button_data.set_compound_button_on_code(data.getByteArrayExtra(bluetooth_compound_button_data.compound_button_on_code_extra_name));
				compound_button_data.set_compound_button_off_code(data.getByteArrayExtra(bluetooth_compound_button_data.compound_button_off_code_extra_name));
			}			
		});
		
	}

	public bluetooth_compound_button_data get_compound_button_data() 
	{
		return compound_button_data;
	}

	public void set_compound_button_data(bluetooth_compound_button_data compound_button_data) 
	{
		this.compound_button_data = compound_button_data;
		((CompoundButton)get_view()).setText(get_compound_button_data().get_compound_button_text());
	}

	public call_this_method_interface get_on_compound_button_change() {
		return on_compound_button_change;
	}

	public void set_on_compound_button_change(call_this_method_interface on_compound_button_change) {
		this.on_compound_button_change = on_compound_button_change;
	}

	public call_this_method_interface get_on_compound_button_on() {
		return on_compound_button_on;
	}

	public void set_on_compound_button_on(call_this_method_interface on_compound_button_on) {
		this.on_compound_button_on = on_compound_button_on;
	}

	public call_this_method_interface get_on_compound_button_off() {
		return on_compound_button_off;
	}

	public void set_on_compound_button_off(call_this_method_interface on_compound_button_off) {
		this.on_compound_button_off = on_compound_button_off;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		
		int device_count=get_BT_serial_devices().size();
		if(device_count==0)
		{
			if(this.get_on_trying_to_tx_when_device_list_empty()!=null)
			{
				this.get_on_trying_to_tx_when_device_list_empty().call_this_method(null);
			}
		}
		else
		{
			for(int count=0;count<device_count;count++)
			{
				this.get_BT_serial_devices().get(count).write(this.compound_button_data.get_compound_button_code());
			}
		}
		
		if(on_compound_button_change!=null)
		{
			on_compound_button_change.call_this_method(null);
		}
		
	    if(isChecked)
		{
	    	for(int count=0;count<device_count;count++)
			{
				this.get_BT_serial_devices().get(count).write(this.compound_button_data.get_compound_button_on_code());
			}
	    	
	    	if(on_compound_button_on!=null)
			{
				on_compound_button_on.call_this_method(null);
			}
	    }
		else
		{
			for(int count=0;count<device_count;count++)
			{
				this.get_BT_serial_devices().get(count).write(this.compound_button_data.get_compound_button_off_code());
			}
			
			if(on_compound_button_off!=null)
			{
				on_compound_button_off.call_this_method(null);
			}
		}
	}	
}
