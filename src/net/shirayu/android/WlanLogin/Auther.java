package net.shirayu.android.WlanLogin;


import java.io.IOException;
import java.security.KeyStore;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

public class Auther {
	Context context;
	SharedPreferences sharedPreferences;

	public Auther(Context contextArg) {
		context = contextArg;
	}

	public void doAuth(final AuthData authData) {
		String postArg = 
			authData.pass_fld + "=" + authData.pass +
			"&" +  authData.id_fld + "=" + authData.id
			+"&" + authData.hidden ;
		
		try {
			//FIXME Prepare the client because its generation may have heavy load.

			StringEntity paramEntity = new StringEntity(postArg);
			paramEntity.setChunked(false);
			paramEntity.setContentType("application/x-www-form-urlencoded");
			HttpPost method = new HttpPost(authData.url);
			method.setEntity(paramEntity);

			KeyStore certstore = MyHttpClient.loadKeyStore(this.context);
			MyHttpClient client = new MyHttpClient(certstore);
			//HttpResponse res = 
				client.execute(method);
			//HttpEntity entity = res.getEntity();
			//InputStream stream = entity.getContent();
			Toast.makeText(context, context.getResources().getString(R.string.login_finished), Toast.LENGTH_SHORT  ).show();
		} catch (Exception e) {
			/*
		    StringWriter s = new StringWriter();
		    PrintWriter prt = new PrintWriter(s);
		    e.printStackTrace(prt);
		    Log.e("err", s.toString());
		    */
			Toast.makeText(context, context.getResources().getString(R.string.login_error), Toast.LENGTH_SHORT  ).show();
		}
	}

	public void doAuth() {
		final String ssid = this.getSSID();
		if (ssid==null){ //if SSID is null, do nothing
			return;
		};
		
    	final AuthInfoSQLitepenHelper db_mng = new AuthInfoSQLitepenHelper(this.context);
    	AuthData authData;
		try {
			authData = db_mng.getAll_field( ssid, true);
		} catch (IOException e) {
			//Toast.makeText(context, "Not Found=> " + ssid, Toast.LENGTH_SHORT).show();	
			//not found
			return;
		}
    	this.doAuth(authData);
	}
	
	protected String getSSID() {
		WifiManager wifiMan;
		WifiInfo wifiInfo;

		wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		wifiInfo = wifiMan.getConnectionInfo();

		return wifiInfo.getSSID();
	}

}