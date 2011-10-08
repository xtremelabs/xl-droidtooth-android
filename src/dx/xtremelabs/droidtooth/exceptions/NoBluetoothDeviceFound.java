package dx.xtremelabs.droidtooth.exceptions;

/**
 * Thrown if there is no Bluetooth adapter available running on mobile device.
 * 
 * @author Dritan Xhabija
 *
 */
public class NoBluetoothDeviceFound extends Exception {
	
	public NoBluetoothDeviceFound() { }
	public NoBluetoothDeviceFound(String message){
		super(message);
	}
	
	/**
	 * 
	 * Not being used in code atm, but it doesn't mean it can't be used when it's needed itf.
	 * @param reason
	 */
	public NoBluetoothDeviceFound(Throwable reason){
		super(reason);
	}

}
