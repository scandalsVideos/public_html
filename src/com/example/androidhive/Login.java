package com.example.androidhive;


import info.androidhive.slidingmenu.MainActivity;
import info.androidhive.slidingmenu.UserFunctions;
import org.json.JSONObject;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
 
public class Login extends Activity {
    // Declare variables
    ProgressDialog pDialog;
    VideoView videoview;
    Login abc=this;
 		ListView list;
 	    LazyAdapter adapter;
 		JSONObject json; 
 		public static final String Mypref="MyPrefs";
 	    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Insert your Video URL
        setContentView(R.layout.login);
        Button uploadData= (Button)findViewById(R.id.proceedButton);
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
    			TextView email=(TextView)findViewById(R.id.email);
				TextView password=(TextView)findViewById(R.id.password);
				String emailString=email.getText().toString();
				String passwordString=password.getText().toString();
    		  login(emailString,passwordString);
    	   }
    	});
    }
   
    public void login(String emailString, String passwordString){
    	UserFunctions userFunction = new UserFunctions();
    	JSONObject json = userFunction.loginUser(emailString,passwordString);
    	try{
    	Log.e("Login String", json.get("error").toString());
    	String errorMsg=json.get("error").toString();
    	if(errorMsg.equals("0")){
    		Editor editor=sharedPreferences.edit();
    		editor.putString("email", json.get("email").toString());
    		editor.putString("id", json.get("uid").toString());
    		editor.putString("name", json.get("name").toString());
    		editor.commit();
    		   Intent in = new Intent(abc, MainActivity.class);
               startActivity(in);
    	}else
    	{
        	Log.e("Login String", "Incorrect Username and Password.");
        	Toast.makeText(abc, "Incorrect Username and Password.", 
					   Toast.LENGTH_LONG).show();
    	}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    
}
