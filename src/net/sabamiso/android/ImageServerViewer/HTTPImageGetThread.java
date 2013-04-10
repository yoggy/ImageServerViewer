package net.sabamiso.android.ImageServerViewer;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HTTPImageGetThread extends Thread {

	String url = "";
	int status_code = 0;
	byte [] body_data;
	boolean break_flag = false;
	
	DefaultHttpClient http_client;
	HttpGet http_get;
	
    byte [] buf = new byte[4096];
	
	HTTPImageGetThreadUpdateListener listener;
	
	public HTTPImageGetThread() {
	}

	public void setUrl(String string) {
		url = string;
	}

	public String getUrl() {
		return url;
	}

	public void setUpdateListener(HTTPImageGetThreadUpdateListener listener) {
		this.listener = listener;
	}
	
	public int getStatusCode() {
		return status_code;
	}

	public byte [] body() {
		return null;
	}

	public Bitmap getBitmap() {
		if (body_data == null) return null;
		
		Bitmap bmp;
		try {
			bmp = BitmapFactory.decodeByteArray(body_data, 0,
					body_data.length);
		}
		catch (Exception ex) {
			return null;
		}
		return bmp;
	}
	
	@Override
	public void start() {
		http_client = new DefaultHttpClient();
		http_get = new HttpGet(url);
		
		super.start();
	}
	
	@Override
	public void run() {
		while(!break_flag) {
			doHttpGet();
			if (listener != null) {
				listener.update(this);
			}
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
	}

	public void stopThread() {
		break_flag = true;
		try {
			this.join();
		} catch (InterruptedException e) {
		}
	}

	private boolean doHttpGet() {
		HttpResponse res;
		try {
			res = http_client.execute( http_get );
	        status_code = res.getStatusLine().getStatusCode();
	        
	        if (status_code != HttpStatus.SC_OK) {
				body_data = null;
				return false;
	        }
	        
	        InputStream is = res.getEntity().getContent();
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();

	        while(true) {
	            int len = is.read(buf);
	            if(len < 0) {
	                break;
	            }
	            baos.write(buf, 0, len);
	        }
	        
			body_data = baos.toByteArray();
		} catch (Exception e) {
			status_code = -1;
			body_data = null;
			return false;
		}
		return true;
	}

}
