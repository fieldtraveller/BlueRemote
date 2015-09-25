package com.alex.blueremote;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.OnTouchListener;
//import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

public class button_programming_activity extends AppCompatActivity implements OnTouchListener,Runnable {

	EditText et[];
	Button ok_button;
	
	final int hexboard_activity_request_code=4;
	Handler hex_board_handler;
	int hex_board_call_delay=ViewConfiguration.getLongPressTimeout()*3;
	
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
		
		ok_button=(Button)findViewById(R.id.button_bpa_1);
		
		hex_board_handler=new Handler();
		
		et[0].setText(getIntent().getStringExtra(bluetooth_button_data.button_text_extra_name));
		et[1].setText(getIntent().getStringExtra(bluetooth_button_data.button_code_extra_name));
		et[2].setText(getIntent().getStringExtra(bluetooth_button_data.button_on_down_code_extra_name));
		et[3].setText(getIntent().getStringExtra(bluetooth_button_data.button_on_up_code_extra_name));
		
		et[0].setOnTouchListener(this);
		et[1].setOnTouchListener(this);
		et[2].setOnTouchListener(this);
		et[3].setOnTouchListener(this);	
	}

	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  
    {
		if(requestCode==hexboard_activity_request_code)
		{
			int index=-1;
			
			switch(data.getIntExtra(HexBoard.identification_number,-1))
			{
				case 0:
					index=0;
					break;
					
				case 1:
					index=1;
					break;
					
				case 2:
					index=2;
					break;
					
				case 3:
					index=3;
					break;
			}
			
			et[index].setText(et[index].getText().toString()+data.getStringExtra(HexBoard.stringed_data));
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
//				hex_board_handler.postDelayed(hex_board_runnable,hex_board_call_delay);
				hex_board_handler.postDelayed(this,hex_board_call_delay);
				break;
					
			case MotionEvent.ACTION_UP:
				v.setPressed(false);
//				hex_board_handler.removeCallbacks(hex_board_runnable);
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
			identification_number=0;
			initial_text=et[0].getText().toString();
		}
		
		if(et[1].isPressed())
		{
			identification_number=1;
			initial_text=et[1].getText().toString();
		}
		
		if(et[2].isPressed())
		{
			identification_number=2;
			initial_text=et[2].getText().toString();
		}
		
		if(et[3].isPressed())
		{
			identification_number=3;
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
		close_intent.putExtra(bluetooth_button_data.button_code_extra_name,et[1].getText().toString());
		close_intent.putExtra(bluetooth_button_data.button_on_down_code_extra_name,et[2].getText().toString());
		close_intent.putExtra(bluetooth_button_data.button_on_up_code_extra_name,et[3].getText().toString());
		
		setResult(RESULT_OK, close_intent);        
		finish();
	}
}
