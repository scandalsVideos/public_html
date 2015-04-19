package info.androidhive.slidingmenu;


import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class UserFunctions {
	
	private JSONParser jsonParser;
	public static String siteUrl="http://192.168.0.100/";
private static String loginURL = siteUrl+"/login/";
	private static String registerURL = "login/";
	//private static String videoWebservice = "http://scandalsvideos.com/webservice.php";
	private static String videoWebservice = siteUrl+"webservice.php";
	
	//private static String loginURL = "http://softtech.webege.com/login/";
	//private static String registerURL = "http://softtech.webege.com/login/";
	private static String login_tag = "login";
	private static String register_tag = "register";
	private static String video_tag = "shayari";
	private static String detail__shayari_tag = "shayariData";
	private static String shayariUpload = "shayariUpload";
	private static String userDetail = "uesr_detail";
	private static String userUpload = "updateUser";
	private static String getUser = "get_User";
	private static String deleteShayri = "deleteShayri";

	// constructor
	
	
	
	public UserFunctions(){
		jsonParser = new JSONParser();
	}
	
	/**
	 * function make Login Request
	 * @param email
	 * @param password
	 * */
	public JSONObject loginUser(String email, String password){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", login_tag));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
		return json;
	}
	
	/**
	 * function make Login Request
	 * @param name
	 * @param email
	 * @param password
	 * */
	@SuppressWarnings({ "deprecation", "deprecation" })
	public JSONObject registerUser(String name, String password, String email,String image,String city,String country){
		// Building Parameters

		try{
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(videoWebservice);
		MultipartEntity postEntity = new MultipartEntity();
		FileBody contentFile = null;
		//if(image.equalsIgnoreCase("a"))
		{
		File file = new File(image);
		contentFile=new FileBody(file);
		}
		 if(!image.equalsIgnoreCase("a"))
		postEntity.addPart("image", contentFile);
		postEntity.addPart("tag", new StringBody(register_tag));
		postEntity.addPart("name", new StringBody(name));
		postEntity.addPart("password", new StringBody(password));
		postEntity.addPart("email", new StringBody(email));
		postEntity.addPart("city", new StringBody(city));
		postEntity.addPart("country", new StringBody(country));
		post.setEntity(postEntity);
		HttpResponse response = client.execute(post);
	
		  HttpEntity resEntity = response.getEntity();
	        String Response=EntityUtils.toString(resEntity);
	        Log.e("bbbb", Response);
	    	JSONObject myObject = new JSONObject(Response);
		
	        client.getConnectionManager().shutdown();
	        Log.e("rrrr ", myObject.toString());
	        return myObject;
	        }
		catch(Exception e){
			
			return null; 
		}
	}
	public JSONObject getUser(String email,String id){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", getUser));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("id", id));
		
		// getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
	
		// return json
		return json;
	}
	public JSONObject getShayari(String cat,int rec){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", video_tag));
		params.add(new BasicNameValuePair("cat", cat));
		params.add(new BasicNameValuePair("rec", String.valueOf(rec)));
		
		// getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
		
        Log.d("Response:", json.toString());
		// return json
		return json;
	}
	public JSONObject getChannel(){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "chanels"));
		// getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(videoWebservice, params);
        Log.d("Response:", json.toString());
		// return json
		return json;
	}
	
	public JSONObject getAndroidVersion(){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "version"));
		// getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(videoWebservice, params);
        Log.d("Response:", json.toString());
		// return json
		return json;
	}
	
	public JSONObject getChannelData(String cat,int rec){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "channel_list"));
		params.add(new BasicNameValuePair("cat", cat));
		params.add(new BasicNameValuePair("rec", String.valueOf(rec)));
		
		// getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(videoWebservice, params);
		
        Log.d("Response:", json.toString());
		// return json
		return json;
	}

	public JSONObject getVideoData(String id){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "detail__video_tag"));
		params.add(new BasicNameValuePair("id", id));
		// getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(videoWebservice, params);
		 Log.d("Response:", json.toString());
		// return json
		return json;
	}

	public JSONObject getAllComments(String id){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "get_all_comments"));
		params.add(new BasicNameValuePair("id", id));
		// getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(videoWebservice, params);
		 Log.d("Response:", json.toString());
		// return json
		return json;
	}
	
	public JSONObject getShayariData(String id){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", detail__shayari_tag));
		params.add(new BasicNameValuePair("id", id));
		// getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
		 Log.d("Response:", json.toString());
		// return json
		return json;
	}
	
	
	public JSONObject getUserDetails(String id){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", userDetail));
		params.add(new BasicNameValuePair("id", id));
		// getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(videoWebservice, params);
		// return json
		return json;
	}
	
	public JSONObject deleteShayari(String id,String uid,String email){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", deleteShayri));
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("uid", uid));
		params.add(new BasicNameValuePair("email", email));
		// getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
		// return json
		return json;
	}
	
	@SuppressWarnings({ "deprecation", "deprecation" })
	public JSONObject uploadShayariData(String name,String catValue,String shayaritext,String image,String uid){
		
		try{
		
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(registerURL);
		MultipartEntity postEntity = new MultipartEntity();
		File file = new File(image);
		FileBody contentFile = new FileBody(file);
		 if(!image.equalsIgnoreCase("a"))
		postEntity.addPart("image", contentFile);
		postEntity.addPart("tag", new StringBody(shayariUpload));
		postEntity.addPart("cat", new StringBody(catValue));
		postEntity.addPart("title", new StringBody(name));
		postEntity.addPart("data", new StringBody(shayaritext));
		postEntity.addPart("uid", new StringBody(uid));

		post.setEntity(postEntity);
		HttpResponse response = client.execute(post);
	
		  HttpEntity resEntity = response.getEntity();
	        String Response=EntityUtils.toString(resEntity);
	    	JSONObject myObject = new JSONObject(Response);
		
	        client.getConnectionManager().shutdown();
	        
	        return myObject;
	        }
		catch(Exception e){
			
			return null; 
		}
	}
	
	@SuppressWarnings("deprecation")
	public JSONObject updateUser(String name,String password,String email,String image,String id){
		
		try{
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(registerURL);
		MultipartEntity postEntity = new MultipartEntity();
		FileBody contentFile = null;
		//if(image.equalsIgnoreCase("a"))
		{
		File file = new File(image);
		contentFile=new FileBody(file);
		}
		Log.e("aaa", "aaa");
		if(!image.equalsIgnoreCase("a"))		
			postEntity.addPart("image", contentFile);
		postEntity.addPart("tag", new StringBody(userUpload));
		postEntity.addPart("name", new StringBody(name));
		
		postEntity.addPart("password", new StringBody(password));
		postEntity.addPart("email", new StringBody(email));
		postEntity.addPart("id", new StringBody(id));
		
		post.setEntity(postEntity);
		HttpResponse response = client.execute(post);
	
		  HttpEntity resEntity = response.getEntity();
	        String Response=EntityUtils.toString(resEntity);
	        Log.e("bbbb", Response);
	    	JSONObject myObject = new JSONObject(Response);
		
	        client.getConnectionManager().shutdown();
	        Log.e("rrrr ", myObject.toString());
	        return myObject;
	        }
		catch(Exception e){
			
			return null; 
		}
	}
	
	
	
}
