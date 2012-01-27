package dx.xtremelabs.droidtooth.common;

import java.util.Date;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

/**
 * 
 * Wrapper object for holding information about any devices found while discovering.
 * To be accessed as a property container. Example use:
 * 
 * 	for (FoundDevice device : allDevices){
 * 		Log.d("DroidTooth", device.DEVICE_NAME+" [ "+device.DEVICE.getAddress()+" ] ");
 * }  
 * 
 * @author Dritan Xhabija
 *
 */
public class FoundDevice {

	final public String DEVICE_NAME;
	final public String DEVICE_MAC;
	final public Date DEVICE_FOUND_TIME;
	final public BluetoothDevice DEVICE;
	private BluetoothSocket socket;
	
	/**
	 * Wrapper for these objects as well as a timestamp of creation, which is assumed
	 * to happen whenever a new device is found via a device found listener.
	 * @param deviceName name returned from intent of found device
	 * @param device object representing, essentially a filedescriptor.  
	 */
	public FoundDevice(String deviceName, BluetoothDevice device){
		DEVICE_NAME = deviceName;
		DEVICE_MAC = device.getAddress();
		DEVICE = device;
		DEVICE_FOUND_TIME = new Date(); 
	}
	
	public void setSocket(BluetoothSocket socket){
		this.socket = socket;
	}
	
	public BluetoothSocket getSocket(){
		return socket;
	}
}
