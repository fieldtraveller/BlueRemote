package com.alex.blueremote;

import java.util.ArrayList;

import helper.bluetooth_helper.BT_spp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;

public class disconnect_device_activity extends AppCompatActivity {

	ListView lv;
	Button ok_b;
	
	public final static int disconnect_device_activity_request_code=8461;
	
	ArrayList<BT_spp> connected_device_list;
	
	String cb_text[];
	boolean device_use_status[];
	boolean device_disconnect_list[];
	
	ArrayAdapter<String> lv_adapter;
	OnCheckedChangeListener cb_OnCheckedChangeListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connected_device_list_activity_layout);
		
		setFinishOnTouchOutside(false);
		
		lv = (ListView) findViewById(R.id.list_cdla);
		ok_b = (Button) findViewById(R.id.button_cdla);
		
		connected_device_list=((BlueRemote)getApplicationContext()).get_connected_device_list();
		
		Thread getting_cb_state_thread=new Thread(){
			
			public void run()
			{
				int number_of_iterations=connected_device_list.size();
				
				device_use_status=new boolean[number_of_iterations];
				device_disconnect_list=new boolean[number_of_iterations];
				
				for(int count=0;count<number_of_iterations;count++)
				{
					device_use_status[count]=(connected_device_list.get(count).get_number_of_components_using_this_object()==0);
					device_disconnect_list[count]=false;
				}
			}
		};
		getting_cb_state_thread.start();
		
		cb_text=new String[(connected_device_list.size())];
		
		Thread getting_cb_text_thread=new Thread(){
			
			public void run()
			{
				for(int count=0;count<cb_text.length;count++)
				{
					cb_text[count]=connected_device_list.get(count).toString();
				}
			}
		};
		getting_cb_text_thread.start();
		
		lv_adapter = new ArrayAdapter<String>(this,R.layout.lv_checkbox,R.id.checkBox1_cdla,cb_text){
			
			  @Override
			  public View getView(final int position, View convertView, ViewGroup parent) 
			  {
				  View v = (View)super.getView(position, convertView, parent);

				  CheckBox cb=(CheckBox)v.findViewById(R.id.checkBox1_cdla);
				  
				  cb.setOnCheckedChangeListener(null);
				  
				  if(device_use_status[position]==true)
				  {
					  cb.setEnabled(true);
				  }
				  else
				  {
					  cb.setEnabled(false);
				  }
				  
				  cb.setOnCheckedChangeListener(cb_OnCheckedChangeListener);
				  
				  return v;
			  }
		};
		
		lv.setAdapter(lv_adapter);
		
		cb_OnCheckedChangeListener = new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) 
			{
				int position=lv_adapter.getPosition(buttonView.getText().toString());
				
				device_disconnect_list[position]=isChecked;
			}			  
		  };
		
		ok_b.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) 
			{
				int number_of_iterations=device_disconnect_list.length;
				
				for(int count=0;count<number_of_iterations;count++)
				{
					if(device_disconnect_list[count]==true)
					{
						connected_device_list.get(count).disconnect();
						connected_device_list.remove(count);
					}
				}
				
				finish();
			}
		});
		
		try 
		{
			getting_cb_state_thread.join();
			getting_cb_text_thread.join();
		}
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		
		lv_adapter.notifyDataSetChanged();
	}
		
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	@Override
	public void onBackPressed ()
	{
	}
	
	@Override
	protected void onDestroy ()
	{
		super.onDestroy();
	}	
}
