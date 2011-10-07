package net.shirayu.android.WlanLogin;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class AdvancePrefActivity extends PreferenceActivity {
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        addPreferencesFromResource(R.xml.advance_pref);
	    }
}
