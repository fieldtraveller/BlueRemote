package com.alex.blueremote;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class BT_spp implements Parcelable
{
	BluetoothDevice BT_Device;
	boolean isDiscovered=false,isDataTransferReady=false;

	//SPP UUID
    UUID BT_spp_uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothSocket BT_socket;
    
    OutputStream BT_outputStream;
    InputStream BT_inputStream;
    	
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
			BT_socket = BT_Device.createInsecureRfcommSocketToServiceRecord(BT_spp_uuid);
			
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
			this.BT_outputStream.write(input,offset,count);
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

	public boolean isDataTransferReady() 
	{
		return isDataTransferReady;
	}

	public void setDataTransferReady(boolean isdataTransferReady) 
	{
		isDataTransferReady = isdataTransferReady;
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

	
    protected BT_spp(Parcel in) {
        BT_Device = (BluetoothDevice) in.readValue(BluetoothDevice.class.getClassLoader());
        isDiscovered = in.readByte() != 0x00;
        isDataTransferReady = in.readByte() != 0x00;
        BT_spp_uuid = (UUID) in.readValue(UUID.class.getClassLoader());
        BT_socket = (BluetoothSocket) in.readValue(BluetoothSocket.class.getClassLoader());
        BT_outputStream = (OutputStream) in.readValue(OutputStream.class.getClassLoader());
        BT_inputStream = (InputStream) in.readValue(InputStream.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(BT_Device);
        dest.writeByte((byte) (isDiscovered ? 0x01 : 0x00));
        dest.writeByte((byte) (isDataTransferReady ? 0x01 : 0x00));
        dest.writeValue(BT_spp_uuid);
        dest.writeValue(BT_socket);
        dest.writeValue(BT_outputStream);
        dest.writeValue(BT_inputStream);
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
}
