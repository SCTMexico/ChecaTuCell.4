package gob.sct.checatucell;

import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import gob.sct.android.checatucell.services.BootReceiver;
import gob.sct.android.checatucell.services.LocationReceiver;
import gob.sct.checatucell.utils.DebugMode;

import java.util.Timer;
import java.util.TimerTask;

import com.sct.locationpoller.LocationPoller;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
	 
	public class SplashScreenActivity extends Activity {
	 
	  private long splashDelay = 3000; //3 segundos
	  
		private static final int PERIOD = 1000 * 60 * 180;  // 3 hours

		private PendingIntent mPendingIntent = null;

		private AlarmManager mAlarm=null;

		private String DEBUG_TAG = "Splash";

	 
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_splash_screen);
	    
	    startLocationService();
	 
	    TimerTask task = new TimerTask() {
	      @Override
	      public void run() {
	        Intent mainIntent = new Intent().setClass(SplashScreenActivity.this, MainMenuActivity.class);
	        startActivity(mainIntent);
	        finish();//Destruimos esta activity para prevenir que el usuario retorne aqui presionando el boton Atras.
	      }
	    };
	 
	    Timer timer = new Timer();
	    timer.schedule(task, splashDelay);//Pasado los 3 segundos dispara la tarea
	  }
	 
	  private void startLocationService() {

			mAlarm = (AlarmManager)getSystemService(ALARM_SERVICE);

			Intent i=new Intent(this, LocationPoller.class);

			i.putExtra(LocationPoller.EXTRA_INTENT,
					new Intent(this, LocationReceiver.class));        

			i.putExtra(LocationPoller.EXTRA_PROVIDER,
					LocationManager.GPS_PROVIDER);

			mPendingIntent = PendingIntent.getBroadcast(this, 0, i, 0);

			mAlarm.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
					SystemClock.elapsedRealtime(),
					PERIOD,
					mPendingIntent);	
			
			ComponentName receiver = new ComponentName(this, BootReceiver.class);
	        PackageManager pm = getPackageManager();

	        pm.setComponentEnabledSetting(receiver,
	                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
	                PackageManager.DONT_KILL_APP);
			
			DebugMode.logger(DEBUG_TAG, "Service initialized");
					
		}

}
