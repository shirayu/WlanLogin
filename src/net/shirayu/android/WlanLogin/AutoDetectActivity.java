package net.shirayu.android.WlanLogin;


import net.shirayu.android.WlanLogin.R;
import android.app.Activity;
import android.os.Bundle;

public class AutoDetectActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.autodetect);
		
		/*
		Auther auther= new Auther(this);
    	AuthData authData;
    	final AuthInfoSQLitepenHelper db_mng = new AuthInfoSQLitepenHelper(this);
		try {
			authData = db_mng.getAll_field("eFdwdN14JquL" , true);
		} catch (IOException e) {
			return;
		}
		auther.doAuth(authData);
		*/
	}
}
