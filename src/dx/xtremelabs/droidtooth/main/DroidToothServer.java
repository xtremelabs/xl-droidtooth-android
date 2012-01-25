package dx.xtremelabs.droidtooth.main;

import java.io.IOException;
import java.util.LinkedList;
import java.util.UUID;

import dx.xtremelabs.droidtooth.callbacks.DTCallback;
import dx.xtremelabs.droidtooth.callbacks.NewIncomingServerConnectionCallback;
import dx.xtremelabs.droidtooth.common.Constants;
import dx.xtremelabs.droidtooth.common.RunnableCallback;
import dx.xtremelabs.droidtooth.common.Utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Defines a Bluetooth server that listens for incoming connections.
 * Takes an integer value as parameter defining how many connections
 * this server is to allow (up to 7).
 * 
 * @author Dritan Xhabija
 *
 */
public class DroidToothServer extends AsyncTask<Integer, Void, Void> {
	
	//one instance of this class can have only one instance of BluetoothServerSocket for reliability
	//of channels and communication, we declare the socket as final then assign a different pointer to it.
	private BluetoothServerSocket droidToothSocket;
	
	private final BluetoothAdapter droidToothAdapter;
	private String broadcastName, oldBroadcastName;
	private UUID uuid;
	
	private boolean isRunning = false;
	private boolean wasRunning = false;
	
	private NewIncomingServerConnectionCallback newConnection;
	private DTCallback serverStartedCallback;
	
	//list containing all connected devices
	private LinkedList <BluetoothSocket> connectedDevices = new LinkedList <BluetoothSocket> (); //up to 7 connected devices according to BT
	
	public DroidToothServer(BluetoothAdapter droidToothAdapter){
		this.droidToothAdapter = droidToothAdapter;
	}
	
	public DroidToothServer(BluetoothAdapter droidToothAdapter, UUID uuid){
		this.droidToothAdapter = droidToothAdapter;
		this.uuid = uuid;
		
	}
	public DroidToothServer(BluetoothAdapter droidToothAdapter, String broadcastName, UUID uuid){
		this.droidToothAdapter = droidToothAdapter;
		this.broadcastName = broadcastName;
		this.uuid = uuid;
		
	}
	
	private void initServerSocket(){
		//we don't care to re-init this socket if it's already created
		if (droidToothSocket !=null){
			return;
		}
		
		BluetoothServerSocket tmpSocket;
		
		try {
			tmpSocket = droidToothAdapter.listenUsingRfcommWithServiceRecord(Utils.getSDPName(), uuid);
		} catch (IOException e){
			tmpSocket = null;
		}
		droidToothSocket = tmpSocket;
	}
	
	public String getBroadcastName() {
		return broadcastName;
	}

	public void setBroadcastName(String broadcastName) {
		this.broadcastName = broadcastName;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	
	public boolean isRunning() {
		return isRunning;
	}
	
	public boolean wasRunning(){
		return wasRunning;
	}

	/**
	 * Start the server listening process.
	 * @param params an array of Integers, we are interested in the first [0] which 
	 * 			defines the number of connections we should allow.
	 * 			If params[0] == null, then allow at most 1 pairing.
	 *  
	 * @return Void
	 */
	@Override
	protected Void doInBackground(Integer... params) {
		isRunning = true;
		DroidToothInstance dtinstance = DroidToothInstance.get();
		
		//when creating a server either use default tagging for clients identifying 
		//hosts, or use a custom name, either way, keep track of device name prior to
		//any renaming, then when closeSocket() gets called, which always gets called
		//from the public methods shutdownServer()/restartServer(), rename device back
		//to original name.
		String currentDeviceName = dtinstance.getDeviceName();
		oldBroadcastName = currentDeviceName;
		
		
		if (broadcastName!=null){
			dtinstance.setDeviceName(broadcastName);
		} else {
			//if current name of device already has DroidTooth server suffix
			//don't set it again
			if (!currentDeviceName.contains(Constants.DroidToothServerID)){
				dtinstance.setDeviceName(Utils.getHostName(currentDeviceName));
			}
		}
		
		Integer permittedConnections = params.length > 0 ? params[0] : null;
		
		//initialize server socket
		initServerSocket();
		
		BluetoothSocket incomingDevice;
		while (true){
			while(droidToothSocket==null){ //wait until we have a socket 
				; //do nothing
			}
			
			//notify any interested callbacks that the server started
			if (serverStartedCallback!=null){
				dtinstance.getActivity().runOnUiThread( new Runnable() {
					public void run() {
						serverStartedCallback.callback();
					}
				});
			}
			
			try {
				//block this thread until some device decides to connect
				incomingDevice = droidToothSocket.accept();
			} catch (IOException e) {
				break;
			}
			
			//if some connection went through
			if (incomingDevice !=null){
				//issue a callback to those interested in this new device
				if (newConnection !=null){
					
					DroidToothInstance.get().getActivity().runOnUiThread( new RunnableCallback(incomingDevice) {
						@Override
						public void run() {
							newConnection.callback(o);
						}
					});
					
				}
				
				/*
				if (permittedConnections==null){ //only 1 device pairing
					shutdownServer();
					break;
				} else if (connectedDevices.size() == permittedConnections){
					
					shutdownServer();
					isRunning = false;
					break;
				}
				*/
				
				shutdownServer();
				isRunning = false;
				wasRunning = true;
				break;
			}
		}
		
		return null; //aka Void
	}

	/**
	 * Shutdown the server and "renew" the socket for next use.
	 */
	public void shutdownServer(){
		revertDeviceName();
		closeServer();
		droidToothSocket = null;
	}
	
	public void restartServer(){
		shutdownServer();
		initServerSocket();
	}
	
	public void initServer(){
		initServerSocket();
	}
	
	private void closeServer(){
		try {
			droidToothSocket.close();
			//change back the device name to what it was originally, if set at all
			isRunning = false; //after name change we can conclude that we're done
		} catch (IOException e) {
			Log.d(Constants.DEBUG_DROIDTOOTH, "Unable to terminate BluetoothServerSocket from listening server!");
		}
	}
	
	private void revertDeviceName(){
		if (broadcastName!=null && oldBroadcastName!=null && !broadcastName.equals(oldBroadcastName)){
			DroidToothInstance.get().setDeviceName(oldBroadcastName);
			
			oldBroadcastName = null;
		}
	}
	public NewIncomingServerConnectionCallback getNewIncomingConnectionCallback() {
		return newConnection;
	}

	public void setNewIncomingConnectionCallback(NewIncomingServerConnectionCallback newConnection) {
		this.newConnection = newConnection;
	}


	public void setServerStartedCallback(DTCallback serverStarted) {
		this.serverStartedCallback = serverStarted;
	}
}
