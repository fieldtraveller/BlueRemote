package com.alex.blueremote;

import android.support.v7.app.AppCompatActivity;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

	BluetoothAdapter BtAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		BtAdapter = BluetoothAdapter.getDefaultAdapter();
		/*
		if (BtAdapter != null) 
		{
		    Log.d(BLUETOOTH_SERVICE, "Bluetooth HW exist.");
		}
		//*/
		
		//*
		BtAdapter.enable();
	    Log.d(BLUETOOTH_SERVICE, "Bluetooth turned On.");
	    //*/
	    
		/*
		if (!BtAdapter.isEnabled()) 
		{
			Log.d(BLUETOOTH_SERVICE, "Bluetooth is Off.");
			Log.d(BLUETOOTH_SERVICE, "Turning on Bluetoooth.");
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, 1);
		    
		    Log.d(BLUETOOTH_SERVICE, "Bluetooth is On.");
		}
		else
		{
			Log.d(BLUETOOTH_SERVICE, "Bluetooth is On.");
		}
		//*/
		
	}

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
	
	@Override
	protected void onDestroy ()
	{
		BtAdapter.disable();
	    Log.d(BLUETOOTH_SERVICE, "Bluetooth turned Off.");
	}
}
