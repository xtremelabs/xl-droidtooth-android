package dx.xtremelabs.droidtooth.abstract_listeners;

import java.util.Date;

import dx.xtremelabs.droidtooth.common.Constants;
import dx.xtremelabs.droidtooth.main.DroidToothInstance;


import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Bluetooth discovery state listener for when discovery starts and finishes. 
 * 
 * @author Dritan Xhabija
 *
 */
public abstract class DiscoveryStateListener extends BroadcastReceiver {

	long lastDiscoveryTime = 0;
	String currentState;
	
	@Override
	public void onReceive(Context ctx, Intent intent) {
		if (intent.getAction().equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)){
			Log.d(Constants.DEBUG_DROIDTOOTH, "DroidTooth detected that Bluetooth discovery has started.");
			currentState = BluetoothAdapter.ACTION_DISCOVERY_STARTED;
			discoveryStarted();
			//clear the device listener of any previously found devices
			((DeviceFoundListener) DroidToothInstance.get().getDeviceFoundListener()).clearList();
		} else if (intent.getAction().equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)){ 
			Log.d(Constants.DEBUG_DROIDTOOTH, "DroidTooth detected that Bluetooth discovery has finished.");
			lastDiscoveryTime = new Date().getTime();
			currentState = BluetoothAdapter.ACTION_DISCOVERY_FINISHED;
			discoveryFinished();
		} 
	
	}
	
	public long getLastScanTime(){
		return lastDiscoveryTime;
	}
	
	public String getCurrentDiscoveryState(){
		return currentState;
	}
	
	public boolean hasDiscoveryFinished() {
		return currentState.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
	}
	public boolean hasDiscoveryStarted() {
		return currentState.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
	}
	
	public abstract void discoveryStarted();
	public abstract void discoveryFinished();
}
