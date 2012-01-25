package dx.xtremelabs.droidtooth.callbacks;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

/**
 * To be called whenever the DroidToothServer accepts an incoming connection.
 * To be used as a new instance, overriding the method that interests you and using this class's extra function to convert objects to classes. 
 * 
 * @author Dritan Xhabija
 *
 */
public abstract class DiscoveryCallback implements DTCallback {

	@Override
	public void callback() {//client code to do something when a device is found
	}

	/**
	 * We expect a new BluetoothSocket as a parameter, which identifies the newly paired device.
	 * @param object passed during callback, we expect it to be a BluetoothSocket for this callback.
	 */
	@Override
	public void callback(Object o) {}
	
	/**
	 * Return a BluetoothSocket from the generic object, if applicable 
	 * @param o object returend to callback
	 * @return a BluetoothSocket object
	 */
	public BluetoothSocket getIncomingSocketFromObject(Object o){
		
		return (BluetoothSocket) o;
	}
	
	public boolean discoveryStarted(Object o){
		return ((String)o).equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
	}
	
	public boolean discoveryFinished(Object o){
		return ((String)o).equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
	}
	
	

}
