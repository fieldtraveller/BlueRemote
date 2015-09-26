package com.alex.blueremote;

import java.util.Timer;
import java.util.TimerTask;

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
	
	private boolean mode=false;
	
	bluetooth_button_data button_data;
	
	Intent programming_activity_intent;
	int programming_activity_request_code;
	Activity calling_activity;
	
	TimerTask button_task;
	Timer button_timer;
	final int button_delay=200;

	public static final boolean PROGRAM_MODE=true;
	public static final boolean NORMAL_MODE=false;
	
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
		
	public bluetooth_button(Activity calling_activity,BT_spp BT_serial_device,Button button,bluetooth_button_data button_data) 
	{
		super();
		
		this.calling_activity=calling_activity;
		this.BT_serial_device=BT_serial_device;
		this.button = button;
	
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
						Log.e("Where?", "onTouchdown");
						break;
							
					case MotionEvent.ACTION_UP:
						v.setPressed(false);
						button_pressed_response(false);
						BT_serial_device.write(button_data.getButton_on_up_code());
						Log.e("Where?", "onTouchup");
						v.performClick();
						break;							
				}
				
				return true;
			}
		}
	}

	private void button_pressed_response(boolean start_or_stop)
	{
		if(start_or_stop==true)
		{
			button_task=new TimerTask(){
				
				@Override
				public void run() {
					
					BT_serial_device.write(button_data.getButton_on_down_code());
				}
			};
			
			button_timer = new Timer();
			button_timer.scheduleAtFixedRate(button_task,0,button_delay);			
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
		Log.e("Where?", "onClick1");
		BT_serial_device.write(button_data.getButton_code());
	}

	@Override
	public boolean onLongClick(View v) 
	{
		if(this.isMode()==bluetooth_button.PROGRAM_MODE)
		{
			this.programming_activity_intent.putExtra(bluetooth_button_data.button_text_extra_name,this.getButton_data().getButton_text());
			
			this.programming_activity_intent.putExtra(bluetooth_button_data.button_code_extra_name,this.getButton_data().getButton_code());
			this.programming_activity_intent.putExtra(bluetooth_button_data.button_on_down_code_extra_name,this.getButton_data().getButton_on_down_code());
			this.programming_activity_intent.putExtra(bluetooth_button_data.button_on_up_code_extra_name,this.getButton_data().getButton_on_up_code());
			
			this.programming_activity_intent.putExtra(bluetooth_button_data.respond_on_continuous_touch_extra_name,this.getButton_data().isRespond_on_continuous_touch());
			
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
			Log.e("Where?", "update_button_onActivityResult");
			this.button_data.setButton_text(data.getStringExtra(bluetooth_button_data.button_text_extra_name));
			update_button_text();
			
			this.button_data.setButton_code(data.getByteArrayExtra(bluetooth_button_data.button_code_extra_name));
			this.button_data.setButton_on_down_code(data.getByteArrayExtra(bluetooth_button_data.button_on_down_code_extra_name));
			this.button_data.setButton_on_up_code(data.getByteArrayExtra(bluetooth_button_data.button_on_up_code_extra_name));
			
			this.button_data.setRespond_on_continuous_touch(data.getBooleanExtra(bluetooth_button_data.respond_on_continuous_touch_extra_name,false));
			
//			Log.e("What?","Button_text: "+this.button_data.getButton_text());
//			Log.e("What?","Button_code: "+this.button_data.getButton_code().toString());
//			Log.e("What?","Button_on_down_code: "+this.button_data.getButton_on_down_code().toString());
//			Log.e("What?","Button_on_up_code: "+this.button_data.getButton_on_up_code().toString());
//			Log.e("What?","Respond_on_continuous_touch: "+this.button_data.isRespond_on_continuous_touch());
		}
	}
	
	void update_button_text()
	{
		this.button.setText(this.button_data.getButton_text());
	}
}
