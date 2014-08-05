package gob.sct.checatucell;

import gob.sct.checatucell.json.JSONMapPoints;
import gob.sct.checatucell.utils.Point;
import gob.sct.checatucell.utils.Vendor;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

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
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
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

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
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
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("UseSparseArrays")
public class TelephonyFragment_TabActivity extends ActionBarActivity implements FragmentManager.OnBackStackChangedListener, ActionBar.TabListener {

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
	//private static ProgressDialog mProgress;
	//private static SimpleFacebook mSimpleFacebook;

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
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

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
				/*
            case 2:
            	tempTab.setIcon(R.drawable.img_report_button_icon);
                break;
            case 3:
            	tempTab.setIcon(R.drawable.img_config_button_icon);
                break;
				 */
			}
			actionBar.addTab(tempTab);

			System.gc();
		}

		mViewPager.setCurrentItem(tabselection);
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
			switch (i) {
			case 0:
				// The first section of the app is the most interesting -- it offers
				// a launchpad into the other demonstrations in this example application.
				return new HeatMapSectionFragment();
				/*case 1:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                    return new TelephonyControlSectionFragment();*/
			default:
				return new TelephonyReportSectionFragment();
				/*default:
	                    // The other sections of the app are dummy placeholders.                    
	                return new DummySectionFragment();*/
			}
		}

		@Override
		public int getCount() {
			return 2;
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
	 * A fragment that launches map part of QOSCELL application.
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
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
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
		private static List<Point> pointsHigh;
		private static List<Point> pointsMedium;
		private static List<Point> pointsLow;
		private static String url = "http://ttr.sct.gob.mx/qoscell/web/QOSCSENALGEO/viewSpecific.action";
		private static String urlC = "http://ttr.sct.gob.mx/qoscell/web/QOSCSENALGEO/viewSpecificOperator.action";
		double culsteringDistanceFactor = 500;
		
		
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

			new companyNamesRetreiving().execute();
			final View v = inflater.inflate(R.layout.fragment_mapview_telephony, container, false);
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
			mMap.setMyLocationEnabled(true);
			LatLng latLng = new LatLng(23.8995798,-101.3231806);
			mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
			//setUpCheckUpSpots();
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

					pointsHigh = new ArrayList<Point>();
					pointsMedium = new ArrayList<Point>();
					pointsLow = new ArrayList<Point>();
					
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
				//    	        //This is the current user-viewable region of the map
				//    	        LatLngBounds bounds = this.mMap.getProjection().getVisibleRegion().latLngBounds;
				mMap.clear();    	
				//Loop through all the items that are available to be placed on the map
				
				for(Point item : items) 
				{
					if(item.getIntensity()==3 || item.getIntensity()==4)
						pointsHigh.add(item);

					else if(item.getIntensity()==2)
						pointsMedium.add(item);
					
					else if(item.getIntensity()==1)
						pointsLow.add(item);
				}
				
				new drawHighPoints().execute();
				new drawMediumPoints().execute();
				new drawLowPoints().execute();
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
		
		private class drawHighPoints extends AsyncTask<Object, Object, Object> {

			@Override
			protected Object doInBackground(Object... arg0) {
				List<Point> tempPointList = new ArrayList<Point>();
				for(int i=0; i<pointsHigh.size();i++){
					tempPointList.add(pointsHigh.get(i));
					for(int j=0; j<pointsHigh.size();j++){
						if(j<pointsHigh.size() && i<pointsHigh.size()){
							try {
							double distance = CalculationByDistance(pointsHigh.get(i).getLat(),pointsHigh.get(i).getLon(),pointsHigh.get(j).getLat(),pointsHigh.get(j).getLon());
							if(distance<boundsRadius*culsteringDistanceFactor && i!=j)
								pointsHigh.remove(j);
							} catch (IndexOutOfBoundsException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					
				}
				pointsHigh = tempPointList;
				return null;
			}
			@Override
			protected void onPostExecute(Object o){
				//Toast.makeText(ctxt,"TEST",Toast.LENGTH_SHORT).show();
				for(int i=0; i<pointsHigh.size();i++){
					if(boundsRadius<0.1){
						mMap.addMarker(
								new MarkerOptions()
								.position(new LatLng(pointsHigh.get(i).getLat(),pointsHigh.get(i).getLon()))
								.title("")
								.snippet("")
								.icon(BitmapDescriptorFactory.fromResource(R.drawable.img_signal_stain_blue30)));
					}
					else{
						mMap.addMarker(
								new MarkerOptions()
								.position(new LatLng(pointsHigh.get(i).getLat(),pointsHigh.get(i).getLon()))
								.title("")
								.snippet("")
								.icon(BitmapDescriptorFactory.fromResource(R.drawable.img_signal_stain_blue10)));
					}
				}
			}
		}
		
		private class drawMediumPoints extends AsyncTask<Object, Object, Object> {

			@Override
			protected Object doInBackground(Object... arg0) {
				List<Point> tempPointList = new ArrayList<Point>();
				for( int i=0; i<pointsMedium.size();i++){
					tempPointList.add(pointsMedium.get(i));
					for(int j=0; j<pointsMedium.size();j++){
						if(j<pointsMedium.size() && i<pointsMedium.size()){
							try{
							double distance = CalculationByDistance(pointsMedium.get(i).getLat(),pointsMedium.get(i).getLon(),pointsMedium.get(j).getLat(),pointsMedium.get(j).getLon());
							if(distance<boundsRadius*culsteringDistanceFactor && i!=j)
								pointsMedium.remove(j);
							} catch (IndexOutOfBoundsException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					
					
				}
				pointsMedium = tempPointList;
				return null;
			}
			@Override
			protected void onPostExecute(Object o){
				//Toast.makeText(ctxt,"TEST",Toast.LENGTH_SHORT).show();
				for(int i=0; i<pointsMedium.size();i++){
					if(boundsRadius<0.1){
						mMap.addMarker(
								new MarkerOptions()
								.position(new LatLng(pointsMedium.get(i).getLat(),pointsMedium.get(i).getLon()))
								.title("")
								.snippet("")
								.icon(BitmapDescriptorFactory.fromResource(R.drawable.img_signal_stain_orange30)));
					}
					else{
						mMap.addMarker(
								new MarkerOptions()
								.position(new LatLng(pointsMedium.get(i).getLat(),pointsMedium.get(i).getLon()))
								.title("")
								.snippet("")
								.icon(BitmapDescriptorFactory.fromResource(R.drawable.img_signal_stain_orange10)));
					}
				}
			}
		}
		
		private class drawLowPoints extends AsyncTask<Object, Object, Object> {

			@Override
			protected Object doInBackground(Object... arg0) {
				List<Point> tempPointList = new ArrayList<Point>();
				System.out.println("TAMAÑO: "+pointsLow.size());
				for(int i=0; i<pointsLow.size();i++){
					tempPointList.add(pointsLow.get(i));
					for(int j=0; j<pointsLow.size();j++){
						if(j<pointsLow.size() && i<pointsLow.size()){
							try{
							double distance = CalculationByDistance(pointsLow.get(i).getLat(),pointsLow.get(i).getLon(),pointsLow.get(j).getLat(),pointsLow.get(j).getLon());
							if(distance<boundsRadius*culsteringDistanceFactor && i!=j)
								pointsLow.remove(j);
							} catch (IndexOutOfBoundsException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					
				}
				pointsLow = tempPointList;
				return null;
			}
			@Override
			protected void onPostExecute(Object o){
				//Toast.makeText(ctxt,"TEST",Toast.LENGTH_SHORT).show();
				System.out.println("TAMAÑO: "+pointsLow.size());
				for(int i=0; i<pointsLow.size();i++){
					if(boundsRadius<0.1){
						mMap.addMarker(
								new MarkerOptions()
								.position(new LatLng(pointsLow.get(i).getLat(),pointsLow.get(i).getLon()))
								.title("")
								.snippet("")
								.icon(BitmapDescriptorFactory.fromResource(R.drawable.img_signal_stain_red30)));
					}
					else{
						mMap.addMarker(
								new MarkerOptions()
								.position(new LatLng(pointsLow.get(i).getLat(),pointsLow.get(i).getLon()))
								.title("")
								.snippet("")
								.icon(BitmapDescriptorFactory.fromResource(R.drawable.img_signal_stain_red10)));
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
					if(signalSpotValues.length>1){
						newPoint.setLat(signalSpotValues[0]);
						newPoint.setLon(signalSpotValues[1]);
						newPoint.setAlt(signalSpotValues[2]);
						newPoint.setIntensity(signalSpotValues[3]);
						newPoint.setBandWidth(signalSpotValues[4]);
						//Log.w("POINTID", ""+signalSpotValues[5]);
						newPoint.setId(signalSpotValues[5]);
						newPoint.setConnectionType(signalSpotValues[6]);
						points.add(newPoint);
					}
				}

				return null;
			}

			@Override
			protected void onPostExecute(Object o){
				//Toast.makeText(ctxt,"TEST",Toast.LENGTH_SHORT).show();
				pointsHigh = new ArrayList<Point>();
				pointsMedium = new ArrayList<Point>();
				pointsLow = new ArrayList<Point>();
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
	/*
    public static class TelephonyControlSectionFragment extends Fragment {

        public static final String ARG_SECTION_NUMBER = "section_number";

        @SuppressWarnings("deprecation")
		@Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_section_minutescontrol, container, false);
            Resources res = getResources();

            ImageView medidorDeDias = (ImageView) rootView.findViewById(R.id.imageViewAnimated1);
            ImageView medidorDatConsumidos = (ImageView) rootView.findViewById(R.id.imageViewAnimated2);
            ImageView medidorDatRestantes = (ImageView) rootView.findViewById(R.id.imageViewAnimated3);

            AnimationDrawable animation1 = new AnimationDrawable();
            AnimationDrawable animation2 = new AnimationDrawable();
            AnimationDrawable animation3 = new AnimationDrawable();

            animation1.setOneShot(true);
            animation2.setOneShot(true);
            animation3.setOneShot(true);

            int from = 1;
            while (from < 45) {
                String name = "circle_" + String.format("%d", from);
                int image = res.getIdentifier(name, "drawable", ctxt.getPackageName());
                animation1.addFrame(res.getDrawable(image), 40);
                if(from<=12)
                animation3.addFrame(res.getDrawable(image), 40);
                else if(from<=37)
                animation2.addFrame(res.getDrawable(image), 40);
                from++;
            }
            medidorDeDias.setBackgroundDrawable(animation1);
            medidorDatConsumidos.setBackgroundDrawable(animation2);
            medidorDatRestantes.setBackgroundDrawable(animation3);

            animation1.start();
            System.gc();
            animation2.start();
            System.gc();
            animation3.start();

            System.gc();
            return rootView;
        }
    }
	 */
	/**
	 * A fragment representing data control section.
	 */
	public static class TelephonyReportSectionFragment extends Fragment {

		double completedCallsValue;
		double failedCallsValue;
		double unknownCallsValue;

		public static final String ARG_SECTION_NUMBER = "section_number";

		@SuppressWarnings("deprecation")
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_section_telephonyreport, container, false);
			Resources res = getResources();

			//**Values must come from DataBase**
			completedCallsValue = 0.69;
			failedCallsValue = 0.11;
			unknownCallsValue = 0.20;
			//*********************************

			ImageView medidorDeDias = (ImageView) rootView.findViewById(R.id.imageViewAnimated1);
			ImageView medidorDatConsumidos = (ImageView) rootView.findViewById(R.id.imageViewAnimated2);
			ImageView medidorDatRestantes = (ImageView) rootView.findViewById(R.id.imageViewAnimated3);

			TextView completedCallsText = (TextView) rootView.findViewById(R.id.textViewCompletedCalls);
			TextView failedCallsText = (TextView) rootView.findViewById(R.id.textViewFailedCalls);
			TextView unknownCallsText = (TextView) rootView.findViewById(R.id.textViewUnknown);
			Typeface myTypeface = Typeface.createFromAsset(getActivity().getAssets(), "cabin_regular.otf");
			completedCallsText.setTypeface(myTypeface);
			failedCallsText.setTypeface(myTypeface);
			unknownCallsText.setTypeface(myTypeface);

			completedCallsText.setText(""+(int)(completedCallsValue*100)+"%");
			failedCallsText.setText(""+(int)(failedCallsValue*100)+"%");
			unknownCallsText.setText(""+(int)(unknownCallsValue*100)+"%");

			AnimationDrawable animation1 = new AnimationDrawable();
			AnimationDrawable animation2 = new AnimationDrawable();
			AnimationDrawable animation3 = new AnimationDrawable();

			animation1.setOneShot(true);
			animation2.setOneShot(true);
			animation3.setOneShot(true);

			int from = 1;
			while (from < 50) {
				String name = "circlegreen_" + String.format("%d", from);
				int image = res.getIdentifier(name, "drawable", ctxt.getPackageName());
				if(from<=(int)(completedCallsValue*50))
					animation1.addFrame(res.getDrawable(image), 40);
				if(from<=(int)(unknownCallsValue*50))
					animation3.addFrame(res.getDrawable(image), 40);
				if(from<=(int)(failedCallsValue*50))
					animation2.addFrame(res.getDrawable(image), 40);
				from++;
			}
			medidorDeDias.setBackgroundDrawable(animation1);
			medidorDatConsumidos.setBackgroundDrawable(animation2);
			medidorDatRestantes.setBackgroundDrawable(animation3);

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
	 * A dummy fragment representing a section of the app, but that simply displays dummy.
	 */
	/*
    public static class DummySectionFragment extends Fragment {

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
				showDialog();
    		}

    		@Override
    		public void onLogin() {

            	SharedPreferences prefs = ctxt.getSharedPreferences("checatucell_prefs",Context.MODE_PRIVATE);
    			if(prefs.getLong("FacebookLogin", 0) == 0){
    				SharedPreferences.Editor editor = prefs.edit();
            		editor.putLong("FacebookLogin", 1);
                	editor.commit();	

	    			Feed feed = new Feed.Builder()
	    			.setMessage("Test")
	    			.setName("Checa Tu Cell para Android")
	    			.setCaption("Control de datos y minutos.")
	    			.setDescription(
	    					"Controla tu consumo de datos y minutos con la aplicaci??n oficial de la SCT.")
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
    				Log.w("TAG", "Fall?? la publicaci??n: "+reason);
    			}

    			@Override
    			public void onException(Throwable throwable) {
    				hideDialog();
    				Log.e("TAG", "Ocurrio algo extra??o intenta de nuevo.", throwable);
    			}

    			@Override
    			public void onThinking() {
    				// show progress bar or something to the user while publishing
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

            View rootView = inflater.inflate(R.layout.fragment_section_telephonconfig, container, false);
            final SharedPreferences prefs = ctxt.getSharedPreferences("checatucell_prefs",Context.MODE_PRIVATE);
            final TextView billDay = (TextView)rootView.findViewById(R.id.textView);
            Button loginFb = (Button)rootView.findViewById(R.id.fblogin);
            ImageView billingDate = (ImageView)rootView.findViewById(R.id.imageViewDia);
            final ImageView alarmSwitch = (ImageView)rootView.findViewById(R.id.imageViewAlarm);
            ImageView limitDataField = (ImageView)rootView.findViewById(R.id.imageViewLimit);

    		if(prefs.getLong("ActiveTelephonAlarm", 0) == 1){
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
                		editor.putLong("ActiveTelephonAlarm", 0);
                    	editor.commit();
            			toast("Alarma de consumo de datos desactivada!");
                		alarmOnFlag = false;
                	}
                	else{
                		alarmSwitch.setImageDrawable(getResources().getDrawable(R.drawable.img_alarma_activada));              		
                    	getActivity().startService(new Intent(ctxt, ActiveService.class));
                    	SharedPreferences.Editor editor = prefs.edit();
                		editor.putLong("ActiveTelephonAlarm", 1);
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

            String date = prefs.getString("LimitDate", "04/10/2014");
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
		    billDay.setText(""+npDate.getValue());
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

		    final String[] dataLimitnumbers1 = new String[11];
		    for(int i=0; i<dataLimitnumbers1.length; i++) {
		    	dataLimitnumbers1[i] = Integer.toString(i);
		    }
		    final String[] dataLimitnumbers2 = new String[102];
		    for(int i=0; i<dataLimitnumbers2.length; i++) {
		    	dataLimitnumbers2[i] = Integer.toString(i);
		    }
		    final String[] dataLimitByte = new String[3];
		    dataLimitByte[0] = "MB";
		    dataLimitByte[1] = "GB";
		    dataLimitByte[2] = "";


		    final NumberPicker npData1 = (NumberPicker)dialogDataLimit.findViewById(R.id.numberPickerA);
		    npData1.setMaxValue(9);
		    npData1.setMinValue(0);
		    npData1.setWrapSelectorWheel(true);
		    npData1.setDisplayedValues(dataLimitnumbers1);
		    final NumberPicker npData2 = (NumberPicker)dialogDataLimit.findViewById(R.id.numberPickerB);
		    npData2.setMaxValue(99);
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

		    System.gc();
            return rootView;
        }
    }

    /**
	 * Secci??n de activaci??n de servicio de control de consumo de datos.
	 */
	/*
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

	public void setLimitData(long limitData){

  	SharedPreferences prefs = getSharedPreferences("checatucell_prefs",Context.MODE_PRIVATE);
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
		mViewPager.setCurrentItem(tab.getPosition());

	}


	@Override
	public void onTabUnselected(Tab tab,
			android.support.v4.app.FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		System.gc();
	}

}
