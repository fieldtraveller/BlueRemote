package com.alex.blueremote;

import java.util.ArrayList;

import helper.bluetooth_helper.BT_spp;

import android.content.Intent;
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

public class connected_device_list_activity extends AppCompatActivity {

	ListView lv;
	Button ok_b;
	
	Intent close_intent=new Intent();

	ArrayList<BT_spp> connected_device_list;
	ArrayList<Integer> selected_devices_list_indices;
	
	String cb_text[];
	boolean selected_status[];
	
	ArrayAdapter<String> lv_adapter;
	OnCheckedChangeListener cb_OnCheckedChangeListener;
	
	public final static int connected_device_list_activiy_request_code=8461;
	public static final String selected_devices_list_indices_extra_name="SELECTED_DEVICES_LIST_INDICES";
		
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connected_device_list_activity_layout);
		
		setFinishOnTouchOutside(false);
		
		lv = (ListView) findViewById(R.id.list_cdla);
		ok_b = (Button) findViewById(R.id.button_cdla);
		
		connected_device_list=BlueRemote.get_connected_device_list();
		selected_devices_list_indices=getIntent().getIntegerArrayListExtra(connected_device_list_activity.selected_devices_list_indices_extra_name);
		
		Thread getting_cb_state_thread=new Thread(){
			
			public void run()
			{
				selected_status=new boolean[(connected_device_list.size()+1)];
				
				selected_status[0]=false;
				
				int number_of_iterations=selected_devices_list_indices.size();
				for(int count=0;count<number_of_iterations;count++)
				{
					selected_status[selected_devices_list_indices.get(count).intValue()+1]=true;
				}
			}
		};
		getting_cb_state_thread.start();
		
		cb_text=new String[(connected_device_list.size()+1)];
		
		Thread getting_cb_text_thread=new Thread(){
			
			public void run()
			{
				cb_text[0]="ALL";
				
				for(int count=1;count<cb_text.length;count++)
				{
					cb_text[count]=connected_device_list.get(count-1).toString();
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
				  
				  if(selected_status[position]==true)
				  {
					  cb.setChecked(true);
				  }
				  else
				  {
					  cb.setChecked(false);
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
				
				if(position == 0)
				{
					int size=lv_adapter.getCount();
					
					for(int count=0;count<size;count++)
					{
						selected_status[count]=isChecked;
					}
				}
				else
				{
					selected_status[0]=false;
					selected_status[position]=isChecked;
				}

				lv.invalidateViews();
			}			  
		  };
		
		ok_b.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) 
			{
				selected_devices_list_indices=null;
				selected_devices_list_indices=new ArrayList<Integer>();
				
				int size=lv_adapter.getCount();
				for(int count=1;count<size;count++)
				{
					if(selected_status[count]==true)
					{
						selected_devices_list_indices.add(count-1);
					}
				}
				
				close_intent.putIntegerArrayListExtra(connected_device_list_activity.selected_devices_list_indices_extra_name,selected_devices_list_indices);
				setResult(RESULT_OK, close_intent);        
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
