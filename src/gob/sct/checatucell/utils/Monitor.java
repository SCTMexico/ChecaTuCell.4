package gob.sct.checatucell.utils;


import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

public class Monitor {

	Context context;

	TelephonyManager mTelephonyManager;

	private OnTaskCompleted listener;
	MyPhoneStateListener    MyListener;

	public Monitor(Context context, OnTaskCompleted listener) {

		this.context = context;
		mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);		
		this.listener=listener;
		MyListener   = new MyPhoneStateListener();


	}

	/**
	 * 
	 * Signal tyoe
	 * 0 = 4G/3G
	 * 1 = HSPA
	 * 2 = EDGE
	 * 3 = Sin Internet
	 * 
	 * */
	public int getDataConnectionType() {
		mTelephonyManager = (TelephonyManager)
				context.getSystemService(Context.TELEPHONY_SERVICE);
		int networkType = mTelephonyManager.getNetworkType();
		switch (networkType) {

		case TelephonyManager.NETWORK_TYPE_GPRS:
			return 2;
		case TelephonyManager.NETWORK_TYPE_EDGE:
			return 2;
		case TelephonyManager.NETWORK_TYPE_CDMA:
			return 2;


		case TelephonyManager.NETWORK_TYPE_HSPA:
			return 1;

		case TelephonyManager.NETWORK_TYPE_HSPAP:		        		        	
		case TelephonyManager.NETWORK_TYPE_LTE:
			return 0;

		case TelephonyManager.NETWORK_TYPE_1xRTT:

		case TelephonyManager.NETWORK_TYPE_IDEN:

		case TelephonyManager.NETWORK_TYPE_UMTS:

		case TelephonyManager.NETWORK_TYPE_EVDO_0:

		case TelephonyManager.NETWORK_TYPE_EVDO_A:

		case TelephonyManager.NETWORK_TYPE_HSDPA:

		case TelephonyManager.NETWORK_TYPE_HSUPA:

		case TelephonyManager.NETWORK_TYPE_EVDO_B:

		case TelephonyManager.NETWORK_TYPE_EHRPD:
			return -1;


		default:
			return -1;
		}
		
	}

	public String getPhoneCompanyName(){		
		return mTelephonyManager.getNetworkOperatorName();
	}

	public void startGsmListening() {

		mTelephonyManager       = ( TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		mTelephonyManager.listen(MyListener ,PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);


	}

	
	private class MyPhoneStateListener extends PhoneStateListener
	{
		/* Get the Signal strength from the provider, each time there is an update */
		@Override
		public void onSignalStrengthsChanged(SignalStrength signalStrength)
		{
			super.onSignalStrengthsChanged(signalStrength);

			int proportionalSignal;
			
			if(signalStrength.getGsmSignalStrength() == 99)
				proportionalSignal = 0;
			else if(signalStrength.getGsmSignalStrength() >= 26 )
				proportionalSignal = 4;
			else if(signalStrength.getGsmSignalStrength() >= 20 )
				proportionalSignal = 3;
			else if(signalStrength.getGsmSignalStrength() >= 14 )
				proportionalSignal = 2;
			else if(signalStrength.getGsmSignalStrength() >= 9 )
				proportionalSignal = 1;
			else 
				proportionalSignal = 0;
			
			mTelephonyManager.listen(MyListener, PhoneStateListener.LISTEN_NONE);
			
			
			
			listener.onTaskCompleted(proportionalSignal);

		}
	};
	
	
	
	
	
	
	
	
	
	
	


}
