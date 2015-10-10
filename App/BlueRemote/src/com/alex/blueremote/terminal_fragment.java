package com.alex.blueremote;

import java.nio.charset.Charset;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

public class terminal_fragment extends Fragment {
 
	Activity container_activity;
	
	ScrollView sv;
	TextView tv_1;
	EditText et_1;
	
	Button send,devices,clear;
	ArrayList<BT_spp> send_device_list;
	
	Handler terminal_handler;
	
	final int hexboard_activity_request_code=4;
	Runnable hex_board_runnable;
	
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.terminal_fragment_layout,container,false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return v;
    }
    
    @Override
	public void onActivityCreated (Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		Log.e("","Fragment:onActivityCreated");
		this.container_activity=(MainActivity) getActivity();
		this.container_activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        
		sv=(ScrollView)this.getView().findViewById(R.id.scrollView1_terminal);
		tv_1=(TextView)this.getView().findViewById(R.id.textView1_terminal);
		et_1=(EditText)this.getView().findViewById(R.id.editText1_terminal);
		devices=(Button)this.getView().findViewById(R.id.button1_terminal);
  		send=(Button)this.getView().findViewById(R.id.button2_terminal);
  		clear=(Button)this.getView().findViewById(R.id.button3_terminal);
  		
  		tv_1.setText("");
  		
  		terminal_handler=new Handler();
  		
  		send_device_list=((MainActivity)this.container_activity).terminal_device_list;
  		
  		devices.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent connected_device_list= new Intent(container_activity, connected_device_list_activity.class);
				connected_device_list.putIntegerArrayListExtra(connected_device_list_activity.selected_devices_list_indices_extra_name,
						BT_spp.get_indices(
								((MainActivity)container_activity).global_variables_object.connected_device_list
								,send_device_list));
				
				startActivityForResult(connected_device_list, connected_device_list_activity.connected_device_list_request_code);
			}
  			
  		});
  		
	    send.setOnClickListener(new OnClickListener() {
	    	
	    	@Override
			public void onClick(View v) {
	    		
	    		int device_count=send_device_list.size();
				for(int count=0;count<device_count;count++)
				{
					send_device_list.get(count).write(et_1.getText().toString().getBytes(Charset.forName("ISO-8859-1")));
				}
	    		
	    	}
		});
	    
	    clear.setOnClickListener(new OnClickListener() {
	    	
	    	@Override
			public void onClick(View v) {
	    		
	    		tv_1.setText("");
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
						terminal_handler.postDelayed(hex_board_runnable,HexBoard.hex_board_call_time_out);
						break;
							
					case MotionEvent.ACTION_UP:
						terminal_handler.removeCallbacks(hex_board_runnable);
						v.performClick();
						break;					
				}
				
				return false;
			}	    	
	    });
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
		else if(requestCode==connected_device_list_activity.connected_device_list_request_code)
		{
			BT_spp.update_list_based_on_indices(
					((MainActivity)container_activity).global_variables_object.connected_device_list
					,send_device_list
					,data.getIntegerArrayListExtra(connected_device_list_activity.selected_devices_list_indices_extra_name));
		}
		else
		{
			Log.e("","InFragment2 requestCode:"+requestCode+"\n data:"+data);
		}
    }  

}
