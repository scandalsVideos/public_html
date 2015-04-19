package info.androidhive.slidingmenu;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.androidhive.CustomizedListView;
import com.example.androidhive.LazyAdapter;
import com.example.androidhive.Login;
import com.example.androidhive.LoginRegister;
import com.example.androidhive.R;
import com.example.androidhive.XMLParser;
import com.example.androidhive.showVideo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SignOutFragment extends Fragment {
		public static final String Mypref="MyPrefs";
 	    SharedPreferences sharedPreferences;
	ListView list;
	    LazyAdapter adapter;
		JSONObject json;
	public SignOutFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		
		StrictMode.setThreadPolicy(policy); 

		 final View rootView = inflater.inflate(R.layout.sign_out, container, false);
		 sharedPreferences=rootView.getContext().getSharedPreferences(Mypref,Context.MODE_PRIVATE);
	       // TextView tvLabel =  (TextView)rootView.findViewById(R.id.txtLabel);
	       // tvLabel.setText("Hello"); try

		 try{
		    	Button uploadData= (Button)rootView.findViewById(R.id.Signout);

		    	uploadData.setOnClickListener(new OnClickListener() {
		    	   @Override
		    	   public void onClick(View v) {
		    		  Editor editor= sharedPreferences.edit();
		    		  editor.clear();
		    		  editor.commit();
		    		   Intent in = new Intent(rootView.getContext(), LoginRegister.class);
			              startActivity(in);
		    	   }
		    	});

		    	

       return rootView;
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		return rootView;
		 
    }
}
