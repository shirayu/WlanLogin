package net.shirayu.android.WlanLogin;

/*
 *  THIS FILE IS FROM "FerecAuth"
 *  2-clause BSD license
 */

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.IBinder;

public class WlanAuthService extends Service {
	static boolean isRunning = false;
	private WifiConnectReceiver receiver;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// Register broadcast intent receiver
		receiver = new WifiConnectReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		registerReceiver(receiver, filter);
		
		isRunning = true;
	};

	@Override
	public void onStart(Intent intent, int startId) {
		// Nothing to do
	}

	@Override
	public void onDestroy() {
		// Unregister broadcast intent receiver
		unregisterReceiver(receiver);
		
		isRunning = false;
	};


	
}