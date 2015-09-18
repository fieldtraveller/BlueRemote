package bluetooth_helper;

import android.bluetooth.BluetoothDevice;

public class custom_BluetoothDevice {
	
	BluetoothDevice BT_Device;
	boolean isDiscovered;
	
	custom_BluetoothDevice(BluetoothDevice BT_Device)
	{
		this.BT_Device=BT_Device;
		this.isDiscovered = false;
	}
	
	custom_BluetoothDevice(BluetoothDevice BT_Device,boolean isDiscovered)
	{
		this.BT_Device=BT_Device;
		this.isDiscovered = isDiscovered;
	}
	
	public boolean isDiscovered() 
	{
		 if (BT_Device == null) 
	     {
				throw new NullPointerException("No BlueTooth Device Assigned."); 
	     }
		 
		 return isDiscovered;
	 }

	public void setDiscovered(boolean isDiscovered) 
	{
		if (BT_Device == null) 
        {
			throw new NullPointerException("No BlueTooth Device Assigned."); 
        }
		
		this.isDiscovered = isDiscovered;
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
	 
	public boolean equals(custom_BluetoothDevice newDevice)
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

}
