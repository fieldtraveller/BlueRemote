package com.alex.blueremote;

import java.nio.charset.Charset;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
//import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

public class button_programming_activity extends AppCompatActivity implements OnTouchListener,Runnable {

	EditText et[]=new EditText[4];
	CheckBox cb;
	Button ok_button;
	
	final int hexboard_activity_request_code=4;
	Handler hex_board_handler;
	
	Intent close_intent=new Intent();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.button_programming_activity);
		
		et[0]=(EditText)findViewById(R.id.editText_bpa_1);
		et[1]=(EditText)findViewById(R.id.editText_bpa_2);
		et[2]=(EditText)findViewById(R.id.editText_bpa_3);
		et[3]=(EditText)findViewById(R.id.editText_bpa_4);
		
		cb=(CheckBox)findViewById(R.id.checkBox_bpa_1);
				
		ok_button=(Button)findViewById(R.id.button_bpa_1);
		
		hex_board_handler=new Handler();
		
		et[0].setText(getIntent().getStringExtra(bluetooth_button_data.button_text_extra_name));
		
		et[1].setText(new String(getIntent().getByteArrayExtra(bluetooth_button_data.button_code_extra_name)));
		et[2].setText(new String(getIntent().getByteArrayExtra(bluetooth_button_data.button_on_down_code_extra_name)));
		et[3].setText(new String(getIntent().getByteArrayExtra(bluetooth_button_data.button_on_up_code_extra_name)));
		
		et[0].setSelection(et[0].getText().length());
		et[1].setSelection(et[1].getText().length());
		et[2].setSelection(et[2].getText().length());
		et[3].setSelection(et[3].getText().length());
		
		cb.setChecked(getIntent().getBooleanExtra(bluetooth_button_data.respond_on_continuous_touch_extra_name,false));
		
		if(cb.isChecked()==false)
		{
			et[2].setEnabled(false);
			et[3].setEnabled(false);
		}
		
		et[0].setOnTouchListener(this);
		et[1].setOnTouchListener(this);
		et[2].setOnTouchListener(this);
		et[3].setOnTouchListener(this);	
		
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
		switch(event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				v.setPressed(true);
				v.requestFocus();
				hex_board_handler.postDelayed(this,HexBoard.hex_board_call_delay);
				break;
					
			case MotionEvent.ACTION_UP:
				v.setPressed(false);
				hex_board_handler.removeCallbacks(this);
				v.performClick();
				break;
		}
		
		return false;
	}

	@Override
	public void run() 
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
		
		Intent launch_hex_board= new Intent(button_programming_activity.this,HexBoard.class);
		launch_hex_board.putExtra(HexBoard.identification_number, identification_number);
		launch_hex_board.putExtra(HexBoard.initial_text, initial_text);
	    startActivityForResult(launch_hex_board, hexboard_activity_request_code);
	}	
	
	void bye_bye()
	{
		close_intent.putExtra(bluetooth_button_data.button_text_extra_name,et[0].getText().toString());

		close_intent.putExtra(bluetooth_button_data.button_code_extra_name,
							  et[1].getText().toString().getBytes(Charset.forName("ISO-8859-1")));
		close_intent.putExtra(bluetooth_button_data.button_on_down_code_extra_name,
							  et[2].getText().toString().getBytes(Charset.forName("ISO-8859-1")));
		close_intent.putExtra(bluetooth_button_data.button_on_up_code_extra_name,
							  et[3].getText().toString().getBytes(Charset.forName("ISO-8859-1")));
		
		close_intent.putExtra(bluetooth_button_data.respond_on_continuous_touch_extra_name,cb.isChecked());
		
		setResult(RESULT_OK, close_intent);        
		finish();
	}
}
