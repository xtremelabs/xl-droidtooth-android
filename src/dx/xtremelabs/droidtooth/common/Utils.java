package dx.xtremelabs.droidtooth.common;

import java.util.UUID;

import android.util.Log;

/**
 * Various global functions.
 * 
 * @author Dritan Xhabija
 *
 */
public class Utils {

	
	/**
	 * Used for when DroidTooth attempts to become a master awaiting incoming connections.
	 * @return random UUID object.
	 */
	public static UUID getRandomUUID(){
		return UUID.randomUUID();
	}
	
	public static UUID getUUIDForName(String broadcastName){
		return UUID.fromString(broadcastName);
	}
	
	/** Globally Accessible UUID keywords for teeth() to broadcast
	 * and tooth() to connect.
	 * @return the UUID object at the known index, null if index is out of bounds.
	 */
	
	public static UUID getKnownUUID(int index){
		if (index >= 0 && index <= Constants.KNOWN_UUIDS.length-1){
			return UUID.fromString(Constants.KNOWN_UUIDS[index]);
		}
		return null;
	}
	
	/**
	 * Alwatys return a unique name for the Service Discovery Protocol
	 * used to internally identify the Bluetooth socket connection.
	 * @return
	 */
	public static String getSDPName(){
		String randomNumbers = Utils.getRandomUUID().toString().replaceAll("[^0-9]", "");
		return "droidtooth_"+randomNumbers;
	}
	
	/**
	 * Determine whether the given device name is 99% the DroidTooth server.
	 * The other 1% that this function will return true when the device is not 
	 * a DroidTooth server is when a device's bluetooth name contains 
	 * @param deviceName
	 * @return
	 */
	public static boolean isHost(String deviceName){
		return deviceName.contains(Constants.DroidToothServerID);
	}
	
	public static String getHostName(String deviceName){
		return deviceName + Constants.DroidToothServerID;
	}
}
