package com.alex.blueremote;

public class bluetooth_button_data 
{
	String button_text;
	
	byte[] button_code;
	byte[] button_on_up_code;
	byte[] button_on_down_code;
	
	public static final String button_text_extra_name="BUTTON_TEXT";
	public static final String button_code_extra_name="BUTTON_CODE";
	public static final String button_on_up_code_extra_name="BUTTON_ON_UP_CODE";
	public static final String button_on_down_code_extra_name="BUTTON_ON_DOWN_CODE";
	
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
	
	public bluetooth_button_data(String button_text, byte[] button_code,
								byte[] button_on_up_code, byte[] button_on_down_code) 
	{
		super();
	
		this.button_text = button_text;
		this.button_code = button_code;
		this.button_on_up_code = button_on_up_code;
		this.button_on_down_code = button_on_down_code;
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

	public byte[] getButton_on_up_code() 
	{
		return button_on_up_code;
	}

	public void setButton_on_up_code(byte[] button_on_up_code)
	{
		this.button_on_up_code = button_on_up_code;
	}
	
	public byte[] getButton_on_down_code() 
	{
		return button_on_down_code;
	}

	public void setButton_on_down_code(byte[] button_on_down_code) 
	{
		this.button_on_down_code = button_on_down_code;
	}
	
}
