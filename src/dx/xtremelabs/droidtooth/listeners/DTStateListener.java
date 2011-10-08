package dx.xtremelabs.droidtooth.listeners;

import dx.xtremelabs.droidtooth.abstract_listeners.BluetoothStateListener;
import dx.xtremelabs.droidtooth.callbacks.DTCallback;
import android.content.Context;
import android.content.Intent;

/**
 * Listener for when Bluetooth changes states to "turning on, on, turning off, off". 
 * Calls back any clients interested in these states.
 * 
 * @author Dritan Xhabija
 *
 */
public class DTStateListener extends BluetoothStateListener {

	private DTCallback bluetoothIsTurningOnCallback, bluetoothIsOnCallback, bluetoothIsTurningOffCallback, bluetoothIsOffCallback;
	
	/**
	 * For each method below, check for any associated callbacks and if they exist, callback! 
	 */
	
	public void bluetoothIsTurningOn() {
		if (bluetoothIsTurningOnCallback!=null){
			bluetoothIsTurningOnCallback.callback();
		}
	}

	public void bluetoothIsOn() {
		if (bluetoothIsOnCallback!=null){
			bluetoothIsOnCallback.callback();
		}
		
	}
	public void bluetoothIsTurningOff() {
		if (bluetoothIsTurningOffCallback!=null){
			bluetoothIsTurningOffCallback.callback();
		}
		
	}

	public void bluetoothIsOff() {
		if (bluetoothIsOffCallback!=null){
			bluetoothIsOffCallback.callback();
		}
		
	}
	
	public DTCallback getBluetoothIsTurningOnCallback() {
		return bluetoothIsTurningOnCallback;
	}

	public void setBluetoothIsTurningOnCallback(
			DTCallback bluetoothIsTurningOnCallback) {
		this.bluetoothIsTurningOnCallback = bluetoothIsTurningOnCallback;
	}

	public DTCallback getBluetoothIsOnCallback() {
		return bluetoothIsOnCallback;
	}

	public void setBluetoothIsOnCallback(DTCallback bluetoothIsOnCallback) {
		this.bluetoothIsOnCallback = bluetoothIsOnCallback;
	}

	public DTCallback getBluetoothIsTurningOffCallback() {
		return bluetoothIsTurningOffCallback;
	}

	public void setBluetoothIsTurningOffCallback(
			DTCallback bluetoothIsTurningOffCallback) {
		this.bluetoothIsTurningOffCallback = bluetoothIsTurningOffCallback;
	}

	public DTCallback getBluetoothIsOffCallback() {
		return bluetoothIsOffCallback;
	}

	public void setBluetoothIsOffCallback(DTCallback bluetoothIsOffCallback) {
		this.bluetoothIsOffCallback = bluetoothIsOffCallback;
	}

}
