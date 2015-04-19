package info.androidhive.slidingmenu;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.androidhive.CustomizedListView;
import com.example.androidhive.LazyAdapter;
import com.example.androidhive.R;
import com.example.androidhive.XMLParser;
import com.example.androidhive.showVideo;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("ValidFragment")
public class HindiFragment extends Fragment {
	// XML node keys
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
		JSONObject json;
		ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

		public Context hindiFragment=null;
		private int catId=0;
	public HindiFragment(){}
	
	public HindiFragment(int position) {
		// TODO Auto-generated constructor stub
		catId=position;
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		
		StrictMode.setThreadPolicy(policy); 

		
		
		 final View rootView = inflater.inflate(R.layout.main, container, false);
	       // TextView tvLabel =  (TextView)rootView.findViewById(R.id.txtLabel);
	       // tvLabel.setText("Hello"); try
		 hindiFragment=rootView.getContext();
		 try{
			 //Toast.makeText(hindiFragment,  "catid is "+catId, 
				//	   Toast.LENGTH_LONG).show();
			UserFunctions userFunction = new UserFunctions();
			json=userFunction.getAndroidVersion();
			 JSONArray android_version_array = json.getJSONArray("version");
			 TextView tv = (TextView) rootView.findViewById(R.id.android_version);
			 for (int i = 0; i < android_version_array.length(); i++) {
					// creating new HashMap
					HashMap<String, String> map = new HashMap<String, String>();
					 JSONObject myObj = android_version_array.getJSONObject(i);
					 if(! myObj.getString("version").equalsIgnoreCase(String.valueOf(getString(R.string.android_version)))){
					 tv.setText( Html.fromHtml(myObj.getString("text")));
			         tv.setMovementMethod(LinkMovementMethod.getInstance());
					 }else
					 {
						 tv.setVisibility(View.GONE);
					 }
					// adding each child node to HashMap key => value
				}
				
			
			
			 json = userFunction.getChannelData(String.valueOf(catId),0);
			 JSONArray deletedtrs_array = json.getJSONArray("cat");
			
			for (int i = 0; i < deletedtrs_array.length(); i++) {
				// creating new HashMap
				HashMap<String, String> map = new HashMap<String, String>();
				 JSONObject myObj = deletedtrs_array.getJSONObject(i);
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
			list=(ListView)rootView.findViewById(R.id.list);
			// Getting adapter by passing xml data ArrayList
	        adapter=new LazyAdapter(this.getActivity(), songsList);     
	        
	        list.setAdapter(adapter);
	        
	     // Click event for single list row
	        list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					
					HashMap<Object, String> obj = (HashMap<Object, String>) list.getAdapter().getItem(position);
		            String name = (String) obj.keySet().toString();
		            String name1 = (String) obj.get("id").toString();
					
					//Toast.makeText(rootView.getContext(), name1, 
						//   Toast.LENGTH_LONG).show();
					 Intent in = new Intent(rootView.getContext(), showVideo.class);
		                in.putExtra("id",name1);
		               startActivity(in);
				}
			});	
	        
	        
	        list.setOnScrollListener(new OnScrollListener() {
	            private int currentFirstVisibleItem;
				private int currentVisibleItemCount;
				private int currentScrollState;
				 private  boolean flag_loading=false;
				@Override
	            public void onScrollStateChanged(AbsListView view, int scrollState) {
	                // TODO Auto-generated method stub
					this.currentScrollState = scrollState;
	            }

	            @Override
	            public void onScroll(AbsListView view, int firstVisibleItem,
	                    int visibleItemCount, int totalItemCount) {
	            	   this.currentFirstVisibleItem = firstVisibleItem;
	            	    this.currentVisibleItemCount = visibleItemCount;
	            	    Log.e("firstVisibleItem+visibleItemCount ",String.valueOf(firstVisibleItem+visibleItemCount));
	            	    Log.e("totalItemCount ",""+totalItemCount);
	            	    this.flag_loading = false;
	            	    if((firstVisibleItem+visibleItemCount) == totalItemCount)
	                    {
	                        {
	                        int page=	totalItemCount+1;
	                            this.flag_loading = true;
	                           this.isScrollCompleted(page);
	                        }
	                    }
	            }
	            

				private void isScrollCompleted(int page) {
				   // if (this.currentVisibleItemCount > 0 && this.currentScrollState == SCROLL_STATE_IDLE)
				    {
				        /*** In this way I detect if there's been a scroll which has completed ***/
				        /*** do the work for load more date! ***/
				    	if(this.flag_loading){
				    	 loadMore(rootView,page);
				    	}
				    }
				}
	        });

       return rootView;
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		return rootView;
		 
    }

	public void loadMore(View rootView,int page){
		try{
		UserFunctions userFunction = new UserFunctions();
	    Log.e("page ",""+page);
	    int status=0;
		 json = userFunction.getChannelData(String.valueOf(catId),page);
		 if(json.has("video")){
		 JSONArray deletedtrs_array = json.getJSONArray("video");
		
		for (int i = 0; i < deletedtrs_array.length(); i++) {
			// creating new HashMap
			HashMap<String, String> map = new HashMap<String, String>();
			 JSONObject myObj = deletedtrs_array.getJSONObject(i);
			// adding each child node to HashMap key => value
			map.put(KEY_ID, myObj.getString("uid"));
			map.put(KEY_TITLE, myObj.getString("uid"));
			map.put(KEY_ARTIST,myObj.getString("video"));
			map.put(KEY_DURATION, myObj.getString("duration"));
			map.put(KEY_THUMB_URL,myObj.getString("thumb_url"));
			map.put(KEY_VIDEO_URL, myObj.getString("url"));
			map.put(KEY_UPLOAD_BY,"By: "+ myObj.getString("upload_by"));

			// adding HashList to ArrayList
			//if(!songsList.contains(map))
			{
			songsList.add(map);
			status=1;
			}
		}
		 }
		if(status==1){
		list=(ListView)rootView.findViewById(R.id.list);
		// Getting adapter by passing xml data ArrayList
       adapter=new LazyAdapter(this.getActivity(), songsList);     
       
       list.setAdapter(adapter);
       adapter.notifyDataSetChanged();
		}
		}catch(Exception e){
			
			e.printStackTrace();
		}
		
	}
	
	

}
