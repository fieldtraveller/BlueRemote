package com.alex.blueremote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class preferences_activity extends AppCompatActivity {
	
	RadioGroup rg;
	EditText et[]=new EditText[3];
	Button ok,color_button_background,color_button_incoming_foreground,color_button_outgoing_foreground;
	
	int colors[]=new int[3];
	
	Intent close_intent=new Intent();
	
	public static String device_assignment_extra_name="DEVICE_ASSIGNMENT";
	public static String button_repetition_period_extra_name="BUTTON_REPETITION_PERIOD";
	public static String hex_board_call_time_out_factor_extra_name="HEXBOARD_CALL_TIME_OUT_FACTOR";
	public static String hex_board_backspace_repetition_period_extra_name="HEXBOARD_BACKSPACE_REPETITION_PERIOD";
	
	public static String terminal_background_color_extra_name="TERMINAL_BACKGROUND_COLOR";
	public static String terminal_incoming_foreground_color_extra_name="TERMINAL_INCOMING_FOREGROUND_COLOR";
	public static String terminal_outgoing_foreground_color_extra_name="TERMINAL_OUTGOING_FOREGROUND_COLOR";
	
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
        
		colors[0]=getIntent().getIntExtra(terminal_background_color_extra_name, 0);
		colors[1]=getIntent().getIntExtra(terminal_incoming_foreground_color_extra_name, 0);
		colors[2]=getIntent().getIntExtra(terminal_outgoing_foreground_color_extra_name, 0);
		
		color_button_background.setBackgroundColor(colors[0]);
		color_button_incoming_foreground.setBackgroundColor(colors[1]);
		color_button_outgoing_foreground.setBackgroundColor(colors[2]);
		
		ok.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				bye_bye();
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
		if(requestCode==1)
		{
			colors[0]=data.getIntExtra(color_picker.passed_color, 0);
			this.color_button_background.setBackgroundColor(colors[0]);
		}
		else if(requestCode==2)
		{
			colors[1]=data.getIntExtra(color_picker.passed_color, 0);
			this.color_button_incoming_foreground.setBackgroundColor(colors[1]);
		}
		else if(requestCode==3)
		{
			colors[2]=data.getIntExtra(color_picker.passed_color, 0);
			this.color_button_outgoing_foreground.setBackgroundColor(colors[2]);
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
		
		close_intent.putExtra(preferences_activity.terminal_background_color_extra_name,colors[0]);
		close_intent.putExtra(preferences_activity.terminal_incoming_foreground_color_extra_name,colors[1]);
		close_intent.putExtra(preferences_activity.terminal_outgoing_foreground_color_extra_name,colors[2]);
		
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
