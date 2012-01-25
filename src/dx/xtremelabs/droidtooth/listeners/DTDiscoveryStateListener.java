package dx.xtremelabs.droidtooth.listeners;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import dx.xtremelabs.droidtooth.abstract_listeners.DiscoveryStateListener;
import dx.xtremelabs.droidtooth.callbacks.DTCallback;
import dx.xtremelabs.droidtooth.main.DroidToothInstance;

/**
 * Issue callbacks to clients interested in knowing the states of 
 * this listener which rides on DiscoveryStateListener doing the grunt work.
 * Simply extend the appropriate abstract classes and set them in DroidToothInstance
 * @author Dritan Xhabija
 *
 */
public class DTDiscoveryStateListener extends DiscoveryStateListener {

	private DTCallback discoveryStartedCallback, discoveryFinishedCallback;
	

	public void discoveryStarted() {
		//once the discovery starts, we want our parent to have a fresh new list
		//to keep track of currently found devices.
		DroidToothInstance.get().getDeviceFoundListener().clearList();

		if (discoveryStartedCallback!=null){
			discoveryStartedCallback.callback();
			discoveryStartedCallback.callback(getCurrentDiscoveryState());
		}
	}

	public void discoveryFinished() {
		if (discoveryFinishedCallback!=null){
			discoveryFinishedCallback.callback();
			discoveryStartedCallback.callback(getCurrentDiscoveryState());
		}
	}

	public DTCallback getDiscoveryStartedCallback() {
		return discoveryStartedCallback;
	}

	public void setDiscoveryStartedCallback(DTCallback discoveryStartedCallback) {
		this.discoveryStartedCallback = discoveryStartedCallback;
	}


	public DTCallback getDiscoveryFinishedCallback() {
		return discoveryFinishedCallback;
	}

	public void setDiscoveryFinishedCallback(DTCallback discoveryFinishedCallback) {
		this.discoveryFinishedCallback = discoveryFinishedCallback;
	}
}
