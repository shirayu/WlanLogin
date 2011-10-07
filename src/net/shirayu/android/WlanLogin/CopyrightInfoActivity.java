package net.shirayu.android.WlanLogin;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class CopyrightInfoActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.copyright_info);
		
		PackageInfo packageInfo = null;

		TextView tv = (TextView) findViewById(R.id.version_info);
        try {
			packageInfo = getPackageManager().getPackageInfo("net.shirayu.android.WlanLogin", PackageManager.GET_META_DATA);
			tv.setText("  WlanLogin  " +"ver."+packageInfo.versionName + " (Code:" + packageInfo.versionCode + ")" );
		} catch (NameNotFoundException e) {
			tv.setText("Unknown Version.");
		}	
		Drawable icon = getResources().getDrawable(R.drawable.icon);
	    icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
	    tv.setCompoundDrawables(icon, null, null, null);

		
        //activate link
		((TextView) findViewById(R.id.copyright_text)).setMovementMethod(LinkMovementMethod.getInstance());
	}
}
