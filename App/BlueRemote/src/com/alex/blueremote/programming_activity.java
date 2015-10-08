package com.alex.blueremote;

import java.nio.charset.Charset;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

public class programming_activity extends AppCompatActivity implements OnTouchListener,Runnable {
	
	TextView tv[]=new TextView [4];
	EditText et[]=new EditText[4];
	CheckBox cb;
	Button ok_button,select_devices_button;
	
	boolean scroll_lock=false;
	
	final int hexboard_activity_request_code=4;
	Handler hex_board_handler;
	
	Intent close_intent=new Intent();
	
	public static final String input_type="INPUT_TYPE";
	public static final String input_type_just_devices="JUST_DEVICES";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.programming_activity_layout);
		
		tv[0]=(TextView)findViewById(R.id.textView_bpa_1);
		tv[1]=(TextView)findViewById(R.id.textView_bpa_2);
		tv[2]=(TextView)findViewById(R.id.textView_bpa_3);
		tv[3]=(TextView)findViewById(R.id.textView_bpa_4);
		
		et[0]=(EditText)findViewById(R.id.editText_bpa_1);
		et[1]=(EditText)findViewById(R.id.editText_bpa_2);
		et[2]=(EditText)findViewById(R.id.editText_bpa_3);
		et[3]=(EditText)findViewById(R.id.editText_bpa_4);
		
		cb=(CheckBox)findViewById(R.id.checkBox_bpa_1);
		
		ok_button=(Button)findViewById(R.id.button_bpa_1);
		select_devices_button=(Button)findViewById(R.id.button_bpa_2);
		
		hex_board_handler=new Handler();
		
		if(getIntent().getStringExtra(input_type).compareTo(input_type_just_devices)==0)
		{
			tv[1].setVisibility(View.GONE);
			tv[2].setVisibility(View.GONE);
			tv[3].setVisibility(View.GONE);
			
			et[0].setVisibility(View.GONE);
			et[1].setVisibility(View.GONE);
			et[2].setVisibility(View.GONE);
			et[3].setVisibility(View.GONE);
			
			cb.setVisibility(View.GONE);
			
			tv[0].setText(R.string.set_component_device_list);	
		}
		else
		{
			if(getIntent().getStringExtra(input_type).compareTo(bluetooth_button.input_type_button)==0)
			{
				tv[0].setText(R.string.enter_button_text);
				tv[1].setText(R.string.enter_button_code);
				tv[2].setText(R.string.enter_button_on_down_code);
				tv[3].setText(R.string.enter_button_on_up_code);
				
				et[0].setText(getIntent().getStringExtra(bluetooth_button_data.button_text_extra_name));
				
				et[1].setText(new String(getIntent().getByteArrayExtra(bluetooth_button_data.button_code_extra_name)));
				et[2].setText(new String(getIntent().getByteArrayExtra(bluetooth_button_data.button_on_down_code_extra_name)));
				et[3].setText(new String(getIntent().getByteArrayExtra(bluetooth_button_data.button_on_up_code_extra_name)));
				
				cb.setChecked(getIntent().getBooleanExtra(bluetooth_button_data.respond_on_continuous_touch_extra_name,false));
				
				if(cb.isChecked()==false)
				{
					et[2].setEnabled(false);
					et[3].setEnabled(false);
				}
				
				cb.setOnCheckedChangeListener(new OnCheckedChangeListener(){

					@Override
					public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) 
					{
						if(isChecked==true)
						{
							et[2].setEnabled(true);
							et[3].setEnabled(true);
						}
						else
						{
							et[2].setEnabled(false);
							et[3].setEnabled(false);
						}
					}
					
				} );
			}
			else if(getIntent().getStringExtra(input_type).compareTo(bluetooth_switch.input_type_switch)==0)
			{
				tv[0].setText(R.string.enter_switch_text);
				tv[1].setText(R.string.enter_switch_code);
				tv[2].setText(R.string.enter_switch_on_code);
				tv[3].setText(R.string.enter_switch_off_code);
				
				et[0].setText(getIntent().getStringExtra(bluetooth_switch_data.switch_text_extra_name));
				
				et[1].setText(new String(getIntent().getByteArrayExtra(bluetooth_switch_data.switch_code_extra_name)));
				et[2].setText(new String(getIntent().getByteArrayExtra(bluetooth_switch_data.switch_on_code_extra_name)));
				et[3].setText(new String(getIntent().getByteArrayExtra(bluetooth_switch_data.switch_off_code_extra_name)));
				
				cb.setEnabled(false);
				cb.setVisibility(View.GONE);
			}
			
			et[0].setSelection(et[0].getText().length());
			et[1].setSelection(et[1].getText().length());
			et[2].setSelection(et[2].getText().length());
			et[3].setSelection(et[3].getText().length());
			
			et[0].setOnTouchListener(this);
			et[1].setOnTouchListener(this);
			et[2].setOnTouchListener(this);
			et[3].setOnTouchListener(this);
			
		}
			
		select_devices_button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				Intent connected_device_list= new Intent(programming_activity.this, connected_device_list_activity.class);
				
				connected_device_list.putParcelableArrayListExtra(connected_device_list_activity.selected_devices_list_extra_name,
						getIntent().getParcelableArrayListExtra(connected_device_list_activity.selected_devices_list_extra_name));
				
				startActivityForResult(connected_device_list, connected_device_list_activity.connected_device_list_request_code);
			}
		});
		
		ok_button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				bye_bye();
			}	
		});
	}

	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  
    {
		if(requestCode==hexboard_activity_request_code)
		{
			int index=-1;
			int identification_number=data.getIntExtra(HexBoard.identification_number,-1);
			
			if(identification_number==et[0].getId())
			{
				index=0;
			}
			else if(identification_number==et[1].getId())
			{
				index=1;
			}
			else if(identification_number==et[2].getId())
			{
				index=2;
			}
			else if(identification_number==et[3].getId())
			{
				index=3;
			}
						
			et[index].setText(data.getStringExtra(HexBoard.stringed_data));
			et[index].setSelection(et[index].getText().length());
		}
		else if(requestCode==connected_device_list_activity.connected_device_list_request_code)
		{
			close_intent.putParcelableArrayListExtra(connected_device_list_activity.selected_devices_list_extra_name,
					data.getParcelableArrayListExtra(connected_device_list_activity.selected_devices_list_extra_name));
		}
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

	@Override
	public boolean onTouch(View v, MotionEvent event) 
	{
//		Log.e("","onTouch:"+event);
		
		switch(event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				v.setPressed(true);
				v.requestFocus();
				scroll_lock=false;
				hex_board_handler.postDelayed(this,HexBoard.hex_board_call_time_out);
				break;
					
			case MotionEvent.ACTION_UP:
				v.setPressed(false);
				hex_board_handler.removeCallbacks(this);
				v.performClick();
				break;
				
			case MotionEvent.ACTION_MOVE:
				scroll_lock=true;
				break;
		}
		
		return false;
	}

	@Override
	public void run() 
	{
		if(scroll_lock==false)
		{
			int identification_number=-1;
			String initial_text=null;
			
			if(et[0].isPressed())
			{
				identification_number=et[0].getId();
				initial_text=et[0].getText().toString();
			}
			
			if(et[1].isPressed())
			{
				identification_number=et[1].getId();
				initial_text=et[1].getText().toString();
			}
			
			if(et[2].isPressed())
			{
				identification_number=et[2].getId();
				initial_text=et[2].getText().toString();
			}
			
			if(et[3].isPressed())
			{
				identification_number=et[3].getId();
				initial_text=et[3].getText().toString();
			}
			
			Intent launch_hex_board= new Intent(programming_activity.this,HexBoard.class);
			launch_hex_board.putExtra(HexBoard.identification_number, identification_number);
			launch_hex_board.putExtra(HexBoard.initial_text, initial_text);
		    startActivityForResult(launch_hex_board, hexboard_activity_request_code);
		}
	}	
	
	void bye_bye()
	{
		if(getIntent().getStringExtra(input_type).compareTo(bluetooth_button.input_type_button)==0)
		{
			close_intent.putExtra(bluetooth_button_data.button_text_extra_name,et[0].getText().toString());

			close_intent.putExtra(bluetooth_button_data.button_code_extra_name,
								  et[1].getText().toString().getBytes(Charset.forName("ISO-8859-1")));
			close_intent.putExtra(bluetooth_button_data.button_on_down_code_extra_name,
								  et[2].getText().toString().getBytes(Charset.forName("ISO-8859-1")));
			close_intent.putExtra(bluetooth_button_data.button_on_up_code_extra_name,
								  et[3].getText().toString().getBytes(Charset.forName("ISO-8859-1")));
			
			close_intent.putExtra(bluetooth_button_data.respond_on_continuous_touch_extra_name,cb.isChecked());

		}
		else if(getIntent().getStringExtra(input_type).compareTo(bluetooth_switch.input_type_switch)==0)
		{
			close_intent.putExtra(bluetooth_switch_data.switch_text_extra_name,et[0].getText().toString());

			close_intent.putExtra(bluetooth_switch_data.switch_code_extra_name,
								  et[1].getText().toString().getBytes(Charset.forName("ISO-8859-1")));
			close_intent.putExtra(bluetooth_switch_data.switch_on_code_extra_name,
								  et[2].getText().toString().getBytes(Charset.forName("ISO-8859-1")));
			close_intent.putExtra(bluetooth_switch_data.switch_off_code_extra_name,
								  et[3].getText().toString().getBytes(Charset.forName("ISO-8859-1")));
			
			close_intent.putExtra(bluetooth_button_data.respond_on_continuous_touch_extra_name,cb.isChecked());
		}

		setResult(RESULT_OK, close_intent);        
		finish();
	}
	
}