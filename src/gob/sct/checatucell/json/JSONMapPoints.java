package gob.sct.checatucell.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONMapPoints {
	
	//URL to get JSON Array
	//private static String url = "http://ttr.sct.gob.mx/qoscell/web/QOSCSENALGEO/view.action";
	
	//JSON Node Names 
	private static final String TAG_DATA = "data";
	private static final String TAG_LAT = "dlatitud";
	private static final String TAG_LON = "dlongitud";
	private static final String TAG_ALT = "iOperador";
	private static final String TAG_INT = "dintensidad";
	private static final String TAG_BND = "danchobanda";
	private static final String TAG_ID = "tsregistro";
	private static final String TAG_CTYPE = "iTipoConex";
	
	
	JSONArray data = null;
	JSONParser jParser;
	JSONObject json;

    
      
    public int initJSONParser(String url, double lat, double lon, double radius, int company){
        int jArrayLength = 0;
		// Creating new JSON Parser
		jParser = new JSONParser();

		// Getting JSON from URL
		json = jParser.getJSONFromUrl(url,lat,lon,radius,company);
		if(json!=null)
		try {
			data = json.getJSONArray(TAG_DATA);
			jArrayLength = data.length();
			return jArrayLength;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return jArrayLength;

		}
		
		return jArrayLength;

    }
    
		
    public String[] getJSONMapPoints(int jsonArrayIndex){	
        
	    String [] pointValues = new String[]{"*"};
	        	
		try {
				// Getting JSON Array
				JSONObject jsonObj;
				jsonObj = data.getJSONObject(jsonArrayIndex);
				
				// Storing  JSON item in a Variable
				pointValues = new String[]{ jsonObj.getString(TAG_LAT),
											jsonObj.getString(TAG_LON),
											jsonObj.getString(TAG_ALT),
											jsonObj.getString(TAG_INT),
											jsonObj.getString(TAG_BND),
											jsonObj.getString(TAG_ID),
											jsonObj.getString(TAG_CTYPE)};
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	
		return pointValues;
	
     }    

}
