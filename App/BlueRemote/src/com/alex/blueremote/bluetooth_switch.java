package com.alex.blueremote;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

public class bluetooth_switch implements OnCheckedChangeListener,OnLongClickListener
{
	ArrayList<BT_spp> BT_serial_devices;
	Switch switcher;
	
	private boolean mode=NORMAL_MODE;
	
	bluetooth_switch_data switch_data;
	
	Intent programming_activity_intent;
	int programming_activity_request_code;
	Activity calling_activity;
	
	set_task set_task_on_switch_change=null; 
	set_task set_task_on_switch_on=null; 
	set_task set_task_on_switch_off=null; 
	
	public void setSet_task_on_switch_on(set_task set_task_on_switch_on) {
		this.set_task_on_switch_on = set_task_on_switch_on;
	}

	public set_task getSet_task_on_switch_off() {
		return set_task_on_switch_off;
	}

	public void setSet_task_on_switch_off(set_task set_task_on_switch_off) {
		this.set_task_on_switch_off = set_task_on_switch_off;
	}

	public static final boolean PROGRAM_MODE=true;
	public static final boolean NORMAL_MODE=false;
	
	public static final String input_type_switch="SWITCH";
	
	public bluetooth_switch(Activity calling_activity,ArrayList<BT_spp> BT_serial_devices,Switch switcher) 
	{
		super();
		
		this.calling_activity=calling_activity;

		if(BT_serial_devices==null)
		{
			this.BT_serial_devices=new ArrayList<BT_spp>();
		}
		else
		{
			this.BT_serial_devices=BT_serial_devices;
		}
		
		this.switcher = switcher;
		
		this.switcher.setOnCheckedChangeListener(this);
		this.switcher.setOnLongClickListener(this);
	}
		
	public bluetooth_switch(Activity calling_activity,ArrayList<BT_spp> BT_serial_devices,Switch switcher,bluetooth_switch_data switch_data) 
	{
		super();
		
		this.calling_activity=calling_activity;

		if(BT_serial_devices==null)
		{
			this.BT_serial_devices=new ArrayList<BT_spp>();
		}
		else
		{
			this.BT_serial_devices=BT_serial_devices;
		}
		
		this.switcher = switcher;
	
		this.switch_data=switch_data;
		
		this.switcher.setText(this.switch_data.getSwitch_text());
		
		this.switcher.setOnCheckedChangeListener(this);
		this.switcher.setOnLongClickListener(this);
	}

	public Activity getCalling_activity() 
	{
		return calling_activity;
	}

	public void setCalling_activity(Activity calling_activity) 
	{
		this.calling_activity = calling_activity;
	}

	public ArrayList<BT_spp> getBT_serial_devices() 
	{
		return BT_serial_devices;
	}

	public void setBT_serial_device(ArrayList<BT_spp> bT_serial_devices) 
	{
		BT_serial_devices = bT_serial_devices;
	}

	public void add_BT_serial_device(BT_spp bT_serial_device) 
	{
		this.BT_serial_devices.add(bT_serial_device);
	}
	
	public Switch getSwitch() 
	{
		return switcher;
	}

	public void setSwitch(Switch switcher) 
	{
		this.switcher = switcher;
	}

	public bluetooth_switch_data getSwitch_data() 
	{
		return switch_data;
	}

	public void setSwitch_data(bluetooth_switch_data switch_data) 
	{
		this.switch_data = switch_data;
		this.switcher.setText(this.switch_data.getSwitch_text());
	}

	public boolean isMode() 
	{
		return mode;
	}

	public void setMode(boolean mode) 
	{
		this.mode = mode;
	}
	
	public Intent getProgramming_activity_intent()
	{
		return programming_activity_intent;
	}

	public void setProgramming_activity_intent(Intent programming_activity_intent) 
	{
		this.programming_activity_intent = programming_activity_intent;
	}
	
	public void setProgramming_activity_intent(String input) 
	{
		this.programming_activity_intent = new Intent(input);
	}
	
	public int getProgramming_activity_request_code() 
	{
		return programming_activity_request_code;
	}

	public void setProgramming_activity_request_code(int programming_activity_request_code) 
	{
		this.programming_activity_request_code = programming_activity_request_code;
	}
	
	public void setProgramming_parameters(Intent programming_activity_intent,int programming_activity_request_code) 
	{
		this.programming_activity_intent = programming_activity_intent;
		this.programming_activity_request_code = programming_activity_request_code;
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		
		int device_count=BT_serial_devices.size();
		for(int count=0;count<device_count;count++)
		{
			this.BT_serial_devices.get(count).write(this.switch_data.getSwitch_code());
		}
		
		do_task(this.set_task_on_switch_change);
		
	    if(isChecked)
		{
	    	for(int count=0;count<device_count;count++)
			{
				this.BT_serial_devices.get(count).write(this.switch_data.getSwitch_on_code());
			}
	    	
	    	do_task(this.set_task_on_switch_on);
		}
		else
		{
			for(int count=0;count<device_count;count++)
			{
				this.BT_serial_devices.get(count).write(this.switch_data.getSwitch_off_code());
			}
			
			do_task(this.set_task_on_switch_off);
		}
	}
	
	@Override
	public boolean onLongClick(View v) 
	{
		if(this.isMode()==bluetooth_switch.PROGRAM_MODE)
		{
			this.programming_activity_intent.putExtra(programming_activity.input_type,bluetooth_switch.input_type_switch);
			this.programming_activity_intent.putExtra(bluetooth_switch_data.switch_text_extra_name,this.getSwitch_data().getSwitch_text());
			
			this.programming_activity_intent.putExtra(bluetooth_switch_data.switch_code_extra_name,this.getSwitch_data().getSwitch_code());
			this.programming_activity_intent.putExtra(bluetooth_switch_data.switch_on_code_extra_name,this.getSwitch_data().getSwitch_on_code());
			this.programming_activity_intent.putExtra(bluetooth_switch_data.switch_off_code_extra_name,this.getSwitch_data().getSwitch_off_code());
			
			this.programming_activity_intent.putParcelableArrayListExtra(connected_device_list_activity.selected_devices_list_extra_name,this.getBT_serial_devices());
			
			calling_activity.startActivityForResult(this.programming_activity_intent,this.programming_activity_request_code);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	void update_switch_onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(requestCode==this.programming_activity_request_code)
		{
			this.switch_data.setSwitch_text(data.getStringExtra(bluetooth_switch_data.switch_text_extra_name));
			update_switch_text();
			
			this.switch_data.setSwitch_code(data.getByteArrayExtra(bluetooth_switch_data.switch_code_extra_name));
			this.switch_data.setSwitch_on_code(data.getByteArrayExtra(bluetooth_switch_data.switch_on_code_extra_name));
			this.switch_data.setSwitch_off_code(data.getByteArrayExtra(bluetooth_switch_data.switch_off_code_extra_name));
			
			this.BT_serial_devices=data.getParcelableArrayListExtra(connected_device_list_activity.selected_devices_list_extra_name);
			
//			Log.e("What?","Switch_text: "+this.switch_data.getSwitch_text());
//			Log.e("What?","Switch_code: "+this.switch_data.getSwitch_code().toString());
//			Log.e("What?","Switch_on_code: "+this.switch_data.getSwitch_on_code().toString());
//			Log.e("What?","Switch_off_code: "+this.switch_data.getSwitch_off_code().toString());
		}
	}
	
	void update_switch_text()
	{
		this.switcher.setText(this.switch_data.getSwitch_text());
	}
	
	void do_task(set_task a)
	{
		if(a!=null)
		{
			a.perform_task();
		}
	}
	
	public set_task getSet_task_on_switch_change() {
		return set_task_on_switch_change;
	}

	public void setSet_task_on_switch_change(set_task set_task_on_switch_change) {
		this.set_task_on_switch_change = set_task_on_switch_change;
	}

	public set_task getSet_task_on_switch_on() {
		return set_task_on_switch_on;
	}
}
