package net.shirayu.android.WlanLogin;


import java.io.IOException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;


public class AuthInfoSQLitepenHelper extends SQLiteOpenHelper {

	static final String DB = "auth_info.db";
	static final int DB_VERSION = 1;
	static final String TABLENAME = "mytable";
	static final String CREATE_TABLE = "create table " + TABLENAME + 
			" (" +
			" ssid TEXT primary key ," +
			" id TEXT not null," +
			" id_fld TEXT not null," +
			" pass TEXT," +
			" pass_fld TEXT," +
			" url TEXT," +
			" hidden TEXT," +
			" active INTEGER" +
			" );";
	static final String DROP_TABLE = "drop table " + TABLENAME + " ;";
	static final String GET_ALL_SSID = "select ssid from "  + TABLENAME + " ;";
	static final String GET_ALL_FIELD = "select * from "  + TABLENAME + " where ssid='%s' ;";
	static final String GET_ALL_FIELD_ACTIVE = "select * from "  + TABLENAME + " where ssid='%s' and active=1 ;";
	
	public AuthInfoSQLitepenHelper(Context context) {
		super( context, DB, null, DB_VERSION );
	}
	private void insert(SQLiteDatabase db, AuthData authData){
		//SAMPLE
        ContentValues values = new ContentValues();
        String PARAM[] = {"ssid", "id", "id_fld", "pass", "pass_fld", "hidden", "url"};
        String VAL[] = {authData.ssid, authData.id, authData.id_fld, authData.pass,
        		authData.pass_fld, authData.hidden, authData.url};
        for(int i=0; i<PARAM.length; ++i){
            values.put( PARAM[i], VAL[i]);
        }
        values.put("active", authData.active?1:0 );          
        this.insert(db, values);	
	};
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
		
        this.insert(db, new AuthData( "mm2010", "", "user", "", "password", "https://aruba.naist.jp/cgi-bin/login", "cmd=authenticate", false ) );
        this.insert(db, new AuthData( "mobilepoint", "", "id", "", "pw", "https://www.login2.w-lan.jp/signup/Authentication", "", false ) );

	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		  db.execSQL( DROP_TABLE );
          onCreate(db);
	}

    public void insert(SQLiteDatabase db, ContentValues values) {
        //SQLiteDatabase db = super.getWritableDatabase();
        db.insert(TABLENAME, null, values); 
        //db.close();
    }

    public void setAll_SSID(ArrayAdapter<String> adapter){
    	try{
	        SQLiteDatabase db = super.getWritableDatabase();
	        Cursor c = db.rawQuery(GET_ALL_SSID, null);
	 
	        boolean isEof = c.moveToFirst();
	        while (isEof) {
	        	adapter.add( c.getString(0) );
	            isEof = c.moveToNext();
	        }
	        c.close();
	        db.close();
	        
    	}
    	catch(SQLiteException e){
    		adapter.add("Erorr:" + e.getMessage());
    	};
    };
    
    private String escape_quote(String query){
    	return query.replaceAll("'", "''");
    }
    
    public AuthData getAll_field(final String ssid, final boolean activeConstraint) throws IOException{
	        SQLiteDatabase db = super.getReadableDatabase();
	 
	        Cursor c;
	        if(activeConstraint){
	        	c = db.rawQuery( String.format(GET_ALL_FIELD_ACTIVE, escape_quote(ssid) ), null);
	        }
	        else{
	        	c = db.rawQuery( String.format(GET_ALL_FIELD, escape_quote(ssid) ), null);
	        };
	        boolean isEof = c.moveToFirst();
	        AuthData authData = new AuthData();
	        if (isEof) {
	        	 authData.ssid = c.getString(0);
	        	 authData.id = c.getString(1);
	        	 authData.id_fld = c.getString(2);
	        	 authData.pass = c.getString(3);
	        	 authData.pass_fld = c.getString(4);
	        	 authData.url = c.getString(5);
	        	 authData.hidden = c.getString(6);
	        	 if ( c.getInt(7) == 1 )
	        		 authData.active = true;
	        	 else
	        		 authData.active = false;
	        }
	        else {
				throw new IOException(); 
			}
	        c.close();
	        db.close();
	        return authData;
    };
    

    public boolean hasSsid(final String ssid, final boolean activeConstraint){
        SQLiteDatabase db = super.getReadableDatabase();
 
        Cursor c;
        if(activeConstraint){
        	c = db.rawQuery( String.format(GET_ALL_FIELD_ACTIVE, escape_quote(ssid) ), null);
        }
        else{
        	c = db.rawQuery( String.format(GET_ALL_FIELD, escape_quote(ssid) ), null);
        };
        boolean isEof = c.moveToFirst();
        boolean hasSsid = false;
        if (isEof) {
        	 hasSsid = true;
        };
        c.close();
        db.close();
        return hasSsid;
};


	public void update(final String ssid, final AuthData authData) {
		SQLiteDatabase db = super.getWritableDatabase();
		this.delete(db, ssid);
		this.insert(db, authData);
	    db.close();
	}
	
	public void delete(SQLiteDatabase db, final String ssid) {
        db.delete(TABLENAME, "ssid='" + escape_quote(ssid) + "'", null);
	};
 
	public void delete(final String ssid) {
		SQLiteDatabase db = super.getWritableDatabase();
        this.delete(db, ssid);
	    db.close();
	};
}
