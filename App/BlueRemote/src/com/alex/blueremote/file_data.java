package com.alex.blueremote;

import java.io.Serializable;
import java.util.ArrayList;

import helper.bluetooth_helper.bluetooth_button_data;
import helper.bluetooth_helper.bluetooth_compound_button_data;

public class file_data implements Serializable 
{
	private static final long serialVersionUID = 925852688478290516L;
	
	ArrayList<bluetooth_button_data> button_data; 
	ArrayList<bluetooth_compound_button_data> compound_button_data;
	
	int button_repetition_period;
	int hex_board_call_time_out_factor;
	int hex_board_backspace_repetition_period;
	
	int colors[];
	int number_of_active_terminals;
	boolean clear_input_on_send;
	
	byte device_assignment;
	
	public file_data(ArrayList<bluetooth_button_data> button_data,ArrayList<bluetooth_compound_button_data> compound_button_data) 
	{
		super();
		this.button_data = button_data;
		this.compound_button_data = compound_button_data;
	}
	
	public file_data(ArrayList<bluetooth_button_data> button_data,ArrayList<bluetooth_compound_button_data> compound_button_data,
			int button_repetition_period,int hex_board_call_time_out_factor,int hex_board_backspace_repetition_period) 
	{
		super();
		this.button_data = button_data;
		this.compound_button_data = compound_button_data;
		
		this.button_repetition_period=button_repetition_period;
		this.hex_board_call_time_out_factor=hex_board_call_time_out_factor;
		this.hex_board_backspace_repetition_period=hex_board_backspace_repetition_period;
	}
	
	public file_data(ArrayList<bluetooth_button_data> button_data,ArrayList<bluetooth_compound_button_data> compound_button_data,
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
	
	public file_data(ArrayList<bluetooth_button_data> button_data,ArrayList<bluetooth_compound_button_data> compound_button_data,
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
	
	public file_data(ArrayList<bluetooth_button_data> button_data,ArrayList<bluetooth_compound_button_data> compound_button_data,
			int button_repetition_period,int hex_board_call_time_out_factor,int hex_board_backspace_repetition_period,
			byte device_assignment,int colors[],int number_of_active_terminals,boolean clear_input_on_send) 
	{
		super();
		this.button_data = button_data;
		this.compound_button_data = compound_button_data;
		
		this.button_repetition_period=button_repetition_period;
		this.hex_board_call_time_out_factor=hex_board_call_time_out_factor;
		this.hex_board_backspace_repetition_period=hex_board_backspace_repetition_period;
		
		this.device_assignment=device_assignment;
		
		this.colors=colors;
		this.number_of_active_terminals=number_of_active_terminals;
		this.clear_input_on_send=clear_input_on_send;
	}
	
	public ArrayList<bluetooth_button_data> get_button_data() 
	{
		return button_data;
	}

	public void set_button_data(ArrayList<bluetooth_button_data> button_data) 
	{
		this.button_data = button_data;
	}

	public ArrayList<bluetooth_compound_button_data> get_compound_button_data() 
	{
		return compound_button_data;
	}

	public void set_compound_button_data(ArrayList<bluetooth_compound_button_data> compound_button_data) 
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
	
	public int get_number_of_active_terminals() {
		return number_of_active_terminals;
	}

	public void set_number_of_active_terminals(int number_of_active_terminals) {
		this.number_of_active_terminals = number_of_active_terminals;
	}

	public boolean is_clear_input_on_send() {
		return clear_input_on_send;
	}

	public void set_clear_input_on_send(boolean clear_input_on_send) {
		this.clear_input_on_send = clear_input_on_send;
	}
	
	public byte get_device_assignment() {
		return device_assignment;
	}

	public void set_device_assignment(byte device_assignment) {
		this.device_assignment = device_assignment;
	}
}
