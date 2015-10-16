package com.alex.blueremote;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class splash extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		this.getSupportActionBar().hide();
		
//		ImageView imageView = (ImageView) findViewById(R.id.iv);
//		imageView.setImageResource(R.drawable.logo);
		
		BlueRemote.set_BtAdapter(BluetoothAdapter.getDefaultAdapter());
		
		if(BlueRemote.get_BtAdapter() == null) 
		{
		    Toast.makeText(getApplicationContext(), "No BlueTooth Hardware Detected.\nApp Exited", Toast.LENGTH_LONG).show();
		    finish();
		}
		
		//Direct Turn On BT by App 
		if (BlueRemote.get_BtAdapter().isEnabled()==false) 
		{
			BlueRemote.get_BtAdapter().enable();
		}

		new Thread(){
			
			public void run()
			{
				try
				{
					sleep(1000);
					
					while(BlueRemote.get_BtAdapter().getState()==BluetoothAdapter.STATE_TURNING_ON)
					{
						//Log.d(BLUETOOTH_SERVICE, "Bluetooth turning On.");
					}
					Log.d(BLUETOOTH_SERVICE, "Bluetooth turned On.");
				    
					startActivity(new Intent(".MainActivity"));
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();		
				}
			}
		}.start();
			    
	    /*
		//Request User to turn on BT 
	    //Can not turned off
	    if (!BlueRemote.getBtAdapter().isEnabled()) 
		{
			Log.d(BLUETOOTH_SERVICE, "Bluetooth is Off.");
			Log.d(BLUETOOTH_SERVICE, "Turning on Bluetoooth.");
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, BT_turn_on_fragment_request_code);
		    //startActivity(enableBtIntent);
		    
		    while(BlueRemote.getBtAdapter().getState()==BluetoothAdapter.STATE_TURNING_ON)
			{
				//Log.d(BLUETOOTH_SERVICE, "Bluetooth turning On.");
			}		
		}
		else
		{
			Log.d(BLUETOOTH_SERVICE, "Bluetooth is On.");
		}
		//*/
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		this.finish();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}
	
	@Override
	public void onBackPressed ()
	{
		finish();
	}
	
	@Override
	protected void onDestroy ()
	{
		super.onDestroy();
	}
}
