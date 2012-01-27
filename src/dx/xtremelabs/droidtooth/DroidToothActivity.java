package dx.xtremelabs.droidtooth;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import dx.xtremelabs.droidtooth.callbacks.DefaultCallback;
import dx.xtremelabs.droidtooth.callbacks.DeviceFoundCallback;
import dx.xtremelabs.droidtooth.callbacks.NewIncomingServerConnectionCallback;
import dx.xtremelabs.droidtooth.callbacks.NewOutgoingClientConnectionCallback;
import dx.xtremelabs.droidtooth.common.Constants;
import dx.xtremelabs.droidtooth.common.FoundDevice;
import dx.xtremelabs.droidtooth.main.DroidTooth;

/**
 * Demo activity
 * @author Dritan Xhabija
 *
 */
public class DroidToothActivity extends Activity {

	Button action1, action2, action3, action4, indefDisco, becomeDisco;
	Button nameChange1, nameChange2;
	TextView status;
	EditText console;
	private int currentOpenPane = -1;
	private DeviceListAdapter listAdapter; 
	private ListView deviceListView;
	private LinearLayout deviceListContainer;
	private LinearLayout extraButtons;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		try {
			DroidTooth.init(this);
		} catch (Exception e) {
			Log.d(Constants.DEBUG_DROIDTOOTH, "Error Initializing DroidTooth: "+e);
		}
		console = (EditText) findViewById(R.id.console);
		deviceListContainer = (LinearLayout) findViewById(R.id.deviceListContainer);
		extraButtons = (LinearLayout) findViewById(R.id.extra_buttons);

		action1 = (Button) findViewById(R.id.action1);
		action1.setText("Radius Scan");

		action2 = (Button) findViewById(R.id.action2);
		action2.setText("Clear");

		action3 = (Button) findViewById(R.id.action3);
		action3.setText("Become Host");

		action4 = (Button) findViewById(R.id.action4);
		action4.setText("Join Host");

		indefDisco = (Button) findViewById(R.id.action5);
		indefDisco.setText("Indefinite Discoverability");

		becomeDisco = (Button) findViewById(R.id.action6);
		becomeDisco.setText("Become Visible");

		nameChange1 = (Button) findViewById(R.id.action7);
		nameChange1.setText("Name1");

		nameChange2 = (Button) findViewById(R.id.action8);
		nameChange2.setText("Name2");


		//create the list adapter, for managing the device list
		listAdapter = new DeviceListAdapter(this);
		deviceListView = (ListView) findViewById(R.id.deviceList);
		deviceListView.setAdapter(listAdapter);

		action1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(Constants.DEBUG_DROIDTOOTH, "Action 1 Clicked");
				DroidTooth.scanRadius(new DefaultCallback(){
					//called whenever scanning starts
					@Override
					public void callback(){
						updateConsole("Scanning started...");
					}
				}, new DeviceFoundCallback() {
					@Override
					public void callback(Object o) {
						FoundDevice device = this.getFoundDeviceFromObject(o);
						listAdapter.addDevice(device);
						DroidToothActivity.this.updateConsole("Found device: "+device.DEVICE_NAME+" with address: "+device.DEVICE.getAddress());
					}

				}, true, null);
			}
		});

		//clear screen
		action2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				DroidToothActivity.this.clearScreens();

			}
		});

		indefDisco.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				DroidTooth.becomeVisibleIndefinitely();
			}
		});

		becomeDisco.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				updateConsole("Becoming visible...");
				DroidTooth.becomeVisible(DroidTooth.DEFAULT_DISCOVERABLE_DURATION);
			}
		});



		nameChange1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				DroidTooth.changeDeviceName("Dr.Itan");
			}
		});

		nameChange2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				DroidTooth.changeDeviceName("Dr.Dre");
			}
		});

		//become host
		action3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//initiate a new Host server with the specified broadcast name and 0th index key for UUID
				DroidTooth.teeth(new DefaultCallback(){
					/**
					 * callback when the server starts
					 */
					@Override
					public void callback(){
						action3.setText("Turn Off Host");
						action3.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								DroidTooth.unteeth();
							}
						});
						DroidToothActivity.this.updateConsole("DroidTooth Host Server Started...");
					}
				}, new NewIncomingServerConnectionCallback() {
					@Override
					public void callback (Object o){
						BluetoothSocket socket = this.getIncomingSocketFromObject(o);
						updateConsole("New Incoming Connection to DroidToothServer from: "+socket.getRemoteDevice().getName()+" with address: "+socket.getRemoteDevice().getAddress());
						try {
							socket.connect();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

			}
		});

		//join any public host
		action4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//initiate a new default Host server connectivity that has the specified suffix as in Constants class.
				DroidTooth.tooth(new DefaultCallback(){
					//callback for when scanning starts
					@Override
					public void callback(){
						updateConsole("Scanning started...");
					}
				}, new NewOutgoingClientConnectionCallback() {

					@Override
					public void callback (Object o){
						final BluetoothSocket socket = this.getOutgoingSocketFromObject(o);
						if (socket!=null){
							runOnUiThread( new Runnable() {
								public void run() {
									updateConsole("New Client handshake connection to Server "+socket.getRemoteDevice().getName()+" with address: "+socket.getRemoteDevice().getAddress()+"!!");		
								}
							});
						}
					}
				}, new DeviceFoundCallback() {
					@Override
					public void callback(Object o){

						FoundDevice scannedDevice = this.getFoundDeviceFromObject(o);
						listAdapter.addDevice(scannedDevice);
						deviceListView.invalidate();
						DroidToothActivity.this.updateConsole("Found device "+scannedDevice.DEVICE_NAME+" with address: "+scannedDevice.DEVICE.getAddress()+" while trying to tooth().");
					}
				}, null);

			}
		});
		console.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (currentOpenPane==1){
					expandPane(0);
				} else {
					expandPane(1);
				}

			}
		});
	}

	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data){
		if(requestCode==DroidTooth.BLUETOOTH_DISCOVERABILITY_REQUEST){
			if (resultCode == Activity.RESULT_OK){
				updateConsole("This device will continue to be discoverable");

			} else if (resultCode == Activity.RESULT_CANCELED){
				DroidTooth.stopIndefiniteVisibility();
				updateConsole("User cancelled discoverability");
			}
			
		}
	}

	/**
	 * Expand the given pane,
	 * 0 - Left device list
	 * 1 - Right console
	 * @param pane
	 */
	public void expandPane(int pane){
		currentOpenPane = pane;

		switch (pane){
		//device list
		case 0:
			deviceListContainer.setLayoutParams(new LinearLayout.LayoutParams(400, LayoutParams.FILL_PARENT));
			console.setWidth(20);
			extraButtons.setVisibility(View.GONE);
			break;
			//console
		case 1:
			deviceListContainer.setLayoutParams(new LinearLayout.LayoutParams(20, LayoutParams.FILL_PARENT));
			console.setWidth(450);
			extraButtons.setVisibility(View.VISIBLE);
			break;
		}

	}


	public void clearScreens(){
		clearConsole();
		listAdapter = new DeviceListAdapter(this);
		deviceListView.setAdapter(listAdapter);
		deviceListView.invalidate();
	}

	public void updateConsole(String msg){
		console.append(msg+"\n");
	}

	public void clearConsole(){
		console.setText("");
	}

	public void updateStatus(String msg){
		status.setText(msg);
	}

	@Override
	public void onStop(){
		DroidTooth.releaseResources(true);
		super.onStop();
	}

	protected void showDeviceDialog(BluetoothDevice device, final DialogListener listener) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String message = "Device "+device.getName()+" has address: "+device.getAddress()+".\n";
		boolean isBonded = false;
		if (device.getBondState() == BluetoothDevice.BOND_BONDED){
			isBonded = true;
			message += "You are Bonded to this device.\n";
		} else if (device.getBondState() == BluetoothDevice.BOND_BONDING){
			message += "You are currently bonding to this device.\n";
		} else {
			message += "You are not bonded to this device.\n";
		}

		builder.setMessage(message)

		.setCancelable(true);
		if (!isBonded){
			builder.setPositiveButton("Pair to this device",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					listener.pairDevice();
				}
			});
		} else {
			builder.setNegativeButton("Un-Pair from this device", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					listener.unpairDevices();
					dialog.cancel();
				}
			});
		}

		AlertDialog deviceDialog = builder.create();
		deviceDialog.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				return DroidToothActivity.this.onKeyDown(keyCode, event);
			}
		});
		deviceDialog.show();
	}

}