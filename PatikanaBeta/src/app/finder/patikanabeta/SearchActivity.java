package app.finder.patikanabeta;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SearchActivity extends Activity {

	class SearchDocument extends AsyncTask<String, String, String> {
		InputStream is = null;
		JSONObject jObj = null;

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			Log.e("Docuemnts array", "working");
			idnumber = (EditText) findViewById(R.id.editText1);
			parameter = idnumber.getText().toString();
			JSONArray document = null;
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("idnumber", parameter));
			JSONParser jParser = new JSONParser();
			JSONObject json = jParser.makeHttpRequest(url, "POST", params);
			Log.i("Documents array", json.toString());
			
			try {
				int success = json.getInt("success");
				if (success == 1) {
					String id = json.getString("idnumber");
					String type = json.getString("doctype");
					String packagename = "com.example.mystart";
					String classname = packagename + ".Second";
					try {
						Class ItsMine = Class.forName("app.finder.patikanabeta.ItsMineActivity");
						Intent intent = new Intent(SearchActivity.this,ItsMine);
						Bundle b = new Bundle();
						intent.putExtra("idnumber", id);
						intent.putExtra("type", type);
						startActivity(intent);
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					//Load the Report Lost Activity
					Class ReportLost;
					try {
						ReportLost = Class.forName("app.finder.patikanabeta.ReportLostActivity");
						Intent To_ReportLost=new Intent(SearchActivity.this, ReportLost);
						startActivity(To_ReportLost);
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			} catch (JSONException E) {
				E.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute() {
			pDialog.dismiss();
			// try parse the string to a JSON object

		}

		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(SearchActivity.this);
			pDialog.setMessage("Searching...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

	}
	static String url = "http://10.0.2.2/newptk/dalvik/search.php";
	public EditText idnumber;
	public String parameter;

	private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		Button searchbutton = (Button) findViewById(R.id.button1);
		searchbutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new SearchDocument().execute(parameter);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_search, menu);
		return true;
	}

}
