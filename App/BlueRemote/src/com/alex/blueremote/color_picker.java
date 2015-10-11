package com.alex.blueremote;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class color_picker extends AppCompatActivity implements OnSeekBarChangeListener {
	
	View color_view;
	TextView tv[]=new TextView[9];
	SeekBar sb[]=new SeekBar[4];
	Button ok_b;
	
	boolean block_call_to_onProgressChanged=false;
	
	int component_color,alpha_component,red_component,green_component,blue_component;
	
	Intent close_intent=new Intent();
	
	final int hexboard_request_code=786;
	public static String passed_color="PASSED_COLOR";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.color_picker);
		
		color_view=(View)findViewById(R.id.view_color_picker);
		
		tv[0]=(TextView)findViewById(R.id.textView_4_color_picker);
		tv[1]=(TextView)findViewById(R.id.textView_5_color_picker);
		tv[2]=(TextView)findViewById(R.id.textView_6_color_picker);
		tv[7]=(TextView)findViewById(R.id.textView_15_color_picker);
		
		tv[3]=(TextView)findViewById(R.id.textView_7_color_picker);
		tv[4]=(TextView)findViewById(R.id.textView_8_color_picker);
		tv[5]=(TextView)findViewById(R.id.textView_9_color_picker);
		tv[8]=(TextView)findViewById(R.id.textView_16_color_picker);
		
		tv[6]=(TextView)findViewById(R.id.textView_13_color_picker);
		
		sb[0]=(SeekBar)findViewById(R.id.seekBar_1_color_picker);
		sb[1]=(SeekBar)findViewById(R.id.seekBar_2_color_picker);
		sb[2]=(SeekBar)findViewById(R.id.seekBar_3_color_picker);
		sb[3]=(SeekBar)findViewById(R.id.seekBar_4_color_picker);
		
		ok_b=(Button)findViewById(R.id.button_color_picker);
		
		set_color(getIntent().getIntExtra(passed_color, 0));
		
		sb[0].setOnSeekBarChangeListener(this);
		sb[1].setOnSeekBarChangeListener(this);
		sb[2].setOnSeekBarChangeListener(this);
		sb[3].setOnSeekBarChangeListener(this);
		
		tv[6].setOnLongClickListener(new OnLongClickListener(){

			@Override
			public boolean onLongClick(View v) {
				
				Intent hexboard_intent=new Intent(color_picker.this,HexBoard.class);
				hexboard_intent.putExtra(HexBoard.initial_text_as_hex, true);
				hexboard_intent.putExtra(HexBoard.set_number_of_nibbles_to_return, 8);
				hexboard_intent.putExtra(HexBoard.initial_text, tv[6].getText().toString().substring(1));
				startActivityForResult(hexboard_intent, hexboard_request_code);
				
				return true;
			}
			
		});
		
		ok_b.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				close_intent.putExtra(passed_color, component_color);
				setResult(RESULT_OK, close_intent);        
				finish();
			}
			
		});
	}

	void set_color(int input_color)
	{
		component_color=input_color;
		alpha_component=Color.alpha(input_color);
		red_component=Color.red(component_color);
		green_component=Color.green(component_color);
		blue_component=Color.blue(component_color);
		
		tv[0].setText(""+red_component);
		tv[3].setText(""+String.format("%02X", red_component));
		
		tv[1].setText(""+green_component);
		tv[4].setText(""+String.format("%02X", green_component));
		
		tv[2].setText(""+blue_component);
		tv[5].setText(""+String.format("%02X", blue_component));
		
		tv[7].setText(""+alpha_component);
		tv[8].setText(""+String.format("%02X", alpha_component));
		
		tv[6].setText("#"+String.format("%08X", component_color));
		
		block_call_to_onProgressChanged=true;
		
		sb[0].setProgress(red_component);
		sb[1].setProgress(green_component);
		sb[2].setProgress(blue_component);
		sb[3].setProgress(alpha_component);
		
		block_call_to_onProgressChanged=false;
		
		color_view.setBackgroundColor(component_color);
//		color_view.setBackgroundColor(Color.rgb(red_component, green_component, blue_component));
//		tv[6].setTextColor(Color.rgb(red_component, green_component, blue_component));
	}
	
	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  
    {
		if(requestCode==hexboard_request_code)
		{
			String color_value=data.getStringExtra(HexBoard.hex_data);
			set_color((int)Long.parseLong(color_value, 16));
		}
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
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		
		if(block_call_to_onProgressChanged==false)
		{
			set_color(Color.argb(
					sb[3].getProgress(),
					sb[0].getProgress(),
					sb[1].getProgress(),
					sb[2].getProgress()
		 ));			
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		
	}	
}
