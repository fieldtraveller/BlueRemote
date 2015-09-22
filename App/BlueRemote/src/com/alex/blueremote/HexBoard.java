package com.alex.blueremote;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;

public class HexBoard extends AppCompatActivity {

	Button button[]=new Button[18];
	EditText ev1;
	String data="";
	Intent close_intent=new Intent();
	
	TimerTask backspace_task;
	Timer backspace_timer;
	Handler ui_Handler;
	final int backspace_delay=200;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hexboard);
		
        button[0]=(Button)findViewById(R.id.Button01);
        button[1]=(Button)findViewById(R.id.Button02);
        button[2]=(Button)findViewById(R.id.Button03);
        button[3]=(Button)findViewById(R.id.Button04);
        button[4]=(Button)findViewById(R.id.Button05);
        button[5]=(Button)findViewById(R.id.Button06);
        button[6]=(Button)findViewById(R.id.Button07);
        button[7]=(Button)findViewById(R.id.Button08);
        button[8]=(Button)findViewById(R.id.Button09);
        button[9]=(Button)findViewById(R.id.Button10);
        button[10]=(Button)findViewById(R.id.Button11);
        button[11]=(Button)findViewById(R.id.Button12);
        button[12]=(Button)findViewById(R.id.Button13);
        button[13]=(Button)findViewById(R.id.Button14);
        button[14]=(Button)findViewById(R.id.Button15);
        button[15]=(Button)findViewById(R.id.Button16);
        button[16]=(Button)findViewById(R.id.Button17);
        button[17]=(Button)findViewById(R.id.Button18);
        
        ev1=(EditText)findViewById(R.id.editText1);
        
        ui_Handler=new Handler();
                
        button[0].setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				setData("1");
			}
		});
        
        button[1].setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				setData("2");
			}
		});
        
        button[2].setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				setData("3");
			}
		});
        
        button[3].setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				setData("4");
			}
		});
        
        button[4].setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				setData("5");
			}
		});
        
        button[5].setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				setData("6");
			}
		});
        
        button[6].setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				setData("7");
			}
		});
        
        button[7].setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				setData("8");
			}
		});
        
        button[8].setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				setData("9");
			}
		});
        
        button[9].setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				setData("0");
			}
		});
        
        button[10].setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				setData("A");
			}
		});
        
        button[11].setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				setData("B");
			}
		});
        
        button[12].setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				setData("C");
			}
		});
        
        button[13].setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				setData("D");
			}
		});
        
        button[14].setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				setData("E");
			}
		});
        
        button[15].setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				setData("F");
			}
		});
        
        button[16].setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				close_intent.putExtra("data", data);
    			setResult(RESULT_OK, close_intent);        
    			finish();
			}
		});
        
//        button[17].setOnClickListener(new View.OnClickListener() {
//			
//        	@Override
//			public void onClick(View v) {
//			
//				int cursor_start=ev1.getSelectionStart();
//				int cursor_end=ev1.getSelectionEnd();
//        		
//				if((cursor_start<=0)||(cursor_end<=0))
//        		{
////					Log.e("Selection", "Start:"+cursor_start+" End:"+cursor_end);
//        		}
//				else if(cursor_start==cursor_end)
//        		{
//        			data=data.substring(0,cursor_end-1)+data.substring(cursor_end);
//        		}
//        		else
//        		{
//        			data=data.substring(0,cursor_start)+data.substring(cursor_end);
//        		}
//        		
////        		Log.e("Data", data);
//        		
//        		setData();	
//			}
//		});
        
//        button[17].setOnLongClickListener(new OnLongClickListener(){
//
//			@Override
//			public boolean onLongClick(View v) {
//				
//				return true;
//			}
//        	
//        });
        
        button[17].setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(final View v, MotionEvent event) 
			{	
				switch(event.getAction())
				{
					case MotionEvent.ACTION_DOWN:
						v.setPressed(true);
						backspace(true);
						Log.e("Button?","Down");
						break;
							
					case MotionEvent.ACTION_UP:
						v.setPressed(false);
						backspace(false);
						v.performClick();
						Log.e("Button?","UP");
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
					
					int cursor_start=ev1.getSelectionStart();
					int cursor_end=ev1.getSelectionEnd();
	        		
//					Log.e("TimerTask?", "Called");
					
					if((cursor_start<=0)||(cursor_end<=0))
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
	        		
					ui_Handler.post(new Runnable()
					{
						public void run(){
					        	
							setData();	
						}
					});
	        		
				}
			};
			
			backspace_timer = new Timer();
			backspace_timer.scheduleAtFixedRate(backspace_task,0,backspace_delay);
			
//			Log.e("Timer?", "On");
		}
		else
		{
			backspace_task.cancel();
			backspace_timer.purge();
			backspace_task=null;	//Timer Task not Reusable
			
			backspace_timer.cancel();
			backspace_timer=null;
			
//			Log.e("Timer?", "Off");
		}
	}
	
	void setData(String dataIn)
	{
		data=data+dataIn;
		ev1.setText(data);
		ev1.setSelection(data.length());
	}
	
	void setData()
	{
		ev1.setText(data);
		ev1.setSelection(data.length());
	}
	
//     @Override
//	 protected void onRestart()
//	 {
//	   	super.onRestart();
//	 }
//	 
//	 @Override
//	 protected void onStart()
//	 {
//	 	super.onStart();
//	 }
// 
//	@Override
//	protected void onResume()
//	{
//		super.onResume();
//	}
//
//	 @Override
//	 protected void onPause ()
//	 {
//	   	super.onPause();
//	 }
//	 
//	 @Override
//	 protected void onStop ()
//	 {
//	 	super.onStop();
//	 }
//	
//	@Override  
//    protected void onActivityResult(int requestCode, int resultCode, Intent data)  
//    {
//	}  
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}

//	@Override
//	public boolean onSupportNavigateUp()
//	{
//		return false;
//	}
//	
//	@Override
//	public void onBackPressed ()
//	{
//	}
	
//	@Override
//	protected void onDestroy ()
//	{
//		super.onDestroy();
//	}	
}
