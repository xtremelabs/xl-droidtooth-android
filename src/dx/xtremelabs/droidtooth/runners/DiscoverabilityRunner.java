package dx.xtremelabs.droidtooth.runners;

import android.os.SystemClock;
import dx.xtremelabs.droidtooth.DroidToothActivity;
import dx.xtremelabs.droidtooth.main.DroidTooth;
import dx.xtremelabs.droidtooth.main.DroidToothInstance;

public class DiscoverabilityRunner extends Runner {

	private int seconds;
	
	public DiscoverabilityRunner(int durationInSeconds) {
		this.seconds = durationInSeconds;
	}
	
	@Override
	public void run() {
		if (seconds == DroidTooth.INDEFINITE_DISCOVERABLE_DURATION){
			runIndefinitely();
		} else {
			
			if (seconds == 0){
				seconds = DroidTooth.DEFAULT_DISCOVERABLE_DURATION;
			}
			/*
			DroidToothInstance.get().getActivity().runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
				
				}
			});
			*/
			DroidToothInstance.get().issueDiscoveryIntent(seconds);
			
		}
	}

	/**
	 * Sleep for as long as Bluetooth is discoverable, then ask user again 
	 * for connectivity.
	 */
	private void runIndefinitely(){
		while(true && DroidToothInstance.get().isIndefinitelyDiscoverable()){
			DroidToothInstance.get().issueDiscoveryIntent(DroidTooth.MAXIMUM_DISCOVERABLE_DURATION);
			SystemClock.sleep(DroidTooth.MAXIMUM_DISCOVERABLE_DURATION*1000); //TODO: rather than sleep every X-seconds to bring up the intent for discoverability,
																			//tune in the event handler for when the device becomes discoverable or undiscoverable.
		}
	}

	@Override
	public void onCancelled(){
		super.onCancelled();
		DroidToothInstance.get().stopDiscovery();
	}
}
