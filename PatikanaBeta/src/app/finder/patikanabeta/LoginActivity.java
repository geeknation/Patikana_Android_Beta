package app.finder.patikanabeta;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity{

	//http class starts here.
	class Login extends AsyncTask<Void ,ProgressDialog, JSONObject > {
		static final String url = "http://10.0.2.2/newptk/dalvik/auth.php";
		InputStream is = null;
		JSONObject jObj = null;
		ProgressDialog pDialog;

		@Override
		protected JSONObject doInBackground(Void... res) {
			// TODO Auto-generated method stub
			Log.e("Auth", "working");
			JSONArray document = null;
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", username));
			params.add(new BasicNameValuePair("password", password));
			JSONParser jParser = new JSONParser();
			JSONObject json = jParser.makeHttpRequest(url, "POST", params);
			
	
			return json;
		}
		@Override
		protected void onPostExecute(JSONObject json) {
			pDialog.dismiss();
			SessionManager smg=new SessionManager(getApplicationContext());
			int flag = 0;
			try {
				flag = json.getInt("success");
				
				if(flag==1){
					userid=json.getString("userid");
					//set the session 
					smg.createLoginSession(username, userid);	
					//Login the user
					Intent i = new Intent(getApplicationContext(), ReportFound.class);
					startActivity(i);
					finish();
					
					
				}else{
					AlertDialogManager diag=new AlertDialogManager();
					diag.showAlertDialog(LoginActivity.this, "Login", "Incorrect Username/password", false);
				}
				
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(LoginActivity.this);
			pDialog.setMessage("Authenticating...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		

	}//end of http class
	Context context;
	public String password;
	
	public String userid;
	public String username;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context=getApplicationContext();
		setContentView(R.layout.activity_login);
		
		
		
		Button loginbutton=(Button) findViewById(R.id.loginbutton);
		
		final EditText usernameText=(EditText) findViewById(R.id.usernameInput);
		final EditText passwordText=(EditText) findViewById(R.id.passwordInput);
		
		loginbutton.setOnClickListener(new OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				username=usernameText.getText().toString();
				password=passwordText.getText().toString();
				
				if(username.trim().length()==0 || password.trim().length()==0){
					Toast.makeText(getApplicationContext(), "username and password are required", Toast.LENGTH_LONG);
					
				}else{
					//send the username and password for verification
					new Login().execute();
					
				}
				
				
			}
		});
		
		
	}
	
	
}
