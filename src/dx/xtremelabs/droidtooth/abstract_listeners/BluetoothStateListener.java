package dx.xtremelabs.droidtooth.abstract_listeners;

import dx.xtremelabs.droidtooth.common.Constants;
import dx.xtremelabs.droidtooth.main.DroidToothInstance;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * This class hooks into the "queue" of system-wide callbacks for whenever 
 * Bluetooth state changes. (So long as it's defined in the manifest).
 * 
 * Ensure that DroidTooth instance is updated of the latest state.
 *   
 * @author Dritan Xhabija
 *
 */
public abstract class BluetoothStateListener extends BroadcastReceiver {

	//holds latest Bluetooth state
	private int currentState = 0;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
		int pstate = intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_STATE, -1);

		switch (state){
		case BluetoothAdapter.STATE_TURNING_ON:
			Log.d(Constants.DEBUG_DROIDTOOTH, "DroidTooth detected Bluetooth is turning on...");
			bluetoothIsTurningOn();
			currentState = BluetoothAdapter.STATE_TURNING_ON;
			break;
		case BluetoothAdapter.STATE_ON:
			Log.d(Constants.DEBUG_DROIDTOOTH, "DroidTooth detected Bluetooth is fully ON.");
			bluetoothIsOn();
			currentState = BluetoothAdapter.STATE_ON;
			break;
		case BluetoothAdapter.STATE_TURNING_OFF:
			Log.d(Constants.DEBUG_DROIDTOOTH, "DroidTooth detected Bluetooth is turning off...");
			bluetoothIsTurningOff();
			currentState = BluetoothAdapter.STATE_TURNING_OFF;
			break;
		case BluetoothAdapter.STATE_OFF:
			Log.d(Constants.DEBUG_DROIDTOOTH, "DroidTooth detected Bluetooth is fully OFF.");
			bluetoothIsOff();
			currentState = BluetoothAdapter.STATE_OFF;
			break;
		}
	}

	public int getCurrentState(){
		return currentState;
	}
	
	//methods for children classes to use to know or do something at these states
	public abstract void bluetoothIsTurningOn();
	public abstract void bluetoothIsOn();
	public abstract void bluetoothIsTurningOff();
	public abstract void bluetoothIsOff();
	
}
