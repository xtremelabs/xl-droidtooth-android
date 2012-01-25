package dx.xtremelabs.droidtooth.common;

import java.util.UUID;

/**
 * Global Constants class.
 * 
 * @author Dritan Xhabija
 *
 */
public class Constants {

	public final static String DEBUG_DROIDTOOTH = "droidtooth";
	
	//suffix to be added to device name when server starts, so that clients can easily identify them
	public final static String DroidToothServerID = "_DTHOST";
	
	//there needs to be a set of standard UUIDs that BT servers can broadcast and clients match
	//these pre-defined UUIDs can be used brute force and authentication on any default DroidTooth BT teeth() 
	//found while scanning.
	public final static String [] KNOWN_UUIDS = { 
													"00001101-0000-1000-8000-00805f9b34fb", //Default UUID for pairing with other devices
													"D1201D70-011D-121A-1177-11AB11A77100",
													"3892a9a1-095a-42b8-a0f9-afd761bf1860",
													"e96e897c-b8a6-4a1e-8f15-76bb6ebb3b3b"
													};

	
}
