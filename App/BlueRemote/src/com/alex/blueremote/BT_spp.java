package com.alex.blueremote;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

public class BT_spp implements Parcelable
{
	BluetoothDevice BT_Device;
	boolean isDiscovered=false;
	
	device_connection_status_interface dcsi;
	device_write_interface dwi;
	device_read_interface dri;
	
	//SPP UUID
    final static UUID BT_spp_uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
   
    BluetoothSocket BT_socket;
    OutputStream BT_outputStream;
    InputStream BT_inputStream;
    
    static Handler ui_handler;
	
    int number_of_components_using_this_object=0;
    
    BT_spp(BluetoothDevice BT_Device,boolean isDiscovered)
	{
		this.BT_Device=BT_Device;
		this.isDiscovered = isDiscovered;
	}
    
    BT_spp(BluetoothAdapter BtAdapter,String BT_mac_address)
    {
    	BT_Device = BtAdapter.getRemoteDevice(BT_mac_address);

    	if(BT_Device!=null)
    	{
//    		Log.d("Bluetooth","BT Device Obtained.");
    	}
    	else
    	{
//    		Log.d("Bluetooth","BT Device:Null.");
    	}
    }

    public void connect() 
    {
    	Thread Connect_thread = new Thread(){
    		public void run() 
    	    {	
    	    	if(BT_socket==null)
    			{
    	    		try 
    	    		{
    					BT_socket = BT_Device.createInsecureRfcommSocketToServiceRecord(BT_spp_uuid);
    				} 
    	    		catch (IOException e) 
    	    		{
//    					e.printStackTrace();
    				}
    			}
    	    	
    	    	if(BT_socket!=null)
    	        {
//    	           	Log.d("Bluetooth","Socket Created.");
    	    			
    	    		if(BT_socket.isConnected()==false)
    	    		{
    	    			try 
    	    			{
    	    				BT_socket.connect();
    	    				
//    	        	    	Log.d("Bluetooth","isConnected():"+BT_socket.isConnected());
    	    			} 
    	    			catch (IOException e1) 
    	    			{
//    	    				e1.printStackTrace();
    	    			}
    	    		}
    	        }
    	        else
    	        {
//    	           	Log.d("Bluetooth","Socket:Null.");
    	        }
    	    	
    	    	if(BT_socket.isConnected()==true)
    	    	{
    	    		if(BT_outputStream==null)
    	    		{
    	    			try 
            		    {
    	    				BT_outputStream = BT_socket.getOutputStream();
            			}
            		    catch (IOException e) 
            		    {
            				e.printStackTrace();
            			}
    	    		}
    	    		
    	    		if(BT_inputStream==null)
    	    		{
    	    			try 
            		    {
            	    		BT_inputStream = BT_socket.getInputStream();
            			}
            		    catch (IOException e) 
            		    {
            				e.printStackTrace();
            			}
    	    		}
    	    	}
    	    	
       	    	if(BT_spp.ui_handler!=null)
   	    		{
   	    			if((BT_socket==null)||(BT_socket.isConnected()==false))
       	        	{
   	    				dcsi.on_device_connection_fail();
   	    				
   	    				ui_handler.post(new Runnable(){
    	
       	          	    	@Override
    						public void run() {
    							Toast.makeText(BlueRemote.getContext(), "Unable To Connect To Device.", Toast.LENGTH_LONG).show();
    						}
        	        			
        	        	});
        	        }
        	       	else
        	       	{
        	       		dcsi.on_device_connection_pass();
        	       		
        	       		Thread device_input_reader_thread=new Thread(){
   	    					
   	    					public void run()
   	    					{
   	    						try 
	    						{
   	    							while(BT_socket.isConnected())
   	   	    						{
   	   	    							if(BT_inputStream.available()>0)
   										{
   											read(new byte[1024]);
   										}
   										else
   										{
   											Thread.sleep(100);
   										}
   	   	    						}
	    						}
   	    						catch (IOException e) 
  	    						{
									e.printStackTrace();
								} 
  	    						catch (InterruptedException e) 
  	    						{
									e.printStackTrace();
								}
   	    					}
   	    				};
   	    				
//   	    				device_input_reader_thread.start();
   	    				
        	       		ui_handler.post(new Runnable(){

        	       	    	@Override
    						public void run() {
        	       	    		Toast.makeText(BlueRemote.getContext(), "Device Connected", Toast.LENGTH_SHORT).show();
    						}
        	        			
        	      		});
        	       	}
    	   		}
    		}
    	};
    	
    	Connect_thread.start();
    }
    
    public byte read()
    {
    	byte return_value = 0;
    	try 
    	{
    		return_value=(byte) this.BT_inputStream.read();
		}
    	catch (IOException e) 
    	{
			e.printStackTrace();
		}
    	
    	dri.on_device_read();
    	dri.on_device_read(this.BT_Device.getName()+"<:",return_value,"");
    	
		return return_value;
    }
    
    public int read(byte[] buffer)
    {
    	int return_value=0;
    	try 
    	{
    		return_value=this.BT_inputStream.read(buffer);
		}
    	catch (IOException e) 
    	{
			e.printStackTrace();
		}
		
    	dri.on_device_read();
    	dri.on_device_read(this.BT_Device.getName()+"<:",buffer,return_value,"");
    	
    	return return_value;
    }
    
    public int read(byte[] buffer, int byteOffset, int byteCount)
    {
    	int return_value=0;
    	try 
    	{
    		return_value=this.BT_inputStream.read(buffer, byteOffset, byteCount);
		}
    	catch (IOException e) 
    	{
			e.printStackTrace();
		}
		
    	dri.on_device_read();
    	dri.on_device_read(this.BT_Device.getName()+"<:",buffer,return_value,"");
    	
    	return return_value;
    }
    
    public void write(byte input)
    {
    	try 
    	{
			this.BT_outputStream.write(input);
		}
    	catch (IOException e) 
    	{
			e.printStackTrace();
		}
    	
    	dwi.on_device_write();
    	dwi.on_device_write(this.BT_Device.getName()+">: ",input,"");
    }
    
    public void write(byte[] input)
    {
    	if(input.length>0)
    	{
    		try 
        	{
        		this.BT_outputStream.write(input);
    		}
        	catch (IOException e) 
        	{
    			e.printStackTrace();
    		}
    		
    		dwi.on_device_write();
    		dwi.on_device_write(this.BT_Device.getName()+">: ",input,"");
    	}
    }
    
    public void write(byte[] input,int offset,int count)
    {
    	if(input.length>0)
    	{
    		try 
        	{
    			this.BT_outputStream.write(input,offset,count);
    		}
        	catch (IOException e) 
        	{
    			e.printStackTrace();
    		}
    		
    		dwi.on_device_write();
    		dwi.on_device_write(this.BT_Device.getName()+">: ",input,"");
    	}
    }
    
    public void disconnect()
    {
    	Thread Disconnect_thread = new Thread(){
    		
    		public void run()
    		{
    			try 
    	    	{
    				BT_outputStream.close();
    				BT_inputStream.close();
    			
    				if(BT_socket.isConnected())
    				{
    					BT_socket.close();
    				}
    			}
    			catch (IOException e) 
				{
					e.printStackTrace();
				}
    			
    			Log.w("","Device Disconnected");
    		}
    	};
    	
    	Disconnect_thread.start();
    }
    
	public boolean isDiscovered() 
	{
		 if (BT_Device == null) 
	     {
				throw new NullPointerException("No BlueTooth Device Assigned."); 
	     }
		 
		 return this.isDiscovered;
	 }

	public void setDiscovered(boolean isDiscovered) 
	{
		if (BT_Device == null) 
        {
			throw new NullPointerException("No BlueTooth Device Assigned."); 
        }
		
		this.isDiscovered = isDiscovered;
	}

	void used_by_new_component()
    {
    	this.number_of_components_using_this_object++;
    }
    
    void not_used_by_a_component()
    {
    	this.number_of_components_using_this_object--;
    }
    
    int get_number_of_components_using_this_object()
    {
    	return number_of_components_using_this_object;
    }
    
    public static ArrayList<Integer> get_indices(ArrayList<BT_spp> master_list,ArrayList<BT_spp> component_list)
    {
    	ArrayList<Integer> return_value=new ArrayList<Integer>();
    	
    	int number_of_iterations=component_list.size();
    	int number_of_devices_in_master_list=master_list.size();
    	
    	for (int count_0=0;count_0<number_of_iterations;count_0++)
    	{
    		for(int count_1=0;count_1<number_of_devices_in_master_list;count_1++)
        	{
        		if(master_list.get(count_1).equals(component_list.get(count_0)))
        		{
        			return_value.add(count_1);
        		}
        	}
    	}
    	
    	return return_value;
    }
    
    public static void update_list_based_on_indices(ArrayList<BT_spp> master_list,ArrayList<BT_spp> component_list,ArrayList<Integer> input_indices)
    {
    	int number_of_iterations=component_list.size();
    	for(int count=0;count<number_of_iterations;count++)
    	{
    		component_list.remove(0);
    	}
    	
    	Log.e("","number_of_iterations:"+number_of_iterations+"\n input_indices:"+input_indices);
    	number_of_iterations=input_indices.size();
    	for(int count=0;count<number_of_iterations;count++)
    	{
    		component_list.add(master_list.get(input_indices.get(count).intValue()));
    	}
    }
    
    public String toString()  
	 {
         if (BT_Device != null) 
         {
            return (BT_Device.getName()+"\n"+BT_Device.getAddress());
         }
         else
         {
        	throw new NullPointerException("No BlueTooth Device Assigned."); 
         }
    }
	 
	public boolean equals(BT_spp newDevice)
	{
		if (BT_Device == null) 
        {
			throw new NullPointerException("No BlueTooth Device Assigned."); 
        }
		
		if((this.BT_Device.getAddress().compareTo(newDevice.BT_Device.getAddress()))==0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public boolean equals(BluetoothDevice newDevice)
	{
		if (BT_Device == null) 
        {
			throw new NullPointerException("No BlueTooth Device Assigned."); 
        }
		
		if((this.BT_Device.getAddress().compareTo(newDevice.getAddress()))==0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public boolean equals(Object input_object)
    {
        if (input_object == null) 
        {
        	return false;
        }
        
        if (input_object == this) 
        {
        	return true; //if both pointing towards same object on heap
        }
        
        if(input_object instanceof BluetoothDevice)
        {
        	return equals((BluetoothDevice)input_object);
        }

//        BT_spp output_object = (BT_spp)input_object;
        
        return equals((BT_spp)input_object);
    }
	
    protected BT_spp(Parcel in) {
        BT_Device = (BluetoothDevice) in.readValue(BluetoothDevice.class.getClassLoader());
//        isDiscovered = in.readByte() != 0x00;
//        BT_socket = (BluetoothSocket) in.readValue(BluetoothSocket.class.getClassLoader());
//        BT_outputStream = (OutputStream) in.readValue(OutputStream.class.getClassLoader());
//        BT_inputStream = (InputStream) in.readValue(InputStream.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(BT_Device);
//        dest.writeByte((byte) (isDiscovered ? 0x01 : 0x00));
//        dest.writeValue(BT_socket);
//        dest.writeValue(BT_outputStream);
//        dest.writeValue(BT_inputStream);
    }

    public static final Parcelable.Creator<BT_spp> CREATOR = new Parcelable.Creator<BT_spp>() {
        @Override
        public BT_spp createFromParcel(Parcel in) {
            return new BT_spp(in);
        }

        @Override
        public BT_spp[] newArray(int size) {
            return new BT_spp[size];
        }
    };
    
    public interface device_connection_status_interface
    {
    	public void on_device_connection_pass();
    	public void on_device_connection_pass(BT_spp passed_device);
    	
    	public void on_device_connection_fail();
    	public void on_device_connection_fail(BT_spp failed_device);
    }
    
    public interface device_write_interface
    {
    	public void on_device_write();
    	public void on_device_write(String start_text,byte written_byte,String end_text);
    	public void on_device_write(String start_text,byte[] written_bytes,String end_text);	
    }
    
    public interface device_read_interface
    {
    	public void on_device_read();
    	public void on_device_read(String start_text,byte read_byte,String end_text);
    	public void on_device_read(String start_text,byte[] read_bytes,int number_of_bytes_read,String end_text);	
    }
}
