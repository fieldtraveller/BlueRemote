package com.alex.blueremote;

import java.io.Serializable;

public class bluetooth_button_data implements Serializable
{
	private static final long serialVersionUID = -7759786860591402406L;

	String button_text;
	
	byte[] button_code;
	byte[] button_on_up_code;
	byte[] button_on_down_code;
	
	boolean respond_on_continuous_touch=false;
	
	public static final String button_text_extra_name="BUTTON_TEXT";
	
	public static final String button_code_extra_name="BUTTON_CODE";
	public static final String button_on_up_code_extra_name="BUTTON_ON_UP_CODE";
	public static final String button_on_down_code_extra_name="BUTTON_ON_DOWN_CODE";
	
	public static final String respond_on_continuous_touch_extra_name="RESPOND_ON_CONTINUOUS_TOUCH";
	
	public bluetooth_button_data() 
	{	
		super();
		
		this.button_text="";
		
		this.button_code=new byte[0];
		this.button_on_up_code=new byte[0];
		this.button_on_down_code=new byte[0];
	}
	
	public bluetooth_button_data(String button_text) 
	{	
		super();
		
		this.button_text = button_text;
		
		this.button_code=new byte[0];
		this.button_on_up_code=new byte[0];
		this.button_on_down_code=new byte[0];
	}
	
	public bluetooth_button_data(String button_text, byte[] button_code) 
	{
		super();
		
		this.button_text = button_text;
		
		this.button_code = button_code;
		this.button_on_up_code=new byte[0];
		this.button_on_down_code=new byte[0];
	}
	
	public bluetooth_button_data(String button_text, byte[] button_code,boolean respond_on_continuous_touch,
								 byte[] button_on_down_code,byte[] button_on_up_code) 
	{
		super();
	
		this.button_text = button_text;
		this.button_code = button_code;
		
		this.respond_on_continuous_touch=respond_on_continuous_touch;
		this.button_on_down_code = button_on_down_code;
		this.button_on_up_code = button_on_up_code;
		
	}
	
	public String getButton_text() 
	{
		return button_text;
	}

	public void setButton_text(String button_text) 
	{
		this.button_text = button_text;
	}

	public byte[] getButton_code() 
	{
		return button_code;
	}

	public void setButton_code(byte[] button_code)
	{
		this.button_code = button_code;
	}

	public boolean isRespond_on_continuous_touch() 
	{
		return respond_on_continuous_touch;
	}

	public void setRespond_on_continuous_touch(boolean respond_on_touch) 
	{
		this.respond_on_continuous_touch = respond_on_touch;
	}

	public byte[] getButton_on_down_code() 
	{
		return button_on_down_code;
	}

	public void setButton_on_down_code(byte[] button_on_down_code) 
	{
		this.button_on_down_code = button_on_down_code;
	}

	public byte[] getButton_on_up_code() 
	{
		return button_on_up_code;
	}

	public void setButton_on_up_code(byte[] button_on_up_code)
	{
		this.button_on_up_code = button_on_up_code;
	}
	
}
