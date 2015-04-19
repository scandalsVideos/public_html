package com.example.androidhive;


import info.androidhive.slidingmenu.MainActivity;
import org.json.JSONObject;
import com.example.androidhive.LazyAdapter;
import com.example.androidhive.Login;
import com.example.androidhive.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class LoginRegister extends Activity {
	// XML node keys
		static final String KEY_SONG = "song"; // parent node
		static final String KEY_ID = "id";
		static final String KEY_TITLE = "title";
		static final String KEY_ARTIST = "artist";
		static final String KEY_DURATION = "duration";
		static final String KEY_THUMB_URL = "thumb_url";
		static final String KEY_VIDEO_URL = "video";
	    SharedPreferences sharedPreferences;
 		public static final String Mypref="MyPrefs";

		LoginRegister abc=this;
		ListView list;
	    LazyAdapter adapter;
		JSONObject json;
	public LoginRegister(){}
	
	@Override
	   protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		    setContentView(R.layout.lgin_register);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		
		StrictMode.setThreadPolicy(policy); 

	       // TextView tvLabel =  (TextView)rootView.findViewById(R.id.txtLabel);
	       // tvLabel.setText("Hello"); try

		 try{
		    	Button uploadData= (Button)findViewById(R.id.Signin);
		    	sharedPreferences=getSharedPreferences(Mypref,Context.MODE_PRIVATE);
		        if(sharedPreferences.contains("email")){
					if(sharedPreferences.contains("name")){
						if(sharedPreferences.contains("id")){
							 Intent in = new Intent(abc, MainActivity.class);
				               startActivity(in);
						}
					}
				}
		    	uploadData.setOnClickListener(new OnClickListener() {
		    	   @Override
		    	   public void onClick(View v) {
		    		   Intent in = new Intent(abc, Login.class);
			              startActivity(in);
		    	   }
		    	});

		    	Button registerButton= (Button)findViewById(R.id.register);

		    	registerButton.setOnClickListener(new OnClickListener() {
		    	   @Override
		    	   public void onClick(View v) {
		    		   Intent in = new Intent(abc, EditProfileLayout.class);
			              startActivity(in);
		    	   }
		    	});

		 }catch(Exception e){
			 e.printStackTrace();
		 }
		 
    }
}
