package dx.xtremelabs.droidtooth.callbacks;

/**
 * Superclass for callbacks. Can just create a new DTCallback() to force override of both methods.
 * 
 * @author Dritan Xhabija
 *
 */
public interface DTCallback {

	public void callback();
	
	public void callback(Object o);
	
}
