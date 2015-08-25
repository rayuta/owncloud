package com.owncloud.android.authentication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.owncloud.android.StaticData;
import com.owncloud.android.UserData;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class AccountNetworkHandler extends AsyncTask<String, Integer, String>{
	
	private ProgressDialog dialog;
	private UserData mData = UserData.GetInstance();
	private StaticData sData = StaticData.GetInstance();
	
	public AccountNetworkHandler(Context context){
		dialog = new ProgressDialog(context);
	}

	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		dialog.setMessage("기다리세요");
        dialog.show();
	}





	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub

		// params[0]= 1가입,2수정,3조회
		// params[1]= id
		// params[2]= pwd
		// params[3]= email
		
		// params[4]= email or pass ( 수정할 때 키 )
		// params[5]= email or pass ( 수정할 때 밸류 )
		

		
		
		if("1".equals(params[0])){
			/////////////// 가입
			int a = addUser(params[1],params[2]);
			Log.w("TEST10", "addUser statuscode : " + a+"");
			int b = editUserInfo(params[1], "email", params[3] );
			Log.w("TEST10", "editUser statuscode : "+b+"");
			//////////////
			
			return String.valueOf(a);
		}
		
		else if("2".equals(params[0])){
			// 수정
			int b = editUserInfo(params[1], params[4], params[5] );
			
			return String.valueOf(b);
		}
		else if("3".equals(params[0])){
			int c = getUserInfo(params[1]);
			
			return String.valueOf(c);
		}
		
		return null;



	}
	
	
	

	protected void onPostExecute(String result){
		if (dialog.isShowing()) {
            dialog.dismiss();
        }
	}
	protected void onProgressUpdate(Integer... progress){
		//	pb.setProgress(progress[0]);


	}

	public int editUserInfo(String id, String key, String value) {

		int statuscode = 0;
		DefaultHttpClient client = new DefaultHttpClient();
		Credentials creds = new UsernamePasswordCredentials(sData.getManagerId(), sData.getManagerPw());
		client.getCredentialsProvider().setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), creds);

		try {
			HttpPut put = new HttpPut(sData.getHttpBaseUrl() + "/ocs/v1.php/cloud/users/" + id);
		



			put.addHeader("Content-Type", "application/json");
			put.addHeader("Accept", "application/json");
			JSONObject keyArg = new JSONObject();

			keyArg.put("key", key);
			keyArg.put("value", value);

			StringEntity input = null;
			try {
				input = new StringEntity(keyArg.toString());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			put.setEntity(input);


			HttpResponse response = client.execute(put);
			final int httpStatusCode = response.getStatusLine().getStatusCode();

			//Log.w("TEST10", httpStatusCode+"");
			if (httpStatusCode == HttpStatus.SC_OK) {


				HttpEntity entity = response.getEntity();


				if (entity != null) {
					// System.out.println("Response content length: " + entity.getContentLength());
					// Log.w("TEST10", "Response content length: " + entity.getContentLength());
					// 콘텐츠를 읽어들임.
					BufferedReader rd = new BufferedReader(new InputStreamReader(
							entity.getContent()));

					statuscode = xmlParse(id,convertStreamToString(rd));
					//Log.w("TEST10", "editUserInfo statuscode : "+statuscode);


				}

			} else statuscode = httpStatusCode;

		} catch (UnsupportedEncodingException e) {
			//
		} catch (IOException e) {
			//
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 

		return statuscode;



	}














	public int getUserInfo(String id){
		int statuscode = 0;
		DefaultHttpClient client = new DefaultHttpClient();
		Credentials creds = new UsernamePasswordCredentials(sData.getManagerId(), sData.getManagerPw());
		client.getCredentialsProvider().setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), creds);

		try {
			HttpGet get = new HttpGet(sData.getHttpBaseUrl() + "/ocs/v1.php/cloud/users/" + id);

			HttpResponse response = client.execute(get);
			final int httpStatusCode = response.getStatusLine().getStatusCode();

			//Log.w("TEST10", httpStatusCode+"");
			if (httpStatusCode == HttpStatus.SC_OK) {


				HttpEntity entity = response.getEntity();

				if (entity != null) {
					// System.out.println("Response content length: " + entity.getContentLength());
					//Log.w("TEST10", "Response content length: " + entity.getContentLength());
					// 콘텐츠를 읽어들임.
					BufferedReader rd = new BufferedReader(new InputStreamReader(
							entity.getContent()));


					//Log.w("TEST10", convertStreamToString(rd));
					statuscode = xmlParse(id,convertStreamToString(rd));
					//Log.w("TEST10", "getUserInfo statuscode : "+statuscode);

				}

			} else statuscode = httpStatusCode;

		} catch (UnsupportedEncodingException e) {
			//
		} catch (IOException e) {
			//
		} 
		return statuscode;
	}









	public int addUser(String id, String pwd){

		int statuscode = 0;
		DefaultHttpClient client = new DefaultHttpClient();
		Credentials creds = new UsernamePasswordCredentials(sData.getManagerId(), sData.getManagerPw());
		client.getCredentialsProvider().setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), creds);

		try {
			HttpPost post = new HttpPost(sData.getHttpBaseUrl() + "/ocs/v1.php/cloud/users");

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);   
			nameValuePairs.add(new BasicNameValuePair("userid", id));
			nameValuePairs.add(new BasicNameValuePair("password", pwd));
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = client.execute(post);
			final int httpStatusCode = response.getStatusLine().getStatusCode();

			//Log.w("TEST10", httpStatusCode+"");
			if (httpStatusCode == HttpStatus.SC_OK) {


				HttpEntity entity = response.getEntity();

				if (entity != null) {
					// System.out.println("Response content length: " + entity.getContentLength());
					//Log.w("TEST10", "Response content length: " + entity.getContentLength());
					// 콘텐츠를 읽어들임.
					BufferedReader rd = new BufferedReader(new InputStreamReader(
							entity.getContent()));


					statuscode = xmlParse(id,convertStreamToString(rd));



				}

			} else statuscode = httpStatusCode;

		} catch (UnsupportedEncodingException e) {
			//
		} catch (IOException e) {
			//
		} 
		return statuscode;
	}

	public String convertStreamToString(BufferedReader reader) {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
		return sb.toString();
	}



	public int xmlParse(String _id, String xml)  {
		String statuscode="";
		try{ 
			InputSource is = new InputSource(new StringReader(xml));
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);



			// xpath 생성
			XPath xpath = XPathFactory.newInstance().newXPath();
			String expression = "//*/meta";
			String data = "//*/data";

			NodeList statcode = (NodeList)xpath.evaluate(expression, document, XPathConstants.NODESET);
			NodeList dataset = (NodeList)xpath.evaluate(data, document, XPathConstants.NODESET);


			for( int idx=0; idx<statcode.getLength(); idx++ ){

				expression = "//*/statuscode";
				statuscode = xpath.compile(expression).evaluate(document);
				//Log.w("TEST10", statuscode);

			}

			for( int idx=0; idx<dataset.getLength(); idx++ ){

				mData.setId(_id);
				mData.setDisplayname(_id);
	
				data = "//*/email";
				String email = xpath.compile(data).evaluate(document);
				//Log.w("TEST10", email);
				mData.setEmail(email);

				data = "//*/used";
				String used = xpath.compile(data).evaluate(document);
				//Log.w("TEST10", used);
				mData.setUsedSpace(used);

				data = "//*/total";
				String total = xpath.compile(data).evaluate(document);
				//Log.w("TEST10", total);
				mData.setTotalSpace(total);
			}
		}catch (Exception e) { }





		//http://pandorica.tistory.com/34

		//Log.w("TEST10", statuscode+"");
		return Integer.parseInt(statuscode);

	}


}