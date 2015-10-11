package com.alex.blueremote;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import bluetooth_helper.BT_spp;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
//import android.os.Parcelable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class bluetooth_button<T> implements OnClickListener,OnTouchListener,OnLongClickListener
{
	ArrayList<BT_spp> BT_serial_devices;
	Button button;
	
	private boolean mode=NORMAL_MODE;
	
	bluetooth_button_data button_data;
	
	Intent programming_activity_intent;
	int programming_activity_request_code;
	T calling_activity_or_fragment;
	
	TimerTask button_task;
	Timer button_timer;
	
	set_task set_task_on_button_click=null; 
	set_task set_task_on_button_down=null; 
	set_task set_task_on_button_up=null; 
	
//	public static int button_repetition_period=200;
	public static int button_repetition_period;
	
	public static final boolean PROGRAM_MODE=true;
	public static final boolean NORMAL_MODE=false;
	
	public static final String input_type_button="BUTTON";
	
	public bluetooth_button()
	{
		
	}
	
	public bluetooth_button(T calling_activity_or_fragment,ArrayList<BT_spp> BT_serial_devices,Button button) 
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
		
		this.button = button;
		
		this.button.setOnTouchListener(this);
		this.button.setOnClickListener(this);
		this.button.setOnLongClickListener(this);
	}
		
	public bluetooth_button(T calling_activity_or_fragment,ArrayList<BT_spp> BT_serial_devices,Button button,bluetooth_button_data button_data) 
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
		
		this.button = button;
	
		this.button_data=button_data;
		
		this.button.setText(this.button_data.getButton_text());
		
		this.button.setOnTouchListener(this);
		this.button.setOnClickListener(this);
		this.button.setOnLongClickListener(this);
	}

	public T getCalling_activity_or_fragment() 
	{
		return calling_activity_or_fragment;
	}

	public void setCalling_activity_or_fragment(T calling_activity_or_fragment) 
	{
		this.calling_activity_or_fragment = calling_activity_or_fragment;
	}

	public ArrayList<BT_spp> getBT_serial_devices() 
	{
		return BT_serial_devices;
	}

	public void setBT_serial_devices(ArrayList<BT_spp> bT_serial_devices) 
	{
		this.BT_serial_devices = bT_serial_devices;
	}

	public void add_BT_serial_device(BT_spp bT_serial_device) 
	{
		this.BT_serial_devices.add(bT_serial_device);
	}
	
	public Button getButton() 
	{
		return button;
	}

	public void setButton(Button button) 
	{
		this.button = button;
	}

	public bluetooth_button_data getButton_data() 
	{
		return button_data;
	}

	public void setButton_data(bluetooth_button_data button_data) 
	{
		this.button_data = button_data;
		this.button.setText(this.button_data.getButton_text());
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
	
	public static int getButton_delay() 
	{
		return button_repetition_period;
	}

	public static void setButton_delay(int button_delay) 
	{
		bluetooth_button.button_repetition_period = button_delay;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) 
	{
		
		if(this.isMode()==bluetooth_button.PROGRAM_MODE)
		{
			return false;
		}
		else
		{
			if(button_data.isRespond_on_continuous_touch()==false)
			{
				return false;
			}
			else
			{
				switch(event.getAction())
				{
					case MotionEvent.ACTION_DOWN:
						v.setPressed(true);
						button_pressed_response(true);
						do_task(this.set_task_on_button_down);
						break;
							
					case MotionEvent.ACTION_UP:
						v.setPressed(false);
						button_pressed_response(false);
						
						int device_count=this.BT_serial_devices.size();
						for(int count=0;count<device_count;count++)
						{
							BT_serial_devices.get(count).write(button_data.getButton_on_up_code());
						}
						
						do_task(this.set_task_on_button_up);
						v.performClick();
						break;
				}
				
				return true;
			}
		}
	}

	void button_pressed_response(boolean start_or_stop)
	{
		if(start_or_stop==true)
		{
			button_task=new TimerTask(){
				
				@Override
				public void run() {
					
					int device_count=BT_serial_devices.size();
					for(int count=0;count<device_count;count++)
					{
						BT_serial_devices.get(count).write(button_data.getButton_on_down_code());
					}	
				}
			};
			
			button_timer = new Timer();
			button_timer.scheduleAtFixedRate(button_task,0,button_repetition_period);			
		}
		else
		{
			button_task.cancel();
			button_timer.purge();
			button_task=null;	//Timer Task not Reusable
			
			button_timer.cancel();
			button_timer=null;			
		}
	}
	
	@Override
	public void onClick(View v) 
	{	
		int device_count=this.BT_serial_devices.size();
		for(int count=0;count<device_count;count++)
		{
			BT_serial_devices.get(count).write(button_data.getButton_code());
		}
		
		do_task(this.set_task_on_button_click);
	}

	@Override
	public boolean onLongClick(View v) 
	{
		if(this.isMode()==bluetooth_button.PROGRAM_MODE)
		{
			this.programming_activity_intent.putExtra(programming_activity.input_type,bluetooth_button.input_type_button);
			this.programming_activity_intent.putExtra(bluetooth_button_data.button_text_extra_name,this.getButton_data().getButton_text());
			
			this.programming_activity_intent.putExtra(bluetooth_button_data.button_code_extra_name,this.getButton_data().getButton_code());
			this.programming_activity_intent.putExtra(bluetooth_button_data.button_on_down_code_extra_name,this.getButton_data().getButton_on_down_code());
			this.programming_activity_intent.putExtra(bluetooth_button_data.button_on_up_code_extra_name,this.getButton_data().getButton_on_up_code());
			
			this.programming_activity_intent.putExtra(bluetooth_button_data.respond_on_continuous_touch_extra_name,this.getButton_data().isRespond_on_continuous_touch());
			
			if(calling_activity_or_fragment instanceof Activity)
			{
				this.programming_activity_intent.putIntegerArrayListExtra(connected_device_list_activity.selected_devices_list_indices_extra_name,BT_spp.get_indices(
						((BlueRemote)((Activity) calling_activity_or_fragment).getApplicationContext())
						.getConnected_device_list(), this.getBT_serial_devices()));
				
				((Activity)calling_activity_or_fragment).startActivityForResult(this.programming_activity_intent,this.programming_activity_request_code);
			}
			else if(calling_activity_or_fragment instanceof Fragment)
			{
				this.programming_activity_intent.putIntegerArrayListExtra(connected_device_list_activity.selected_devices_list_indices_extra_name,BT_spp.get_indices(
						((BlueRemote)(((Fragment)calling_activity_or_fragment).getActivity()).getApplicationContext())
						.getConnected_device_list(), this.getBT_serial_devices())
						);
				
//				Log.e("","get_indices:"+BT_spp.get_indices(
//						((BlueRemote)(((Fragment)calling_activity_or_fragment).getActivity()).getApplicationContext())
//							.getConnected_device_list(), this.getBT_serial_devices())
//							);
				((Fragment)calling_activity_or_fragment).startActivityForResult(this.programming_activity_intent,this.programming_activity_request_code);
			}
			
			return true;
		}
		else
		{
			return false;
		}
	}
	
	void update_button_onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(requestCode==this.programming_activity_request_code)
		{
			this.button_data.setButton_text(data.getStringExtra(bluetooth_button_data.button_text_extra_name));
			update_button_text();
			
			this.button_data.setButton_code(data.getByteArrayExtra(bluetooth_button_data.button_code_extra_name));
			this.button_data.setButton_on_down_code(data.getByteArrayExtra(bluetooth_button_data.button_on_down_code_extra_name));
			this.button_data.setButton_on_up_code(data.getByteArrayExtra(bluetooth_button_data.button_on_up_code_extra_name));
			
			this.button_data.setRespond_on_continuous_touch(data.getBooleanExtra(bluetooth_button_data.respond_on_continuous_touch_extra_name,false));
			
//			this.BT_serial_devices=data.getParcelableArrayListExtra(connected_device_list_activity.selected_devices_list_indices_extra_name);
			
			if(calling_activity_or_fragment instanceof Activity)
			{
				BT_spp.update_list_based_on_indices(
						((BlueRemote)((Activity) calling_activity_or_fragment).getApplicationContext())
						.getConnected_device_list(), this.getBT_serial_devices()
						,data.getIntegerArrayListExtra(connected_device_list_activity.selected_devices_list_indices_extra_name));
			}
			else if(calling_activity_or_fragment instanceof Fragment)
			{
				BT_spp.update_list_based_on_indices(
						((BlueRemote)(((Fragment)calling_activity_or_fragment).getActivity()).getApplicationContext())
						.getConnected_device_list(), this.getBT_serial_devices()
						,data.getIntegerArrayListExtra(connected_device_list_activity.selected_devices_list_indices_extra_name));
			}
			
		}
	}
	
	void update_button_text()
	{
		this.button.setText(this.button_data.getButton_text());
	}
	
	void do_task(set_task a)
	{
		if(a!=null)
		{
			a.perform_task();
		}
	}
	
	public set_task getSet_task_on_button_click() {
		return set_task_on_button_click;
	}

	public void setSet_task_on_button_click(set_task set_task_on_button_click) {
		this.set_task_on_button_click = set_task_on_button_click;
	}

	public set_task getSet_task_on_button_down() {
		return set_task_on_button_down;
	}

	public void setSet_task_on_button_down(set_task set_task_on_button_down) {
		this.set_task_on_button_down = set_task_on_button_down;
	}

	public set_task getSet_task_on_button_up() {
		return set_task_on_button_up;
	}

	public void setSet_task_on_button_up(set_task set_task_on_button_up) {
		this.set_task_on_button_up = set_task_on_button_up;
	}
}
