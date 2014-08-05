/***
  Copyright (c) 2010 CommonsWare, LLC

  Licensed under the Apache License, Version 2.0 (the "License"); you may
  not use this file except in compliance with the License. You may obtain
  a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */

package gob.sct.android.checatucell.services;



import gob.sct.checatucell.R;
import gob.sct.checatucell.SplashScreenActivity;
import gob.sct.checatucell.utils.DebugMode;
import gob.sct.checatucell.utils.Monitor;
import gob.sct.checatucell.utils.OnTaskCompleted;
import gob.sct.checatucell.utils.WebUtil;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;
import com.sct.locationpoller.LocationPoller;

public class LocationReceiver extends BroadcastReceiver implements OnTaskCompleted {

	private static String DEBUG_TAG = "QosCellServices";
	
	
	private int idOperdador;

	//	MyPhoneStateListener    MyListener;

	Context mContext;

	int type;

	private NotificationManager mNotificationManager;

	NotificationCompat.Builder builder;

	// An ID used to post the notification.
	public static final int NOTIFICATION_ID = 1;

	/**Current users location*/
	Location mLocation;

	String operatorName;
	int dataConnectionType,  operatorGsmSignalStrength;


	Monitor mMonitor;


	@Override
	public void onReceive(Context context, Intent intent) {
		mContext = context;

		Bundle b=intent.getExtras();

		mLocation = getLocationUpdate(b, intent);
		
		DebugMode.logger(DEBUG_TAG, "Service launch");

		if(mLocation != null){
			DebugMode.logger(DEBUG_TAG, "Location found");
//			sendNotification("Location found");

			mMonitor = new Monitor(context,this);

			dataConnectionType = mMonitor.getDataConnectionType();

			operatorName = mMonitor.getPhoneCompanyName();
			
			if(operatorName.contentEquals("TELCEL"))
				idOperdador = 1;
			if(operatorName.contentEquals("UNEFON"))
				idOperdador = 3;
			if(operatorName.contentEquals("IUSACELL"))
				idOperdador = 2;
			if(operatorName.contentEquals("NEXTEL"))
				idOperdador = 5;			
			if(operatorName.contentEquals("movistar"))
				idOperdador = 4;
			if(operatorName.contentEquals("Virgin"))
				idOperdador = 6;

			mMonitor.startGsmListening();

			//			MyListener   = new MyPhoneStateListener();
			//			mTelephonyManager       = ( TelephonyManager )mContext.getSystemService(Context.TELEPHONY_SERVICE);
			//			mTelephonyManager.listen(MyListener ,PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

		}else
		{
			DebugMode.logger(DEBUG_TAG, "No location found");
			//			sendNotification("No location found");
		}

		//		
		//
		//		mLocation = (Location)b.get(LocationPoller.EXTRA_LOCATION);				
		//
		//		String msg;
		//		
		//		if (mLocation == null) {
		//			mLocation = (Location)b.get(LocationPoller.EXTRA_LASTKNOWN);
		//
		//			if (mLocation == null) {
		//				msg=intent.getStringExtra(LocationPoller.EXTRA_ERROR);
		//			}
		//			else {
		//				msg="TIMEOUT, lastKnown="+mLocation.toString();
		//			}
		//		}
		//		else {								
		//
		//			msg=mLocation.toString();
		//
		//			Log.d("Location Poller", msg);
		//
		//			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);      
		//
		//			if ((tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSDPA)) {
		//				Log.d("Type", "3g");// for 3g HSDPA networktype will be return as
		//				// per testing(real) in device with 3g enable data
		//				// and speed will also matters to decide 3g network type
		//				type = 2;
		//			} else if ((tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSPAP)) {
		//				Log.d("Type", "4g"); // /No specification for the 4g but from wiki
		//				// i found(HSPAP used in 4g)
		//				// http://goo.gl/bhtVT
		//				type = 3;
		//			} else if ((tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_GPRS)) {
		//				Log.d("Type", "GPRS");
		//				type = 1;
		//			} else if ((tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_EDGE)) {
		//				Log.d("Type", "EDGE 2g");
		//				type = 0;
		//			}
		//
		//			/* Update the listener, and start it */
		//			MyListener   = new MyPhoneStateListener();
		//			mTelephonyManager       = ( TelephonyManager )mContext.getSystemService(Context.TELEPHONY_SERVICE);
		//			mTelephonyManager.listen(MyListener ,PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
		//		}
		//		if (msg==null) {
		//			msg="Invalid broadcast received!";
		//		}
	}


	/* ?????????????????????????????????????????????????????????????????????????????????????????? */
	/* Start the PhoneState listener */
	/* ?????????????????????????????????????????????????????????????????????????????????????????? */
	//	private class MyPhoneStateListener extends PhoneStateListener
	//	{
	//		/* Get the Signal strength from the provider, each time there is an update */
	//		@Override
	//		public void onSignalStrengthsChanged(SignalStrength signalStrength)
	//		{
	//			super.onSignalStrengthsChanged(signalStrength);
	//			Log.d("Poller", "Go to Firstdroid!!! GSM Cinr = "+ String.valueOf(signalStrength.getGsmSignalStrength()));
	//
	//			String url = "http://ttr.sct.gob.mx/qoscell/web/QOSCSENALGEO/REGISTRO/SENAL.action?dintensidad=1" +
	//					"&tipo="+String.valueOf(signalStrength.getGsmSignalStrength())+"&danchobanda=1&" +
	//					"dlatitud="+mLocation.getLatitude()+"&dlongitud=" +
	//					mLocation.getLongitude()+"&d_altitud="+mLocation.getAltitude()+"&dtipoconexiond="+type;
	//
	//
	//
	//			downloadPage(url.trim());
	//
	//			Log.d(DEBUG_TAG,url.trim());					
	//
	//			mTelephonyManager.listen(MyListener, PhoneStateListener.LISTEN_NONE);
	//
	//			sendNotification("Se ha enviado el reporte");
	//		}
	//	};



	/***
	 * Makes a call to the location service and gets current user's location
	 * */
	private Location getLocationUpdate(Bundle mBundle, Intent mIntent) {

		String message;

		Location location = (Location) mBundle.get(LocationPoller.EXTRA_LOCATION);				

		if (location == null) {
			location = (Location) mBundle.get(LocationPoller.EXTRA_LASTKNOWN);
			if (location == null) {
				message = mIntent.getStringExtra(LocationPoller.EXTRA_ERROR);
			}
			else {
				message = "TIMEOUT, lastKnownlocation = "+location.toString();
			}								
		}
		else {			
			return location;
		}
		if (message==null) 
			message = "Invalid broadcast received!";
		else 
			DebugMode.logger(DEBUG_TAG, message);
		return null;
	}


	private void sendNotification(String msg) {
//		if(isDebugging){
			mNotificationManager = (NotificationManager)
					mContext.getSystemService(Context.NOTIFICATION_SERVICE);

			PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0,
					new Intent(mContext, SplashScreenActivity.class), 0);

			NotificationCompat.Builder mBuilder =
					new NotificationCompat.Builder(mContext)	
			.setSmallIcon(R.drawable.ic_launcher)
			.setContentTitle("Checa tu cel")
			.setStyle(new NotificationCompat.BigTextStyle()
			.bigText(msg))
			.setContentText(msg);

			mBuilder.setContentIntent(contentIntent);
						

			mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
//		}
	}


	@SuppressLint("NewApi") @Override
	public void onTaskCompleted(int signalStrength) {
		operatorGsmSignalStrength = signalStrength;
				
		final String url = "http://ttr.sct.gob.mx/qoscell/web/QOSCSENALGEO/REGISTRO/SENAL.action?" +
				"dintensidad=" + operatorGsmSignalStrength +
				".0&tipo=0"+
				"&danchobanda=0&" +
				"dlatitud=" + mLocation.getLatitude() + 
				"&dlongitud=" + mLocation.getLongitude()+
				"&d_altitud="+mLocation.getAltitude()
				+"&dtipoconexiond="+dataConnectionType+
				"&ioperador=" + idOperdador;
		  
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		    WebUtil.downloadPage(url);
//		sendNotification(WebUtil.downloadPage(url));
		
		
	
		
		

//		http://ttr.sct.gob.mx/qoscell/web/QOSCSENALGEO/REGISTRO/SENAL.action?dintensidad=1&tipo=1&danchobanda=1&dlatitud=1&dlongitud=1&d_altitud=1&dtipoconexiond=1&ioperador=1
	}


	//	msg=mLocation.toString();
	//
	//	Log.d("Location Poller", msg);
	//
	//	TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);      
	//
	//	if ((tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSDPA)) {
	//		Log.d("Type", "3g");// for 3g HSDPA networktype will be return as
	//		// per testing(real) in device with 3g enable data
	//		// and speed will also matters to decide 3g network type
	//		type = 2;
	//	} else if ((tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSPAP)) {
	//		Log.d("Type", "4g"); // /No specification for the 4g but from wiki
	//		// i found(HSPAP used in 4g)
	//		// http://goo.gl/bhtVT
	//		type = 3;
	//	} else if ((tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_GPRS)) {
	//		Log.d("Type", "GPRS");
	//		type = 1;
	//	} else if ((tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_EDGE)) {
	//		Log.d("Type", "EDGE 2g");
	//		type = 0;
	//	}
	//
	//	/* Update the listener, and start it */
	//	MyListener   = new MyPhoneStateListener();
	//	mTelephonyManager       = ( TelephonyManager )mContext.getSystemService(Context.TELEPHONY_SERVICE);
	//	mTelephonyManager.listen(MyListener ,PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
	//}
}
