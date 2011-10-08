 package android.bluetooth;

    /**
     * System private API for Bluetooth service callbacks.
     * See http://stackoverflow.com/questions/3462968/how-to-unpair-bluetooth-device-using-android-2-1-sdk
     */
    interface IBluetoothCallback
    {
        void onRfcommChannelFound(int channel);
    }
