package com.example.androidhive;



import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import info.androidhive.slidingmenu.MainActivity;
import info.androidhive.slidingmenu.UserFunctions;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.androidhive.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class EditProfileLayout  extends Activity{
	JSONObject json;
	EditProfileLayout abc=this;
	static final String KEY_SONG = "song"; // parent node
	static final String KEY_ID = "id";
	static final String KEY_TITLE = "title";
	static final String KEY_ARTIST = "artist";
	static final String KEY_DURATION = "duration";
	static final String KEY_THUMB_URL = "thumb_url";
	static final String KEY_VIDEO_URL = "video";
	 private ImageView btnselect;
	 private Button uploadData;
	  private int serverResponseCode = 0;
	 String  imagepath="a";
	ListView list;
    LazyAdapter adapter;
    String status="register";
	public static final String Mypref="MyPrefs";
	    SharedPreferences sharedPreferences;
	@Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.edit_profile_layout);
	    try{
	    	UserFunctions userFunction = new UserFunctions();
	    	EditText name=(EditText)findViewById(R.id.editText1);
	    	EditText email=(EditText)findViewById(R.id.editText2);
	    	uploadData= (Button)findViewById(R.id.button1);
	    	EditText password=(EditText)findViewById(R.id.editText3);
	    	 sharedPreferences=getSharedPreferences(Mypref,Context.MODE_PRIVATE);
			  ImageView  profilepic = (ImageView) findViewById(R.id.imageView1);

	         if(sharedPreferences.contains("email")){
	 			if(sharedPreferences.contains("name")){
	 				if(sharedPreferences.contains("id")){
	 					uploadData.setText("Update");
	 					json = userFunction.getUserDetails(sharedPreferences.getString("id",""));
	 					 JSONArray deletedtrs_array = json.getJSONArray("user");		
	 			    	for (int i = 0; i < deletedtrs_array.length(); i++) {
	 						 JSONObject myObj = deletedtrs_array.getJSONObject(i);
	 						 name.setText(myObj.getString("name"));
	 						 email.setText(myObj.getString("email"));
	 						 if(!myObj.getString("profile_pic").equals("")){
	 						 URL url = new URL(myObj.getString("profile_pic"));
	 				        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	 				        connection.setDoInput(true);
	 				        connection.connect();
	 				        InputStream input = connection.getInputStream();
	 				        Bitmap myBitmap = BitmapFactory.decodeStream(input);
	 				        profilepic.setImageBitmap( myBitmap);
	 						 }
	 			    	}
	 			    	status="update";
	 				}
	 			}
	 		}
			 
	    	btnselect = (ImageView)findViewById(R.id.imageView1);
	    	btnselect.setOnClickListener(new OnClickListener() {
		    	   @Override
		    	   public void onClick(View v) {
		    		   Intent intent = new Intent();
		               intent.setType("image/*");
		               intent.setAction(Intent.ACTION_GET_CONTENT);
		               startActivityForResult(Intent.createChooser(intent, "Complete action using"), 1);
		    	   }
		    	});
	    	uploadData.setOnClickListener(new OnClickListener() {
		    	   @Override
		    	   public void onClick(View v) {
		    		   uploadData();
		    	   }
		    	});
	    	 }catch(Exception e){
	    	e.printStackTrace();
	    	
	    }
	    
	   
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
         
        if (requestCode == 1) {
            //Bitmap photo = (Bitmap) data.getData().getPath(); 
            Uri selectedImageUri = data.getData();
           imagepath= getRealPathFromURI(selectedImageUri);
            Toast.makeText(this,  imagepath, 
					   Toast.LENGTH_LONG).show();
        }
    }
	public String getRealPathFromURI(Uri contentUri) {
	    String res = null;
	    String[] proj = { MediaStore.Images.Media.DATA };
	    Cursor cursor =this.getContentResolver().query(contentUri, proj, null, null, null);
	    if(cursor.moveToFirst()){;
	       int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	       res = cursor.getString(column_index);
	    }
	    cursor.close();
	    return res;
	}
	
		public int uploadData() {   
	              try { 
	            	 
	            System.out.close();	            	  
	            		EditText name=(EditText)findViewById(R.id.editText1);
		            	  String userName=name.getText().toString();
	            	  if(userName.trim().length()!=0){
		        	    	EditText email=(EditText)findViewById(R.id.editText2);
		        	    	EditText password=(EditText)findViewById(R.id.editText3);
	            	  String emailString=email.getText().toString();
	            	  String passwordString=password.getText().toString();
	            	  UserFunctions userFunction = new UserFunctions();

	            	  if(imagepath.isEmpty()){
	            		  imagepath="a";
	            	  }
	            	  if(status.equals("update")){

		            	
	     			 json = userFunction.updateUser(userName,passwordString,emailString, imagepath,sharedPreferences.getString("id",""));
	     			  String errorMsg=json.get("error").toString();
		              	if(errorMsg.equals("0")){
	     			 Toast.makeText(abc,  "Record Sucessfully Updated", 
		   					   Toast.LENGTH_LONG).show();
	     			Intent in = new Intent(abc, MainActivity.class);
		             startActivity(in);
		              	}else{
		              		 Toast.makeText(abc,  json.get("error_msg").toString(), 
				   					   Toast.LENGTH_LONG).show();
		              	}
	            	  }
	            	  else{
	 	     			 json = userFunction.registerUser(userName,passwordString,emailString, imagepath,"city","country");
		            	 
		            	  String errorMsg=json.get("error").toString();
		              	if(errorMsg.equals("0")){
		              		 Toast.makeText(abc,  "Profile Sucessfully Created. Please login to continue...", 
				   					   Toast.LENGTH_LONG).show();
		     			 Intent in = new Intent(abc, Login.class);
		     			 startActivity(in);
		              	}else{
		              		 Toast.makeText(abc,  json.get("error_msg").toString(), 
				   					   Toast.LENGTH_LONG).show();
		              	}
	            	  }
	     		
	     			// progress.dismiss();
	            	  }else{
	            		  Toast.makeText(abc,  "Please Enter details", 
	          					   Toast.LENGTH_LONG).show();
	            		  
	            	  }
	                    
	             } catch (Exception e) {
	                 e.printStackTrace();   
	             }
	               
	             return serverResponseCode; 
	        }	 
}
