package gob.sct.checatucell;

import gob.sct.checatucell.datacontrol.service.ActiveService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainMenuActivity extends ActionBarActivity 
	implements FragmentManager.OnBackStackChangedListener { 
    /**
     * Called when the activity is first created.
     */
	/*
     * A handler object, used for deferring UI operations.
     */
    private Handler mHandler = new Handler();

    /**
     * Whether or not we're showing the back of the card (otherwise showing the front).
     */
    private boolean mShowingBack = false;
	/**
     * Secci??n de activaci??n de servicio de control de consumo de datos.
     */
	@SuppressWarnings("deprecation")
	public void setLimitDate(int daylimit){
		
		  Calendar currentDate = Calendar.getInstance();
	      SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy",Locale.US);
	      String dateNow = sdf.format(currentDate.getTime());
	      Date today =  new Date(dateNow);
	      
	      Calendar cal = Calendar.getInstance();  
	      cal.set(2014, today.getMonth(), daylimit);
	      
	      Date dateLimit = (Date) cal.getTime();  
	      int numberOfDays=(int)((dateLimit.getTime()-today.getTime())/(3600*24*1000));
	      
	    if(numberOfDays<=0){
	    	cal.setTime(dateLimit);
	    	cal.add(Calendar.MONTH, 1);
	    	dateLimit = (Date) cal.getTime(); 
	    }
	    String limitDate = sdf.format(dateLimit);	
  	SharedPreferences prefs = getSharedPreferences("checatucell_prefs",Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
  	editor.putString("LimitDate", limitDate);
  	editor.commit();
		
	}
	
	public void setLimitData(long limitData){
		
  	SharedPreferences prefs = getSharedPreferences("checatucell_prefs",Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
  	editor.putLong("LimitData", limitData);
  	editor.commit();
		
	}
	
	public void setCheckUpRate(long checkUpRate){
		
  	SharedPreferences prefs = getSharedPreferences("checatucell_prefs",Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
  	editor.putLong("CheckUpRate", checkUpRate);
  	editor.commit();
		
	}
	/**
	 * 
	 */
	NotificationManager manager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        SharedPreferences prefs = this.getSharedPreferences("checatucell_prefs",Context.MODE_PRIVATE);
        
        setCheckUpRate(60000);
        Log.w("CONFIGMODIFICADA", ""+prefs.getLong("ConfigDataFlag", 0));
        Log.w("LIMITDATA", ""+prefs.getLong("LimitData", 1048576000));
        if(prefs.getLong("ConfigDataFlag", 0)==0)
		setLimitData(104857600);
		//setLimitDate(30);
		startService(new Intent(this, ActiveService.class));

		SharedPreferences.Editor editor = prefs.edit();
		if(getIntent().getStringExtra("NotificatedAlarm")!=null){
			System.out.println("EXTRA: "+getIntent().getStringExtra("NotificatedAlarm"));
			   manager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	           manager.cancel(0);
			if(getIntent().getStringExtra("NotificatedAlarm").equals("off"))
				editor.putLong("ActiveDataAlarm", 0);
			else
				editor.putLong("ActiveDataAlarm", 1);
		}
				
		editor.commit();
    	
       
        if (savedInstanceState == null) {
            // If there is no saved instance state, add a fragment representing the
            // front of the card to this activity. If there is saved instance state,
            // this fragment will have already been added to the activity.
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.container_menunuttons_layout, new DataViewFragment())
                    .commit();
        } else {
            mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);
        }
        
        getFragmentManager().addOnBackStackChangedListener(this);
        
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "cabin_medium.otf");
        final TextView textData = (TextView)findViewById(R.id.textViewData);
        final TextView textTele = (TextView)findViewById(R.id.textViewTele);
        
        textData.setTypeface(myTypeface);
        textTele.setTypeface(myTypeface);
        
        final RelativeLayout buttonTelephon = (RelativeLayout)findViewById(R.id.buttonA);
        final RelativeLayout buttonData = (RelativeLayout)findViewById(R.id.buttonB);

        final ImageView telephonyButtonImage = (ImageView)findViewById(R.id.imageView1);
        final ImageView dataButtonImage = (ImageView)findViewById(R.id.imageView2);

        if(mShowingBack){
    		buttonTelephon.setSelected(true);
    		buttonData.setSelected(false);
    		telephonyButtonImage.setColorFilter(Color.rgb(156,247,254), Mode.MULTIPLY );
    		dataButtonImage.setColorFilter(Color.WHITE, Mode.MULTIPLY );
    		textData.setTextColor(Color.WHITE);
            textTele.setTextColor(Color.rgb(156,247,254));

        }
        else{
        	buttonTelephon.setSelected(false);
        	buttonData.setSelected(true);
        	telephonyButtonImage.setColorFilter(Color.WHITE, Mode.MULTIPLY );
    		dataButtonImage.setColorFilter(Color.rgb(156,247,254), Mode.MULTIPLY );
    		textData.setTextColor(Color.rgb(156,247,254));
            textTele.setTextColor(Color.WHITE);
        	}
        
        
        buttonTelephon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            	if(!mShowingBack){
            		buttonTelephon.setSelected(true);
            		buttonData.setSelected(false);
            		flipCard();
            		telephonyButtonImage.setColorFilter(Color.rgb(156,247,254), Mode.MULTIPLY );
            		dataButtonImage.setColorFilter(Color.WHITE, Mode.MULTIPLY );
            		textData.setTextColor(Color.WHITE);
                    textTele.setTextColor(Color.rgb(156,247,254));
            	}
            }
        });
        buttonData.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
            	
            	if(mShowingBack){
            		buttonTelephon.setSelected(false);
            		buttonData.setSelected(true);
            		flipCard();
            		telephonyButtonImage.setColorFilter(Color.WHITE, Mode.MULTIPLY );
            		dataButtonImage.setColorFilter(Color.rgb(156,247,254), Mode.MULTIPLY );
            		textData.setTextColor(Color.rgb(156,247,254));
                    textTele.setTextColor(Color.WHITE);
            	}
            	
            }
        });
        
        
    }
    
   
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressLint("NewApi")
	private void flipCard() {
        if (mShowingBack) {
            getFragmentManager().popBackStack();
            return;
        }

        // Flip to the back.

        mShowingBack = true;

        // Create and commit a new fragment transaction that adds the fragment for the back of
        // the card, uses custom animations, and is part of the fragment manager's back stack.

        getFragmentManager()
                .beginTransaction()

                // Replace the default fragment animations with animator resources representing
                // rotations when switching to the back of the card, as well as animator
                // resources representing rotations when flipping back to the front (e.g. when
                // the system Back button is pressed).
                .setCustomAnimations(
                        R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in, R.animator.card_flip_left_out)

                // Replace any fragments currently in the container view with a fragment
                // representing the next page (indicated by the just-incremented currentPage
                // variable).
                .replace(R.id.container_menunuttons_layout, new TelephonyViewFragment())

                // Add this transaction to the back stack, allowing users to press Back
                // to get to the front of the card.
                .addToBackStack(null)

                // Commit the transaction.
                .commit();

        // Defer an invalidation of the options menu (on modern devices, the action bar). This
        // can't be done immediately because the transaction may not yet be committed. Commits
        // are asynchronous in that they are posted to the main thread's message loop.
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                invalidateOptionsMenu();
            }
        });
    }

    @Override
    public void onBackStackChanged() {
        mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);

        // When the back stack changes, invalidate the options menu (action bar).
        invalidateOptionsMenu();
    }

    /**
     * A fragment representing the front of the card.
     */
    public static class TelephonyViewFragment extends Fragment {
        public TelephonyViewFragment() {
        }
        
        TelephonyFragment_TabActivity tabActivity;
        
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	
        	View v = inflater.inflate(R.layout.fragment_mainmenu_telephonyface, container, false);
        	 Typeface myTypeface = Typeface.createFromAsset(getActivity().getAssets(), "cabin_medium.otf");
        	 TextView textA1 = (TextView) v.findViewById(R.id.text1T);
             //TextView textA2 = (TextView) v.findViewById(R.id.text2T);
             TextView textA3 = (TextView) v.findViewById(R.id.text3T);
             //TextView textA4 = (TextView) v.findViewById(R.id.text4T);
             
             textA1.setTypeface(myTypeface);
             //textA2.setTypeface(myTypeface);
             textA3.setTypeface(myTypeface);
             //textA4.setTypeface(myTypeface);
             
             final RelativeLayout buttonTelephon1 = (RelativeLayout) v.findViewById(R.id.button_layout_telephon1);
             //final RelativeLayout buttonTelephon2 = (RelativeLayout) v.findViewById(R.id.button_layout_telephon2);
             final RelativeLayout buttonTelephon3 = (RelativeLayout) v.findViewById(R.id.button_layout_telephon3);
             //final RelativeLayout buttonTelephon4 = (RelativeLayout) v.findViewById(R.id.button_layout_telephon4);

             tabActivity = new TelephonyFragment_TabActivity();
             
             buttonTelephon1.setOnClickListener(new View.OnClickListener(){
             @Override
             public void onClick(View v){
                 	tabActivity.setTabSelector(0);
                 	Intent mainIntent = new Intent(getActivity(), TelephonyFragment_TabActivity.class);
                     startActivity(mainIntent);
                 	
                 }
             });
             /*
             buttonTelephon2.setOnClickListener(new View.OnClickListener(){
                 @Override
                 public void onClick(View v){
                  	tabActivity.setTabSelector(1);
                     	Intent mainIntent = new Intent(getActivity(), TelephonyFragment_TabActivity.class);
                         startActivity(mainIntent);
                     	
                     }
                 });
             */
             buttonTelephon3.setOnClickListener(new View.OnClickListener(){
                 @Override
                 public void onClick(View v){
                  	tabActivity.setTabSelector(1);
                     	Intent mainIntent = new Intent(getActivity(), TelephonyFragment_TabActivity.class);
                         startActivity(mainIntent);
                     	
                     }
                 });
             /*
             buttonTelephon4.setOnClickListener(new View.OnClickListener(){
                 @Override
                 public void onClick(View v){
                  	tabActivity.setTabSelector(3);
                     	Intent mainIntent = new Intent(getActivity(), TelephonyFragment_TabActivity.class);
                         startActivity(mainIntent);
                     	
                     }
                 });
             */
            return v;
             
        }
        
    }

    /**
     * A fragment representing the back of the card.
     */
    public static class DataViewFragment extends Fragment {
        public DataViewFragment() {
        }

        DataFragment_TabActivity tabActivity;
        
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            	
        View v = inflater.inflate(R.layout.fragment_mainmenu_dataface, container, false);
       	Typeface myTypeface = Typeface.createFromAsset(getActivity().getAssets(), "cabin_medium.otf");
       	TextView textB1 = (TextView) v.findViewById(R.id.text1D);
        TextView textB2 = (TextView) v.findViewById(R.id.text2D);
        TextView textB3 = (TextView) v.findViewById(R.id.text3D);
        TextView textB4 = (TextView) v.findViewById(R.id.text4D);
        
        textB1.setTypeface(myTypeface);
        textB2.setTypeface(myTypeface);
        textB3.setTypeface(myTypeface);
        textB4.setTypeface(myTypeface);
        
        final RelativeLayout buttonData1 = (RelativeLayout) v.findViewById(R.id.button_layout_data1);
        final RelativeLayout buttonData2 = (RelativeLayout) v.findViewById(R.id.button_layout_data2);
        final RelativeLayout buttonData3 = (RelativeLayout) v.findViewById(R.id.button_layout_data3);
        final RelativeLayout buttonData4 = (RelativeLayout) v.findViewById(R.id.button_layout_data4);

        tabActivity = new DataFragment_TabActivity();
        
        buttonData1.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v){
            	tabActivity.setTabSelector(0);
            	Intent mainIntent = new Intent(getActivity(), DataFragment_TabActivity.class);
                startActivity(mainIntent);
            	
            }
        });
        buttonData2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
             	tabActivity.setTabSelector(1);
                	Intent mainIntent = new Intent(getActivity(), DataFragment_TabActivity.class);
                    startActivity(mainIntent);
                	
                }
            });
        buttonData3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
             	tabActivity.setTabSelector(2);
                	Intent mainIntent = new Intent(getActivity(), DataFragment_TabActivity.class);
                    startActivity(mainIntent);
                	
                }
            });
        buttonData4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
             	tabActivity.setTabSelector(3);
                	Intent mainIntent = new Intent(getActivity(), DataFragment_TabActivity.class);
                    startActivity(mainIntent);
                	
                }
            });
        
        
        return v;
        }
    }
}
