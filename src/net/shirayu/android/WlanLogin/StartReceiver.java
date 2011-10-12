package net.shirayu.android.WlanLogin;

/*
 *  THIS FILE IS FROM "FerecAuth"
 *  2-clause BSD license
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class StartReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());

                if (sharedPreferences.getBoolean(Const.exec_autolunch, false)) {
                	Intent authServiceIntent = new Intent(context.getApplicationContext(),
                                    WlanAuthService.class);
                        context.startService(authServiceIntent);
                }
        }
}
