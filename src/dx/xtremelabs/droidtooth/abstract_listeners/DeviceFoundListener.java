package dx.xtremelabs.droidtooth.abstract_listeners;

import java.util.ArrayList;
import java.util.Date;

import dx.xtremelabs.droidtooth.common.Constants;
import dx.xtremelabs.droidtooth.common.FoundDevice;


import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Listener for when a device is found while scanning.
 * 
 * @author Dritan Xhabija
 * 
 */
public abstract class DeviceFoundListener extends BroadcastReceiver {

	//array to hold all found devices this listener can sniff
	private ArrayList <FoundDevice> foundDevices = new ArrayList <FoundDevice> ();
	
	//onReceive is triggered for this listener class only when a device is found while scanning
	//if registered correctly in manifest. in that case, grab name of device and the device object
	//and take corresponding action.
	@Override
	public void onReceive(Context context, Intent intent) {
		String rname = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
		BluetoothDevice rdev = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		if (rname!=null && rdev!=null){

			Log.d(Constants.DEBUG_DROIDTOOTH, "FOUND DEVICE: "+rdev.getName()+", ["+rdev.getAddress()+"] and RNAME is: "+rname);
			FoundDevice deviceFound = new FoundDevice(rname, rdev); 
			foundDevices.add(deviceFound); //make sure this listener knows of every found device
			deviceDiscovered(deviceFound); //make sure any direct children of this class are made aware of the newly found device
		}
		
	}
	
	//allows a client class to get pinged back with a device 
	public abstract void deviceDiscovered(FoundDevice device);

	public ArrayList <FoundDevice> getLatestScanFoundDevices(){
		return foundDevices;
	}
	
	public void clearList(){
		foundDevices = new ArrayList <FoundDevice> ();
	}
	
	
}
