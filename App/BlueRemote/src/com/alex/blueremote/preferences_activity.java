package com.alex.blueremote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class preferences_activity extends AppCompatActivity {
	
	EditText et[]=new EditText[3];
	Button ok;
	
	Intent close_intent=new Intent();
	
	public static String button_repetition_period_extra_name="BUTTON_REPETITION_PERIOD";
	public static String hex_board_call_time_out_factor_extra_name="HEXBOARD_CALL_TIME_OUT_FACTOR";
	public static String hex_board_backspace_repetition_period_extra_name="HEXBOARD_BACKSPACE_REPETITION_PERIOD";
	
	public static int preferences_activity_request_code=4896;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences_activity_layout);
		
		et[0]=(EditText)findViewById(R.id.editText1_preferences);
		et[1]=(EditText)findViewById(R.id.editText2_preferences);
		et[2]=(EditText)findViewById(R.id.editText3_preferences);
		ok=(Button)findViewById(R.id.button1_preferences);
		
		et[0].setText(""+getIntent().getIntExtra(button_repetition_period_extra_name, 200));
		et[1].setText(""+getIntent().getIntExtra(hex_board_call_time_out_factor_extra_name, 2));
		et[2].setText(""+getIntent().getIntExtra(hex_board_backspace_repetition_period_extra_name, 200));
		
		et[0].setSelection(et[0].getText().toString().length());
		et[1].setSelection(et[1].getText().toString().length());
		et[2].setSelection(et[2].getText().toString().length());
        
		ok.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				bye_bye();
			}
		});
	}
	
	void bye_bye()
	{
		int button_repetition_period=Integer.parseInt(et[0].getText().toString());
		int hex_board_call_time_out_factor=Integer.parseInt(et[1].getText().toString());
		int hex_board_backspace_repetition_period=Integer.parseInt(et[2].getText().toString());
		
		if(button_repetition_period<200)
		{
			button_repetition_period=200;
		}
		
		if(hex_board_call_time_out_factor<2)
		{
			hex_board_call_time_out_factor=2;
		}
		
		if(hex_board_backspace_repetition_period<200)
		{
			hex_board_backspace_repetition_period=200;
		}
		
		close_intent.putExtra(button_repetition_period_extra_name,button_repetition_period );
		close_intent.putExtra(hex_board_call_time_out_factor_extra_name, hex_board_call_time_out_factor);
		close_intent.putExtra(hex_board_backspace_repetition_period_extra_name, hex_board_backspace_repetition_period);
		
		setResult(RESULT_OK, close_intent);        
		finish();
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
}
