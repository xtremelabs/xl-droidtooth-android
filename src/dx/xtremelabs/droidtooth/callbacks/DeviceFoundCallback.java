 package dx.xtremelabs.droidtooth.callbacks;

import dx.xtremelabs.droidtooth.common.Constants;
import dx.xtremelabs.droidtooth.common.FoundDevice;
import android.util.Log;

/**
 * To be used whenever developer wants to get notified when a device is found.
 * Provides more functionality than the default callback, for instance,
 * there is a function for returning a FoundDevice object from the passed-back Object o. 
 * 
 * @author Dritan Xhabija
 *
 */
public abstract class DeviceFoundCallback implements DTCallback {

	@Override
	public void callback() {
		//client code to do something when a device is found
		
	}

	/**
	 * We expect the FoundDevice object to be returned, but for safety's
	 * sake, we'll verify.
	 * @param object passed during callback, we expect it to be a FoudDevice for this callback.
	 */
	@Override
	public void callback(Object o) {
		FoundDevice device = getFoundDeviceFromObject(o);
		Log.d(Constants.DEBUG_DROIDTOOTH, "DeviceFoundCallback:: Found a device!: "+device.DEVICE_NAME+", "+device.DEVICE.getAddress());
		
	}
	
	public FoundDevice getFoundDeviceFromObject(Object o){
		if (!(o instanceof FoundDevice)){
			return null;
		}
		return (FoundDevice) o;
	}

}
