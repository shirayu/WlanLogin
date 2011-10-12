package net.shirayu.android.WlanLogin;

//import android.app.Activity;
import java.io.IOException;

import net.shirayu.android.WlanLogin.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

class AuthEditClickListener implements View.OnClickListener{
	private Context context;
	private String original_ssid;
	private boolean newid;
	private int position; 
	
	AuthEditClickListener(Context context, String original_ssid, boolean newid, int position){
		this.context = context;
		this.original_ssid = original_ssid;
		this.newid = newid;
		this.position = position;
	}
	@Override
	public void onClick(View v) {
		final String new_ssid = ((EditText) ((Activity) context).findViewById(R.id.ssid)).getText().toString();
		if ( new_ssid.equals("") ){
	        Toast.makeText(context, R.string.confirm_input_ssid, Toast.LENGTH_SHORT).show();
		}
		else{
	    	AuthInfoSQLitepenHelper db_mng = new AuthInfoSQLitepenHelper(context);
			if (this.newid==true & db_mng.hasSsid(new_ssid, false) ){
				Toast.makeText(context, R.string.confirm_duplicate_ssid, Toast.LENGTH_SHORT).show();
			}
			else{
				update_this_data();
				Intent intent = new Intent();
				intent.putExtra("original_ssid", original_ssid);
				intent.putExtra("ssid", new_ssid );
				intent.putExtra("position", position);
				((Activity) context).setResult(Activity.RESULT_OK, intent);
				((Activity) context).finish();
			};
		};
	};
	
    private void update_this_data(){
    	AuthInfoSQLitepenHelper db_mng = new AuthInfoSQLitepenHelper(context);
    	db_mng.update( original_ssid, new AuthData(
    			((EditText) ((Activity) context).findViewById(R.id.ssid)).getText().toString(),
    			((EditText) ((Activity) context).findViewById(R.id.id)).getText().toString(),
    			((EditText) ((Activity) context).findViewById(R.id.id_fld)).getText().toString(),
    			((EditText) ((Activity) context).findViewById(R.id.pass)).getText().toString(),
    			((EditText) ((Activity) context).findViewById(R.id.pass_fld)).getText().toString(),
    			((EditText) ((Activity) context).findViewById(R.id.url)).getText().toString(),
    			((EditText) ((Activity) context).findViewById(R.id.hidden)).getText().toString(),
    			((CheckBox)(((Activity) context).findViewById(R.id.active))).isChecked()
    	) );
    };  
 
}


//public class WlanLoginActivity extends Activity {
public class AuthEditActivity extends Activity {
	private String original_ssid;
	private boolean newid;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_edit);
        //activate link
		((TextView) findViewById(R.id.hint_setting)).setMovementMethod(LinkMovementMethod.getInstance());
        
        //set parameters to the field
        Bundle extras = getIntent().getExtras();
        this.original_ssid = extras.getString("ssid");
        this.newid = extras.getBoolean("new");
        final int position = extras.getInt("position");
        this.set_field_data();
        
        //Set of Buttons
        Button save_btn = (Button) findViewById(R.id.button_save);
        AuthEditClickListener aec = new AuthEditClickListener(this, original_ssid, newid, position);
        save_btn.setOnClickListener( aec );
        Button delete_Button = (Button) findViewById(R.id.button_delete);
        delete_Button.setOnClickListener( 
        		new View.OnClickListener() {
        			@Override
        			public void onClick(View v) {
        				new AlertDialog.Builder(AuthEditActivity.this)
        				.setMessage( getResources().getString(R.string.prefs_delete_confirm) )
        				.setCancelable(false)
        				.setPositiveButton( getResources().getString(R.string.confirm_yes), new DialogInterface.OnClickListener() {
        					public void onClick(DialogInterface dialog, int id) {
        						final boolean deleted = delete_this_data();
        						if(deleted){
	        		    			Intent intent = new Intent();
	        		    			intent.putExtra("original_ssid", original_ssid);
	        		    			intent.putExtra("ssid", "");
	        		    			intent.putExtra("position", position);
	        						setResult(RESULT_OK, intent);
	        						finish();
        						}
        						else{
	        						setResult(RESULT_CANCELED);
	        						finish();
        						}
        				      }
        				  })
        				  .setNegativeButton( getResources().getString(R.string.confirm_no) , new DialogInterface.OnClickListener() {
        				      public void onClick(DialogInterface dialog, int id) {
        				           dialog.cancel();
        				      }
        				  })
        				  .show();
        			}
        	    }
        );
        Button cancel_Button = (Button) findViewById(R.id.button_cancel);
        cancel_Button.setOnClickListener( cancel_ButtonListner);
    }
    
    private View.OnClickListener cancel_ButtonListner = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			setResult(RESULT_CANCELED);
			finish();
		}
	};
	

    private void set_field_data(){
        
        if(this.newid ){
        	//do nothing
        }
        else{
        	AuthInfoSQLitepenHelper db_mng = new AuthInfoSQLitepenHelper(this);
        	AuthData authData;
			try {
				authData = db_mng.getAll_field(this.original_ssid, false);
			} catch (IOException e) {
				//not found
				return;
			}
        	
        	((EditText) findViewById(R.id.ssid)).setText( this.original_ssid );
        	((EditText) findViewById(R.id.id)).setText( authData.id);
        	((EditText) findViewById(R.id.id_fld)).setText( authData.id_fld);
        	((EditText) findViewById(R.id.pass)).setText( authData.pass);
        	((EditText) findViewById(R.id.pass_fld)).setText( authData.pass_fld);
        	((EditText) findViewById(R.id.url)).setText( authData.url);
        	((EditText) findViewById(R.id.hidden)).setText( authData.hidden);
        	((CheckBox) findViewById(R.id.active)).setChecked(authData.active );
        };
    };
    

    private boolean delete_this_data(){
        if(this.newid ){
        	//do nothing
        	return false;
        }
        else{
        	AuthInfoSQLitepenHelper db_mng = new AuthInfoSQLitepenHelper(this);
        	db_mng.delete(original_ssid);
        	return true;
        }
    };

    
    
}
