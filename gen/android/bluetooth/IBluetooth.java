/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: C:\\Users\\Dexitor\\workspace\\xl-droidtooth-android\\src\\android\\bluetooth\\IBluetooth.aidl
 */
package android.bluetooth;
/**
  * System private API for talking with the Bluetooth service.
  * See http://stackoverflow.com/questions/3462968/how-to-unpair-bluetooth-device-using-android-2-1-sdk
  */
public interface IBluetooth extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements android.bluetooth.IBluetooth
{
private static final java.lang.String DESCRIPTOR = "android.bluetooth.IBluetooth";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an android.bluetooth.IBluetooth interface,
 * generating a proxy if needed.
 */
public static android.bluetooth.IBluetooth asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof android.bluetooth.IBluetooth))) {
return ((android.bluetooth.IBluetooth)iin);
}
return new android.bluetooth.IBluetooth.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_isEnabled:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isEnabled();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getBluetoothState:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getBluetoothState();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_enable:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.enable();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_disable:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
boolean _result = this.disable(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getAddress:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getAddress();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getName:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getName();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_setName:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _result = this.setName(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getScanMode:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getScanMode();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_setScanMode:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
boolean _result = this.setScanMode(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getDiscoverableTimeout:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getDiscoverableTimeout();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_setDiscoverableTimeout:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
boolean _result = this.setDiscoverableTimeout(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_startDiscovery:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.startDiscovery();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_cancelDiscovery:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.cancelDiscovery();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_isDiscovering:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isDiscovering();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_createBond:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _result = this.createBond(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_cancelBondProcess:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _result = this.cancelBondProcess(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_removeBond:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _result = this.removeBond(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_listBonds:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String[] _result = this.listBonds();
reply.writeNoException();
reply.writeStringArray(_result);
return true;
}
case TRANSACTION_getBondState:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _result = this.getBondState(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getRemoteName:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.getRemoteName(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getRemoteClass:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _result = this.getRemoteClass(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getRemoteUuids:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
android.os.ParcelUuid[] _result = this.getRemoteUuids(_arg0);
reply.writeNoException();
reply.writeTypedArray(_result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
return true;
}
case TRANSACTION_fetchRemoteUuids:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
android.os.ParcelUuid _arg1;
if ((0!=data.readInt())) {
_arg1 = android.os.ParcelUuid.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
android.bluetooth.IBluetoothCallback _arg2;
_arg2 = android.bluetooth.IBluetoothCallback.Stub.asInterface(data.readStrongBinder());
boolean _result = this.fetchRemoteUuids(_arg0, _arg1, _arg2);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getRemoteServiceChannel:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
android.os.ParcelUuid _arg1;
if ((0!=data.readInt())) {
_arg1 = android.os.ParcelUuid.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
int _result = this.getRemoteServiceChannel(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_setPin:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
byte[] _arg1;
_arg1 = data.createByteArray();
boolean _result = this.setPin(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_setPasskey:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
boolean _result = this.setPasskey(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_setPairingConfirmation:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _arg1;
_arg1 = (0!=data.readInt());
boolean _result = this.setPairingConfirmation(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_cancelPairingUserInput:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _result = this.cancelPairingUserInput(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_setTrust:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _arg1;
_arg1 = (0!=data.readInt());
boolean _result = this.setTrust(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getTrustState:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _result = this.getTrustState(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_addRfcommServiceRecord:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
android.os.ParcelUuid _arg1;
if ((0!=data.readInt())) {
_arg1 = android.os.ParcelUuid.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
int _arg2;
_arg2 = data.readInt();
android.os.IBinder _arg3;
_arg3 = data.readStrongBinder();
int _result = this.addRfcommServiceRecord(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_removeServiceRecord:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.removeServiceRecord(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements android.bluetooth.IBluetooth
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
public boolean isEnabled() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isEnabled, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public int getBluetoothState() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getBluetoothState, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public boolean enable() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_enable, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public boolean disable(boolean persistSetting) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((persistSetting)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_disable, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public java.lang.String getAddress() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getAddress, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public java.lang.String getName() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getName, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public boolean setName(java.lang.String name) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(name);
mRemote.transact(Stub.TRANSACTION_setName, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public int getScanMode() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getScanMode, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public boolean setScanMode(int mode, int duration) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(mode);
_data.writeInt(duration);
mRemote.transact(Stub.TRANSACTION_setScanMode, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public int getDiscoverableTimeout() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getDiscoverableTimeout, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public boolean setDiscoverableTimeout(int timeout) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(timeout);
mRemote.transact(Stub.TRANSACTION_setDiscoverableTimeout, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public boolean startDiscovery() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_startDiscovery, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public boolean cancelDiscovery() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_cancelDiscovery, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public boolean isDiscovering() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isDiscovering, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public boolean createBond(java.lang.String address) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(address);
mRemote.transact(Stub.TRANSACTION_createBond, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public boolean cancelBondProcess(java.lang.String address) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(address);
mRemote.transact(Stub.TRANSACTION_cancelBondProcess, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public boolean removeBond(java.lang.String address) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(address);
mRemote.transact(Stub.TRANSACTION_removeBond, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public java.lang.String[] listBonds() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_listBonds, _data, _reply, 0);
_reply.readException();
_result = _reply.createStringArray();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public int getBondState(java.lang.String address) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(address);
mRemote.transact(Stub.TRANSACTION_getBondState, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public java.lang.String getRemoteName(java.lang.String address) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(address);
mRemote.transact(Stub.TRANSACTION_getRemoteName, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public int getRemoteClass(java.lang.String address) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(address);
mRemote.transact(Stub.TRANSACTION_getRemoteClass, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public android.os.ParcelUuid[] getRemoteUuids(java.lang.String address) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
android.os.ParcelUuid[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(address);
mRemote.transact(Stub.TRANSACTION_getRemoteUuids, _data, _reply, 0);
_reply.readException();
_result = _reply.createTypedArray(android.os.ParcelUuid.CREATOR);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public boolean fetchRemoteUuids(java.lang.String address, android.os.ParcelUuid uuid, android.bluetooth.IBluetoothCallback callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(address);
if ((uuid!=null)) {
_data.writeInt(1);
uuid.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_fetchRemoteUuids, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public int getRemoteServiceChannel(java.lang.String address, android.os.ParcelUuid uuid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(address);
if ((uuid!=null)) {
_data.writeInt(1);
uuid.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_getRemoteServiceChannel, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public boolean setPin(java.lang.String address, byte[] pin) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(address);
_data.writeByteArray(pin);
mRemote.transact(Stub.TRANSACTION_setPin, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public boolean setPasskey(java.lang.String address, int passkey) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(address);
_data.writeInt(passkey);
mRemote.transact(Stub.TRANSACTION_setPasskey, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public boolean setPairingConfirmation(java.lang.String address, boolean confirm) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(address);
_data.writeInt(((confirm)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_setPairingConfirmation, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public boolean cancelPairingUserInput(java.lang.String address) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(address);
mRemote.transact(Stub.TRANSACTION_cancelPairingUserInput, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public boolean setTrust(java.lang.String address, boolean value) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(address);
_data.writeInt(((value)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_setTrust, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public boolean getTrustState(java.lang.String address) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(address);
mRemote.transact(Stub.TRANSACTION_getTrustState, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public int addRfcommServiceRecord(java.lang.String serviceName, android.os.ParcelUuid uuid, int channel, android.os.IBinder b) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(serviceName);
if ((uuid!=null)) {
_data.writeInt(1);
uuid.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeInt(channel);
_data.writeStrongBinder(b);
mRemote.transact(Stub.TRANSACTION_addRfcommServiceRecord, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public void removeServiceRecord(int handle) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(handle);
mRemote.transact(Stub.TRANSACTION_removeServiceRecord, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_isEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_getBluetoothState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_enable = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_disable = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_getAddress = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_getName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_setName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_getScanMode = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_setScanMode = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_getDiscoverableTimeout = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_setDiscoverableTimeout = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_startDiscovery = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_cancelDiscovery = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
static final int TRANSACTION_isDiscovering = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
static final int TRANSACTION_createBond = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
static final int TRANSACTION_cancelBondProcess = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
static final int TRANSACTION_removeBond = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
static final int TRANSACTION_listBonds = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
static final int TRANSACTION_getBondState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
static final int TRANSACTION_getRemoteName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 19);
static final int TRANSACTION_getRemoteClass = (android.os.IBinder.FIRST_CALL_TRANSACTION + 20);
static final int TRANSACTION_getRemoteUuids = (android.os.IBinder.FIRST_CALL_TRANSACTION + 21);
static final int TRANSACTION_fetchRemoteUuids = (android.os.IBinder.FIRST_CALL_TRANSACTION + 22);
static final int TRANSACTION_getRemoteServiceChannel = (android.os.IBinder.FIRST_CALL_TRANSACTION + 23);
static final int TRANSACTION_setPin = (android.os.IBinder.FIRST_CALL_TRANSACTION + 24);
static final int TRANSACTION_setPasskey = (android.os.IBinder.FIRST_CALL_TRANSACTION + 25);
static final int TRANSACTION_setPairingConfirmation = (android.os.IBinder.FIRST_CALL_TRANSACTION + 26);
static final int TRANSACTION_cancelPairingUserInput = (android.os.IBinder.FIRST_CALL_TRANSACTION + 27);
static final int TRANSACTION_setTrust = (android.os.IBinder.FIRST_CALL_TRANSACTION + 28);
static final int TRANSACTION_getTrustState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 29);
static final int TRANSACTION_addRfcommServiceRecord = (android.os.IBinder.FIRST_CALL_TRANSACTION + 30);
static final int TRANSACTION_removeServiceRecord = (android.os.IBinder.FIRST_CALL_TRANSACTION + 31);
}
public boolean isEnabled() throws android.os.RemoteException;
public int getBluetoothState() throws android.os.RemoteException;
public boolean enable() throws android.os.RemoteException;
public boolean disable(boolean persistSetting) throws android.os.RemoteException;
public java.lang.String getAddress() throws android.os.RemoteException;
public java.lang.String getName() throws android.os.RemoteException;
public boolean setName(java.lang.String name) throws android.os.RemoteException;
public int getScanMode() throws android.os.RemoteException;
public boolean setScanMode(int mode, int duration) throws android.os.RemoteException;
public int getDiscoverableTimeout() throws android.os.RemoteException;
public boolean setDiscoverableTimeout(int timeout) throws android.os.RemoteException;
public boolean startDiscovery() throws android.os.RemoteException;
public boolean cancelDiscovery() throws android.os.RemoteException;
public boolean isDiscovering() throws android.os.RemoteException;
public boolean createBond(java.lang.String address) throws android.os.RemoteException;
public boolean cancelBondProcess(java.lang.String address) throws android.os.RemoteException;
public boolean removeBond(java.lang.String address) throws android.os.RemoteException;
public java.lang.String[] listBonds() throws android.os.RemoteException;
public int getBondState(java.lang.String address) throws android.os.RemoteException;
public java.lang.String getRemoteName(java.lang.String address) throws android.os.RemoteException;
public int getRemoteClass(java.lang.String address) throws android.os.RemoteException;
public android.os.ParcelUuid[] getRemoteUuids(java.lang.String address) throws android.os.RemoteException;
public boolean fetchRemoteUuids(java.lang.String address, android.os.ParcelUuid uuid, android.bluetooth.IBluetoothCallback callback) throws android.os.RemoteException;
public int getRemoteServiceChannel(java.lang.String address, android.os.ParcelUuid uuid) throws android.os.RemoteException;
public boolean setPin(java.lang.String address, byte[] pin) throws android.os.RemoteException;
public boolean setPasskey(java.lang.String address, int passkey) throws android.os.RemoteException;
public boolean setPairingConfirmation(java.lang.String address, boolean confirm) throws android.os.RemoteException;
public boolean cancelPairingUserInput(java.lang.String address) throws android.os.RemoteException;
public boolean setTrust(java.lang.String address, boolean value) throws android.os.RemoteException;
public boolean getTrustState(java.lang.String address) throws android.os.RemoteException;
public int addRfcommServiceRecord(java.lang.String serviceName, android.os.ParcelUuid uuid, int channel, android.os.IBinder b) throws android.os.RemoteException;
public void removeServiceRecord(int handle) throws android.os.RemoteException;
}
