package dx.xtremelabs.droidtooth.runners;

import android.os.AsyncTask;
import android.os.SystemClock;
import dx.xtremelabs.droidtooth.main.DroidTooth;
import dx.xtremelabs.droidtooth.main.DroidToothInstance;

/**
 * A Runner class is just like Runnable except it uses a <Void, Void, Void> AsyncTask object
 * rather than a Runnable. It's supposed to be the Android-specific thread runner, not just Java.
 * 
 * @author Dritan Xhabija
 *
 */
public abstract class Runner extends AsyncTask<Void, Void, Void> {

	private boolean isRunning = false;

	//to ease function for executing code in thread
	public abstract void run();

	
	@Override
	protected Void doInBackground(Void... arg0) {
		//issue a run on children
		isRunning = true;
		run();
		//after running child's run code, stop this thread.
		stopRunner();
		return null;
	}

	
	public boolean isRunning(){
		return isRunning;
	}

	public boolean stopRunner(){
		return this.cancel(true);
	}

	@Override
	public void onCancelled(){
		super.onCancelled();
		isRunning = false;
	}
	

}
