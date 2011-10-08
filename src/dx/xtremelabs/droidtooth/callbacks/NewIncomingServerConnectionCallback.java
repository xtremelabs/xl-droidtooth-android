package dx.xtremelabs.droidtooth.callbacks;

import android.bluetooth.BluetoothSocket;

/**
 * To be called whenever the DroidToothServer accepts an incoming connection. 
 * 
 * @author Dritan Xhabija
 *
 */
public abstract class NewIncomingServerConnectionCallback implements DTCallback {

	@Override
	public void callback() {
		//client code to do something when a device is found
		
	}

	/**
	 * We expect a new BluetoothSocket as a parameter, which identifies the newly paired device.
	 * @param object passed during callback, we expect it to be a BluetoothSocket for this callback.
	 */
	@Override
	public void callback(Object o) {
		
	}
	
	/**
	 * Return a BluetoothSocket from the generic object, if applicable 
	 * @param o object returend to callback
	 * @return a BluetoothSocket object
	 */
	public BluetoothSocket getIncomingSocketFromObject(Object o){
		if (!(o instanceof BluetoothSocket)){
			return null;
		}
		return (BluetoothSocket) o;
	}

}
