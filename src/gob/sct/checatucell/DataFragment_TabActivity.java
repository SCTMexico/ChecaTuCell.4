package gob.sct.checatucell;


import gob.sct.checatucell.datacontrol.service.ActiveService;
import gob.sct.checatucell.json.JSONMapPoints;
import gob.sct.checatucell.utils.Point;
import gob.sct.checatucell.utils.Utils;
import gob.sct.checatucell.utils.Vendor;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Feed;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnPublishListener;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("UseSparseArrays")
public class DataFragment_TabActivity extends ActionBarActivity implements FragmentManager.OnBackStackChangedListener, ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * three primary sections of the app. We use a {@link android.support.v4.app.FragmentPagerAdapter}
     * derivative, which will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    static Context ctxt;
    /**
     * The {@link ViewPager} that will display the three primary sections of the app, one at a
     * time.
     */
    ViewPager mViewPager;
	private static int tabselection = 0;
	private static ProgressDialog mProgress;
	private static SimpleFacebook mSimpleFacebook;
	
	public void setTabSelector(int selected){
		tabselection = selected;
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public void onBackPressed() {
    	System.gc();
    	this.finish();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
            	System.gc();
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_tab_activity);
        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
        ctxt = getApplicationContext();
        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(android.graphics.Color.BLUE));
        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
        
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
                System.gc();

            }
        });
        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
        	Tab tempTab = actionBar.newTab()
            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
            .setTabListener(this);
        	switch(i){
            case 0:
            	tempTab.setIcon(R.drawable.img_intensity_button_icon);
            	break;
            case 1:
            	tempTab.setIcon(R.drawable.img_minutes_button_icon);
                break;
            case 2:
            	tempTab.setIcon(R.drawable.img_report_button_icon);
                break;
            case 3:
            	tempTab.setIcon(R.drawable.img_config_button_icon);
                break;
            }
            actionBar.addTab(tempTab);
            System.gc();
        }
        System.gc();
        mViewPager.setCurrentItem(tabselection);
    }

    @Override
	protected void onResume() {
		super.onResume();
		mSimpleFacebook = SimpleFacebook.getInstance(this);
		System.gc();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data);
	}


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
        	System.gc();
            switch (i) {
                case 0:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                    return new HeatMapSectionFragment();
                case 1:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                    return new DataControlSectionFragment();
                case 2:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                    return new DataReportSectionFragment();
                default:
                    // The other sections of the app are dummy placeholders.                    
                    return new DataConfigSectionFragment();
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }
        
       
        
    }

    static NumberPicker npVendors;
    static Dialog dialogVendors;
    static Button companySelector;
    
    /**
     * A fragment that launches other parts of the demo application.
     */
    private static HashMap<Integer, Marker> visibleMarkers = new HashMap<Integer, Marker>();
    public static class HeatMapSectionFragment extends Fragment implements LocationListener{
    	
    	Vendor [] vendorsArray;
        static String [] vendorNames;
        int [] vendorIds;
        private class companyNamesRetreiving extends AsyncTask<Object, Object, Object> {
    		
        	String urlCompanyOperators = "http://ttr.sct.gob.mx/qoscell/web/QOSOPERADORES/view.action";
            @Override
            protected Object doInBackground(Object... arg0) {
            	// TODO Auto-generated method stub
            	
         		
         		DefaultHttpClient httpClient = new DefaultHttpClient();
         		HttpResponse httpResponse;
         		HttpGet get;
         		String json;
         		JSONObject mainObject;
         		JSONObject secoObject;
         		JSONArray uniArray;
                
    				try {
    					get = new HttpGet(urlCompanyOperators);
    					httpResponse = httpClient.execute(get);
    					json = EntityUtils.toString(httpResponse.getEntity());
    					System.out.println("JSON: "+json);
    					mainObject = new JSONObject(json);
    					uniArray = mainObject.getJSONArray("data");
    					System.out.println("ARRAY: "+uniArray);
    					vendorsArray = new Vendor[uniArray.length()];
    					for(int i=0; i<uniArray.length(); i++){
    						Vendor tempVendor = new Vendor();
    						secoObject = uniArray.getJSONObject(i);
    						tempVendor.setName(secoObject.getString("nombre"));
    						tempVendor.setDescription(secoObject.getString("descripcion"));
    						tempVendor.setVendorId(Integer.parseInt(secoObject.getString("icveoperador")));
    						vendorsArray[i] = tempVendor;
    					}
    					
    					
    				} catch (ClientProtocolException e) {
    					// TODO Auto-generated catch block
    					//Toast.makeText(getActivity().getApplicationContext(), "Ha ocurrido algo extraño, verifica tu conexión a internet o intenta más tarde.", Toast.LENGTH_SHORT).show();
    					e.printStackTrace();
    				} catch (IOException e) {
    					// TODO Auto-generated catch block
    					//Toast.makeText(getActivity().getApplicationContext(), "Ha ocurrido algo extraño, verifica tu conexión a internet o intenta más tarde.", Toast.LENGTH_SHORT).show();
    					e.printStackTrace();
    				} catch (JSONException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
         		    
         		    
        		return null;
            }
            
            @Override
            protected void onPostExecute(Object o){

            	if(vendorsArray==null){
            		if(getActivity()!=null)
        				Toast.makeText(getActivity().getApplicationContext(), "Ha ocurrido algo extraño, verifica tu conexión a internet o intenta más tarde.", Toast.LENGTH_SHORT).show();
            	
            	}
            	else{
            		
            		if(vendorsArray.length==0)
        				Toast.makeText(getActivity().getApplicationContext(), "Ha ocurrido algo extraño, verifica tu conexión a internet o intenta más tarde.", Toast.LENGTH_SHORT).show();
	            		
            		vendorNames = new String[vendorsArray.length+1];
	                vendorIds = new int[vendorsArray.length];
	                vendorNames[0] = "Todas";
	                	for(int i=0; i<vendorsArray.length; i++){
	                		
	                		vendorNames[i+1] = vendorsArray[i].getName();
	                		vendorIds[i] = vendorsArray[i].getVendorId();
	                	}
	             
	        	    npVendors.setMaxValue(vendorNames.length-1);
	        	    npVendors.setMinValue(0);
	       		    npVendors.setWrapSelectorWheel(true);
	       		    npVendors.setDisplayedValues(vendorNames);
	       		    companySelector.setVisibility(0);

            	}
            	
            }
            
        }
    	
    	
    	public HeatMapSectionFragment() {
        }
    	
    	private MapView mMapView;
    	private GoogleMap mMap;
    	private Bundle mBundle;
    	private JSONMapPoints jMapPoints;
    	private static List<Point> points;
    	private static List<Point> points4gSignal;
		private static List<Point> pointsESignal;
		private static List<Point> pointsHSignal;
		private static List<Point> pointsNoSignal;
		double culsteringDistanceFactor = 500;
    	
    	private static String url = "http://ttr.sct.gob.mx/qoscell/web/QOSCSENALGEO/viewSpecific.action";
    	private static String urlC = "http://ttr.sct.gob.mx/qoscell/web/QOSCSENALGEO/viewSpecificOperator.action";
    	int mCurCheckPosition = 0;

    	@Override
    	public void onSaveInstanceState(Bundle outState) {
    	    super.onSaveInstanceState(outState);
    	    outState.putInt("curChoice", mCurCheckPosition);
    	}

    	@Override
    	public void onActivityCreated(Bundle savedInstanceState) {
    	    super.onActivityCreated(savedInstanceState);
    	    if (savedInstanceState != null) {
    	            // Restore last state for checked position.
    	        mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
    	    }
    	}
    	
    	@Override
    	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            System.gc();
			new companyNamesRetreiving().execute();

    	        final View v = inflater.inflate(R.layout.fragment_mapview_data, container, false);
            	MapsInitializer.initialize(getActivity());
            	companySelector = (Button) v.findViewById(R.id.buttonVendorSelector);
                mMapView = (MapView) v.findViewById(R.id.map);
                mMapView.onCreate(mBundle);
                setUpMapIfNeeded(v);
                Typeface myTypeface = Typeface.createFromAsset(getActivity().getAssets(), "cabin_regular.otf");
                companySelector.setTypeface(myTypeface);
                companySelector.setVisibility(4);
                
                dialogVendors = new Dialog(this.getActivity());
                dialogVendors.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogVendors.setContentView(R.layout.dialog_vendorpicker);
                npVendors = (NumberPicker)dialogVendors.findViewById(R.id.numberPicker1);
    		    
    		    Button cancelButton = (Button)dialogVendors.findViewById(R.id.cancelbutton);
    		    cancelButton.setOnClickListener(new OnClickListener() {
    		    @Override
    		    public void onClick(View v) {
    		    	dialogVendors.dismiss();
    		    }
    		    });
    		    Button acceptButton = (Button)dialogVendors.findViewById(R.id.acceptbutton);
    		    acceptButton.setOnClickListener(new OnClickListener() {
    		    @Override
    		    public void onClick(View v) {
    		    
    		    	System.out.println("VENDOR VALUE: "+npVendors.getValue());
    		    	companyName = npVendors.getValue();
    		    	new mapPointsRetreiving().execute();
    		    	dialogVendors.dismiss();
    		    }
    		    });
    		    companySelector.setOnClickListener(new OnClickListener(){
                    @Override
                    public void onClick(View v){
                    	dialogVendors.show();

                        }
                });
    		    
                System.gc();
    	        return v;
    	 }

    	@Override
    	public void onDestroyView() {
    	    super.onDestroyView();
    	    
    	}

    	

    	@Override
    	public void onCreate(Bundle savedInstanceState) {
    	    super.onCreate(savedInstanceState);
    	    mBundle = savedInstanceState;
    	}

    	private void setUpMapIfNeeded(View inflatedView) {
    	    
    	        mMap = ((MapView) inflatedView.findViewById(R.id.map)).getMap();
    	        if (mMap != null) {
    	            setUpMap();
    	        }
    	    
    	}

    	private void setUpMap() {
    	    //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    	    mMap.setMyLocationEnabled(true);
    	    LatLng latLng = new LatLng(23.8995798,-101.3231806);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));//setUpCheckUpSpots();
            mMap.setOnCameraChangeListener(getCameraChangeListener());
            /*mMap.setOnMarkerClickListener(new OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(
                    ctxt,
                    marker.getTitle(),
                    Toast.LENGTH_SHORT).show();
         
                return false;
            }
        });*/

	}
	public OnCameraChangeListener getCameraChangeListener()
	{
	    return new OnCameraChangeListener() 
	    {

			@Override
			public void onCameraChange(CameraPosition position) {
				LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
    	        boundsCenterLatitude = bounds.getCenter().latitude;
    	    	boundsCenterLongitude = bounds.getCenter().longitude;
    	    	Log.w("ZOOMLEVEL", ""+position.zoom);
    	    	
    	    	if(position.zoom>=1 && position.zoom<2)
    	    		boundsRadius = 360;
    	    	else if(position.zoom>=2 && position.zoom<3)
    	    		boundsRadius = 180;
    	    	else if(position.zoom>=3 && position.zoom<4)
    	    		boundsRadius = 90;
    	    	else if(position.zoom>=4 && position.zoom<5)
    	    		boundsRadius = 45;
    	    	else if(position.zoom>=5 && position.zoom<6)
    	    		boundsRadius = 22.5;
    	    	else if(position.zoom>=6 && position.zoom<7)
    	    		boundsRadius = 11.25;
    	    	else if(position.zoom>=7 && position.zoom<8)
    	    		boundsRadius = 5.625;
    	    	else if(position.zoom>=8 && position.zoom<9)
    	    		boundsRadius = 2.8125;
    	    	else if(position.zoom>=9 && position.zoom<10)
    	    		boundsRadius = 1.40625;
    	    	else if(position.zoom>=10 && position.zoom<11)
    	    		boundsRadius = 0.70313;
    	    	else if(position.zoom>=11 && position.zoom<12)
    	    		boundsRadius = 0.35156;
    	    	else if(position.zoom>=12 && position.zoom<13)
    	    		boundsRadius = 0.17578;
    	    	else if(position.zoom>=13 && position.zoom<14)
    	    		boundsRadius = 0.08789;
    	    	else if(position.zoom>=14 && position.zoom<15)
    	    		boundsRadius = 0.04395;
    	    	else if(position.zoom>=15 && position.zoom<16)
    	    		boundsRadius = 0.02197;
    	    	else if(position.zoom>=16 && position.zoom<17)
    	    		boundsRadius = 0.01099;
    	    	else if(position.zoom>=17 && position.zoom<18)
    	    		boundsRadius = 0.00549;
    	    	else if(position.zoom>=18 && position.zoom<19)
    	    		boundsRadius = 0.00275;
    	    	else if(position.zoom>=19 && position.zoom<20)
    	    		boundsRadius = 0.00155;
    	    	else if(position.zoom>=20 && position.zoom<21)
    	    		boundsRadius = 0.00090;
				new mapPointsRetreiving().execute();

			}
	    };
	}
	
	private void addItemsToMap(List<Point> items)
	{
	    if(this.mMap != null)
	    {
//	        //This is the current user-viewable region of the map
//	        LatLngBounds bounds = this.mMap.getProjection().getVisibleRegion().latLngBounds;
	     	mMap.clear();    	
	        //Loop through all the items that are available to be placed on the map
	     	for(Point item : items) 
			{
				if(item.getConnectionType()==1)
					points4gSignal.add(item);

				else if(item.getConnectionType()==2)
					pointsHSignal.add(item);
				
				else if(item.getConnectionType()==3)
					pointsESignal.add(item);
				
				else if(item.getConnectionType()==4)
					pointsNoSignal.add(item);

	        }
	     	
	     	new draw4gSignalPoints().execute();
			new drawHSignalPoints().execute();
			new drawESignalPoints().execute();
	    }
	}
	
	public double CalculationByDistance(double lat1,double lon1, double lat2, double lon2) {
	    int Radius=6371;//radius of earth in Km         
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLon = Math.toRadians(lon2-lon1);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	    Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	    Math.sin(dLon/2) * Math.sin(dLon/2);
	    double c = 2 * Math.asin(Math.sqrt(a));
	    double valueResult= Radius*c;
	    double km=valueResult/1;
	    double meter=valueResult*1000;
	    DecimalFormat newFormat = new DecimalFormat("####");
	    int kmInDec =  Integer.valueOf(newFormat.format(km));
	    int meterInDec= Integer.valueOf(newFormat.format(meter));
	    //Log.i("Radius Value",""+valueResult+"   KM  "+kmInDec+" Meter   "+meterInDec);
	    return (double)meterInDec;
	 }
	
	private class drawHSignalPoints extends AsyncTask<Object, Object, Object> {

		@Override
		protected Object doInBackground(Object... arg0) {
			List<Point> tempPointList = new ArrayList<Point>();
			System.out.println("TAMAÑO: "+pointsHSignal.size());
			for(int i=0; i<pointsHSignal.size();i++){
				tempPointList.add(pointsHSignal.get(i));
				for(int j=0; j<pointsHSignal.size();j++){
					if(j<pointsHSignal.size() && i<pointsHSignal.size()){
						try{
							double distance = CalculationByDistance(pointsHSignal.get(i).getLat(),pointsHSignal.get(i).getLon(),pointsHSignal.get(j).getLat(),pointsHSignal.get(j).getLon());
							if(distance<boundsRadius*culsteringDistanceFactor && i!=j)
								pointsHSignal.remove(j);
						} catch (IndexOutOfBoundsException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
			}
			pointsHSignal = tempPointList;

			return null;
		}
		@Override
		protected void onPostExecute(Object o){
			//Toast.makeText(ctxt,"TEST",Toast.LENGTH_SHORT).show();
			System.out.println("TAMAÑO: "+pointsHSignal.size());
			for(int i=0; i<pointsHSignal.size();i++){
				if(boundsRadius<0.1){
					mMap.addMarker(
							new MarkerOptions()
							.position(new LatLng(pointsHSignal.get(i).getLat(),pointsHSignal.get(i).getLon()))
							.title("")
							.snippet("")
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.img_signal_stain_orange30)));
				}
				else{
					mMap.addMarker(
							new MarkerOptions()
							.position(new LatLng(pointsHSignal.get(i).getLat(),pointsHSignal.get(i).getLon()))
							.title("")
							.snippet("")
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.img_signal_stain_orange10)));
				}
			}
		}
	}
	
	private class drawESignalPoints extends AsyncTask<Object, Object, Object> {

		@Override
		protected Object doInBackground(Object... arg0) {
			List<Point> tempPointList = new ArrayList<Point>();
			for( int i=0; i<pointsESignal.size();i++){
				tempPointList.add(pointsESignal.get(i));
				for(int j=0; j<pointsESignal.size();j++){
					if(j<pointsESignal.size() && i<pointsESignal.size()){
						try{
							double distance = CalculationByDistance(pointsESignal.get(i).getLat(),pointsESignal.get(i).getLon(),pointsESignal.get(j).getLat(),pointsESignal.get(j).getLon());
							if(distance<boundsRadius*culsteringDistanceFactor && i!=j)
								pointsESignal.remove(j);
						} catch (IndexOutOfBoundsException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			pointsESignal = tempPointList;
			
			return null;
		}
		@Override
		protected void onPostExecute(Object o){
			//Toast.makeText(ctxt,"TEST",Toast.LENGTH_SHORT).show();
			for(int i=0; i<pointsESignal.size();i++){
				if(boundsRadius<0.1){
					mMap.addMarker(
							new MarkerOptions()
							.position(new LatLng(pointsESignal.get(i).getLat(),pointsESignal.get(i).getLon()))
							.title("")
							.snippet("")
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.img_signal_stain_red30)));
				}
				else{
					mMap.addMarker(
							new MarkerOptions()
							.position(new LatLng(pointsESignal.get(i).getLat(),pointsESignal.get(i).getLon()))
							.title("")
							.snippet("")
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.img_signal_stain_red10)));
				}
			}
		}
	}
	
	private class draw4gSignalPoints extends AsyncTask<Object, Object, Object> {

		
		@Override
		protected Object doInBackground(Object... arg0) {
			List<Point> tempPointList = new ArrayList<Point>();
			for(int i=0; i<points4gSignal.size();i++){
				tempPointList.add(points4gSignal.get(i));
				for(int j=0; j<points4gSignal.size();j++){
					if(j<points4gSignal.size() && i<points4gSignal.size()){
						try{
							double distance = CalculationByDistance(points4gSignal.get(i).getLat(),points4gSignal.get(i).getLon(),points4gSignal.get(j).getLat(),points4gSignal.get(j).getLon());
							if(distance<boundsRadius*culsteringDistanceFactor && i!=j)
								points4gSignal.remove(j);
						} catch (IndexOutOfBoundsException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
			}
			points4gSignal = tempPointList;
			
			return null;
		}
		@Override
		protected void onPostExecute(Object o){
			//Toast.makeText(ctxt,"TEST",Toast.LENGTH_SHORT).show();
			for(int i=0; i<points4gSignal.size();i++){
				if(boundsRadius<0.1){
					mMap.addMarker(
							new MarkerOptions()
							.position(new LatLng(points4gSignal.get(i).getLat(),points4gSignal.get(i).getLon()))
							.title("")
							.snippet("")
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.img_signal_stain_blue30)));
				}
				else{
					mMap.addMarker(
							new MarkerOptions()
							.position(new LatLng(points4gSignal.get(i).getLat(),points4gSignal.get(i).getLon()))
							.title("")
							.snippet("")
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.img_signal_stain_blue10)));
				}
			}
		}
	}
	
	double boundsCenterLatitude;
	double boundsCenterLongitude;
	double boundsRadius;
	int companyName = 0;
	
	private class mapPointsRetreiving extends AsyncTask<Object, Object, Object> {
		 
        @Override
        protected Object doInBackground(Object... arg0) {
        	// TODO Auto-generated method stub
			jMapPoints = new JSONMapPoints();
			int signalSpotsCount = 0;
			if(companyName == 0)
				signalSpotsCount = jMapPoints.initJSONParser(url,boundsCenterLongitude,boundsCenterLatitude,boundsRadius,companyName);
			else
				signalSpotsCount = jMapPoints.initJSONParser(urlC,boundsCenterLongitude,boundsCenterLatitude,boundsRadius,companyName);
			String [] signalSpotValues;
			points = new ArrayList<Point>();
			Point newPoint;
    		for(int i=0;i<signalSpotsCount;i++){
    			newPoint = new Point();
    			signalSpotValues = jMapPoints.getJSONMapPoints(i);
    			newPoint.setLat(signalSpotValues[0]);
    			newPoint.setLon(signalSpotValues[1]);
    			newPoint.setAlt(signalSpotValues[2]);
    			newPoint.setIntensity(signalSpotValues[3]);
    			newPoint.setBandWidth(signalSpotValues[4]);
    			newPoint.setId(signalSpotValues[5]);
    			newPoint.setConnectionType(signalSpotValues[6]);
    			points.add(newPoint);
    		}
        	
    		return null;
        }
        
        @Override
        protected void onPostExecute(Object o){
        	//Toast.makeText(ctxt,"TEST",Toast.LENGTH_SHORT).show();
        	pointsHSignal = new ArrayList<Point>();
			pointsNoSignal = new ArrayList<Point>();
			points4gSignal = new ArrayList<Point>();
			pointsESignal = new ArrayList<Point>();
        	addItemsToMap(points);
        }
        
    }


    	
    	@Override
    	public void onResume() {
    	    super.onResume();
    	    mMapView.onResume();
    	}

    	@Override
    	public void onPause() {
    	    super.onPause();
    	    mMapView.onPause();
    	}

    	@Override
    	public void onDestroy() {
    	    mMapView.onDestroy();
    	    super.onDestroy();
    	}

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			
            
		}
    }
    /**
     * A fragment representing data control section.
     */
    
    public static class DataControlSectionFragment extends Fragment {
    	View rootView;
        public static final String ARG_SECTION_NUMBER = "section_number";
        static ImageView dataConsumeBar;
        static LinearLayout chartConsumeData;
        static int auxDataChartWidth;
        static int auxDataChartHeight;
        long percentageConsumption;
        long percentageRest;
        
        @SuppressWarnings("deprecation")
		@Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            System.gc();

            rootView = inflater.inflate(R.layout.fragment_section_datacontrol, container, false);
            Resources res = getResources();

            ImageView daysMeter = (ImageView) rootView.findViewById(R.id.imageViewAnimated1);
            ImageView dataConsumeMeter = (ImageView) rootView.findViewById(R.id.imageViewAnimated2);
            ImageView dataLastMeter = (ImageView) rootView.findViewById(R.id.imageViewAnimated3);
            dataConsumeBar = (ImageView) rootView.findViewById(R.id.imageViewBar);
            chartConsumeData = (LinearLayout)rootView.findViewById(R.id.chartRectangleBar);
            
            TextView dayText = (TextView)rootView.findViewById(R.id.textView1);
            TextView lastDaysText = (TextView)rootView.findViewById(R.id.textView2);
            TextView usedText = (TextView)rootView.findViewById(R.id.textView3);
            TextView lastText = (TextView)rootView.findViewById(R.id.textView4);
            
            Typeface myTypeface = Typeface.createFromAsset(getActivity().getAssets(), "cabin_regular.otf");
            dayText.setTypeface(myTypeface);
            lastDaysText.setTypeface(myTypeface);
            usedText.setTypeface(myTypeface);
            lastText.setTypeface(myTypeface);

        	SharedPreferences prefs = ctxt.getSharedPreferences("checatucell_prefs",Context.MODE_PRIVATE);
        	long limitdata = prefs.getLong("LimitData", 104857600);
            Log.w("LIMITEAAAAAA", ""+limitdata);
			long data = prefs.getLong("LastData", 0);
			long delta = limitdata-data;
			
			String date = prefs.getString("LimitDate", "00/30/2014");
			
			char char1day = date.charAt(3);
			char char2day = date.charAt(4);
			int dayOnlyNum = Integer.parseInt(""+char1day+char2day);
			dayText.setText(""+dayOnlyNum);
			
			  Calendar currentDate = Calendar.getInstance();
		      SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy",Locale.US);
		      String dateNow = sdf.format(currentDate.getTime());
		      Date today =  new Date(dateNow);
		      
		      Calendar cal = Calendar.getInstance();  
		      cal.set(2014, today.getMonth(), dayOnlyNum);
		      
		      Date dateLimit = (Date) cal.getTime(); 
		      int numberOfDays=(int)((dateLimit.getTime()-today.getTime())/(3600*24*1000));
		      if(numberOfDays<=0){
	    	    	
	    	    	cal = Calendar.getInstance();  
	    	    	cal.set(2014, today.getMonth(), dayOnlyNum);  
	    	    	cal.add(Calendar.MONTH, 1);
	    	    	dateLimit = (Date) cal.getTime();  
	    	    	numberOfDays=(int)((dateLimit.getTime()-today.getTime())/(3600*24*1000));
		      }
		      else
		    	  numberOfDays=(int)((dateLimit.getTime()-today.getTime())/(3600*24*1000));
		      
		    /*if(numberOfDays<=0)
		    	lastDaysText.setText("0");
		    else*/
		    	lastDaysText.setText(""+numberOfDays);
		    
			double auxDayPercent = 1-((double)numberOfDays/(double)dayOnlyNum);
            Log.w("PORCENTAJEDIAS 1", ""+auxDayPercent);

			if(auxDayPercent<=0)
				auxDayPercent = 1;
						
		    percentageConsumption =  (long) ((data * 100.0f) / limitdata);
			percentageRest = (long) ((delta * 100.0f) / limitdata);
			percentageConsumption=(percentageConsumption+1);
			
			if(percentageConsumption>=100 || percentageRest<=0){
				
				percentageConsumption = 100;
				percentageRest = 0;
			}
			else if(percentageConsumption<=0 || percentageRest>=100){
				percentageConsumption = 0;
				percentageRest = 100;
			}
			
			ViewTreeObserver vto = rootView.getViewTreeObserver();

            vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
              @Override
              public void onGlobalLayout() {
            	  auxDataChartWidth = (int) ((dataConsumeBar.getWidth()-16)*(percentageConsumption/100.0f));
                  Log.w("BARRAPORCENTAJE 1", "Porcentaje:"+percentageConsumption+" Ancho:"+auxDataChartWidth);
            	  auxDataChartHeight =  (int) (dataConsumeBar.getHeight()/2.1);
            	  chartConsumeData.getLayoutParams().width = auxDataChartWidth;
            	  chartConsumeData.getLayoutParams().height = auxDataChartHeight;
                ViewTreeObserver obs = rootView.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
              }
              });
            Log.w("BARRAPORCENTaJE", ""+auxDataChartWidth);
            chartConsumeData.getLayoutParams().width = auxDataChartWidth;
      	  	chartConsumeData.getLayoutParams().height = auxDataChartHeight;
      	  	
			usedText.setText(percentageConsumption+"%");
			lastText.setText(percentageRest+"%");
			
            AnimationDrawable animation1 = new AnimationDrawable();
            AnimationDrawable animation2 = new AnimationDrawable();
            AnimationDrawable animation3 = new AnimationDrawable();

            animation1.setOneShot(true);
            animation2.setOneShot(true);
            animation3.setOneShot(true);

            
            Log.w("PORCENTAJEDIAS", ""+auxDayPercent);
            int from = 1;
            while (from <= 50) {
                String name = "circle_" + String.format("%d", from);
                int image = res.getIdentifier(name, "drawable", ctxt.getPackageName());
                if(from<=(int)(auxDayPercent*50))
                animation1.addFrame(res.getDrawable(image), 40);
                if(from<=(percentageRest/2))
                animation3.addFrame(res.getDrawable(image), 40);
                if(from<=(percentageConsumption/2))
                animation2.addFrame(res.getDrawable(image), 40);
                from++;
            }
            daysMeter.setBackgroundDrawable(animation1);
            dataConsumeMeter.setBackgroundDrawable(animation2);
            dataLastMeter.setBackgroundDrawable(animation3);

            animation1.start();
            System.gc();
            animation2.start();
            System.gc();
            animation3.start();

            System.gc();
            return rootView;
        }
    }
    /**
     * A fragment representing data control section.
     */
    
    public static final String ARG_SECTION_NUMBER = "section_number";
    
    public static class DataReportSectionFragment extends Fragment {

    	
    	View rootView;
    	ImageView imageHSPA;
    	ImageView imageEDGE;
    	ImageView image4G;
    	
    	LinearLayout chartHSPA;
    	LinearLayout chartEDGE;
    	LinearLayout chart4G;
    	
    	double percentValueHSPA;
    	double percentValueEDGE;
    	double percentValue4G;
    	double averageSpeedValue;

    	static int auxHSPAChartWidth;
        static int auxHSPAChartHeight;
        static int auxEDGEChartWidth;
        static int auxEDGEChartHeight;
        static int aux4GChartWidth;
        static int aux4GChartHeight;
        
        @SuppressWarnings("deprecation")
		@Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            System.gc();

            rootView = inflater.inflate(R.layout.fragment_section_datareport, container, false);
            Resources res = getResources();
            
            //**Values must come from DataBase**
            percentValueHSPA = 0.67;
            percentValueEDGE = 0.12;
            percentValue4G = 0.21;
            averageSpeedValue = 9;
            //*********************************
            
            ImageView medidorVelocidad = (ImageView) rootView.findViewById(R.id.imageViewAnimated);
            imageHSPA = (ImageView) rootView.findViewById(R.id.imageViewHSPA);
            imageEDGE = (ImageView) rootView.findViewById(R.id.imageViewEDGE);
            image4G = (ImageView) rootView.findViewById(R.id.imageView4G);
            
            chartHSPA = (LinearLayout)rootView.findViewById(R.id.chartRectangleHSPA);
            chartEDGE = (LinearLayout)rootView.findViewById(R.id.chartRectangleEDGE);
            chart4G = (LinearLayout)rootView.findViewById(R.id.chartRectangle4G);

            TextView g4Percent = (TextView) rootView.findViewById(R.id.textView4G);
            TextView hspaPercent = (TextView) rootView.findViewById(R.id.textViewHSPA);
            TextView edgePercent = (TextView) rootView.findViewById(R.id.textViewEdge);
            TextView speedAverageText = (TextView) rootView.findViewById(R.id.textViewSpeed);
            
            TextView signalTypeTitle = (TextView) rootView.findViewById(R.id.TextView01);
            Typeface myTypeface = Typeface.createFromAsset(getActivity().getAssets(), "cabin_bold.otf");
            signalTypeTitle.setTypeface(myTypeface);
            myTypeface = Typeface.createFromAsset(getActivity().getAssets(), "cabin_regular.otf");
            g4Percent.setTypeface(myTypeface);
            hspaPercent.setTypeface(myTypeface);
            edgePercent.setTypeface(myTypeface);
            speedAverageText.setTypeface(myTypeface);
            
            g4Percent.setText(""+(int)(percentValue4G*100)+"%");
            hspaPercent.setText(""+(int)(percentValueHSPA*100)+"%");
            edgePercent.setText(""+(int)(percentValueEDGE*100)+"%");
            speedAverageText.setText(""+averageSpeedValue+"mb");
             
            ViewTreeObserver vto = rootView.getViewTreeObserver();

            vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
              @Override
              public void onGlobalLayout() {
                auxHSPAChartWidth = (int) ((imageHSPA.getWidth()-20)*(percentValueHSPA));
                auxHSPAChartHeight =  (int) (imageHSPA.getHeight()/2.1);
                chartHSPA.getLayoutParams().width = auxHSPAChartWidth;
                chartHSPA.getLayoutParams().height = auxHSPAChartHeight;
                
                auxEDGEChartWidth = (int) ((imageEDGE.getWidth()-20)*(percentValueEDGE));
                auxEDGEChartHeight =  (int) (imageEDGE.getHeight()/2.1);
                chartEDGE.getLayoutParams().width = auxEDGEChartWidth;
                chartEDGE.getLayoutParams().height = auxEDGEChartHeight;
                
                aux4GChartWidth = (int) ((image4G.getWidth()-20)*(percentValue4G));
                aux4GChartHeight =  (int) (image4G.getHeight()/2.1);
                chart4G.getLayoutParams().width = aux4GChartWidth;
                chart4G.getLayoutParams().height = aux4GChartHeight;
                
                ViewTreeObserver obs = rootView.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
              }
            });

            
            
            chartHSPA.getLayoutParams().width = auxHSPAChartWidth;
            chartHSPA.getLayoutParams().height = auxHSPAChartHeight;
            
            chartEDGE.getLayoutParams().width = auxEDGEChartWidth;
            chartEDGE.getLayoutParams().height = auxEDGEChartHeight;
            
            chart4G.getLayoutParams().width = aux4GChartWidth;
            chart4G.getLayoutParams().height = aux4GChartHeight;
            
            AnimationDrawable animation1 = new AnimationDrawable();
            animation1.setOneShot(true);
            

            int from = 1;
            while (from < (int)((averageSpeedValue)*2.6)) {
                String name = "circle_" + String.format("%d", from);
                int image = res.getIdentifier(name, "drawable", ctxt.getPackageName());
                animation1.addFrame(res.getDrawable(image), 40);
                from++;

            }
            medidorVelocidad.setBackgroundDrawable(animation1);
            

            animation1.start();
         
            System.gc();
            
            return rootView;
        }
        
        
    }
    /**
     * A fragment representing a config section of the app.
     */
    public static class DataConfigSectionFragment extends Fragment {
    	static String[] dataLimitnumbers1;
        static String[] dataLimitnumbers2;
        static String[] dataLimitByte;
    private void showDialog() {
    		mProgress = ProgressDialog.show(ctxt, "Cargando", "Esperando a Facebook...", true);
   	}
    
   	private void hideDialog() {
    		if (mProgress != null) {
    			mProgress.hide();
    		}
   	}
    	// Login listener
    	private OnLoginListener mOnLoginListener = new OnLoginListener() {

    		@Override
    		public void onFail(String reason) {
    			Log.d("TAG", "Error con el login.");
    		}

    		@Override
    		public void onException(Throwable throwable) {
    			Log.d("TAG", "Ocurrio algo extra??o intenta de nuevo.", throwable);
    		}

    		@Override
    		public void onThinking() {
    			// show progress bar or something to the user while login is
    			// happening
				//showDialog();
    		}

    		@Override
    		public void onLogin() {
    			
            	SharedPreferences prefs = ctxt.getSharedPreferences("checatucell_prefs",Context.MODE_PRIVATE);
    			if(prefs.getLong("FacebookLogin", 0) == 0){
    				SharedPreferences.Editor editor = prefs.edit();
            		editor.putLong("FacebookLogin", 1);
                	editor.commit();	
                	toast("Iniciando sesión...");
	    			Feed feed = new Feed.Builder()
	    			.setMessage("Test")
	    			.setName("Checa Tu Cell para Android")
	    			.setCaption("Control de datos y minutos.")
	    			.setDescription(
	    					"Controla tu consumo de datos y minutos con la aplicación oficial de la SCT.")
	    			.setPicture("http://www.sct.gob.mx/logoSCT_hoz.png").setLink("http://www.sct.gob.mx/")
	    			.build();	
	    			// change the state of the button or do whatever you want
	    			Log.d("FACEBOOK", "Loggeado, tratando de publicar");
	    			mSimpleFacebook.publish(feed, true, onPublishListener);
	    			toast("Te has loggeado correctamente.");
        		
    			}
    			else
        			toast("Ya te has loggeado antes correctamente.");
    		}

    		@Override
    		public void onNotAcceptingPermissions(Permission.Type type) {
    			Log.d("FACEBOOK", "ERROR");
    			toast(String.format("EL usuario no acepto los permisos de %s", type.name()));
    		}
    	};
    	
    	
    		final OnPublishListener onPublishListener = new OnPublishListener() {

    			@Override
    			public void onFail(String reason) {
    				hideDialog();
    				// insure that you are logged in before publishing
        			//toast("Has compartido Checa tu Cell con tus amigos!");
    				Log.w("TAG", "Falló la publicación: "+reason);
    			}

    			@Override
    			public void onException(Throwable throwable) {
    				hideDialog();
    				Log.e("TAG", "Ocurrio algo extraño intenta de nuevo.", throwable);
    			}

    			@Override
    			public void onThinking() {
    				//show progress bar or something to the user while publishing
    				//showDialog();
    			}

    			@Override
    			public void onComplete(String postId) {
        			//toast("Has compartido Checa tu Cell con tus amigos!");
    				hideDialog();
    			}
    		};
    	

    	private void toast(String message) {
    		Toast.makeText(ctxt, message, Toast.LENGTH_SHORT).show();
    	}
    	
        public static final String ARG_SECTION_NUMBER = "section_number";
        boolean alarmOnFlag;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	
            Utils.updateLanguage(ctxt, "en");
    		Utils.printHashKey(ctxt);
    		
            View rootView = inflater.inflate(R.layout.fragment_section_dataconfig, container, false);
            final SharedPreferences prefs = ctxt.getSharedPreferences("checatucell_prefs",Context.MODE_PRIVATE);
            final TextView billDay = (TextView)rootView.findViewById(R.id.textView);
            final TextView dataLimitText = (TextView)rootView.findViewById(R.id.textViewLimit);
            Typeface myTypeface = Typeface.createFromAsset(getActivity().getAssets(), "cabin_regular.otf");
            billDay.setTypeface(myTypeface);
            dataLimitText.setTypeface(myTypeface);

            Log.w("LIMITE", ""+prefs.getLong("LimitData", 104857600));
            String auxDataLimit;
            if(prefs.getLong("LimitData", 104857600)/1048576>=1024)
            	auxDataLimit = ""+(prefs.getLong("LimitData", 104857600)/1073741824)+"--GB";
            else{
            	if(prefs.getLong("LimitData", 104857600)/1048576 < 100 && prefs.getLong("LimitData", 104857600)/1048576 >=10)
            		auxDataLimit = ""+(prefs.getLong("LimitData", 104857600)/1048576)+"-MB";
            	else if(prefs.getLong("LimitData", 104857600)/1048576 < 10)
            		auxDataLimit = ""+(prefs.getLong("LimitData", 104857600)/1048576)+"--MB";
            	else
            		auxDataLimit = ""+(prefs.getLong("LimitData", 104857600)/1048576)+"MB";
            }
            dataLimitText.setText("  "+auxDataLimit.charAt(0)+"      "+auxDataLimit.charAt(1)+"      "+auxDataLimit.charAt(2)+"    "+auxDataLimit.charAt(3)+auxDataLimit.charAt(4));
            
            Button loginFb = (Button)rootView.findViewById(R.id.fblogin);
            ImageView billingDate = (ImageView)rootView.findViewById(R.id.imageViewDia);
            final ImageView alarmSwitch = (ImageView)rootView.findViewById(R.id.imageViewAlarm);
            ImageView limitDataField = (ImageView)rootView.findViewById(R.id.imageViewLimit);
            
    		if(prefs.getLong("ActiveDataAlarm", 0) == 1){
            alarmSwitch.setImageDrawable(getResources().getDrawable(R.drawable.img_alarma_activada));              		
            alarmOnFlag = true;
    		}
    		else{
    			alarmSwitch.setImageDrawable(getResources().getDrawable(R.drawable.img_alarma_desactivada));              		
                alarmOnFlag = false;
    		}
    		
            alarmSwitch.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v){
                	if(alarmOnFlag){
                		alarmSwitch.setImageDrawable(getResources().getDrawable(R.drawable.img_alarma_desactivada));              		
                    	getActivity().stopService(new Intent(ctxt, ActiveService.class));
                    	SharedPreferences.Editor editor = prefs.edit();
                		editor.putLong("ActiveDataAlarm", 0);
                    	editor.commit();
            			toast("Alarma de consumo de datos desactivada!");
                		alarmOnFlag = false;
                	}
                	else{
                		alarmSwitch.setImageDrawable(getResources().getDrawable(R.drawable.img_alarma_activada));              		
                    	getActivity().startService(new Intent(ctxt, ActiveService.class));
                    	SharedPreferences.Editor editor = prefs.edit();
                		editor.putLong("ActiveDataAlarm", 1);
                    	editor.commit();
            			toast("Alarma de consumo de datos activada!");
                		alarmOnFlag = true;
                	}

                    }
                });
            loginFb.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v){
                    	Log.d("FACEBOOK", "LOGIN");
        				mSimpleFacebook.login(mOnLoginListener);

                    }
                });
             
            String date = prefs.getString("LimitDate", "00/30/2014");
			char char1day = date.charAt(3);
			char char2day = date.charAt(4);
			billDay.setText(""+char1day+char2day);
			
			final Dialog dialog = new Dialog(this.getActivity());

		    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		    dialog.setContentView(R.layout.dialog_numpicker);

		    final String[] pickerDatenumbers = new String[35];
		    for(int i=0; i<pickerDatenumbers.length; i++) {
		    	pickerDatenumbers[i] = Integer.toString(i+1);
		    }

		    final NumberPicker npDate = (NumberPicker)dialog.findViewById(R.id.numberPicker1);
		    npDate.setMaxValue(31);
		    npDate.setMinValue(1);
		    npDate.setWrapSelectorWheel(true);
		    npDate.setDisplayedValues(pickerDatenumbers);
		    

		    Button cancelButton = (Button)dialog.findViewById(R.id.cancelbutton);
		    cancelButton.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    dialog.dismiss();
		    }
		    });
		    Button acceptButton = (Button)dialog.findViewById(R.id.acceptbutton);
		    acceptButton.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    billDay.setText(""+(npDate.getValue()));
		    setLimitDate(npDate.getValue());
		    dialog.dismiss();
		    }
		    });
		    billingDate.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v){
        		    dialog.show();

                    }
            });
		    
		    System.gc();

		    final Dialog dialogDataLimit = new Dialog(this.getActivity());

		    dialogDataLimit.requestWindowFeature(Window.FEATURE_NO_TITLE);
		    dialogDataLimit.setContentView(R.layout.dialog_datalimitpicker);

		    dataLimitnumbers1 = new String[11];
		    for(int i=0; i<dataLimitnumbers1.length; i++) {
		    	dataLimitnumbers1[i] = Integer.toString(i);
		    }
		    dataLimitnumbers2 = new String[15];
		    dataLimitnumbers2[0] = "--";
		    dataLimitnumbers2[1] = "00";

		    for(int i=2; i<dataLimitnumbers2.length; i++) {
		    	dataLimitnumbers2[i] = Integer.toString(i*10);
		    }
		    dataLimitByte = new String[3];
		    dataLimitByte[0] = "MB";
		    dataLimitByte[1] = "GB";
		    dataLimitByte[2] = "";


		    final NumberPicker npData1 = (NumberPicker)dialogDataLimit.findViewById(R.id.numberPickerA);
		    npData1.setMaxValue(9);
		    npData1.setMinValue(0);
		    npData1.setWrapSelectorWheel(true);
		    npData1.setDisplayedValues(dataLimitnumbers1);
		    final NumberPicker npData2 = (NumberPicker)dialogDataLimit.findViewById(R.id.numberPickerB);
		    npData2.setMaxValue(9);
		    npData2.setMinValue(0);
		    npData2.setWrapSelectorWheel(true);
		    npData2.setDisplayedValues(dataLimitnumbers2);
		    final NumberPicker npData3 = (NumberPicker)dialogDataLimit.findViewById(R.id.numberPickerC);
		    npData3.setMaxValue(1);
		    npData3.setMinValue(0);
		    npData3.setWrapSelectorWheel(true);
		    npData3.setDisplayedValues(dataLimitByte);
		    
		    limitDataField.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v){
                	dialogDataLimit.show();

                    }
            });
		    
		    Button cancelButtonDialogData = (Button)dialogDataLimit.findViewById(R.id.cancelbutton);
		    cancelButtonDialogData.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    dialogDataLimit.dismiss();
		    }
		    });
		    Button acceptButtonDialogData = (Button)dialogDataLimit.findViewById(R.id.acceptbutton);
		    acceptButtonDialogData.setOnClickListener(new OnClickListener() {
		    
		    	int dataLimitValue;
		    	String auxDataLimtString;
		    @Override
		    public void onClick(View v) {
		    if(!dataLimitnumbers2[npData2.getValue()].equals("--")){
		    	Log.w("LIMITEDEDATOS", ""+dataLimitnumbers1[npData1.getValue()]
		    							+dataLimitnumbers2[npData2.getValue()]
		    							+dataLimitByte[npData3.getValue()]);
		    	
		    	dataLimitText.setText("  "+dataLimitnumbers1[npData1.getValue()]+"     "+dataLimitnumbers2[npData2.getValue()].charAt(0)+"     "+dataLimitnumbers2[npData2.getValue()].charAt(1)+"     "+dataLimitByte[npData3.getValue()]);
		    	auxDataLimtString = ""+dataLimitnumbers1[npData1.getValue()]
		    							+dataLimitnumbers2[npData2.getValue()];
		    	if(dataLimitByte[npData3.getValue()].equals("GB"))
		    		dataLimitValue = (Integer.parseInt(auxDataLimtString))*1073741824;
		    	else if(dataLimitByte[npData3.getValue()].equals("MB"))
		    		dataLimitValue = (Integer.parseInt(auxDataLimtString))*1048576; 
		    	
		    	setLimitData(dataLimitValue);
		    	SharedPreferences.Editor editor = prefs.edit();
				editor.putLong("ConfigDataFlag", 1);
		    	editor.commit();
		    	Log.w("CONFIGMODIFICADA", ""+prefs.getLong("ConfigDataFlag", 0));		    	
		    }
		    else{
		    	Log.w("LIMITEDEDATOS", ""+dataLimitnumbers1[npData1.getValue()]
										+dataLimitByte[npData3.getValue()]);
		    	
		    	dataLimitText.setText("  "+dataLimitnumbers1[npData1.getValue()]+"      "+dataLimitnumbers2[npData2.getValue()].charAt(0)+"      "+dataLimitnumbers2[npData2.getValue()].charAt(1)+"     "+dataLimitByte[npData3.getValue()]);
		    	auxDataLimtString = ""+dataLimitnumbers1[npData1.getValue()];
		    	if(dataLimitByte[npData3.getValue()].equals("GB"))
		    		dataLimitValue = (Integer.parseInt(auxDataLimtString))*1073741824;
		    	else if(dataLimitByte[npData3.getValue()].equals("MB"))
		    		dataLimitValue = (Integer.parseInt(auxDataLimtString))*1048576; 
		    	
		    	setLimitData(dataLimitValue);
		    }
		    //billDay.setText(""+npDate.getValue());
		    //setLimitDate(npDate.getValue());
		    dialogDataLimit.dismiss();
		    }
		    });
		    
		    
		    System.gc();
            return rootView;
        }
    }

    /**
     * Secci??n de activaci??n de servicio de control de consumo de datos.
     */
	@SuppressWarnings("deprecation")
	public static void setLimitDate(int daylimit){
		
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
	    
  	SharedPreferences prefs =  ctxt.getSharedPreferences("checatucell_prefs",Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
  	editor.putString("LimitDate", limitDate);
  	editor.commit();
		
	}
	
	public static void setLimitData(long limitData){
		
  	SharedPreferences prefs = ctxt.getSharedPreferences("checatucell_prefs",Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
  	editor.putLong("LimitData", limitData);
  	editor.commit();
		
	}
	/**
	 * 
	 */
	@Override
	public void onBackStackChanged() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onTabReselected(Tab tab,
			android.support.v4.app.FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onTabSelected(Tab tab,
			android.support.v4.app.FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		System.gc();
        mViewPager.setCurrentItem(tab.getPosition());
	}


	@Override
	public void onTabUnselected(Tab tab,
			android.support.v4.app.FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		System.gc();
	}

	
}
