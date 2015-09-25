package com.alex.blueremote;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class bluetooth_button implements OnClickListener,OnTouchListener,OnLongClickListener
{
	BT_spp BT_serial_device;
	Button button;
	
	boolean respond_on_continuous_touch=false;
	private boolean mode=false;
	
	bluetooth_button_data button_data;
	
	Intent programming_activity_intent;
	int programming_activity_request_code;
	Activity calling_activity;

	final boolean PROGRAM_MODE=true;
	final boolean NORMAL_MODE=true;
	
	public bluetooth_button(Activity calling_activity,BT_spp BT_serial_device,Button button) 
	{
		super();
		
		this.calling_activity=calling_activity;
		this.BT_serial_device=BT_serial_device;
		this.button = button;
		
		this.button.setOnTouchListener(this);
		this.button.setOnClickListener(this);
		this.button.setOnLongClickListener(this);
	}
	
	public bluetooth_button(Activity calling_activity,BT_spp BT_serial_device,Button button, boolean respond_on_touch) 
	{
		super();
		
		this.calling_activity=calling_activity;
		this.BT_serial_device=BT_serial_device;
		this.button = button;
		
		this.respond_on_continuous_touch = respond_on_touch;
		
		this.button.setOnTouchListener(this);
		this.button.setOnClickListener(this);
		this.button.setOnLongClickListener(this);
	}
	
	public bluetooth_button(Activity calling_activity,BT_spp BT_serial_device,Button button, boolean respond_on_touch,bluetooth_button_data button_data) 
	{
		super();
		
		this.calling_activity=calling_activity;
		this.BT_serial_device=BT_serial_device;
		this.button = button;
	
		this.respond_on_continuous_touch = respond_on_touch;
		
		this.button_data=button_data;
		
		this.button.setText(this.button_data.getButton_text());
		
		this.button.setOnTouchListener(this);
		this.button.setOnClickListener(this);
		this.button.setOnLongClickListener(this);
	}

	public Activity getCalling_activity() 
	{
		return calling_activity;
	}

	public void setCalling_activity(Activity calling_activity) 
	{
		this.calling_activity = calling_activity;
	}

	public BT_spp getBT_serial_device() 
	{
		return BT_serial_device;
	}

	public void setBT_serial_device(BT_spp bT_serial_device) 
	{
		BT_serial_device = bT_serial_device;
	}

	public Button getButton() 
	{
		return button;
	}

	public void setButton(Button button) 
	{
		this.button = button;
	}

	public boolean isRespond_on_continuous_touch() 
	{
		return respond_on_continuous_touch;
	}

	public void setRespond_on_continuous_touch(boolean respond_on_touch) 
	{
		this.respond_on_continuous_touch = respond_on_touch;
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
	
	public int getProgramming_activity_request_code() {
		return programming_activity_request_code;
	}

	public void setProgramming_activity_request_code(
			int programming_activity_request_code) {
		this.programming_activity_request_code = programming_activity_request_code;
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		if(this.isMode()==this.PROGRAM_MODE)
		{
			return false;
		}
		else
		{
			if(respond_on_continuous_touch==false)
			{
				return false;
			}
			else
			{
				Log.e("Where?", "onTouch2");
				
				switch(event.getAction())
				{
					case MotionEvent.ACTION_DOWN:
						v.setPressed(true);
						Log.e("Where?", "onTouchdown");
						break;
							
					case MotionEvent.ACTION_UP:
						v.setPressed(false);
						Log.e("Where?", "onTouchup");
//						v.performClick();
						break;
							
//					default:				
				}
				
				return true;
			}
		}
	}

	@Override
	public void onClick(View v) {
		
		Log.e("Where?", "onClick1");
//		Log.e("What?", "BT_serial_device: "+BT_serial_device);
		BT_serial_device.write(button_data.getButton_code());
	}

	@Override
	public boolean onLongClick(View v) 
	{
		if(this.isMode()==this.PROGRAM_MODE)
		{
			calling_activity.startActivityForResult(this.programming_activity_intent,this.programming_activity_request_code);
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
			this.button_data.setButton_code(data.getByteArrayExtra(bluetooth_button_data.button_code_extra_name));
			this.button_data.setButton_on_up_code(data.getByteArrayExtra(bluetooth_button_data.button_on_up_code_extra_name));
			this.button_data.setButton_on_down_code(data.getByteArrayExtra(bluetooth_button_data.button_on_down_code_extra_name));
			
			this.setMode(NORMAL_MODE);
		}
	}
	
}
