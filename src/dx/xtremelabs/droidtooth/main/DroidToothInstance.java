package dx.xtremelabs.droidtooth.main;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.IBluetooth;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.RemoteException;
import android.widget.Toast;
import dx.xtremelabs.droidtooth.DroidToothActivity;
import dx.xtremelabs.droidtooth.callbacks.DTCallback;
import dx.xtremelabs.droidtooth.listeners.DTDeviceFoundListener;
import dx.xtremelabs.droidtooth.listeners.DTDiscoveryStateListener;
import dx.xtremelabs.droidtooth.listeners.DTStateListener;
import dx.xtremelabs.droidtooth.runners.DiscoverabilityRunner;
import dx.xtremelabs.droidtooth.runners.ListenForMessageRunner;
import dx.xtremelabs.droidtooth.runners.Runner;

/**
 * The all-knowing DroidToothInstance singleton which deals with all low-level
 * Bluetooth functionality. This class is a core component of this library and
 * can be used externally outside of DroidTooth class.
 * 
 * @author Dritan Xhabija
 * 
 */
public class DroidToothInstance {

	// possible return values from startDiscovery()
	public static final int DISCOVERY_ADAPTER_NULL = 0;
	public static final int DISCOVERY_ALREADY_ONGOING = 1;
	public static final int DISCOVERY_STARTED = 2;
	public static final int DISCOVERY_RESTARTED = 3;

	// Bluetooth adapter
	BluetoothAdapter droidTooth;

	private Activity activity; // reference to the activity of application

	public Activity getActivity() {
		return activity;
	}

	public DroidToothActivity getDActivity() {
		return (DroidToothActivity) activity;
	}

	// A local trigger without having an instantiated adapter to check whether
	private boolean isScanning = false;

	//a boolean set when someone wants their device to remain discoverable,
	//and checked throughout the various listeners for whether to turn off
	//discoverability based on user's input
	private boolean becomeDiscoverableIndefinitely = false;
	
	// The asumption is that Bluetooth (BT) is off prior to using this app.
	// If detected that BT was ON already, it will leave it ON when it finishes.
	private boolean bluetoothPreviouslyOn = false;

	// various Bluetooth state listeners for when BT is turning on/ff, is
	// on/off,
	// started/stopped discovery, and found device while scanning.
	private BroadcastReceiver bluetoothStateListener, deviceFoundListener,
			discoveryStateListener;

	// server object for handling incoming connections
	private DroidToothServer droidToothServer;

	// has a mapping of found host devices (from a scanning client) and after
	// successfully establishing pairing, to the socket connection.
	private HashMap<BluetoothDevice, BluetoothSocket> deviceToSocketMap = new HashMap<BluetoothDevice, BluetoothSocket>();

	// a thread class for handling discoverability
	private Runner discoverabilityRunner;

	private Runner listenForMessageRunner;

	// private holder of instance, don't want it swapped now!
	private static DroidToothInstance instance;

	/**
	 * To be called first thing in the morning! Then subsequent calls to
	 * getInstance() are guaranteed to provide a non-null instance. This avoids
	 * the null-checker statement every time getInstance() is called.
	 * 
	 * @param activity
	 *            main activity onto which DroidTooth will register listeners.
	 */
	public static void initInstance(Activity activity) {
		instance = new DroidToothInstance(activity);
	}

	/**
	 * If initInstance(Activity) is called first, this method will always return
	 * an instance of DroidToothInstance which contains a variety of mid-low
	 * level Bluetooth functions. The assumption is that at this level of
	 * access, the developer is responsible for having called initInstance()
	 * before attempting to get an instance.
	 * 
	 * @return the instance if instantiated, else null.
	 */
	public static DroidToothInstance get() {
		return instance;
	}

	public DroidToothInstance(Activity activity) {
		this.activity = activity;

		// listen for all things concerning Bluetooth
		initToothListeners();
	}

	/**
	 * Obviously, initialize the listeners for the tooth. Each listener handles
	 * their corresponding tasks associated with the various event triggers of
	 * the Droid Ttooth (Bluetooth).
	 * 
	 * To entirely change the way any of DroidTooth's listeners work, simply
	 * extend one of the abstract listeners in
	 * dx.xtremelabs.droidtooth.abstract_listeners, and override the onReceive()
	 * method, but be sure to call super on it so that DroidTooth is also made
	 * aware of those events as they happen.
	 * 
	 */
	private void initToothListeners() {
		// listen for whenever the state of Bluetooth changes (turning on, on,
		// turning off, off)
		bluetoothStateListener = new DTStateListener();
		// grab a common listener for both the starting and stopping of scanning
		discoveryStateListener = new DTDiscoveryStateListener();
		// finally make sure we are made aware whenever a device is found while
		// scanning in progress
		deviceFoundListener = new DTDeviceFoundListener();
		// register these listeners into the system
		registerListeners();
	}

	/**
	 * If required to re-initialize existing listeners after a call
	 * unregisterListeners() call this method to put the listeners in the
	 * android system.
	 */
	public void registerListeners() {
		activity.registerReceiver(bluetoothStateListener, new IntentFilter(
				BluetoothAdapter.ACTION_STATE_CHANGED));
		activity.registerReceiver(discoveryStateListener, new IntentFilter(
				BluetoothAdapter.ACTION_DISCOVERY_STARTED));
		activity.registerReceiver(discoveryStateListener, new IntentFilter(
				BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
		activity.registerReceiver(deviceFoundListener, new IntentFilter(
				BluetoothDevice.ACTION_FOUND));

	}

	/**
	 * Unregister listeners when Bluetooth turns off.
	 */
	public void unregisterListeners() {
		activity.unregisterReceiver(bluetoothStateListener);
		activity.unregisterReceiver(discoveryStateListener);
		activity.unregisterReceiver(deviceFoundListener);
	}

	/**
	 * Return the current Bluetooth broadcasting name.
	 * 
	 * @return the current Bluetooth broadcasting name.
	 */
	public String getDeviceName() {
		return droidTooth.getName();
	}

	/**
	 * Set a new Bluetooth device name.
	 * 
	 * @param broadcastName
	 */
	public void setDeviceName(String broadcastName) {
		droidTooth.setName(broadcastName);
	}

	/**
	 * Return the current Bluetooth broadcasting MAC address.
	 * 
	 * @return the current Bluetooth broadcasting MAC address.
	 */
	public String getDeviceAddress() {
		return droidTooth.getAddress();
	}

	/**
	 * Return the registered listener DTBluetoothStateListener which listens for
	 * when Bluetooth is turning on, is on, turning off, is off.
	 * 
	 * @return the instantiated instance of DTBluetoothStateListener.
	 */
	public DTStateListener getBluetoothStateListener() {
		return (DTStateListener) bluetoothStateListener;
	}

	/**
	 * Return the registered listener DTDeviceFoundListener which listens and
	 * takes action for when a new device is found while scanning.
	 * 
	 * @return the instantiated DTDeviceFoundListener object.
	 */
	public DTDeviceFoundListener getDeviceFoundListener() {
		return (DTDeviceFoundListener) deviceFoundListener;
	}

	/**
	 * Return the registered listener DTDiscoveryStateListener which listens and
	 * takes action for when discovery starts and stops.
	 * 
	 * @return the instantiated DTDiscoveryStateListener object.
	 */
	public DTDiscoveryStateListener getDiscoveryStateListener() {
		return (DTDiscoveryStateListener) discoveryStateListener;
	}

	/**
	 * Use the following setter-methods to change the way DroidTooth works by
	 * simply riding on the parent abstract classes and implement your own
	 * functionality rather than issuing callbacks to interested clients.
	 * 
	 * @param the
	 *            associated listeners cast up to parent BroadcastReceiver
	 *            objects.
	 */

	public void setBluetoothStateListener(
			BroadcastReceiver bluetoothStateListener) {
		this.bluetoothStateListener = bluetoothStateListener;
	}

	public void setDeviceFoundListener(BroadcastReceiver deviceFoundListener) {
		this.deviceFoundListener = deviceFoundListener;
	}

	public void setDiscoveryStateListener(
			BroadcastReceiver discoveryStateListener) {
		this.discoveryStateListener = discoveryStateListener;
	}

	/********************************************************************/

	/**
	 * Return a new DroidToothServer instance thread used to handle incoming
	 * connections.
	 * 
	 * @return
	 */
	public DroidToothServer getDroidToothServer() {
		return droidToothServer == null || droidToothServer.wasRunning() ? 
																			droidToothServer = new DroidToothServer(droidTooth)
																		 : 
																			droidToothServer;
	}

	/**
	 * Experimentation attempts to open simultaneous sockets.
	 * 
	 * @return
	 */
	public DroidToothServer getNewDroidToothServer() {
		return new DroidToothServer(droidTooth);
	}

	/**
	 * Method for returning the Bluetooth interface for fetching paired devices.
	 * See android.bluetooth.IBluetooth*.aidl See
	 * http://stackoverflow.com/questions
	 * /3462968/how-to-unpair-bluetooth-device-using-android-2-1-sdk
	 * 
	 * @return
	 */
	public IBluetooth getIBluetooth() {
		IBluetooth ibt = null;

		try {
			Class c2 = Class.forName("android.os.ServiceManager");
			java.lang.reflect.Method m2 = c2.getDeclaredMethod("getService",
					String.class);
			android.os.IBinder b = (android.os.IBinder) m2.invoke(null,
					"bluetooth");
			Class c3 = Class.forName("android.bluetooth.IBluetooth");
			Class[] s2 = c3.getDeclaredClasses();
			Class c = s2[0];
			java.lang.reflect.Method m = c.getDeclaredMethod("asInterface",
					android.os.IBinder.class);
			m.setAccessible(true);
			ibt = (IBluetooth) m.invoke(null, b);
		} catch (Exception e) {
			return null;
		}

		return ibt;
	}

	/**
	 * Try to unpair with a device specified by the MAC.
	 * @param MAC address of already paired device.
	 * @return true whether unpairing was successful.
	 */
	public boolean unpairDevice(String MAC){
		IBluetooth ib = DroidToothInstance.get().getIBluetooth();
		try {
			ib.removeBond(MAC);
			return true;
		} catch (RemoteException e) {
			return false;
		}
	}
	/**
	 * Notify some callback class whenever a new device is a found.
	 * 
	 * @param callback
	 */
	public void setOnDeviceFoundCallback(DTCallback callback) {
		((DTDeviceFoundListener) deviceFoundListener)
				.setOnDeviceFoundCallback(callback);
	}

	public void setDiscoveryStartedCallback(DTCallback callback) {
		((DTDiscoveryStateListener) discoveryStateListener)
				.setDiscoveryStartedCallback(callback);
	}
	
	public void setDiscoveryFinishedCallback(DTCallback callback) {
		((DTDiscoveryStateListener) discoveryStateListener)
				.setDiscoveryFinishedCallback(callback);
	}
	
	

	/**
	 * Return the last updated status of the Bluetooth device.
	 * 
	 * @return the state as last updated from the device-state listener.
	 */
	public int getBluetoothDeviceState() {
		return getBluetoothStateListener().getCurrentState();
	}

	/**
	 * Return whether the Bluetooth device is On.
	 * 
	 * @return True if BT is on, else False.
	 */
	public boolean isOn() {
		return droidTooth != null && droidTooth.isEnabled();
	}

	/**
	 * Return whether the Bluetooth device is currently scanning.
	 * 
	 * @return True if the device is scanning, else False.
	 */
	public boolean isDiscovering() {
		return droidTooth.isDiscovering();
	}

	/**
	 * Return the current discovery state, discovering or not.
	 * 
	 * @return the last discovery state, started or finished.
	 */
	public String getDiscoveryState() {
		return getDiscoveryStateListener().getCurrentDiscoveryState();
	}

	/**
	 * Return whether or not the mobile device running has a Bluetooth adapter.
	 * 
	 * @return True if Bluetooth exists, False otherwise.
	 */
	public boolean doesBluetoothExist() {
		if (droidTooth == null) {
			droidTooth = BluetoothAdapter.getDefaultAdapter();
		}

		// if null then running really old version of sdk (such as v2.0)
		return droidTooth != null;
	}

	/**
	 * Return a set of all bonded devices to this adapter.
	 * 
	 * @return
	 */
	public Set<BluetoothDevice> getBondedDevices() {
		return droidTooth.getBondedDevices();
	}

	public BluetoothDevice getDeviceByMAC(String MAC) {
		return droidTooth.getRemoteDevice(MAC);
	}

	/**
	 * Initialize the Bluetooth adapter for use. If Bluetooth is not ON, it will
	 * be turned on and BluetoothStateListener will known of the BT state.
	 * 
	 * @return whether Bluetooth could be initialized.
	 */
	public boolean initBluetooth() {

		if (!doesBluetoothExist()) {
			return false;
		}
		// ensure Bluetooth is enabled, at which point BluetoothStateListener
		// would know what to do.
		if (!droidTooth.isEnabled()) {
			droidTooth.enable();
		} else {
			bluetoothPreviouslyOn = true; // Bluetooth device is already ON,
											// keep this state for when we're
											// done.
		}

		return true;
	}

	/**
	 * Start the discovery process of any nearby visible devices. Return whether
	 * discovery was possible. Prevent attempts to issue a re-discover if device
	 * is already discovering. It is up to the developer to utilize the
	 * DiscoveryStateListener to know when to issue the next available time for
	 * discovery, if required.
	 * 
	 * @param forceRestart
	 *            whether to stop current discovery (if present) and start
	 *            discovery again.
	 * @return DISCOVERY_STARTED if the discovery course is able to carry
	 *         through. DISCOVERY_ADAPTER_NULL if adapter is null.
	 *         DISCOVERY_ALREADY_ONGOING if discovery is already.
	 *         DISCOVERY_RESTARTED if forced to stop an existing discovery
	 *         process and started anew.
	 */
	private int startDiscovery(boolean forceRestart) {
		if (droidTooth == null) {
			return DISCOVERY_ADAPTER_NULL;
		}
		
		int returnValue = DISCOVERY_STARTED; // default value

		if (droidTooth.isDiscovering()) { // if device is currently scanning
			if (!forceRestart) { // return gracefully that it's scanning already
				return DISCOVERY_ALREADY_ONGOING;
			} else { // cancel any existing discovery process if asked to force
						// restart
				droidTooth.cancelDiscovery();
				returnValue = DISCOVERY_RESTARTED;
			}
		}
		droidTooth.startDiscovery();
		return returnValue;
	}

	/**
	 * Try to start discovering for devices. Does not intrude in any existing
	 * discovery processes.
	 * 
	 * @return whether discovery was possible. See startDiscovery(boolean).
	 */
	public int startDiscovery() {
		return startDiscovery(false);
	}

	/**
	 * Attempt to stop the discovery process.
	 * 
	 * @return whether stopping the discovery process was a success.
	 */
	public boolean stopDiscovery() {
		return droidTooth.cancelDiscovery();
	}

	/**
	 * Forcefully terminates any existing discovery process and starts again.
	 * 
	 * @return whether discovery started for the first time or
	 *         DISCOVERY_RESTARTED (forced).
	 */
	public int forceDiscovery() {
		return startDiscovery(true);
	}

	/**
	 * Try and turn off the Bluetooth device. If 'gracefully' is passed is True, it
	 * will restore the state of Bluetooth as it was prior to DroidTooth
	 * interfacing with the BT device. Also, receivers will not be unregistered
	 * from the main activity. If gracefully = False, then any discovery process
	 * will be cancelled, BT listeners will be unregistered and the BT finally
	 * off.
	 * 
	 * BluetoothStateListener will be made aware of the "turning off" and "off"
	 * states as they occur.
	 * 
	 * Bluetooth should never be disabled without direct user consent.
	 * 
	 * @param gracefully
	 *            toggle whether to turn off Bluetooth gracefully
	 * @return whether Bluetooth was successfully turned off.
	 */
	private boolean turnOffBluetooth(boolean gracefully) {
		if (droidTooth == null) {
			return true;
		}
		
		//graceful mode, if BT was ON prior to running your app
		//leave it on when you exit
		if (gracefully) {
			//if when we first got a hold of the BluetoothDevice we found out that 
			//it was ON prior to us requesting it to be enabled but for some 
			//strange reason the device is OFF (due to mingling with the adapter),
			//we are graceful towards leaving it in the original state which was ON
			if (bluetoothPreviouslyOn && !droidTooth.isEnabled()) {
				droidTooth.enable(); //turn ON Bluetooth
				return false; // bluetooth was not turned off
			}
		} else if (droidTooth.isDiscovering()) {
				droidTooth.cancelDiscovery();
		}
		
		// unregister all declared listeners
		unregisterListeners();

		// disable which turns OFF Bluetooth
		return droidTooth.disable();
	}

	/**
	 * Cancel any discovery processes and turn off the Bluetooth device.
	 * 
	 * @return whether Bluetooth was turned off.
	 */
	public boolean turnOffBluetooth() {
		return turnOffBluetooth(false);
	}

	/**
	 * Try and turn off Bluetooth if the device state was originally off prior
	 * to DroidTooth enabling it. If Bluetooth was ON when DroidTooth launched,
	 * then leave it on for the user to decide.
	 * 
	 * @return whether Bluetooth was turned off, if BT was on before DroidTooth
	 *         interfaced with it, returns True.
	 */
	public boolean turnOffBluetoothGracefully() {
		return turnOffBluetooth(true);
	}

	public void msg(String msg) {
		Toast.makeText(activity, msg, Toast.LENGTH_SHORT);
	}

	public void longMsg(String msg) {
		Toast.makeText(activity, msg, Toast.LENGTH_LONG);
	}

	/**
	 * Start an intent to prompt the user to make this device discoverable for
	 * the default amount of seconds.
	 */
	public void becomeDiscoverable() {
		becomeDiscoverable(0);
	}

	public void becomeDiscoverableIndefinitely() {
		becomeDiscoverable(DroidTooth.INDEFINITE_DISCOVERABLE_DURATION);
	}

	public void stopIndefiniteDiscoverability(){
		becomeDiscoverableIndefinitely = false;
		if (discoverabilityRunner==null){
			return;
		}
		
		//stop the running thread
		discoverabilityRunner.stopRunner();
	}
	
	public boolean isIndefinitelyDiscoverable(){
		return becomeDiscoverableIndefinitely;
	}
	
	/**
	 * Start the intent for prompting the user to allow the request of
	 * discoverability for the specified seconds. Essentially the method below
	 * executes a threaded class that calls the next method lower.
	 * 
	 * @param durationInSeconds
	 *            integer value for the duration of being discoverable, if 0
	 *            then the default discovery duration is used.
	 */
	public void forceBecomeDiscoverable(int durationInSeconds, boolean forced) {
		if (discoverabilityRunner == null || discoverabilityRunner.isCancelled() || !discoverabilityRunner.isRunning()) {
			discoverabilityRunner = new DiscoverabilityRunner(durationInSeconds);
		}

		if (durationInSeconds == DroidTooth.INDEFINITE_DISCOVERABLE_DURATION){
			becomeDiscoverableIndefinitely = true;
		}
		
		if (forced && discoverabilityRunner.isRunning()) {
			discoverabilityRunner.stopRunner();
		} else if (!forced && !discoverabilityRunner.isRunning()) {
			// run the discovery process if not already running
			getDActivity().updateConsole(
					"Executing thread for discoverability...");
			discoverabilityRunner.execute();
		}
	}

	public void becomeDiscoverable(int durationInSeconds) {
		forceBecomeDiscoverable(durationInSeconds, false);
	}

	public void issueDiscoveryIntent(final int durationInSeconds) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Intent discoverableIntent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
				discoverableIntent.putExtra(
						BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,
						durationInSeconds);
				activity.startActivityForResult(discoverableIntent, DroidTooth.BLUETOOTH_DISCOVERABILITY_REQUEST);

			}
		});

	}

	/**
	 * At the moment, the only way that Bluetooth stops being discoverable is if
	 * the discovery time's up. Thus, we simply change visibility to end within
	 * 1 second. However to do this, we need permission of the user to turn on
	 * bluetooth discoverability for that 1 second... which is not a very
	 * user-friendly experience.
	 * 
	 * This function is here for those who want to experiment some more.
	 */
	public void stopBeingDiscoverable() {
		issueDiscoveryIntent(1);
	}

	/**
	 * Given a socket,
	 * 
	 * @param socket
	 * @return
	 */
	public void listenForMessage(BluetoothSocket socket, DTCallback gotMessage) {
		listenForMessageRunner = new ListenForMessageRunner(socket, gotMessage);
		listenForMessageRunner.execute();
	}

	public void stopListeningForMessage() {
		listenForMessageRunner.cancel(true);
		listenForMessageRunner = null; // clear any old pointers
	}

	private void sendMessage(BluetoothSocket socket, String message) {
		synchronized (socket) {
			OutputStream outStream;
			try {
				outStream = socket.getOutputStream();
				// Add a stop character.
				byte[] byteArray = (message + " ").getBytes();
				byteArray[byteArray.length - 1] = 0;
				outStream.write(byteArray);

			} catch (IOException e) {
			}
		}
	}

}