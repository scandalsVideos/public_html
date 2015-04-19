package com.example.androidhive;



import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import info.androidhive.slidingmenu.RoundedImageView;
import info.androidhive.slidingmenu.UserFunctions;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.androidhive.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ProfileLayout  extends Activity{
	JSONObject json;
	public static final String Mypref="MyPrefs";
    SharedPreferences sharedPreferences;
	ProfileLayout abc=this;
	static final String KEY_SONG = "song"; // parent node
	static final String KEY_ID = "id";
	static final String KEY_TITLE = "title";
	static final String KEY_ARTIST = "artist";
	static final String KEY_DURATION = "duration";
	static final String KEY_THUMB_URL = "thumb_url";
	static final String KEY_VIDEO_URL = "video";
	static final String KEY_UPLOAD_BY = "upload_by";

	ListView list;
    LazyAdapter adapter;
	@Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.profile_layout);
	    try{
	    	UserFunctions userFunction = new UserFunctions();
			TextView status=(TextView)findViewById(R.id.textView4);
			TextView name=(TextView)findViewById(R.id.textView1);
			TextView email=(TextView)findViewById(R.id.textView2);
			TextView created_at=(TextView)findViewById(R.id.textView6);
	    	 sharedPreferences=getSharedPreferences(Mypref,Context.MODE_PRIVATE);
	    	 findViewById(R.id.button1).setVisibility(View.GONE);
			findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
		    	   @Override
		    	   public void onClick(View v) {
		    		   Intent in = new Intent(abc, EditProfileLayout.class);
		               startActivity(in);
		    	   }
		    	   
		    	});
			  ImageView  profilepic = (ImageView) findViewById(R.id.imageView1);

			   
		 					 Intent intent = getIntent();
		 			        String uid = intent.getStringExtra("uid");
		 			        if(uid.equals(sharedPreferences.getString("id","")))
		 			        	findViewById(R.id.button1).setVisibility(View.VISIBLE);
		 					//uid=sharedPreferences.getString("id","");
			 json = userFunction.getUserDetails(uid);
			 JSONArray deletedtrs_array = json.getJSONArray("user");		
	    	for (int i = 0; i < deletedtrs_array.length(); i++) {
				 JSONObject myObj = deletedtrs_array.getJSONObject(i);
				 status.setText(myObj.getString("Status"));
				 name.setText(myObj.getString("name"));
				 email.setText(myObj.getString("email"));
				 created_at.setText(myObj.getString("created"));
				 if(!myObj.getString("profile_pic").equals("")){
				 URL url = new URL(UserFunctions.siteUrl+myObj.getString("profile_pic"));
			        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			        connection.setDoInput(true);
			        connection.connect();
			        InputStream input = connection.getInputStream();
			        Bitmap myBitmap = BitmapFactory.decodeStream(input);
			        profilepic.setImageBitmap(  RoundedImageView.getCroppedBitmap(myBitmap,60));
				 }
				 
	    	}
			   
	    	
			 JSONArray shayariArray = json.getJSONArray("video");
			ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
			for (int i = 0; i < shayariArray.length(); i++) {
				// creating new HashMap
				HashMap<String, String> map = new HashMap<String, String>();
				 JSONObject myObj = shayariArray.getJSONObject(i);
				// adding each child node to HashMap key => value
					map.put(KEY_ID, myObj.getString("id"));
					map.put(KEY_TITLE, myObj.getString("title"));
					map.put(KEY_ARTIST,myObj.getString("desc"));
					map.put(KEY_DURATION, myObj.getString("duration"));
					map.put(KEY_THUMB_URL,UserFunctions.siteUrl+myObj.getString("thumb"));
					map.put(KEY_VIDEO_URL,"");
					map.put(KEY_UPLOAD_BY,"By: "+ myObj.getString("upload_by"));

				// adding HashList to ArrayList
				songsList.add(map);
			}
			list=(ListView)findViewById(R.id.list);
			// Getting adapter by passing xml data ArrayList
	        adapter=new LazyAdapter(this, songsList);        
	        list.setAdapter(adapter);
	        
	     // Click event for single list row
	        list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					
					HashMap<Object, String> obj = (HashMap<Object, String>) list.getAdapter().getItem(position);
		            String name = (String) obj.keySet().toString();
		            String name1 = (String) obj.get("id").toString();
				
					 Intent in = new Intent(abc, showVideo.class);
		                in.putExtra("id",name1);
		               startActivity(in);
				}
			});	
	    	
	    	
	    	
	    	
	    	
	    }catch(Exception e){
	    	e.printStackTrace();
	    	
	    }
	}
}
