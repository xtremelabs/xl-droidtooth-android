package dx.xtremelabs.droidtooth.main;

import java.util.HashMap;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import dx.xtremelabs.droidtooth.callbacks.DTCallback;
import dx.xtremelabs.droidtooth.callbacks.DefaultCallback;
import dx.xtremelabs.droidtooth.callbacks.DeviceFoundCallback;
import dx.xtremelabs.droidtooth.callbacks.NewIncomingServerConnectionCallback;
import dx.xtremelabs.droidtooth.callbacks.NewOutgoingClientConnectionCallback;
import dx.xtremelabs.droidtooth.common.Constants;
import dx.xtremelabs.droidtooth.common.FoundDevice;
import dx.xtremelabs.droidtooth.common.Utils;
import dx.xtremelabs.droidtooth.exceptions.NoBluetoothDeviceFound;

/**
 * DroidTooth class. Provides public static methods.
 * The purpose of DroidTooth is to simplify the life of an Android developer who has to deal
 * with Bluetooth. While it is fairly easy already to work on Bluetooth and Android, this
 * Library provides 1-liner functions for complex tasks. Searching for any devices around you
 * should be a matter of issuing a single call to a method that does just that, like sniff().
 * 
 * Likewise, pairing between devices should also be as trivial in today's digital era.
 *  
 * @author Dritan Xhabija
 *
 */
public class DroidTooth {

	//default time for being discoverable in seconds

	public static final int MAXIMUM_DISCOVERABLE_DURATION = 300;
	public static final int INDEFINITE_DISCOVERABLE_DURATION = -755; //used as a trigger
	public static final int DEFAULT_DISCOVERABLE_DURATION = MAXIMUM_DISCOVERABLE_DURATION;
	public static final int DEFAULT_GRACE_SECONDS_AFTER_RADIUS_SCAN = 5;

	private static Activity activity;

	//keep track of all the client instances operating
	private static HashMap <String, DroidToothClient> tooths = new HashMap<String, DroidToothClient>();

	/**
	 * This method must be the very first method to be called.
	 * Sets the activity across those classes that need it. 
	 * @param activity
	 * @throws NoBluetoothDeviceFound Exception if no Bluetooth adapter exists. 
	 */
	public static void init(Activity activity)throws Exception{
		DroidToothInstance.initInstance(activity);
		DroidTooth.activity = activity;
		if(!DroidToothInstance.get().doesBluetoothExist()){
			throw new NoBluetoothDeviceFound("No Bluetooth device adapter found on mobile device.");
		}
	}

	/**
	 * Scan for any visible devices and return them in an array list.
	 * Assumes init() is called first.
	 * 
	 * @param deviceFoundCallback object to be called back once a device is found.
	 */
	public static void scanRadius(DTCallback scanningStarted, DeviceFoundCallback deviceFoundCallback){
		scanRadius(scanningStarted, deviceFoundCallback, false, null);
	}

	/**
	 * Scan for any visible devices and return them in an array list.
	 * Assumes init() is called first.
	 * 
	 * @param scanningStartedCallback a notifier for pinging back the user when scanning starts
	 * @param deviceFoundCallback object to be called back once a device is found.
	 * @param keepAlive whether or not to leave bluetooth on after it's done scanning
	 * @param targetDeviceName stop scanning once it finds targetDeviceName if present, 
	 * 			if null, scanning continues as normally
	 */
	public static void scanRadius(final DTCallback scanningStartedCallback, final DeviceFoundCallback deviceFoundCallback, boolean keepAlive, final String targetDeviceName)  {
		Log.d(Constants.DEBUG_DROIDTOOTH, "Starting to scan radius....");
		DroidToothInstance.get().setDiscoveryStartedCallback(scanningStartedCallback);
		if (targetDeviceName==null){
			//set the interesting callback for every found device 
			DroidToothInstance.get().setOnDeviceFoundCallback(deviceFoundCallback);
		} else {
			//if user supplied a target device to scan for, once found, call the callback with the device
			//and stop discovery right away.
			DroidToothInstance.get().setOnDeviceFoundCallback(new DeviceFoundCallback() {
				@Override
				public void callback(Object o){
					FoundDevice device = this.getFoundDeviceFromObject(o);
					if (device.DEVICE_NAME.equals(targetDeviceName)){
						deviceFoundCallback.callback(o);
						DroidToothInstance.get().stopDiscovery();
						return;
					}

				}
			});
		}

		//check if BT is already on, if so, simply start discovering
		if (DroidToothInstance.get().isOn()){
			Log.d(Constants.DEBUG_DROIDTOOTH, "Bluetooth already ON, starting discovery");
			DroidToothInstance.get().startDiscovery();
		} else if (!DroidToothInstance.get().initBluetooth()){ //try initializing bluetooth
			return; //early termination if unable to init
		}

		//now we're setting another interesting callback for what happens when Bluetooth is On.
		DroidToothInstance.get().getBluetoothStateListener().setBluetoothIsOnCallback(new DefaultCallback() {
			/**
			 * Will be called when Bluetooth turns fully on.
			 */
			@Override
			public void callback() {
				//start discovery right away 
				DroidToothInstance.get().startDiscovery();
			}
		});

		//if requsted to be conservative, turn off after 5 seconds 
		if (!keepAlive){
			//we want to be conservative towards power consumption when simply scanning radius, thus after discovery has finished we turn off Bluetooth.
			DroidToothInstance.get().getDiscoveryStateListener().setDiscoveryFinishedCallback(new DefaultCallback() {
				/**
				 * Will be called when Bluetooth has finished the discovery process.
				 * Wait 5 seconds (default) before turning off Bluetooth
				 */
				@Override
				public void callback() {
					Log.d(Constants.DEBUG_DROIDTOOTH, "Doing post-discovery processes....");

					//create a new "thread" that waits in the background graceful-seconds then
					//tries to turn off
					new AsyncTask<Integer, Void, Void> (){

						@Override
						protected Void doInBackground(Integer... arg0) {
							int sleepTimeInSeconds = arg0[0];

							//for every passing graceful second we want to stop the count-down timer if the device
							//starts discovering again. At which point the shutdown won't happen because it will skip
							//the evaluation below of whether the device is discovering or not. 
							for (int i=0; i<sleepTimeInSeconds && !DroidToothInstance.get().isDiscovering(); i++){
								Log.d(Constants.DEBUG_DROIDTOOTH, (i+1)+" second(s) have passed since last discovery.");
								SystemClock.sleep(1000);
							}

							//if the device is discovering, let it discover, ignore request to turn off.
							if (!DroidToothInstance.get().isDiscovering()){
								Log.d(Constants.DEBUG_DROIDTOOTH, "Turning off Bluetooth.");
								if (!DroidToothInstance.get().turnOffBluetoothGracefully()){
									Log.d(Constants.DEBUG_DROIDTOOTH, "Couldn't turn off Bluetooth for some reason...");
								}
							}

							return null;
						}
					}.execute(DEFAULT_GRACE_SECONDS_AFTER_RADIUS_SCAN);
				}
			});
		} 
	} //end scanRadius()


	/**
	 * Wrapper function for requesting the user to become visible (discoverable) 
	 * for the specified seconds.
	 * @param seconds
	 */
	public static void becomeVisible(int seconds){
		DroidToothInstance.get().becomeDiscoverable(seconds);
	}

	public static void becomeVisibleIndefinitely(){
		DroidToothInstance.get().becomeDiscoverableIndefinitely();
	}

	public static void changeDeviceName(String newName){
		DroidToothInstance.get().setDeviceName(newName);
	}
	
	/**
	 * Ease method for ensuring Bluetooth is on at times such as onResume(), etc...
	 * 
	 * @return whether Bluetooth was able to turn on.
	 */
	public static boolean ensureBluetoothIsOn(){
		if (!DroidToothInstance.get().isOn()){
			if (DroidToothInstance.get().initBluetooth()){
				return true; //return true if the initBluetooth() was successful 
			}
		} else { //if BT was on already, return it's ON now
			return true;
		}
		//will only get here if initBluetooth() returns false;
		return false;
	}
	
	/**
	 * Start a new teeth() group or shutdown existing and start again.
	 * @param incomingServerConnectionCallback
	 * @param serverStartedCallback
	 * @return 
	 */
	public static boolean reteeth(DTCallback serverStartedCallback, NewIncomingServerConnectionCallback incomingServerConnectionCallback){
		DroidToothServer dtServer = DroidToothInstance.get().getDroidToothServer();
		if (dtServer.isRunning()){
			dtServer.shutdownServer();
		}
		return teeth(serverStartedCallback, incomingServerConnectionCallback);
	}

	/**
	 * Teeth with the default broadcast name (don't change the device's name) and default uuid index key
	 * @param incomingServerConnectionCallback
	 * @param serverStartedCallback
	 * @return
	 */
	public static boolean teeth(DTCallback serverStartedCallback, NewIncomingServerConnectionCallback incomingServerConnectionCallback){
		return teeth(null, 0, serverStartedCallback, incomingServerConnectionCallback);
	}
	/**
	 * Become a Bluetooth host awaiting incoming connections. The name of the method
	 * depicts a group of individual tooth()'s that want to join the teeth(name) group.
	 * @param broadcastName the name to be broadcasting to clients.
	 * @param uuidIndexKey the index of the key string to use for generating the UUID. 
	 * @param incomingServerConnectionCallback the method to call for when a new client requests a connection.
	 * 					This is where work should be done about this client.
	 */
	public static boolean teeth(String broadcastName, int uuidIndexKey, DTCallback serverStartedCallback, NewIncomingServerConnectionCallback incomingServerConnectionCallback){
		return teeth(broadcastName, Utils.getKnownUUID(uuidIndexKey), serverStartedCallback, incomingServerConnectionCallback);
	}


	/**
	 * Same as above but supply your own UUID rather than use a predefined one as in Constant.KNOWN_UUIDS.
	 * @param broadcastName
	 * @param uuid
	 * @param callback
	 * @param serverStartedCallback
	 * @return
	 */
	public static boolean teeth(String broadcastName, UUID uuid, DTCallback serverStartedCallback, NewIncomingServerConnectionCallback callback){
		DroidToothServer dtServer = DroidToothInstance.get().getDroidToothServer();

		//be graceful towards existing connection because we should not hog BT too without canceling existing.
		if (dtServer.isRunning()){
			return false;
		}

		if (!DroidToothInstance.get().initBluetooth()){ //try initializing bluetooth
			Log.d(Constants.DEBUG_DROIDTOOTH, "DroidTooth could not initialize Bluetooth.");
			return false; //early termination
		}

		//set any listeners that want to know when the server started.
		dtServer.setServerStartedCallback(serverStartedCallback);
		//set new incoming connection callback
		dtServer.setNewIncomingConnectionCallback(callback);
		//set broadcast name
		dtServer.setBroadcastName(broadcastName);
		//set random UUID
		dtServer.setUuid(uuid); //use the 0th key string for getting UUID
		//prompt the user to become discoverable, blocking call that resumes to the next line 
		//after granting access.
		DroidToothInstance.get().becomeDiscoverable();
		//no parameters to server - only 1-to-1 pairing and start the server!
		dtServer.execute(6);
		return true;
	}

	public static void unteeth(){
		DroidToothInstance.get().getDroidToothServer().shutdownServer();
	}


	public static void tooth(final String broadcastingName){
		tooth(broadcastingName, 0, null, null, null);
	}

	/**
	 * Attempt to join a Bluetooth host that was scanned and found to have the specified teeth()'s name.
	 * @param broadcastingName the teeth()'s group name to join
	 * @param uuidIndexKey the UUID index that must one of the predefined teeth indexes
	 * @param outgoingClientConnectionCallback when a connection is established callback
	 * @param newDeviceFound when a new device is found while scanning callback
	 */
	public static void tooth(String broadcastingName, int uuidIndexKey, DTCallback scanningStartedCallback, NewOutgoingClientConnectionCallback outgoingClientConnectionCallback, final DeviceFoundCallback newDeviceFound){
		tooth(broadcastingName, Utils.getKnownUUID(uuidIndexKey), scanningStartedCallback, outgoingClientConnectionCallback, newDeviceFound);
	}

	/**
	 * Attempt to tooth into any detected DroidTooth hosts.
	 * @param uuidIndexKey
	 * @param callback
	 * @param newDeviceFound
	 */
	public static void tooth(DTCallback scanningStartedCallback, NewOutgoingClientConnectionCallback callback, DeviceFoundCallback newDeviceFound){
		tooth(null, 0, scanningStartedCallback, callback, newDeviceFound);
	}

	/**
	 * Same as above but pass in your own UUID for joining a teeth() group that matches it.
	 * (rather than using one of the predefined UUIDS. However, it is benefitiary to 
	 * simply add your own UUIDs to the existing array of UUIDS in Constants.KNOWN_UUIDS
	 * @param broadcastingName
	 * @param uuid
	 * @param callback
	 * @param newDeviceFound
	 */
	public static void tooth(final String broadcastingName, final UUID uuid, DTCallback scanningStartedCallback, final NewOutgoingClientConnectionCallback callback, final DeviceFoundCallback newDeviceFound){

		//first we need to do a scan of the 15m BT radius
		DroidTooth.scanRadius(scanningStartedCallback, new DeviceFoundCallback() {
			//once we find some device
			@Override
			public void callback (Object o){
				//check their name
				FoundDevice newDevice = this.getFoundDeviceFromObject(o);

				//we give priority to finding a specific device 
				if (broadcastingName!=null && newDevice.DEVICE_NAME.equals(broadcastingName)){
					//stop discovering once found the desired teeth() group.
					if (DroidToothInstance.get().stopDiscovery()){
						//attempt to start a handshake for the given device object and UUID
						DroidToothClient handshake = tooth(newDevice.DEVICE, Utils.getKnownUUID(0), callback);
						tooths.put(broadcastingName, handshake);
					}
				}  else if (broadcastingName == null && Utils.isHost(newDevice.DEVICE_NAME)){	//if we detected a host using default DroidTooth library
						//attempt to pair with the device
						DroidToothClient handshake = tooth(newDevice.DEVICE, Utils.getKnownUUID(0), callback);
						//keep track of all existing threaded clients
						tooths.put(broadcastingName, handshake);
				} 
				
				//keep any interested clients notified when a device was found.
				if (newDeviceFound!=null){
					newDeviceFound.callback(o);
				}
			}
		}, true, null); //leave bluetooth on after scan
	}

	/**
	 * Attempt to pair to the found device while scanning.
	 * @param device
	 * @param uuid
	 */
	public static DroidToothClient tooth(BluetoothDevice device, UUID uuid, NewOutgoingClientConnectionCallback newOutgoingClientConnectionCallback){
		if (device == null){
			return null;
		}
		DroidToothClient handshake = new DroidToothClient(device, uuid);
		handshake.setNewOutgoingClientConnectionCallback(newOutgoingClientConnectionCallback);
		handshake.execute(); //initiate the handshake connection
		return handshake;
	}

	/**
	 * Cloase a specific connection established to the specified broadcast name
	 * or close all connections if "null" is specified.
	 * @param broadcastName
	 */
	public static void untooth(String broadcastName){
		if (broadcastName!=null){
			if (tooths.containsKey(broadcastName)){
				tooths.get(broadcastName).closeConnectionWithHost();
				tooths.remove(tooths.get(broadcastName));
			}
		} else {
			for(DroidToothClient client : tooths.values()){
				client.closeConnectionWithHost();
			}
			tooths = new HashMap<String, DroidToothClient>();
		}
	}


	public static void releaseResources(boolean turnOffBluetooth){
		//turn off bluetooth server if still running
		if (DroidToothInstance.get().getDroidToothServer().isRunning()){
			DroidToothInstance.get().getDroidToothServer().shutdownServer();
		}

		//if requested to turn off Bluetooth, all listeners will be unregistered regardless. 
		if (turnOffBluetooth){
			DroidToothInstance.get().turnOffBluetooth();
		} else { //simply unregister listeners
			DroidToothInstance.get().unregisterListeners();
		}

	}
}
