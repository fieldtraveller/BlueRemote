package com.alex.blueremote;

import java.io.Serializable;

public class saved_data implements Serializable 
{
	private static final long serialVersionUID = -3662040439354209969L;
	
	bluetooth_button_data button_data[]; 
	bluetooth_switch_data switch_data[];
	
	String preferred_devices[];
	
	public saved_data(bluetooth_button_data[] button_data,bluetooth_switch_data[] switch_data) 
	{
		super();
		this.button_data = button_data;
		this.switch_data = switch_data;
	}
	
	public saved_data(bluetooth_button_data[] button_data,bluetooth_switch_data[] switch_data, String[] preferred_devices) 
	{
		super();
		this.button_data = button_data;
		this.switch_data = switch_data;
		this.preferred_devices = preferred_devices;
	}

	public bluetooth_button_data[] getButton_data() 
	{
		return button_data;
	}

	public void setButton_data(bluetooth_button_data[] button_data) 
	{
		this.button_data = button_data;
	}

	public bluetooth_switch_data[] getSwitch_data() 
	{
		return switch_data;
	}

	public void setSwitch_data(bluetooth_switch_data[] switch_data) 
	{
		this.switch_data = switch_data;
	}

	public String[] getPreferred_devices() 
	{
		return preferred_devices;
	}

	public void setPreferred_devices(String[] preferred_devices) 
	{
		this.preferred_devices = preferred_devices;
	}
	
}
