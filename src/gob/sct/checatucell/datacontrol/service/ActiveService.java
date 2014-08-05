package gob.sct.checatucell.datacontrol.service;

import gob.sct.checatucell.MainMenuActivity;
import gob.sct.checatucell.R;
import gob.sct.checatucell.R.drawable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@SuppressLint({ "NewApi", "SimpleDateFormat", "HandlerLeak" })
public class ActiveService extends Service{
	
	private static final String TAG = "DataService";
	private static Timer timer = new Timer(); 
    NotificationCompat.Builder notification;
	PendingIntent pIntent;
	NotificationManager manager;
	Intent resultIntent;
	TaskStackBuilder stackBuilder;
	TrafficSnapshot latest=null;
	TrafficSnapshot previous=null;
	long delta = 0;
	MainMenuActivity mc;
	
	
	private void startService()
    {           
		SharedPreferences prefs = getSharedPreferences("checatucell_prefs",Context.MODE_PRIVATE);
		long checkUpRate = prefs.getLong("CheckUpRate", 1200000);
//		Log.d("Rate", ""+checkUpRate);
        timer.scheduleAtFixedRate(new mainTask(), 0, checkUpRate);
    }

	private class mainTask extends TimerTask
    { 
        public void run() 
        {
            toastHandler.sendEmptyMessage(0);
        }
    }    
	
	private final Handler toastHandler = new Handler()
	{
	        @Override
	        public void handleMessage(Message msg)
	        {
	        	mc = new MainMenuActivity();
	        	SharedPreferences prefs = getSharedPreferences("checatucell_prefs",Context.MODE_PRIVATE);
	    		String date = prefs.getString("LimitDate", "00/30/2014");
//	    		Log.d("LIMITEFECHA", "FECHA LIMITE "+date);
	    		
	    		  Calendar currentDate = Calendar.getInstance();
	    	      SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
	    	      String dateNow = sdf.format(currentDate.getTime());
	    	      @SuppressWarnings("deprecation")
	    	      Date today =  new Date(dateNow);
	    	      Date finalDay = null;
	    	    try {
					finalDay = (Date) sdf.parse(date);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	    int numberOfDays=(int)((finalDay.getTime()-today.getTime())/(3600*24*1000));

	    	    if(numberOfDays==0){
	    	    	
	    	    	Calendar cal = Calendar.getInstance();  
	    	    	cal.setTime(finalDay);  
	    	    	cal.add(Calendar.MONTH, 1);
	    	    	finalDay = (Date) cal.getTime();  
	    	    	date = DateFormat.getDateInstance().format(finalDay);
	    	    	
	    	    	SharedPreferences.Editor editor = prefs.edit();
	    	    	editor.putString("LimitDate", date);
	    	    	editor.putLong("LastData", 0);
	    	    	editor.commit();
	    	    	
	    	    }
	    	    else{
	    	    
	    	    previous=latest;
	    		latest=new TrafficSnapshot(getApplicationContext());
	    		
	    		
	    		if (previous!=null) {
	    			
	    			delta = latest.device.rx-previous.device.rx;
	    			long limitdata = prefs.getLong("LimitData", 0);
	    			long data = prefs.getLong("LastData", 0);
	    			long dataadd = data+delta;
	    			
	    			SharedPreferences.Editor editor = prefs.edit();
	    	    	editor.putLong("LastData", dataadd);
	    	    	editor.commit();
	    	    	
//	    			Log.d("LIMITEDATOS", "DATOS LIMITE "+limitdata);

	    			if(data>=limitdata)
	    	        	startNotification();
//	    			Log.d("DATOS", "DATOS CONSUMIDOS "+dataadd);

	    		
	    		}

	          }
	       }    
	};
	
	@SuppressLint("NewApi")
	protected void startNotification() {
		
	       //Creating Notification Builder
           notification = new NotificationCompat.Builder(ActiveService.this);
           notification.setContentTitle("Limite de Datos");
           notification.setContentText("Limite de uso de datos alcanzado.");
           notification.setTicker("Alerta!");
           notification.setSmallIcon(R.drawable.ic_launcher);
           notification.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
           notification.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
           
           //Creating new Stack Builder
           stackBuilder = TaskStackBuilder.create(ActiveService.this);
           stackBuilder.addParentStack(MainMenuActivity.class);
           resultIntent = new Intent(ActiveService.this, MainMenuActivity.class);
           resultIntent.putExtra("NotificatedAlarm", "off");
           stackBuilder.addNextIntent(resultIntent);
           pIntent =  stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
           notification.setContentIntent(pIntent);
           manager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
           manager.notify(0, notification.build());
           
    }
	    
	@Override
    public IBinder onBind(Intent arg0) {
    return null;
    }
	    
	@Override
	public void onCreate() {
		super.onCreate();
	    startService();
//		Log.d(TAG, "onCreate - Servicio Creado");
	}
	    
	@Override
	public void onStart(Intent intent, int startId) {
//		Log.d(TAG, "onStart - Servicio Iniciado");	
	}
	
	@Override
	public void onDestroy() {
//		Log.d(TAG, "onDestroy - Servicio Detenido");
		toastHandler.removeCallbacksAndMessages(null);
	}
}
