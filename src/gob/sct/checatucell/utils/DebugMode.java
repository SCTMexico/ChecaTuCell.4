package gob.sct.checatucell.utils;

import android.util.Log;

public class DebugMode {

	private static final boolean IS_DEBUGING = true; 
	
	public static void logger(String tag, String message){
		if(IS_DEBUGING)
			Log.d(tag, message);
	}

}
