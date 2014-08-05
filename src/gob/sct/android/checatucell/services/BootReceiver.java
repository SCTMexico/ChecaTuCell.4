package gob.sct.android.checatucell.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.SystemClock;

import com.sct.locationpoller.LocationPoller;


public class BootReceiver extends BroadcastReceiver {
	
	private static final int PERIOD = 1000 * 60 * 30;  // 30 min

	/**
	 * This BroadcastReceiver automatically (re)starts the alarm when the device is
	 * rebooted. This receiver is set to be disabled (android:enabled="false") in the
	 * application's manifest file. When the user sets the alarm, the receiver is enabled.
	 * When the user cancels the alarm, the receiver is disabled, so that rebooting the
	 * device will not trigger this receiver.
	 */
	// BEGIN_INCLUDE(autostart)

//	mAlarm = (AlarmManager)getSystemService(ALARM_SERVICE);

	    @Override
	    public void onReceive(Context context, Intent intent) {
	        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
	        {
	        	Intent i = new Intent(context, LocationPoller.class);

	        	i.putExtra(LocationPoller.EXTRA_INTENT,
	        			new Intent(context, LocationReceiver.class));        

	        	i.putExtra(LocationPoller.EXTRA_PROVIDER,
	        			LocationManager.GPS_PROVIDER);

	        	PendingIntent mPendingIntent = null;

	        	mPendingIntent = PendingIntent.getBroadcast(context, 0, i, 0);
	        	
	        	AlarmManager mAlarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
;
	        	mAlarm.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
	        			SystemClock.elapsedRealtime(),
	        			PERIOD,
	        			mPendingIntent);	
	        	
	        	ComponentName receiver = new ComponentName(context, BootReceiver.class);
	            PackageManager pm = context.getPackageManager();

	            pm.setComponentEnabledSetting(receiver,
	                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
	                    PackageManager.DONT_KILL_APP);
	        }
	    }
	
	//END_INCLUDE(autostart)

}
