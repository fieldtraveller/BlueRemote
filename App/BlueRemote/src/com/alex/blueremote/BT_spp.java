package com.alex.blueremote;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class BT_spp 
{
	BluetoothAdapter BtAdapter;
	BluetoothDevice BT_device;
	
	//SPP UUID
    UUID BT_spp_uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothSocket BT_socket;
    
    OutputStream BT_outputStream;
    InputStream BT_inputStream;
    
    String BT_mac_address;
//    String BT_mac_address="98:D3:31:80:18:29";
    
    BT_spp(BluetoothAdapter BtAdapter,String BT_mac_address)
    {
    	this.BtAdapter=BtAdapter;
    	this.BT_mac_address=BT_mac_address;
    	
    	BT_device = BtAdapter.getRemoteDevice(BT_mac_address);

    	if(BT_device!=null)
    	{
    		Log.d("Bluetooth","BT Device Obtained.");
    	}
    	else
    	{
    		Log.d("Bluetooth","BT Device:Null.");
    	}
    	
    }
    
    public void connect()
    {
    	try 
    	{
			BT_socket = BT_device.createInsecureRfcommSocketToServiceRecord(BT_spp_uuid);
			
			if(BT_socket!=null)
	    	{
	        	Log.d("Bluetooth","Socket Created.");
	    	}
	        else
	        {
	        	Log.d("Bluetooth","Socket:Null.");
	        }
	    	
	    	BT_socket.connect();
	    	
	    	Log.d("Bluetooth","isConnected():"+BT_socket.isConnected());
	    	
	    	BT_outputStream = BT_socket.getOutputStream();
	    	BT_inputStream = BT_socket.getInputStream();
	    	
		} 
    	catch (IOException e)
    	{
			e.printStackTrace();
		}
    }
    
    public void write(int input)
    {
    	try 
    	{
			this.BT_outputStream.write(input);
		}
    	catch (IOException e) 
    	{
			e.printStackTrace();
		}
    }
    
    public void write(byte[] input)
    {
    	try 
    	{
			this.BT_outputStream.write(input);
		}
    	catch (IOException e) 
    	{
			e.printStackTrace();
		}
    }
    
    public void write(byte[] input,int offset,int count)
    {
    	try 
    	{
			this.BT_outputStream.write(input,offset,count);;
		}
    	catch (IOException e) 
    	{
			e.printStackTrace();
		}
    }
    
    public void disconnect()
    {
    	try 
    	{
			BT_outputStream.close();
			BT_inputStream.close();
		}
    	catch (IOException e) 
    	{
			e.printStackTrace();
		}
    	
    	if(BT_socket.isConnected())
		{
			try 
			{
				BT_socket.close();
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
    	
    }
    
}
