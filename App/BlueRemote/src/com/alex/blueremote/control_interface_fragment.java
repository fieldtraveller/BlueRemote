package com.alex.blueremote;

import java.nio.charset.Charset;
import java.util.ArrayList;

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
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

public class control_interface_fragment extends Fragment {
 
    Activity container_activity;
	
	EditText et_1;
	TextView tv_1;
	
	Button send;
	bluetooth_button<Fragment> buttons[]=new bluetooth_button[5]; 
	bluetooth_switch<Fragment> switchs[]=new bluetooth_switch[2]; 
	
	ArrayList<BT_spp> send_device_list;
	
//	@SuppressWarnings("unchecked")
//	bluetooth_button<Fragment> buttons[]=(bluetooth_button<Fragment>[]) Array.newInstance((new bluetooth_button<Fragment>()).getClass().getComponentType(),5); 
//	@SuppressWarnings("unchecked")
//	bluetooth_switch<Fragment> switchs[]=(bluetooth_switch<Fragment>[]) Array.newInstance((new bluetooth_switch<Fragment>()).getClass().getComponentType(),2); 
	
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
        
		//channel_up
  		buttons[0]=new bluetooth_button<Fragment>(this, null, (Button)this.getView().findViewById(R.id.button1_cif),((MainActivity)this.container_activity).button_data[0]);
  		//channel_down
  		buttons[1]=new bluetooth_button<Fragment>(this, null, (Button)this.getView().findViewById(R.id.button5_cif),((MainActivity)this.container_activity).button_data[1]);
  		//volume_up
  		buttons[2]=new bluetooth_button<Fragment>(this, null, (Button)this.getView().findViewById(R.id.button4_cif),((MainActivity)this.container_activity).button_data[2]);
  		//volume_down
  		buttons[3]=new bluetooth_button<Fragment>(this, null, (Button)this.getView().findViewById(R.id.button2_cif),((MainActivity)this.container_activity).button_data[3]);
  		//select
  		buttons[4]=new bluetooth_button<Fragment>(this, null, (Button)this.getView().findViewById(R.id.button3_cif),((MainActivity)this.container_activity).button_data[4]);
  		
  		//power
  		switchs[0]=new bluetooth_switch<Fragment>(this, null, (Switch)this.getView().findViewById(R.id.switch1_cif),((MainActivity)this.container_activity).switch_data[0]);
  		//mute
  		switchs[1]=new bluetooth_switch<Fragment>(this, null, (Switch)this.getView().findViewById(R.id.switch2_cif),((MainActivity)this.container_activity).switch_data[1]);
  															 
  		Intent programming_activity_intent = new Intent(container_activity,programming_activity.class);
//  		Intent programming_activity_intent = new Intent(".programming_activity");
  		
  		for(int count=0;count<buttons.length;count++)
  		{
  			buttons[count].setProgramming_parameters(programming_activity_intent,100+count);
  			
  			((MainActivity)this.container_activity)
  				.global_variables_object.list_of_devices_assigned_to_components
  					.add(buttons[count].BT_serial_devices);
  		}
  		
  		for(int count=0;count<switchs.length;count++)
  		{
  			switchs[count].setProgramming_parameters(programming_activity_intent,200+count);
  			
  			((MainActivity)this.container_activity)
				.global_variables_object.list_of_devices_assigned_to_components
					.add(switchs[count].BT_serial_devices);

  		}

  		send=(Button)this.getView().findViewById(R.id.button6_cif);
  		et_1=(EditText)this.getView().findViewById(R.id.editText1_cif);
  		tv_1=(TextView)this.getView().findViewById(R.id.textView1_cif);
  		
  		tv_1.setVisibility(View.GONE);
  		
  		hex_board_handler=new Handler();
  		
  		send_device_list=((MainActivity)this.container_activity).terminal_device_list;
  		
  		switchs[0].setSet_task_on_switch_off(new set_task(){

			@Override
			public void perform_task() 
			{
				switchs[1].switcher.setChecked(false);
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
	    		
//	    		BT_serial_device
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
			for(int count=0;count<buttons.length;count++)
			{
				buttons[count].update_button_onActivityResult(requestCode,resultCode,data);
			}
			
			for(int count=0;count<switchs.length;count++)
			{
				switchs[count].update_switch_onActivityResult(requestCode,resultCode,data);
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
