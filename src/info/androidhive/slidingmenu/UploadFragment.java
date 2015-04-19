package info.androidhive.slidingmenu;
	
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.androidhive.Login;
import com.example.androidhive.R;
import com.example.androidhive.showVideo;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
public class UploadFragment extends Fragment implements OnClickListener {
	String  imagepath="a";
	 View rootView=null;
	  private Spinner spinner1, spinner2;
	  private Button uploadButton, btnselect;
	   private ProgressDialog progress;
	    SharedPreferences sharedPreferences;
 		public static final String Mypref="MyPrefs";
		static final String KEY_ID = "id";
	  private int serverResponseCode = 0;
	  JSONObject json;
	public UploadFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

		StrictMode.setThreadPolicy(policy); 
		rootView = inflater.inflate(R.layout.fragment_upload, container, false);
  	  sharedPreferences=rootView.getContext().getSharedPreferences(Login.Mypref,Context.MODE_PRIVATE);

		 uploadButton = (Button)rootView.findViewById(R.id.button1);
		    btnselect = (Button)rootView.findViewById(R.id.button2);
		    progress = new ProgressDialog(rootView.getContext());
	        progress.setTitle("Please Wait!!");
	        progress.setMessage("Wait!!");
	        progress.setCancelable(true);
		addItemsOnSpinner2();
		addListenerOnButton();
		addListenerOnSpinnerItemSelection();
	        uploadButton.setOnClickListener(this);
			 btnselect.setOnClickListener(this);
		 return rootView;
	}
	

	  // add items into spinner dynamically
	  public void addItemsOnSpinner2() {
	 
		spinner2 = (Spinner) rootView.findViewById(R.id.spinner1);
		List<String> list = new ArrayList<String>();
		list.add("Hindi");
		list.add("English");
		list.add("Comedy");
		list.add("Sad");
		list.add("Nonveg");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(),
			android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(dataAdapter);
	  }
	 
	  public void addListenerOnSpinnerItemSelection() {
		spinner1 = (Spinner) rootView.findViewById(R.id.spinner1);
	  }
	 
	  // get the selected dropdown list value
	  public void addListenerOnButton() {
		spinner1 = (Spinner) rootView.findViewById(R.id.spinner1);
		  }

	@Override
	public void onClick(View arg0) {
		if(arg0==btnselect)
        {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Complete action using"), 1);
        }
        else if (arg0==uploadButton) {   
        	// progress.show();
	                      uploadData(imagepath);
	                 	// progress.dismiss();
	        }
	}	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
         
        if (requestCode == 1) {
            //Bitmap photo = (Bitmap) data.getData().getPath(); 
           
            Uri selectedImageUri = data.getData();
           imagepath= getRealPathFromURI(selectedImageUri);
            Toast.makeText(rootView.getContext(),  imagepath, 
					   Toast.LENGTH_LONG).show();
        }
    }

	public String getRealPathFromURI(Uri contentUri) {
	    String res = null;
	    String[] proj = { MediaStore.Images.Media.DATA };
	    Cursor cursor = rootView.getContext().getContentResolver().query(contentUri, proj, null, null, null);
	    if(cursor.moveToFirst()){;
	       int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	       res = cursor.getString(column_index);
	    }
	    cursor.close();
	    return res;
	}
	 @SuppressWarnings("deprecation")
	public int uploadData(String sourceFileUri) {   
              try { 
            	//  Toast.makeText(rootView.getContext(),  imagepath, 
   					//   Toast.LENGTH_LONG).show();
            System.out.close();
            	  EditText name= (EditText) rootView.findViewById(R.id.editText1);
            	  String shayariName=name.getText().toString();
            	  if(shayariName.trim().length()!=0){
            	  EditText text= (EditText) rootView.findViewById(R.id.editText2);
            	  String shayaritext=text.getText().toString();
            	  Spinner mySpinner=(Spinner) rootView.findViewById(R.id.spinner1);
            	  String catValue = mySpinner.getSelectedItem().toString();
            	  UserFunctions userFunction = new UserFunctions();
      			if(sharedPreferences.contains("email")){
      				if(sharedPreferences.contains("name")){
      					if(sharedPreferences.contains("id")){
      						if(imagepath.isEmpty()){
      	            		  imagepath="a";
      	            	  }
      						 json = userFunction.uploadShayariData(shayariName,catValue,shayaritext, imagepath,sharedPreferences.getString("id",""));
      		     			// progress.dismiss();
      		     			 JSONArray deletedtrs_array = json.getJSONArray("video");
      		     			for (int i = 0; i < deletedtrs_array.length(); i++) {
      		    				// creating new HashMap
      		    				HashMap<String, String> map = new HashMap<String, String>();
      		    				 JSONObject myObj = deletedtrs_array.getJSONObject(i);
      		    				 Intent in = new Intent(rootView.getContext(), showVideo.class);
      				                in.putExtra("video_url",myObj.getString("uid"));
      				                startActivity(in);
      		     			}
      					}
      				}
      			}
     			
            	  }else{
            		  Toast.makeText(rootView.getContext(),  "Please Enter details", 
          					   Toast.LENGTH_LONG).show();
            		  
            	  }
                    
             } catch (Exception e) {
                 e.printStackTrace();   
             }
               
             return serverResponseCode; 
        }	 
}
