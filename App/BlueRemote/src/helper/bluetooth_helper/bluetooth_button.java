package helper.bluetooth_helper;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import helper.call_this_method_interface;
import helper.bluetooth_helper.BT_spp;
import helper.bluetooth_helper.bluetooth_button_data;
import helper.bluetooth_helper.bluetooth_view;
import android.content.Intent;
//import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class bluetooth_button<T> extends bluetooth_view<T> implements OnClickListener,OnTouchListener
{
	bluetooth_button_data button_data;
	
	TimerTask button_task;
	Timer button_timer;
	
	call_this_method_interface on_button_click=null; 
	call_this_method_interface on_button_down=null; 
	call_this_method_interface on_button_up=null; 
	
	public static int button_repetition_period;
	public static final String input_type_button="BUTTON";
	
	public bluetooth_button()
	{
		
	}
	
	public bluetooth_button(T get_calling_activity_or_fragment,ArrayList<BT_spp> get_BT_serial_devices,Button button) 
	{
		super(get_calling_activity_or_fragment,get_BT_serial_devices,(View)button);
		
		initialise_interfaces();
		
		this.get_view().setOnTouchListener(this);
		this.get_view().setOnClickListener(this);
	}
		
	public bluetooth_button(T get_calling_activity_or_fragment,ArrayList<BT_spp> get_BT_serial_devices,Button button
			,bluetooth_button_data button_data) 
	{
		super(get_calling_activity_or_fragment,get_BT_serial_devices,(View)button);
		
		this.button_data=button_data;
		
		initialise_interfaces();
		
		this.get_view().setOnTouchListener(this);
		this.get_view().setOnClickListener(this);
	}
	
	void initialise_interfaces()
	{
		this.set_before_starting_programming_activity(new call_this_method_interface() {

			@Override
			public void call_this_method(Object object) {
				
				get_programming_activity_intent().putExtra(bluetooth_button_data.button_text_extra_name,get_button_data().get_button_text());
				
				get_programming_activity_intent().putExtra(bluetooth_button_data.button_code_extra_name,get_button_data().get_button_code());
				get_programming_activity_intent().putExtra(bluetooth_button_data.button_on_down_code_extra_name,get_button_data().get_button_on_down_code());
				get_programming_activity_intent().putExtra(bluetooth_button_data.button_on_up_code_extra_name,get_button_data().get_button_on_up_code());
				
				get_programming_activity_intent().putExtra(bluetooth_button_data.respond_on_continuous_touch_extra_name,get_button_data().is_respond_on_continuous_touch());
									
			}
			});
		
		this.set_after_finishing_programming_activity(new call_this_method_interface() {

			@Override
			public void call_this_method(Object object) 
			{
				Intent data=(Intent)object;
				
				button_data.set_button_text(data.getStringExtra(bluetooth_button_data.button_text_extra_name));
				((Button)get_view()).setText(get_button_data().get_button_text());
				
				button_data.set_button_code(data.getByteArrayExtra(bluetooth_button_data.button_code_extra_name));
				button_data.set_button_on_down_code(data.getByteArrayExtra(bluetooth_button_data.button_on_down_code_extra_name));
				button_data.set_button_on_up_code(data.getByteArrayExtra(bluetooth_button_data.button_on_up_code_extra_name));
				
				button_data.set_respond_on_continuous_touch(data.getBooleanExtra(bluetooth_button_data.respond_on_continuous_touch_extra_name,false));
				
			}			
		});
		
	}
	
	public bluetooth_button_data get_button_data() 
	{
		return button_data;
	}

	public void set_button_data(bluetooth_button_data button_data) 
	{
		this.button_data = button_data;
		
		((Button)this.get_view()).setText(this.get_button_data().get_button_text());
	}

	public static int get_button_repetition_period() 
	{
		return button_repetition_period;
	}

	public static void set_button_repetition_period(int button_repetition_period) 
	{
		bluetooth_button.button_repetition_period = button_repetition_period;
	}
	
	
	public call_this_method_interface get_on_button_click() {
		return on_button_click;
	}

	public void set_on_button_click(call_this_method_interface on_button_click) {
		this.on_button_click = on_button_click;
	}

	public call_this_method_interface get_on_button_down() {
		return on_button_down;
	}

	public void set_on_button_down(call_this_method_interface on_button_down) {
		this.on_button_down = on_button_down;
	}

	public call_this_method_interface get_on_button_up() {
		return on_button_up;
	}

	public void set_on_button_up(call_this_method_interface on_button_up) {
		this.on_button_up = on_button_up;
	}
		
	@Override
	public boolean onTouch(View v, MotionEvent event) 
	{
		
		if(this.is_mode()==bluetooth_button.PROGRAM_MODE)
		{
			return false;
		}
		else
		{
			if(button_data.is_respond_on_continuous_touch()==false)
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
						
						if(on_button_down!=null)
						{
							on_button_down.call_this_method(null);
						}
						
						break;
					
					case MotionEvent.ACTION_CANCEL:
					case MotionEvent.ACTION_UP:
						v.setPressed(false);
						button_pressed_response(false);
						
						int device_count=this.get_BT_serial_devices().size();
						for(int count=0;count<device_count;count++)
						{
							get_BT_serial_devices().get(count).write(button_data.get_button_on_up_code());
						}
						
						if(on_button_up!=null)
						{
							on_button_up.call_this_method(null);
						}
						
						v.performClick();
						break;
						
						default:
//							Log.e("", ""+event);
				}
				
				return true;
			}
		}
	}

	public void button_pressed_response(boolean start_or_stop)
	{
		if(start_or_stop==true)
		{
			button_task=new TimerTask(){
				
				@Override
				public void run() {
					
					int device_count=get_BT_serial_devices().size();
					for(int count=0;count<device_count;count++)
					{
						get_BT_serial_devices().get(count).write(button_data.get_button_on_down_code());
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
		int device_count=this.get_BT_serial_devices().size();
		for(int count=0;count<device_count;count++)
		{
			get_BT_serial_devices().get(count).write(button_data.get_button_code());
		}
		
		if(on_button_click!=null)
		{
			on_button_click.call_this_method(null);
		}
	}
	
}
