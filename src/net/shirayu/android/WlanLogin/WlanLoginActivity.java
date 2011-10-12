package net.shirayu.android.WlanLogin;

/*
 *  THIS FILE IS FROM  WifiConnectReceiver.java of "FerecAuth"
 *  2-clause BSD license
 */

//import android.app.Activity;
import net.shirayu.android.WlanLogin.R;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;

//public class WlanLoginActivity extends Activity {
public class WlanLoginActivity extends PreferenceActivity {
	private Intent authServiceIntent;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        addPreferencesFromResource(R.xml.pref);
    	authServiceIntent = new Intent(getApplicationContext(),	WlanAuthService.class);
    	
        CheckBoxPreference checkBoxPreference1 = (CheckBoxPreference) findPreference(Const.exec_autoauth);
        if(checkBoxPreference1.isChecked()){
        	startService(authServiceIntent);
        };
        checkBoxPreference1.setOnPreferenceChangeListener(new OnPreferenceChangeListener(){ 
                                @Override
                                public boolean onPreferenceChange(Preference preference, Object newValue) {
                                		final boolean myNewValue =  (Boolean)newValue;
                                        ((CheckBoxPreference) preference).setChecked(myNewValue);
                                        if(myNewValue){
                                        	//Log.i("TEST", "start");
                                    		startService(authServiceIntent);
                                        }
                                        else{
                                        	//Log.i("TEST", "stop");
                                    		stopService(authServiceIntent);
                                        }
                                        return myNewValue; 
                                }
        				});

        CheckBoxPreference checkBoxPreference2 = (CheckBoxPreference) findPreference(Const.exec_autolunch); 
        checkBoxPreference2.setOnPreferenceChangeListener(new OnPreferenceChangeListener(){ 
                                @Override
                                public boolean onPreferenceChange(Preference preference, Object newValue) { 
                                        ((CheckBoxPreference) preference).setChecked((Boolean)newValue); 
                                        return false; 
                                }
        				});
    }
    
}