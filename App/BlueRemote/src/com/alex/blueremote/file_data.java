package com.alex.blueremote;

import java.io.Serializable;

import helper.bluetooth_helper.bluetooth_button_data;
import helper.bluetooth_helper.bluetooth_compound_button_data;

public class file_data implements Serializable 
{
	private static final long serialVersionUID = 5092387345793559107L;
	
	bluetooth_button_data button_data[]; 
	bluetooth_compound_button_data compound_button_data[];
	
	int button_repetition_period;
	int hex_board_call_time_out_factor;
	int hex_board_backspace_repetition_period;
	
	int colors[];
	
	byte device_assignment;
	
	public file_data(bluetooth_button_data[] button_data,bluetooth_compound_button_data[] compound_button_data) 
	{
		super();
		this.button_data = button_data;
		this.compound_button_data = compound_button_data;
	}
	
	public file_data(bluetooth_button_data[] button_data,bluetooth_compound_button_data[] compound_button_data,
			int button_repetition_period,int hex_board_call_time_out_factor,int hex_board_backspace_repetition_period) 
	{
		super();
		this.button_data = button_data;
		this.compound_button_data = compound_button_data;
		
		this.button_repetition_period=button_repetition_period;
		this.hex_board_call_time_out_factor=hex_board_call_time_out_factor;
		this.hex_board_backspace_repetition_period=hex_board_backspace_repetition_period;
	}
	
	public file_data(bluetooth_button_data[] button_data,bluetooth_compound_button_data[] compound_button_data,
			int button_repetition_period,int hex_board_call_time_out_factor,int hex_board_backspace_repetition_period,
			byte device_assignment) 
	{
		super();
		this.button_data = button_data;
		this.compound_button_data = compound_button_data;
		
		this.button_repetition_period=button_repetition_period;
		this.hex_board_call_time_out_factor=hex_board_call_time_out_factor;
		this.hex_board_backspace_repetition_period=hex_board_backspace_repetition_period;
		
		this.device_assignment=device_assignment;
	}
	
	public file_data(bluetooth_button_data[] button_data,bluetooth_compound_button_data[] compound_button_data,
			int button_repetition_period,int hex_board_call_time_out_factor,int hex_board_backspace_repetition_period,
			byte device_assignment,int colors[]) 
	{
		super();
		this.button_data = button_data;
		this.compound_button_data = compound_button_data;
		
		this.button_repetition_period=button_repetition_period;
		this.hex_board_call_time_out_factor=hex_board_call_time_out_factor;
		this.hex_board_backspace_repetition_period=hex_board_backspace_repetition_period;
		
		this.device_assignment=device_assignment;
		
		this.colors=colors;
	}
	
	public bluetooth_button_data[] get_button_data() 
	{
		return button_data;
	}

	public void set_button_data(bluetooth_button_data[] button_data) 
	{
		this.button_data = button_data;
	}

	public bluetooth_compound_button_data[] get_compound_button_data() 
	{
		return compound_button_data;
	}

	public void set_compound_button_data(bluetooth_compound_button_data[] compound_button_data) 
	{
		this.compound_button_data = compound_button_data;
	}
	
	public int get_button_repetition_period() {
		return button_repetition_period;
	}

	public void set_button_repetition_period(int button_repetition_period) {
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
	
	public int[] get_colors() {
		return colors;
	}

	public void set_colors(int[] colors) {
		this.colors = colors;
	}
	
	public byte get_device_assignment() {
		return device_assignment;
	}

	public void set_device_assignment(byte device_assignment) {
		this.device_assignment = device_assignment;
	}
}
