package dx.xtremelabs.droidtooth.listeners;

import java.util.HashMap;

import dx.xtremelabs.droidtooth.abstract_listeners.DeviceFoundListener;
import dx.xtremelabs.droidtooth.callbacks.DTCallback;
import dx.xtremelabs.droidtooth.common.FoundDevice;

/**
 * A generic listener for whenever a new device is found while scanning. Calls back any clients
 * interested in knowing about this listener's events.
 * 
 * @author Dritan Xhabija
 *
 */
public class DTDeviceFoundListener extends DeviceFoundListener {

	private DTCallback deviceFoundCallback;
	//keep track of all devices seen thus far, associated by their MAC address
	private HashMap <String, FoundDevice> allDevicesSeen = new HashMap <String, FoundDevice>();
	
	@Override
	public void deviceDiscovered(FoundDevice device) {
		//issue a callback to application who wanted to know about this event
		if (deviceFoundCallback!=null){
			deviceFoundCallback.callback(device);
		}
		//automatically override any already-seen devices on this list
		//this ensures that all seen devices on this list are of the latest timestamp
		allDevicesSeen.put(device.DEVICE_MAC, device);
		
		//for optimization purposes, a cap of 500 devices in our mapping
		//of all unique devices should be more than enough for general purpose
		//out of the box usage. 
		if (allDevicesSeen.size()>500){
			clearAllDevicesSeen();
		}
		
	}

	public HashMap <String, FoundDevice> getAllDevicesSeen(){
		return allDevicesSeen;
	}
	
	/**
	 * Use a callback to keep of devices in your own class, 
	 * do not make this method public, to get the devices just found
	 * in the latest scan, use getLatestScanFoundDevices()
	 */
	private void clearAllDevicesSeen(){
		allDevicesSeen = new HashMap <String, FoundDevice>();
		
	}
	//callback classes
	public DTCallback getDeviceFoundCallback() {
		return deviceFoundCallback;
	}

	public void setOnDeviceFoundCallback(DTCallback deviceFoundCalback) {
		this.deviceFoundCallback = deviceFoundCalback;
	}
}
