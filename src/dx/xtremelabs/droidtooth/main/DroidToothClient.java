package dx.xtremelabs.droidtooth.main;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.util.Log;
import dx.xtremelabs.droidtooth.callbacks.DefaultCallback;
import dx.xtremelabs.droidtooth.callbacks.NewOutgoingClientConnectionCallback;
import dx.xtremelabs.droidtooth.common.Constants;
import dx.xtremelabs.droidtooth.common.FoundDevice;

/**
 * Defines a Bluetooth client-to-server connection for establishing a pairing with the specified host.
 * This class is to be instantiated every time a request to connect with the server is issued,
 * then call execute() to start the connection process.
 * 
 * @author Dritan Xhabija
 *
 */
public class DroidToothClient extends AsyncTask<Void, Void, Void> {

	//one instance of this class can have only one instance of BluetoothServerSocket which represents
	//the handshake connection between client and server. For reliability and correctness of channels 
	//and communication, we declare the socket as final then assign a different pointer to it.
	private final BluetoothDevice hostDevice;
	private BluetoothSocket socketWithHost;

	//keep track of the host to connect to
	private FoundDevice host;
	private UUID uuid;


	private NewOutgoingClientConnectionCallback newConnection;
	private DefaultCallback errorOccurred;
	
	private boolean isConnected = false;

	public DroidToothClient(FoundDevice host, UUID uuid){
		this.host = host;
		this.hostDevice = host.DEVICE; //Bluetooth device
		this.uuid = uuid;
		initConnectionWithHost();
	}

	private void initConnectionWithHost(){
		try {
			// MY_UUID is the app's UUID string, also used by the server code
			socketWithHost = hostDevice.createRfcommSocketToServiceRecord(uuid);
		} catch (IOException e) { 
			Log.e(Constants.DEBUG_DROIDTOOTH, "Unable to connect to HOST "+hostDevice.getName()+", with UUID: "+uuid);
		}
	}
	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
		initConnectionWithHost(); //re-init socket if setting UUID again
	}

	public boolean isConnected() {
		return isConnected;
	}

	/**
	 * Start the client-server connection
	 */
	@Override
	protected Void doInBackground(Void... params) {

		if (socketWithHost==null){
			initConnectionWithHost();
		}
		//		if (socketWithHost==null){
		//			return null;
		//		}

		//initialize server socket
		BluetoothSocket incomingDevice;

		try {
			//attempt to connect to server
			socketWithHost.connect();
		} catch (Exception e) {
			//issue a callback to any interested clients
			if (errorOccurred!=null){
				//obviously the "agreement" for passing in a string can be switched to be an Exception
				errorOccurred.callback(e.toString()); 
			}
			Log.d(Constants.DEBUG_DROIDTOOTH, "Unable to connect client with host "+hostDevice.getName()+" because: "+e);
			
			return null;
		}

		host.setSocket(socketWithHost);
		
		//issue a callback to those interested clients that a new connection has been established.
		if (newConnection!=null){
			//at this point the clients should be using setSocket() on their own FoundDevice objects
			//to ensure that the data gets saved in an instance and is not statically lose.
			newConnection.callback(socketWithHost);
		}
		
		return null; 
		
	}

	/**
	 * Close connection with host.
	 */
	public void closeConnectionWithHost(){
		try {
			socketWithHost.close();
		} catch (IOException e) {
			Log.d(Constants.DEBUG_DROIDTOOTH, "Unable to terminate client-to-server connection from client.");
		}
	}
	
	/**
	 * Return the socket connection with the host, for establishing i/o streams.
	 * @return
	 */
	public BluetoothSocket getSocket(){
		while (socketWithHost==null) ; //wait until we can get a socket
		return socketWithHost;
	}
	
	/******* Callback setters & getters ******/
	public NewOutgoingClientConnectionCallback getNewOutgoingClientConnectionCallback() {
		return newConnection;
	}
	
	public void setNewOutgoingClientConnectionCallback(NewOutgoingClientConnectionCallback newConnection) {
		this.newConnection = newConnection;
	}
	
	public void setErrorOccurredCallback(DefaultCallback errorOccurred) {
		this.errorOccurred = errorOccurred;
	}
}
