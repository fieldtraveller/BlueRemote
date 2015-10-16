package com.alex.blueremote;

import helper.bluetooth_helper.BT_spp;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class preferences_activity extends AppCompatActivity {
	
	RadioGroup rg;
	EditText et[]=new EditText[3];
	Button ok,color_button_background,color_button_incoming_foreground,color_button_outgoing_foreground;
	CheckBox cb;
	Spinner sp_1,sp_2;
	
	ArrayAdapter<CharSequence> sp_1_adapter;
	
	ArrayList<BT_spp> connected_device_list;
	ArrayList<String> sp_2_options;
	ArrayAdapter<String> sp_2_adapter;
	int selected_device=0;
	
	int colors[]=new int[3];
	
	Intent close_intent=new Intent();
	
	public static String device_assignment_extra_name="DEVICE_ASSIGNMENT";
	public static String button_repetition_period_extra_name="BUTTON_REPETITION_PERIOD";
	public static String hex_board_call_time_out_factor_extra_name="HEXBOARD_CALL_TIME_OUT_FACTOR";
	public static String hex_board_backspace_repetition_period_extra_name="HEXBOARD_BACKSPACE_REPETITION_PERIOD";
	
	public static int preferences_activity_request_code=4896;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences_activity_layout);
		
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		
		rg=(RadioGroup)findViewById(R.id.radioGroup_preferences);
		et[0]=(EditText)findViewById(R.id.editText1_preferences);
		et[1]=(EditText)findViewById(R.id.editText2_preferences);
		et[2]=(EditText)findViewById(R.id.editText3_preferences);
		ok=(Button)findViewById(R.id.button1_preferences);
		color_button_background=(Button)findViewById(R.id.button2_preferences);
		color_button_incoming_foreground=(Button)findViewById(R.id.button3_preferences);
		color_button_outgoing_foreground=(Button)findViewById(R.id.button4_preferences);
		cb=(CheckBox)findViewById(R.id.checkBox1_preferences);
		sp_1=(Spinner)findViewById(R.id.spinner1_preferences);
		sp_2=(Spinner)findViewById(R.id.spinner2_preferences);
		
		sp_1_adapter=ArrayAdapter.createFromResource(this,R.array.spinner_items,R.layout.spinner_text_view);
		sp_1_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_1.setAdapter(sp_1_adapter);
		
		connected_device_list=BlueRemote.get_connected_device_list();
		sp_2_options=new ArrayList<String>();
		
		Thread getting_sp_2_options_thread=new Thread(){
			
			public void run()
			{
				sp_2_options.add("Default");
				
				for(int count=0;count<connected_device_list.size();count++)
				{
					sp_2_options.add(connected_device_list.get(count).toString());
				}
			}
		};
		
		getting_sp_2_options_thread.start();
		
		byte temp=getIntent().getByteExtra(device_assignment_extra_name,BlueRemote.do_not_assign);
		switch(temp)
		{
		case BlueRemote.assign_all_devices:
			rg.check(R.id.radioButton1_preferences);
			break;
			
		case BlueRemote.assign_latest_device_if_list_is_empty:
			rg.check(R.id.radioButton2_preferences);
			break;	
		
		case BlueRemote.do_not_assign:
			rg.check(R.id.radioButton3_preferences);
			break;
		}
		
		et[0].setText(""+getIntent().getIntExtra(button_repetition_period_extra_name, 200));
		et[1].setText(""+getIntent().getIntExtra(hex_board_call_time_out_factor_extra_name, 2));
		et[2].setText(""+getIntent().getIntExtra(hex_board_backspace_repetition_period_extra_name, 200));
		
		et[0].setSelection(et[0].getText().toString().length());
		et[1].setSelection(et[1].getText().toString().length());
		et[2].setSelection(et[2].getText().toString().length());
        
		colors[0]=getIntent().getIntExtra(terminal_fragment.terminal_background_color_extra_name, 0);
		colors[1]=getIntent().getIntExtra(terminal_fragment.terminal_default_incoming_foreground_color_extra_name, 0);
		colors[2]=getIntent().getIntExtra(terminal_fragment.terminal_default_outgoing_foreground_color_extra_name, 0);
		
		color_button_background.setBackgroundColor(colors[0]);
		color_button_incoming_foreground.setBackgroundColor(colors[1]);
		color_button_outgoing_foreground.setBackgroundColor(colors[2]);
		
		cb.setChecked(getIntent().getBooleanExtra(terminal_fragment.terminal_clear_input_on_send_extra_name,false));
		sp_1.setSelection(getIntent().getIntExtra(terminal_fragment.terminal_number_of_active_terminals_extra_name,1)-1);
		
		try 
		{
			getting_sp_2_options_thread.join();
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		
		sp_2_adapter= new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_text_view,sp_2_options);
		sp_2_adapter.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
		sp_2.setAdapter(sp_2_adapter);
		
		ok.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				bye_bye();
			}
		});
		
		sp_2.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				
				if(position==0)
				{
					color_button_incoming_foreground.setBackgroundColor(colors[1]);
					color_button_outgoing_foreground.setBackgroundColor(colors[2]);
				}
				else
				{
					color_button_incoming_foreground.setBackgroundColor(
							BlueRemote.get_foreground_colors().get(position-1)[0]
							);
					color_button_outgoing_foreground.setBackgroundColor(
							BlueRemote.get_foreground_colors().get(position-1)[1]
							);
				}
				
				selected_device=position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
			
		});
		
		color_button_background.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				Intent color_picker_intent =new Intent(preferences_activity.this,color_picker.class);
				color_picker_intent.putExtra(color_picker.passed_color,colors[0]);
				startActivityForResult(color_picker_intent, 1);
			}
		});
		
		color_button_incoming_foreground.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				Intent color_picker_intent =new Intent(preferences_activity.this,color_picker.class);
				color_picker_intent.putExtra(color_picker.passed_color,colors[1]);
				startActivityForResult(color_picker_intent, 2);
			}
		});

		color_button_outgoing_foreground.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				Intent color_picker_intent =new Intent(preferences_activity.this,color_picker.class);
				color_picker_intent.putExtra(color_picker.passed_color,colors[2]);
				startActivityForResult(color_picker_intent, 3);
			}
		});
	}
	
	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  
    {
		int color=data.getIntExtra(color_picker.passed_color, 0);
		
		if(requestCode==1)
		{
			colors[0]=color;
			this.color_button_background.setBackgroundColor(colors[0]);
		}
		else if(requestCode==2)
		{
			if(selected_device==0)
			{
				colors[1]=color;
				this.color_button_incoming_foreground.setBackgroundColor(colors[1]);
			}
			else
			{
				BlueRemote.get_foreground_colors().get(selected_device-1)[0]=color;
				this.color_button_incoming_foreground.setBackgroundColor(color);
			}
		}
		else if(requestCode==3)
		{
			if(selected_device==0)
			{
				colors[2]=color;
				this.color_button_outgoing_foreground.setBackgroundColor(colors[2]);
			}
			else
			{
				BlueRemote.get_foreground_colors().get(selected_device-1)[1]=color;
				this.color_button_outgoing_foreground.setBackgroundColor(color);
			}
		}	
    }
	
	void bye_bye()
	{
		switch(rg.getCheckedRadioButtonId())
		{
		case R.id.radioButton1_preferences:
			close_intent.putExtra(device_assignment_extra_name,BlueRemote.assign_all_devices);
			break;
			
		case R.id.radioButton2_preferences:
			close_intent.putExtra(device_assignment_extra_name,BlueRemote.assign_latest_device_if_list_is_empty);
			break;
			
		case R.id.radioButton3_preferences:
			close_intent.putExtra(device_assignment_extra_name,BlueRemote.do_not_assign);
			break;
		}
		
		int button_repetition_period=Integer.parseInt(et[0].getText().toString());
		int hex_board_call_time_out_factor=Integer.parseInt(et[1].getText().toString());
		int hex_board_backspace_repetition_period=Integer.parseInt(et[2].getText().toString());
		
		if(button_repetition_period<200)
		{
			button_repetition_period=200;
		}
		
		if(hex_board_call_time_out_factor<2)
		{
			hex_board_call_time_out_factor=2;
		}
		
		if(hex_board_backspace_repetition_period<200)
		{
			hex_board_backspace_repetition_period=200;
		}
		
		close_intent.putExtra(button_repetition_period_extra_name,button_repetition_period );
		close_intent.putExtra(hex_board_call_time_out_factor_extra_name, hex_board_call_time_out_factor);
		close_intent.putExtra(hex_board_backspace_repetition_period_extra_name, hex_board_backspace_repetition_period);
		
		close_intent.putExtra(terminal_fragment.terminal_background_color_extra_name,colors[0]);
		close_intent.putExtra(terminal_fragment.terminal_default_incoming_foreground_color_extra_name,colors[1]);
		close_intent.putExtra(terminal_fragment.terminal_default_outgoing_foreground_color_extra_name,colors[2]);
		
		close_intent.putExtra(terminal_fragment.terminal_clear_input_on_send_extra_name,cb.isChecked());
		close_intent.putExtra(terminal_fragment.terminal_number_of_active_terminals_extra_name,sp_1.getSelectedItemPosition()+1);
		
		setResult(RESULT_OK, close_intent);        
		finish();
	}
	
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

    @Override
	public boolean onSupportNavigateUp()
	{
    	bye_bye();
		return true;
	}
	
	@Override
	public void onBackPressed ()
	{
		bye_bye();
	}
	
	@Override
	protected void onDestroy ()
	{
		super.onDestroy();
	}	
}
