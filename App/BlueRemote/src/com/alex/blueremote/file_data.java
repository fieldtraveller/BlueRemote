package com.alex.blueremote;

import java.io.Serializable;

public class file_data implements Serializable 
{
	private static final long serialVersionUID = -8248812345723365553L;
	
	bluetooth_button_data button_data[]; 
	bluetooth_switch_data switch_data[];
	
	int button_repetition_period;
	int hex_board_call_time_out_factor;
	int hex_board_backspace_repetition_period;

	public file_data(bluetooth_button_data[] button_data,bluetooth_switch_data[] switch_data) 
	{
		super();
		this.button_data = button_data;
		this.switch_data = switch_data;
	}
	
	public file_data(bluetooth_button_data[] button_data,bluetooth_switch_data[] switch_data,
			int button_repetition_period,int hex_board_call_time_out_factor,int hex_board_backspace_repetition_period) 
	{
		super();
		this.button_data = button_data;
		this.switch_data = switch_data;
		
		this.button_repetition_period=button_repetition_period;
		this.hex_board_call_time_out_factor=hex_board_call_time_out_factor;
		this.hex_board_backspace_repetition_period=hex_board_backspace_repetition_period;
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
	
	public int getButton_repetition_period() {
		return button_repetition_period;
	}

	public void setButton_repetition_period(int button_repetition_period) {
		this.button_repetition_period = button_repetition_period;
	}

	public int get_hex_board_call_time_out_factor() {
		return hex_board_call_time_out_factor;
	}

	public void set_hex_board_call_time_out_factor(int hex_board_call_time_out_factor) {
		this.hex_board_call_time_out_factor = hex_board_call_time_out_factor;
	}

	public int get_hex_board_backspace_repetition_period() {
		return hex_board_backspace_repetition_period;
	}

	public void set_hex_board_backspace_repetition_period(int hex_board_backspace_repetition_period) {
		this.hex_board_backspace_repetition_period = hex_board_backspace_repetition_period;
	}
}
