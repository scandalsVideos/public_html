package com.example.androidhive;


import info.androidhive.slidingmenu.MainActivity;
import info.androidhive.slidingmenu.UserFunctions;

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

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.AdapterView.OnItemClickListener;
 
public class showVideo extends Activity {
	public  showVideo abc=this;
    // Declare variables
    VideoView videoview;
    public static final String Mypref="MyPrefs";
    SharedPreferences sharedPreferences;
 // XML node keys
 		static final String KEY_SONG = "song"; // parent node
 		static final String KEY_ID = "id";
 		static final String KEY_TITLE = "title";
 		static final String KEY_ARTIST = "artist";
 		static final String KEY_DURATION = "duration";
 		static final String KEY_THUMB_URL = "thumb_url";
 		static final String KEY_VIDEO_URL = "video";
		static final String KEY_UPLOAD_BY = "upload_by";
		// button to show progress dialog
	    Button btnShowProgress;

	    // Progress Dialog
	    private ProgressDialog pDialog;
	    ImageView my_image;
	    // Progress dialog type (0 - for Horizontal progress bar)
	    public static final int progress_bar_type = 0; 
	    private static String file_url = "http://api.androidhive.info/progressdialog/hive.jpg";
 		// JSON Response node names
 			private static String KEY_SUCCESS = "success";
 			private static String KEY_ERROR = "error";
 			private static String KEY_ERROR_MSG = "error_msg";
 			private static String KEY_UID = "uid";
 			private static String KEY_NAME = "name";
 			private static String KEY_EMAIL = "email";
 			private static String KEY_CREATED_AT = "created_at";
String data=null;
 		ListView list;
 	    LazyAdapter adapter;
 	   private ProgressDialog progress;
 		JSONObject json;
 		 String id="";
   // String VideoURL = "http://softtech.webege.com/a.mp4";
 		ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
 		  String videoUrl="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Insert your Video URL
        final Intent intent = getIntent();
      
         id = intent.getStringExtra("id");
        setContentView(R.layout.show_shayari);
        ImageView sharingButton=(ImageView)findViewById(R.id.imageButton1);
        ImageView downloadButton=(ImageView)findViewById(R.id.download);
        progress = new ProgressDialog(this);
        progress.setTitle("Please Wait!!");
        progress.setMessage("Wait!!");
        progress.setCancelable(false);
        sharedPreferences=getSharedPreferences(Mypref,Context.MODE_PRIVATE);
      // sharingButton.setImageResource(R.drawable.heart);
       
		 try{
			UserFunctions userFunction = new UserFunctions();
			 json = userFunction.getVideoData(id);
	    	 findViewById(R.id.button1).setVisibility(View.GONE);
	    	 sharingButton.setOnClickListener(new View.OnClickListener() {   
	         	public void onClick(View v) { 
	         	shareIt();
	         	}
	         	});
	         
	       
			 JSONArray deletedtrs_array = json.getJSONArray("video");			
			for (int i = 0; i < deletedtrs_array.length(); i++) {
				// creating new HashMap
				TextView shayari=(TextView)findViewById(R.id.textView2);
				TextView shayariTitle=(TextView)findViewById(R.id.textView1);
				TextView shayariDate=(TextView)findViewById(R.id.textView3);
				TextView likes=(TextView)findViewById(R.id.likes);
				VideoView shayariImage=(VideoView)findViewById(R.id.imageView1);
				TextView uploadBy=(TextView)findViewById(R.id.textView4);
				 JSONObject myObj = deletedtrs_array.getJSONObject(i);
				shayariTitle.setText(myObj.getString("title"));
				shayari.setText( myObj.getString("desc"));
			final String uploadId=myObj.getString("upload_by");
			
			if(uploadId.equals(sharedPreferences.getString("id","")))
		        	findViewById(R.id.button1).setVisibility(View.VISIBLE);
			
				shayariDate.setText( "Added On: "+myObj.getString("written_on"));
				likes.setText( ""+myObj.getString("likes"));
				uploadBy.setText( "Added By: "+myObj.getString("upload_by"));
				uploadBy.setOnClickListener(new OnClickListener() {
			    	   @Override
			    	   public void onClick(View v) {
			    		   Intent in = new Intent(abc, ProfileLayout.class);
			    		   in.putExtra("uid","1");
				              startActivity(in);
			    	   }
			    	});
				//data=myObj.getString("url");
				 MediaController mediacontroller = new MediaController(this);
		            mediacontroller.setAnchorView(shayariImage);
		            videoUrl=UserFunctions.siteUrl+myObj.getString("source");
		             final String downloadUrl=videoUrl;
		            // Get the URL from String VideoURL
		            Uri video = Uri.parse(videoUrl);
		            shayariImage.setMediaController(mediacontroller);
		            shayariImage.setVideoURI(video);
		            shayariImage.start();
			    	 //shayariImage.requestFocus();
			    	  downloadButton.setOnClickListener(new View.OnClickListener() {   
				         	public void onClick(View v) { 
				         		MainActivity myActivity = new MainActivity();
				 
				         		DownloadFileFromURL asyncTask=new DownloadFileFromURL();
				         		asyncTask.execute(downloadUrl);
				         	}
				         	});
			}

			JSONObject json1 = userFunction.getAllComments(id);
			if(json1.has("comment")){
			 JSONArray comment_array = json1.getJSONArray("comment");			
			for (int i = 0; i < comment_array.length(); i++) {
				// creating new HashMap
				HashMap<String, String> map = new HashMap<String, String>();
				 JSONObject myObj = comment_array.getJSONObject(i);
				// adding each child node to HashMap key => value
				map.put(KEY_ID, myObj.getString("comment_id"));
				map.put(KEY_TITLE, myObj.getString("comment_user_name"));
				map.put(KEY_ARTIST,myObj.getString("comment_text"));
				map.put(KEY_DURATION, "");
				map.put(KEY_THUMB_URL,UserFunctions.siteUrl+myObj.getString("user_thumb"));
				map.put(KEY_VIDEO_URL,"");
				map.put(KEY_UPLOAD_BY,"");
				// adding HashList to ArrayList
				songsList.add(map);
			}
			
			list=(ListView)findViewById(R.id.listView1);
			// Getting adapter by passing xml data ArrayList
	        adapter=new LazyAdapter(this, songsList);     
	        list.setAdapter(adapter);
			}else{
				//TextView shayari=(TextView)findViewById(R.id.textView2);
			      //LinearLayout.LayoutParams Params1 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.FILL_PARENT);
			      //shayari.setLayoutParams(Params1);
			}
		
			
			
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		 findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
	    	   @Override
	    	   public void onClick(View v) {
	    		   UserFunctions userFunction = new UserFunctions();
	  			 json = userFunction.deleteShayari(intent.getStringExtra("video_url"),sharedPreferences.getString("id",""),sharedPreferences.getString("email",""));
	    		  // Intent in = new Intent(abc, EditProfileLayout.class);
	             //  startActivity(in);
	    	   }
	    	});
    
    }
    private void shareIt() {
    	//sharing implementation here
    	Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND); 
        sharingIntent.setType("text/plain");
        String shareBody = "Hi, I found this video very interesting, click here to watch "+UserFunctions.siteUrl+"video/"+id+"/";
      //  sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
    startActivity(Intent.createChooser(sharingIntent, "Share via"));
    	}

/**
 * Showing Dialog
 * */
@Override
protected Dialog onCreateDialog(int id) {
    switch (id) {
    case progress_bar_type: // we set this to 0
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Downloading file. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setMax(100);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setCancelable(true);
        pDialog.show();
        return pDialog;
    default:
        return null;
    }
}

/**
 * Background Async Task to download file
 * */
public class DownloadFileFromURL extends AsyncTask<String, String, String> {

    /**
     * Before starting background thread
     * Show Progress Bar Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        showDialog(progress_bar_type);
    }

    /**
     * Downloading file in background thread
     * */
    @Override
    protected String doInBackground(String... f_url) {
        int count;
        try {
            URL url = new URL(f_url[0]);
            URLConnection conection = url.openConnection();
            conection.connect();
            // this will be useful so that you can show a tipical 0-100% progress bar
            int lenghtOfFile = conection.getContentLength();

            // download the file
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            // Output stream
            OutputStream output = new FileOutputStream("/sdcard/Scandal_Videos_"+Math.random()+".mp4");

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                publishProgress(""+(int)((total*100)/lenghtOfFile));

                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

        return null;
    }

    /**
     * Updating progress bar
     * */
    protected void onProgressUpdate(String... progress) {
        // setting progress percentage
        pDialog.setProgress(Integer.parseInt(progress[0]));
   }

    /**
     * After completing background task
     * Dismiss the progress dialog
     * **/
    @Override
    protected void onPostExecute(String file_url) {
        // dismiss the dialog after the file was downloaded
        dismissDialog(progress_bar_type);

        // Displaying downloaded image into image view
        // Reading image path from sdcard
      
    }

}

    
}
