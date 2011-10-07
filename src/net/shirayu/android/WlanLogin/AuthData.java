package net.shirayu.android.WlanLogin;


public class AuthData {
	public AuthData(){};
	public AuthData(String ssid, String id, String idFld, String pass,
			String passFld, String url, String hidden, boolean active) {
		super();
		this.ssid = ssid;
		this.id = id;
		this.id_fld = idFld;
		this.pass = pass;
		this.pass_fld = passFld;
		this.url = url;
		this.hidden = hidden;
		this.active = active;
	}
	public String ssid;
	public String id;
	public String id_fld;
	public String pass;
	public String pass_fld;
	public String url;
	public String hidden;
	public boolean active;
}
