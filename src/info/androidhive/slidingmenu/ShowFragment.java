package info.androidhive.slidingmenu;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.androidhive.LazyAdapter;
import com.example.androidhive.R;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class ShowFragment extends Fragment{

    // Declare variables
    ProgressDialog pDialog;
    VideoView videoview;
 
 // XML node keys
 		static final String KEY_SONG = "song"; // parent node
 		static final String KEY_ID = "id";
 		static final String KEY_TITLE = "title";
 		static final String KEY_ARTIST = "artist";
 		static final String KEY_DURATION = "duration";
 		static final String KEY_THUMB_URL = "thumb_url";
 		static final String KEY_VIDEO_URL = "video";
 		// JSON Response node names
 			private static String KEY_SUCCESS = "success";
 			private static String KEY_ERROR = "error";
 			private static String KEY_ERROR_MSG = "error_msg";
 			private static String KEY_UID = "uid";
 			private static String KEY_NAME = "name";
 			private static String KEY_EMAIL = "email";
 			private static String KEY_CREATED_AT = "created_at";
String data=null;
View rootView=null;
 		ListView list;
 	    LazyAdapter adapter;
 		JSONObject json;
   // String VideoURL = "http://softtech.webege.com/a.mp4";
 		public ShowFragment(){}
 		
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
    	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

		StrictMode.setThreadPolicy(policy); 
		rootView = inflater.inflate(R.layout.show_shayari, container, false);
        // Insert your Video URL
        Intent intent = getActivity().getIntent();
        String VideoURL = intent.getStringExtra("video_url");
       // setContentView(R.layout.show_shayari);
        ImageButton sharingButton=(ImageButton)rootView.findViewById(R.id.imageButton1);
       // sharingButton.setImageResource(R.drawable.heart);
        sharingButton.setOnClickListener(new View.OnClickListener() {   
        	public void onClick(View v) { 
        	shareIt();
        	}
        	});
        

		 try{
			UserFunctions userFunction = new UserFunctions();
			 json = userFunction.getShayari("Hindi",0);
			 JSONArray deletedtrs_array = json.getJSONArray("video");
			 Toast.makeText(rootView.getContext(), deletedtrs_array.toString(), 
					   Toast.LENGTH_LONG).show();
			//TextView shayari=(TextView)findViewById(R.id.textView2);
			
			
			for (int i = 0; i < deletedtrs_array.length(); i++) {
				// creating new HashMap
				TextView shayari=(TextView)rootView.findViewById(R.id.textView2);
				TextView shayariTitle=(TextView)rootView.findViewById(R.id.textView1);
				HashMap<String, String> map = new HashMap<String, String>();
				 JSONObject myObj = deletedtrs_array.getJSONObject(i);
			
				shayariTitle.setText(myObj.getString("title"));
				shayari.setText( myObj.getString("url"));
				data=myObj.getString("url");
			}
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		 return rootView;
    
    }
    private void shareIt() {
    	//sharing implementation here
    	Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND); 
        sharingIntent.setType("text/plain");
        String shareBody = data;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
    startActivity(Intent.createChooser(sharingIntent, "Share via"));
    	}
    
    
}
