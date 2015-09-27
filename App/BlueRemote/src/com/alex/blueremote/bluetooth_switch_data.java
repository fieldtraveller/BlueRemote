package com.alex.blueremote;

import java.io.Serializable;

public class bluetooth_switch_data implements Serializable
{
	private static final long serialVersionUID = 5637592194580776333L;

	String switch_text;
	
	byte[] switch_code;
	byte[] switch_on_code;
	byte[] switch_off_code;
	
	public static final String switch_text_extra_name="SWITCH_TEXT";
	
	public static final String switch_code_extra_name="SWITCH_CODE";
	public static final String switch_on_code_extra_name="SWITCH_ON_CODE";
	public static final String switch_off_code_extra_name="SWITCH_OFF_CODE";
	
	public bluetooth_switch_data() 
	{	
		super();
		
		this.switch_text="";
		
		this.switch_code=new byte[0];
		this.switch_on_code=new byte[0];
		this.switch_off_code=new byte[0];
	}
	
	public bluetooth_switch_data(String switch_text) 
	{	
		super();
		
		this.switch_text = switch_text;
		
		this.switch_code=new byte[0];
		this.switch_on_code=new byte[0];
		this.switch_off_code=new byte[0];
	}
	
	public bluetooth_switch_data(String switch_text, byte[] switch_code) 
	{
		super();
		
		this.switch_text = switch_text;
		
		this.switch_code = switch_code;
		this.switch_on_code=new byte[0];
		this.switch_off_code=new byte[0];
	}
	
	public bluetooth_switch_data(String switch_text, byte[] switch_code,byte[] switch_on_code,byte[] switch_off_code) 
	{
		super();
	
		this.switch_text = switch_text;
		this.switch_code = switch_code;
		
		this.switch_off_code = switch_off_code;
		this.switch_on_code = switch_on_code;
	}
	
	public String getSwitch_text() 
	{
		return switch_text;
	}

	public void setSwitch_text(String switch_text) 
	{
		this.switch_text = switch_text;
	}

	public byte[] getSwitch_code() 
	{
		return switch_code;
	}

	public void setSwitch_code(byte[] switch_code)
	{
		this.switch_code = switch_code;
	}

	public byte[] getSwitch_on_code() 
	{
		return switch_on_code;
	}

	public void setSwitch_on_code(byte[] switch_on_code)
	{
		this.switch_on_code = switch_on_code;
	}
	
	public byte[] getSwitch_off_code() 
	{
		return switch_off_code;
	}

	public void setSwitch_off_code(byte[] switch_off_code) 
	{
		this.switch_off_code = switch_off_code;
	}

}
