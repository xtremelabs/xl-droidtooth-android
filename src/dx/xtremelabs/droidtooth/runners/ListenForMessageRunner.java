package dx.xtremelabs.droidtooth.runners;

import java.io.IOException;
import java.io.InputStream;

import android.bluetooth.BluetoothSocket;
import dx.xtremelabs.droidtooth.callbacks.DTCallback;

public class ListenForMessageRunner extends Runner {

	private String messageReturned = "";
	private boolean gotMessage = false;
	BluetoothSocket socket;
	DTCallback gotMessageCallback;
	
	public ListenForMessageRunner(BluetoothSocket socket, DTCallback gotMessageCallback) {
		this.socket = socket;
	}
	
	/**
	 * Calling this method will extract the messages thus far stored
	 * while this listener has been listening. It will reset 
	 * the flag that there is a new message because we "just checked our inbox"
	 * @return the message thus far
	 */
	public String getMessage(){
		gotMessage = false;
		return messageReturned;
	}
	
	/**
	 * Return whether the listener got a message 
	 * @return
	 */
	public boolean gotMessage(){
		return gotMessage;
	}
	
	@Override
	public void run() {
		String message="";
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];
		try {
			InputStream instream = socket.getInputStream();
			int bytesRead;
			while (true) {
				bytesRead = instream.read(buffer);
				if (bytesRead != -1) {
					gotMessage = true;
					while ((bytesRead == bufferSize) && (buffer[bufferSize-1] != 0)){
						message = message + new String(buffer, 0, bytesRead);
						bytesRead = instream.read(buffer);
					}
//					message = message + new String(buffer, 0, bytesRead - 1);
					messageReturned += message;
					
					//issue a callback once we finish receiving a message.
					if (gotMessageCallback!=null){
						gotMessageCallback.callback(message);
					}
				} 
			}
		} catch (IOException e) {}
	}
}
