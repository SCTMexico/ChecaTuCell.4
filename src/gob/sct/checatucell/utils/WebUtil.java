package gob.sct.checatucell.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.util.Log;

public class WebUtil {
	
	private static final String DEBUG_TAG = "WebUtil";

	public static String downloadPage(String url){
        Log.v( DEBUG_TAG , url);                        

	 String page = "";
	   	 
	   	 BufferedReader in = null;
	        try {       	
	       	 	HttpClient client = new DefaultHttpClient();	       	 	
	            HttpGet request = new HttpGet(url);	            
	            HttpParams params = request.getParams();	            
	            HttpConnectionParams.setSoTimeout(params, 60000); // 1 minute timeout	            
	            HttpResponse response = client.execute(request);
	            
	            //Predifined buffer size
	            /*
	             * 
	            in = new BufferedReader(
	                    new InputStreamReader(
	                	response.getEntity().getContent()),8*2000);
	             * 
	             * */
	            	           
	            	in = new BufferedReader(
		                    new InputStreamReader(
		                	response.getEntity().getContent()));
	            
	            StringBuffer sb = new StringBuffer("");            
	            String line = "";
	            //String NL = System.getProperty("line.separator");
	            while ((line = in.readLine()) != null) {
	                sb.append(line);
	            }
	            in.close();
	            page = sb.toString();             
	            Log.v( DEBUG_TAG , "Pagina descargada --> "+ page);                        
	        } catch (Exception e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			} finally {
	            if (in != null) {
	                try {
	                    in.close();
	                } catch (IOException e) {
	                	 Log.v( DEBUG_TAG , "Error en la descarga de la pagina");    
	                    e.printStackTrace();
	                }
	            }
	        }
	   	return page;
	}

}
