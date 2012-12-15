package net.shirayu.android.WlanLogin;

/*
 * From http://my.opera.com/crckyl/blog/2011/02/27/android-self-signed-ssl
 * Apache License
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertPath;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthenticationHandler;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;


public class MyHttpClient
implements HttpClient,
           CredentialsProvider,
           HttpResponseInterceptor {
	private DefaultHttpClient client = null;
	private String username = null;
	private String password = null;
	private boolean stop_auth = false;
	
	public MyHttpClient(KeyStore certstore) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
		HttpParams params = new BasicHttpParams();
		SSLSocketFactory sf = new SSLSocketFactory(certstore);
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", sf, 443));
		ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, schemeRegistry);
		client = new DefaultHttpClient(ccm, params);
		client.setCredentialsProvider(this);
		client.addResponseInterceptor(this);
	}

	public void clear() {
	}
	
	public Credentials getCredentials(AuthScope authscope) {
		if ((username != null && username.length() > 0) ||
		    (password != null && password.length() > 0)) {
		  stop_auth = true;
		  return new UsernamePasswordCredentials(username == null ? "" : username,
		                                         password == null ? "" : password);
		} else {
		  throw new RuntimeException("Authentication required");
		}
	}
	
	public void setCredentials(AuthScope authscope, Credentials credentials) {
	}

	public void process(HttpResponse response, HttpContext context)
	  throws HttpException, IOException {
	AuthenticationHandler handler = client.getTargetAuthenticationHandler();
	if (stop_auth && handler.isAuthenticationRequested(response, context)) {
	  throw new ClientProtocolException("Authentication failed");
	}
	}

	public HttpResponse execute(HttpUriRequest request)
	  throws IOException, ClientProtocolException {
	stop_auth = false;
	try {
	  return client.execute(request);
	} catch (RuntimeException ex) {
	  throw new ClientProtocolException(ex.getMessage());
	}
	}

	public HttpResponse execute(HttpUriRequest request, HttpContext context)
	  throws IOException, ClientProtocolException {
	stop_auth = false;
	try {
	  return client.execute(request, context);
	} catch (RuntimeException ex) {
	  throw new ClientProtocolException(ex.getMessage());
	}
	}

	public HttpResponse execute(HttpHost target, HttpRequest request)
	  throws IOException, ClientProtocolException {
	stop_auth = false;
	try {
	  return client.execute(target, request);
	} catch (RuntimeException ex) {
	  throw new ClientProtocolException(ex.getMessage());
	}
	}

	public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> handler)
	  throws IOException, ClientProtocolException {
	stop_auth = false;
	try {
	  return client.execute(request, handler);
	} catch (RuntimeException ex) {
	  throw new ClientProtocolException(ex.getMessage());
	}
	}

	public HttpResponse execute(HttpHost target,
	                          HttpRequest request,
	                          HttpContext context)
	  throws IOException, ClientProtocolException {
	stop_auth = false;
	try {
	  return client.execute(target, request, context);
	} catch (RuntimeException ex) {
	  throw new ClientProtocolException(ex.getMessage());
	}
	}
	
	public <T> T execute(HttpUriRequest request,
	                   ResponseHandler<? extends T> handler,
	                   HttpContext context)
	  throws IOException, ClientProtocolException {
	stop_auth = false;
	try {
	  return client.execute(request, handler, context);
	} catch (RuntimeException ex) {
	  throw new ClientProtocolException(ex.getMessage());
	}
	}
	
	public <T> T execute(HttpHost host,
	                   HttpRequest request,
	                   ResponseHandler<? extends T> handler)
	  throws IOException, ClientProtocolException {
	stop_auth = false;
	try {
	  return client.execute(host, request, handler);
	} catch (RuntimeException ex) {
	  throw new ClientProtocolException(ex.getMessage());
	}
	}
	
	public <T> T execute(HttpHost host,
	                   HttpRequest request,
	                   ResponseHandler<? extends T> handler,
	                   HttpContext context)
	  throws IOException, ClientProtocolException {
	stop_auth = false;
	try {
	  return client.execute(host, request, handler, context);
	} catch (RuntimeException ex) {
	  throw new ClientProtocolException(ex.getMessage());
	}
	}
	
	public ClientConnectionManager getConnectionManager() {
	return client.getConnectionManager();
	}
	
	public HttpParams getParams() {
	return client.getParams();
	}
	
	
	
	
	
	  public static X509Certificate readPem(InputStream stream) throws CertificateException, NoSuchProviderException, IOException {
		    CertPath cp;
		    try {
		      CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");
		      cp = cf.generateCertPath(stream, "PEM");
		    } finally {
		      stream.close();
		    }
		    List<? extends Certificate> certs = cp.getCertificates();
		    if (certs.size() < 1) {
		      throw new CertificateException("Certificate list is empty");
		    } else if (certs.size() > 1) {
		      throw new CertificateException("Intermediate certificate is not allowed");
		    }
		    if (certs.get(0) instanceof X509Certificate) {
		      X509Certificate cert = (X509Certificate)certs.get(0);
		      cert.checkValidity();
		      return cert;
		    } else {
		      throw new CertificateException("Certificate is not X509Certificate");
		    }
		  }
	  
	  
	  public static KeyStore loadKeyStore(Context context) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, NoSuchProviderException{
			/*
			 * Original implementation.
				I refer the following sites about the default keystore.
				http://wiki.livedoor.jp/syo1976/d/javassl
				http://d.hatena.ne.jp/Kazzz/20110319/p1
			 */
			KeyStore certstore;
			if (Integer.valueOf(Build.VERSION.SDK) >= 14) {
				// load from unified key store
				certstore = KeyStore.getInstance("AndroidCAStore");
				certstore.load(null, null);
			} else {
				certstore =  KeyStore.getInstance(KeyStore.getDefaultType());
				certstore.load(new FileInputStream(System.getProperty("javax.net.ssl.trustStore")), null); //load default keystore
			}

			//load self_signed_certificate?
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
			final Boolean use_self_signed_certificate = sharedPreferences.getBoolean("use_self_signed_certificate", false);
			if(use_self_signed_certificate){
				final Boolean use_folder = sharedPreferences.getBoolean("use_self_signed_certificate_folder", false);
				final String filename = sharedPreferences.getString("self_signed_certificate_path", "//");
				File myfile = new File( filename );
				
				if(use_folder){
					for(File file : new File(myfile.getParent()).listFiles()) {
						if (file.isDirectory()) continue;
						FileInputStream stream = new FileInputStream(file);
					    X509Certificate cert = MyHttpClient.readPem(stream);
					    certstore.setCertificateEntry( file.getName(), cert);
					}
				}
				else{
					FileInputStream stream = new FileInputStream(myfile);
				    X509Certificate cert = MyHttpClient.readPem(stream);
				    certstore.setCertificateEntry( myfile.getName(), cert);
				};
			};
			
			return certstore;
	  };
	  
}
 
 