package helper.bluetooth_helper;

import java.io.Serializable;

public class bluetooth_compound_button_data implements Serializable
{
	private static final long serialVersionUID = 5637592194580776333L;

	String compound_button_text;
	
	byte[] compound_button_code;
	byte[] compound_button_on_code;
	byte[] compound_button_off_code;
	
	public static final String compound_button_text_extra_name="COMPOUND_BUTTON_TEXT";
	
	public static final String compound_button_code_extra_name="COMPOUND_BUTTON_CODE";
	public static final String compound_button_on_code_extra_name="COMPOUND_BUTTON_ON_CODE";
	public static final String compound_button_off_code_extra_name="COMPOUND_BUTTON_OFF_CODE";
	
	public bluetooth_compound_button_data() 
	{	
		super();
		
		this.compound_button_text="";
		
		this.compound_button_code=new byte[0];
		this.compound_button_on_code=new byte[0];
		this.compound_button_off_code=new byte[0];
	}
	
	public bluetooth_compound_button_data(String compound_button_text) 
	{	
		super();
		
		this.compound_button_text = compound_button_text;
		
		this.compound_button_code=new byte[0];
		this.compound_button_on_code=new byte[0];
		this.compound_button_off_code=new byte[0];
	}
	
	public bluetooth_compound_button_data(String compound_button_text, byte[] compound_button_code) 
	{
		super();
		
		this.compound_button_text = compound_button_text;
		
		this.compound_button_code = compound_button_code;
		this.compound_button_on_code=new byte[0];
		this.compound_button_off_code=new byte[0];
	}
	
	public bluetooth_compound_button_data(String compound_button_text, byte[] compound_button_code,byte[] compound_button_on_code,byte[] compound_button_off_code) 
	{
		super();
	
		this.compound_button_text = compound_button_text;
		this.compound_button_code = compound_button_code;
		
		this.compound_button_off_code = compound_button_off_code;
		this.compound_button_on_code = compound_button_on_code;
	}
	
	public String get_compound_button_text() 
	{
		return compound_button_text;
	}

	public void set_compound_button_text(String compound_button_text) 
	{
		this.compound_button_text = compound_button_text;
	}

	public byte[] get_compound_button_code() 
	{
		return compound_button_code;
	}

	public void set_compound_button_code(byte[] compound_button_code)
	{
		this.compound_button_code = compound_button_code;
	}

	public byte[] get_compound_button_on_code() 
	{
		return compound_button_on_code;
	}

	public void set_compound_button_on_code(byte[] compound_button_on_code)
	{
		this.compound_button_on_code = compound_button_on_code;
	}
	
	public byte[] get_compound_button_off_code() 
	{
		return compound_button_off_code;
	}

	public void set_compound_button_off_code(byte[] compound_button_off_code) 
	{
		this.compound_button_off_code = compound_button_off_code;
	}
}
