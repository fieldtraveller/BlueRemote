package com.alex.blueremote;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class HexBoard extends AppCompatActivity {

	Button button[]=new Button[18];
	EditText et;
	String data="";
	Intent close_intent=new Intent();
	
	public static int hex_board_call_time_out;
	public static int hex_board_backspace_repetition_period=200;
	
	TimerTask backspace_task;
	Timer backspace_timer;
	Handler ui_Handler;
	
	public static final String identification_number="IDENTIFICATION_NUMBER";
	public static final String initial_text="INITIAL_TEXT";
	public static final String initial_text_as_hex="INITIAL_TEXT_AS_HEX";
	public static final String hex_data="HEX_DATA";
	public static final String stringed_data="STRINGED_DATA";
	public static final String set_number_of_nibbles_to_return="SET_NUMBER_OF_NIBBLES_TO_RETURN";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hexboard);
		
        button[0]=(Button)findViewById(R.id.Button01_hexboard);
        button[1]=(Button)findViewById(R.id.Button02_hexboard);
        button[2]=(Button)findViewById(R.id.Button03_hexboard);
        button[3]=(Button)findViewById(R.id.Button04_hexboard);
        button[4]=(Button)findViewById(R.id.Button05_hexboard);
        button[5]=(Button)findViewById(R.id.Button06_hexboard);
        button[6]=(Button)findViewById(R.id.Button07_hexboard);
        button[7]=(Button)findViewById(R.id.Button08_hexboard);
        button[8]=(Button)findViewById(R.id.Button09_hexboard);
        button[9]=(Button)findViewById(R.id.Button10_hexboard);
        button[10]=(Button)findViewById(R.id.Button11_hexboard);
        button[11]=(Button)findViewById(R.id.Button12_hexboard);
        button[12]=(Button)findViewById(R.id.Button13_hexboard);
        button[13]=(Button)findViewById(R.id.Button14_hexboard);
        button[14]=(Button)findViewById(R.id.Button15_hexboard);
        button[15]=(Button)findViewById(R.id.Button16_hexboard);
        button[16]=(Button)findViewById(R.id.Button17_hexboard);
        button[17]=(Button)findViewById(R.id.Button18_hexboard);
        
        et=(EditText)findViewById(R.id.editText1_hexboard);
        
        if(this.getIntent().getBooleanExtra(initial_text_as_hex, false))
        {
        	et.setText(this.getIntent().getStringExtra(HexBoard.initial_text));
        }
        else
        {
        	et.setText(string_to_hex(this.getIntent().getStringExtra(HexBoard.initial_text)));
        }
        
        et.setSelection(et.getText().toString().length());
        
        ui_Handler=new Handler();
                
        button[0].setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				set_text("1");
			}
		});
        
        button[1].setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				set_text("2");
			}
		});
        
        button[2].setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				set_text("3");
			}
		});
        
        button[3].setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				set_text("4");
			}
		});
        
        button[4].setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				set_text("5");
			}
		});
        
        button[5].setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				set_text("6");
			}
		});
        
        button[6].setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				set_text("7");
			}
		});
        
        button[7].setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				set_text("8");
			}
		});
        
        button[8].setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				set_text("9");
			}
		});
        
        button[9].setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				set_text("0");
			}
		});
        
        button[10].setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				set_text("A");
			}
		});
        
        button[11].setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				set_text("B");
			}
		});
        
        button[12].setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				set_text("C");
			}
		});
        
        button[13].setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				set_text("D");
			}
		});
        
        button[14].setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				set_text("E");
			}
		});
        
        button[15].setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				set_text("F");
			}
		});
        
        button[16].setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				bye_bye();
//				close_intent.putExtra("data",ev1.getText().toString() );
//    			setResult(RESULT_OK, close_intent);        
//    			finish();
			}
		});
                
        button[17].setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(final View v, MotionEvent event) 
			{	
				switch(event.getAction())
				{
					case MotionEvent.ACTION_DOWN:
						v.setPressed(true);
						backspace(true);
						break;
							
					case MotionEvent.ACTION_UP:
						v.setPressed(false);
						backspace(false);
						v.performClick();
						break;
							
					default:				
				}
				return true;
			}
        });
                
	}

	void backspace(boolean start_or_stop)
	{
		if(start_or_stop==true)
		{
			backspace_task=new TimerTask(){
				
				@Override
				public void run() {
					
					final int cursor_start=et.getSelectionStart();
					final int cursor_end=et.getSelectionEnd();
	        		
					data=et.getText().toString();
					
					if((cursor_start<=0)&&(cursor_end<=0))
	        		{
//						Log.e("Selection", "Start:"+cursor_start+" End:"+cursor_end);
	        		}
					else if(cursor_start==cursor_end)
	        		{
	        			data=data.substring(0,cursor_end-1)+data.substring(cursor_end);
	        		}
	        		else
	        		{
	        			data=data.substring(0,cursor_start)+data.substring(cursor_end);
	        		}
//					Log.e("Selection", "Start:"+cursor_start+" End:"+cursor_end+"\n data:"+data);
	        		
					ui_Handler.post(new Runnable()
					{
						public void run(){
					        	
							et.setText(data);
							if(cursor_start==cursor_end)
							{
								if(cursor_start!=0)
								{
									et.setSelection(cursor_start-1);
								}
								else
								{
									et.setSelection(0);
								}
							}
							else
							{
								et.setSelection(cursor_start);
							}
						}
					});	
				}
			};
			
			backspace_timer = new Timer();
			backspace_timer.scheduleAtFixedRate(backspace_task,0,hex_board_backspace_repetition_period);
		}
		else
		{
			backspace_task.cancel();
			backspace_timer.purge();
			backspace_task=null;	//Timer Task not Reusable
			
			backspace_timer.cancel();
			backspace_timer=null;
		}
	}
	
	void set_text(String dataIn)
	{
		int cursor_start=et.getSelectionStart();
		int cursor_end=et.getSelectionEnd();
		
		String editview_text=et.getText().toString();
		
		String text_to_set="";
		if(cursor_start==cursor_end)
		{
			text_to_set=editview_text.substring(0, cursor_start)+dataIn+editview_text.substring(cursor_start);
		}
		else
		{
			text_to_set=editview_text.substring(0, cursor_start)+dataIn+editview_text.substring(cursor_end);
		}
		
		et.setText(text_to_set);
		et.setSelection(cursor_start+1);
	}
	
	public static int get_hex_board_call_time_out() 
	{
		return hex_board_call_time_out;
	}

	public static int get_hex_board_call_delay_factor() 
	{
		return (int)(hex_board_call_time_out/ViewConfiguration.getLongPressTimeout());
	}
	
	public static void set_hex_board_call_time_out(float hex_board_call_delay_factor) 
	{
		HexBoard.hex_board_call_time_out = (int)hex_board_call_delay_factor*ViewConfiguration.getLongPressTimeout();
	}
	
	public static int get_hex_board_backspace_repetition_period() 
	{
		return hex_board_backspace_repetition_period;
	}

	public static void set_hex_board_backspace_repetition_period(int hex_board_backspace_delay) 
	{
		HexBoard.hex_board_backspace_repetition_period = hex_board_backspace_delay;
	}
	
	public static String string_to_hex(String input)
	{
		if(input==null)
		{
			return input; 
		}
		else 
		{
			int number_of_characters=input.length();
			String output="";
			
			for(int count=0;count<number_of_characters;count++)
			{
				output=output+Integer.toHexString((int)input.charAt(count)).toUpperCase(Locale.getDefault());
			}
			
			return output;
		}
	}
	
	public static String hex_to_string(String input)
	{
		int number_of_nibbles=input.length();
		String output="";
		
		if(number_of_nibbles==0)
		{
			return input; 
		}
		else 
		{
			if(number_of_nibbles%2 != 0)
			{
				input="0"+input;
				number_of_nibbles+=1;
			}
			
			for(int count=0;count<number_of_nibbles;count+=2)
			{
				output=output+((char)Short.parseShort("00"+input.substring(count, count+2), 16));
			}
		}
		
		return output;	
	}
	
	void bye_bye()
	{
		int number_of_nibbles=getIntent().getIntExtra(set_number_of_nibbles_to_return, 0);
		if(number_of_nibbles>0)
		{
			if(et.getText().toString().length()==number_of_nibbles)
			{
				
			}
			else
			{
				Toast
					.makeText(getApplicationContext(), "Number of Required Nibbles to return:"+number_of_nibbles, Toast.LENGTH_SHORT)
						.show();
				return;
			}
		}
		else
		{
			
		}
		
		close_intent.putExtra(HexBoard.identification_number,this.getIntent().getIntExtra(HexBoard.identification_number,-1));
		close_intent.putExtra(HexBoard.hex_data,et.getText().toString());
		close_intent.putExtra(HexBoard.stringed_data,hex_to_string(et.getText().toString()));
		setResult(RESULT_OK, close_intent);        
		finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return false;
	}

	@Override
	public void onBackPressed ()
	{
		bye_bye();
	}
		
}
