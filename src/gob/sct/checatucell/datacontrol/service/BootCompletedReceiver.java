package gob.sct.checatucell.datacontrol.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootCompletedReceiver extends BroadcastReceiver {
    final static String TAG = "BootCompletedReceiver";
    @Override
    public void onReceive(Context context, Intent arg1) {    	
        context.startService(new Intent(context, ActiveService.class));
        Log.d(TAG,"Servicio iniciado");
    }
}