package info.androidhive.slidingmenu;

import info.androidhive.slidingmenu.adapter.NavDrawerListAdapter;
import info.androidhive.slidingmenu.model.NavDrawerItem;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.androidhive.CustomizedListView;
import com.example.androidhive.EditProfileLayout;
import com.example.androidhive.Login;
import com.example.androidhive.LoginRegister;
import com.example.androidhive.ProfileLayout;
import com.example.androidhive.R;
import com.example.androidhive.showVideo;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	MainActivity abc=this;
	// nav drawer title
	private CharSequence mDrawerTitle;
    SharedPreferences sharedPreferences;
 // button to show progress dialog
    Button btnShowProgress;
 
 
    // File url to download
   // private static String file_url = "http://api.androidhive.info/progressdialog/hive.jpg";
 
	// used to store app title
	private CharSequence mTitle;
	  LinearLayout drawerll;
	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	  JSONObject json;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

		StrictMode.setThreadPolicy(policy); 
		mTitle = mDrawerTitle = getTitle();
		
		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
		  drawerll = (LinearLayout) findViewById(R.id.drawerll);
		  LinearLayout  profileFrame = (LinearLayout) findViewById(R.id.drawer);
		 
		  profileFrame.setOnClickListener(new OnClickListener() {
	    	   @Override
	    	   public void onClick(View v) {
	    		   Intent in = new Intent(abc, ProfileLayout.class);
	    		   in.putExtra("uid",sharedPreferences.getString("id",""));
	               startActivity(in);
	    	   }
	    	});
		  
		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		try{
			
			UserFunctions userFunction = new UserFunctions();
			 json = userFunction.getChannel();
			 JSONArray cahnnel_array = json.getJSONArray("channel");
				
			 
			 for (int i = 0; i < cahnnel_array.length(); i++) {
					// creating new HashMap
					HashMap<String, String> map = new HashMap<String, String>();
					 JSONObject myObj = cahnnel_array.getJSONObject(i);
					navDrawerItems.add(new NavDrawerItem(myObj.getString("catName"), navMenuIcons.getResourceId(0, -1)));
				}
			// navDrawerItems.add(new NavDrawerItem("Register", navMenuIcons.getResourceId(0, -1)));
		}catch(Exception e){
			e.printStackTrace();
			
		}
		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, //nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name // nav drawer close - description for accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0,navDrawerItems.size());
		}
		
	}

	/**
	 * Slide menu item click listener
	 * */
	public class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			
			// display view for selected nav drawer item
			displayView(position,navDrawerItems.size());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
	//	boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
	//	menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		
		 final boolean drawerOpen = mDrawerLayout.isDrawerOpen(drawerll);
	        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
	      

	    //    return super.onPrepareOptionsMenu(menu);
		
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position,int listLength) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		//if(position!=(listLength-1))
		fragment = new HindiFragment((position+1));
		/*else
		{
			Toast.makeText(abc, "Now register", 
					   Toast.LENGTH_LONG).show();
			 Intent in = new Intent(abc, EditProfileLayout.class);
	
	
             //in.putExtra("id",name1);
            startActivity(in);
		}*/
		/*switch (position) {
		case 0:
			fragment = new HindiFragment();
			 //Intent in = new Intent(getApplicationContext(), CustomizedListView.class);
			// startActivity(in);
			break;
		case 1:
			fragment = new EnglishFragment();
			break;
		case 2:
			fragment = new ComedyFragment();
			break;
		case 3:
			fragment = new SadFragment();
			break;
		case 4:
			fragment = new NonVegFragment();
			break;
		case 5:
			fragment = new WhatsHotFragment();
			break;
		case 6:
			fragment = new UploadFragment();
			break;
		case 7:
			fragment = new SignOutFragment();
			break;
		default:
			break;
		}*/

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(drawerll);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
public static String getPref(String key,Context context){
	SharedPreferences pref=PreferenceManager.getDefaultSharedPreferences(context);
	return pref.getString(key, null);
}
}
