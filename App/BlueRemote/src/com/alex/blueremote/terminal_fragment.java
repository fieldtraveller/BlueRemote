package com.alex.blueremote;

import java.nio.charset.Charset;
import java.util.ArrayList;

import helper.bluetooth_helper.BT_spp;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class terminal_fragment extends Fragment {
 
	Activity container_activity;
	
	ScrollView sv;
	TextView tv_1;
	Button clear;
	
	int number_of_active_terminals;
	private int number_of_active_terminals_p=0;
	final static int number_of_terminals=5;
		
	ArrayList<LinearLayout> ll=new ArrayList<LinearLayout>();
	ArrayList<EditText> et=new ArrayList<EditText>();
	ArrayList<Button> send=new ArrayList<Button>();
	ArrayList<Button> devices=new ArrayList<Button>();
	
	ArrayList<ArrayList<BT_spp>> terminal_device_list;
	
	static boolean clear_input_on_send;
	static int colors[];	//0->background,1->default_incoming_foreground,2->default_outgoing_foreground
	
	Handler terminal_handler;
	Intent launch_hex_board;
	
	final int hexboard_activity_request_code=4;
	Runnable hex_board_runnable;
	
	public static String terminal_background_color_extra_name="TERMINAL_BACKGROUND_COLOR";
	public static String terminal_default_incoming_foreground_color_extra_name="TERMINAL_DEFAULT_INCOMING_FOREGROUND_COLOR";
	public static String terminal_default_outgoing_foreground_color_extra_name="TERMINAL_DEFAULT_OUTGOING_FOREGROUND_COLOR";
	
	public static String terminal_number_of_active_terminals_extra_name="TERMINAL_NUMBER_OF_ACTIVE_TERMINALS";
	public static String terminal_clear_input_on_send_extra_name="TERMINAL_CLEAR_INPUT_ON_SEND";
	
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
		
		this.container_activity=(MainActivity) getActivity();
		this.container_activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        
		sv=(ScrollView)this.getView().findViewById(R.id.scrollView1_terminal);
		tv_1=(TextView)this.getView().findViewById(R.id.textView1_terminal);
		
		ll.add((LinearLayout)this.getView().findViewById(R.id.linearLayout_1_terminal));
		ll.add((LinearLayout)this.getView().findViewById(R.id.linearLayout_2_terminal));
		ll.add((LinearLayout)this.getView().findViewById(R.id.linearLayout_3_terminal));
		ll.add((LinearLayout)this.getView().findViewById(R.id.linearLayout_4_terminal));
		ll.add((LinearLayout)this.getView().findViewById(R.id.linearLayout_5_terminal));
		
		et.add((EditText)this.getView().findViewById(R.id.editText_1_terminal));
		et.add((EditText)this.getView().findViewById(R.id.editText_2_terminal));
		et.add((EditText)this.getView().findViewById(R.id.editText_3_terminal));
		et.add((EditText)this.getView().findViewById(R.id.editText_4_terminal));
		et.add((EditText)this.getView().findViewById(R.id.editText_5_terminal));
		
		devices.add((Button)this.getView().findViewById(R.id.button_devices_1_terminal));
		devices.add((Button)this.getView().findViewById(R.id.button_devices_2_terminal));
		devices.add((Button)this.getView().findViewById(R.id.button_devices_3_terminal));
		devices.add((Button)this.getView().findViewById(R.id.button_devices_4_terminal));
		devices.add((Button)this.getView().findViewById(R.id.button_devices_5_terminal));
  		
		send.add((Button)this.getView().findViewById(R.id.button_send_1_terminal));
		send.add((Button)this.getView().findViewById(R.id.button_send_2_terminal));
		send.add((Button)this.getView().findViewById(R.id.button_send_3_terminal));
		send.add((Button)this.getView().findViewById(R.id.button_send_4_terminal));
		send.add((Button)this.getView().findViewById(R.id.button_send_5_terminal));
		
		clear=(Button)this.getView().findViewById(R.id.button3_terminal);
  		
		launch_hex_board= new Intent(container_activity,HexBoard.class);
		
		tv_1.setText("");
  		
  		set_background_color();
  		
  		terminal_handler=new Handler();
  		terminal_device_list=new ArrayList<ArrayList<BT_spp>>();
  		
  		OnClickListener devices_listener= new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				for(int count_0=0;count_0<number_of_terminals;count_0++)
	    		{
	    			if(v.getId()==devices.get(count_0).getId())
	    			{
	    				Intent connected_device_list= new Intent(container_activity, connected_device_list_activity.class);
	    				connected_device_list.putIntegerArrayListExtra(connected_device_list_activity.selected_devices_list_indices_extra_name,
	    						BT_spp.get_indices(
	    								BlueRemote.connected_device_list
	    								,terminal_device_list.get(count_0)));
	    				
	    				startActivityForResult(connected_device_list, count_0+700);
	    			}
	    		}
			}
  		};
  		
  		OnClickListener send_listener =new OnClickListener() {
	    	
	    	@Override
			public void onClick(View v) {
	    		
	    		for(int count_0=0;count_0<number_of_terminals;count_0++)
	    		{
	    			if(v.getId()==send.get(count_0).getId())
	    			{
	    				int device_count=terminal_device_list.get(count_0).size();
	    				if(device_count==0)
	    				{
	    					Toast.makeText(getActivity(), "No Device Assigned", Toast.LENGTH_SHORT).show();
	    				}
	    				else
	    				{
	    					for(int count_1=0;count_1<device_count;count_1++)
							{
								terminal_device_list.get(count_0).get(count_1)
								.write(et.get(count_0).getText().toString().getBytes(Charset.forName("ISO-8859-1")));
							}
	    				}
						
						if(clear_input_on_send==true)
						{
							et.get(count_0).setText("");
						}
	    			}
	    		}
	    	}
		};
		
	    hex_board_runnable=new Runnable(){

			@Override
			public void run() {
				startActivityForResult(launch_hex_board, hexboard_activity_request_code);
			}			
		};
		
		OnTouchListener et_listener=new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				for(int count_0=0;count_0<number_of_terminals;count_0++)
	    		{
					if(v.getId()==et.get(count_0).getId())
					{
						switch(event.getAction())
						{
							case MotionEvent.ACTION_DOWN:
								launch_hex_board.removeExtra(HexBoard.initial_text);
								launch_hex_board.putExtra(HexBoard.initial_text, et.get(count_0).getText().toString());
								launch_hex_board.removeExtra(HexBoard.identification_number);
								launch_hex_board.putExtra(HexBoard.identification_number, count_0);
								
								terminal_handler.postDelayed(hex_board_runnable,HexBoard.hex_board_call_time_out);
								break;
									
							case MotionEvent.ACTION_UP:
								terminal_handler.removeCallbacks(hex_board_runnable);
								v.performClick();
								break;					
						}
					}
	    		}
				
				return false;
			}	    	
	    };
	    
  		for(int count=0;count<number_of_terminals;count++)
  		{
  			terminal_device_list.add(new ArrayList<BT_spp>());
  			
  			devices.get(count).setOnClickListener(devices_listener);
  	  		send.get(count).setOnClickListener(send_listener);
  	  		et.get(count).setOnTouchListener(et_listener);
  		}
  		
  		update_terminal_view();
		
	    clear.setOnClickListener(new OnClickListener() {
	    	
	    	@Override
			public void onClick(View v) {
	    		
	    		tv_1.setText("");
	    	}
		});
	    
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)  
    {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode==hexboard_activity_request_code)
		{
			int temp=data.getIntExtra(HexBoard.identification_number,-1);
			for(int count=0;count<number_of_terminals;count++)
			{
				if(count==temp)
				{
					EditText temp_et=et.get(count); 
					temp_et.setText(data.getStringExtra(HexBoard.stringed_data));
					temp_et.setSelection(temp_et.getText().length());
				}
			}
		}
		else
		{
			for(int count=0;count<number_of_terminals;count++)
			{
				if((count+700)==requestCode)
				{
					BT_spp.update_list_based_on_indices(
							BlueRemote.connected_device_list
							,terminal_device_list.get(count)
							,data.getIntegerArrayListExtra(connected_device_list_activity.selected_devices_list_indices_extra_name));
				}
			}
		}
    }  

	public int get_number_of_active_terminals() {
		return number_of_active_terminals;
	}

	public void set_number_of_active_terminals(int number_of_active_terminals) {
		this.number_of_active_terminals = number_of_active_terminals;
	}

	public static boolean is_clear_input_on_send() {
		return clear_input_on_send;
	}

	public static void set_clear_input_on_send(boolean clear_input_on_send) {
		terminal_fragment.clear_input_on_send = clear_input_on_send;
	}

	public static int[] get_colors() {
		return colors;
	}

	public static void set_colors(int[] colors) {
		terminal_fragment.colors = colors;
	}
	
	public void set_background_color() {
		this.sv.setBackgroundColor(colors[0]);
	}
	
	public static int get_default_incoming_foreground_color() {
		return colors[1];
	}
	
	public static int get_default_outgoing_foreground_color() {
		return colors[2];
	}
	
	public void update_terminal_view()
	{
		for(int count_0=0;count_0<number_of_terminals;count_0++)
		{
			if(count_0>=this.number_of_active_terminals)
			{
				ll.get(count_0).setVisibility(View.GONE);
			}
			else
			{
				ll.get(count_0).setVisibility(View.VISIBLE);
			}
			
			if(this.number_of_active_terminals_p<this.number_of_active_terminals)
			{
				if((count_0>=this.number_of_active_terminals_p)&&(count_0<this.number_of_active_terminals))
				{
					BlueRemote.list_of_devices_assigned_to_components
		  			.add(terminal_device_list.get(count_0));	
				}
			}
			else if(this.number_of_active_terminals_p>this.number_of_active_terminals)
			{
				if((count_0>=this.number_of_active_terminals)&&(count_0<this.number_of_active_terminals_p))
				{
					BlueRemote.list_of_devices_assigned_to_components
		  			.remove(terminal_device_list.get(count_0));
					
					int number_of_iterations=terminal_device_list.get(count_0).size();
					for(int count_1=0;count_1<number_of_iterations;count_1++)
					{
						terminal_device_list.get(count_0).get(count_1).not_used_by_a_component();
						terminal_device_list.get(count_0).remove(count_1);
					}
				}
			}
		}
		
		this.number_of_active_terminals_p=this.get_number_of_active_terminals();
	}
}
