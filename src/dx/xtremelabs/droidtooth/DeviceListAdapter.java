package dx.xtremelabs.droidtooth;

import java.util.ArrayList;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.IBluetooth;
import android.graphics.Color;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import dx.xtremelabs.droidtooth.callbacks.NewOutgoingClientConnectionCallback;
import dx.xtremelabs.droidtooth.common.Constants;
import dx.xtremelabs.droidtooth.common.FoundDevice;
import dx.xtremelabs.droidtooth.common.Utils;
import dx.xtremelabs.droidtooth.main.DroidTooth;
import dx.xtremelabs.droidtooth.main.DroidToothClient;
import dx.xtremelabs.droidtooth.main.DroidToothInstance;

public class DeviceListAdapter extends BaseAdapter {

	private ArrayList <FoundDevice> devices = new ArrayList<FoundDevice>();
	
	private Activity activity;
	public DeviceListAdapter(Activity activity) {
		this.activity = activity;
	}
	public void addDevice(FoundDevice device){
		devices.add(device);
		this.notifyDataSetChanged();
	}
	
	public void clear(){
		devices = new ArrayList<FoundDevice>();
		this.notifyDataSetChanged();
		
	}
	@Override
	public int getCount() {
		return devices.size();
	}

	@Override
	public FoundDevice getItem(int index) {
		return devices.get(index);
	}

	@Override
	public long getItemId(int index) {
		return index;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		View row = view;
		if (view == null){
			row = View.inflate(activity, R.layout.device_row, null);
		} 
		FoundDevice device = getItem(position);
		setRowName(row, device.DEVICE_NAME);
		setRowDeviceState(row, device.DEVICE);
		
		//scan out for any devices that are marked as DroidTooth servers
		if (device.DEVICE_NAME.contains(Constants.DroidToothServerID)){
			row.setBackgroundColor(Color.GREEN);
			
			//remove the extra tag that identifies this found device as a host
			setRowName(row, device.DEVICE_NAME.substring(0, device.DEVICE_NAME.indexOf(Constants.DroidToothServerID)));
		} else {
			row.setBackgroundColor(Color.WHITE);
		}
		row.setTag(device);
		row.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View rowView) {
				final FoundDevice device = (FoundDevice) rowView.getTag();
				((DroidToothActivity)activity).showDeviceDialog(device.DEVICE, new DialogListener() {
					
					@Override
					public void unpairDevices() {
						DroidToothInstance.get().unpairDevice(device.DEVICE_MAC);
					}
					
					@Override
					public void pairDevice() {
						//todo: auto get the next available UUID index
						DroidToothClient handshakeInitiatedObject = DroidTooth.tooth(device, Utils.getKnownUUID(0), new NewOutgoingClientConnectionCallback() {
							@Override
							public void callback(Object o){
								//grab the new socket connection established with this device
								BluetoothSocket socket = this.getOutgoingSocketFromObject(o);
								Log.d(Constants.DEBUG_DROIDTOOTH, "Pairing was Successful! Got socket: "+socket);
							}
						}, null);
					}
				});
				Log.d(Constants.DEBUG_DROIDTOOTH, "CLICKED ON DEVICE: "+device.DEVICE_NAME+" with Address: "+device.DEVICE.getAddress());
				Toast.makeText(activity, "Clicked on Device: "+device.DEVICE_NAME+" with Address: "+device.DEVICE.getAddress(), Toast.LENGTH_LONG);
			}
		});
		return row;
	}
	
	private void setRowName(View row, String name){
		((TextView)row.findViewById(R.id.deviceName)).setText(name);
	}
	
	public void setRowDeviceState(View row, BluetoothDevice device){
		ImageView statusIcon = (ImageView) row.findViewById(R.id.deviceStatus);
		
		switch(device.getBondState()){
		case BluetoothDevice.BOND_BONDED:
			statusIcon.setBackgroundResource(R.drawable.bonded);
			break;
		case BluetoothDevice.BOND_BONDING:
			statusIcon.setBackgroundResource(R.drawable.bonding);
			break;
		case BluetoothDevice.BOND_NONE:
			statusIcon.setBackgroundResource(R.drawable.not_bonded);
			break;
		}
	}

	
}
