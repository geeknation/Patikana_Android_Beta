package app.finder.patikanabeta;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class ReportFound extends Activity {

	class ReportDocFound extends AsyncTask<Void, ProgressDialog, JSONObject> {
		static final String url = "http://10.0.2.2/newptk/dalvik/reportFound.php";
		AlertDialog alertDialog = new AlertDialog.Builder(ReportFound.this).create();
		InputStream is = null;
		JSONObject jObj = null;
		ProgressDialog pDialog;

		@Override
		protected JSONObject doInBackground(Void... res) {
			// TODO Auto-generated method stub
			Log.e("Docuemnts array", "working");
			JSONArray document = null;
			SessionManager smg=new SessionManager(getApplicationContext());
			HashMap<String, String> user = smg.getUserDetails();
			userid=user.get(SessionManager.KEY_uID);
			
			username=user.get(SessionManager.KEY_uNAME);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			params.add(new BasicNameValuePair("userid", userid));
			params.add(new BasicNameValuePair("type", selected));
			params.add(new BasicNameValuePair("names", names));
			params.add(new BasicNameValuePair("docnumber", docnumber));
			params.add(new BasicNameValuePair("other", extradata));
			params.add(new BasicNameValuePair("location", location));
			
			JSONParser jParser = new JSONParser();
			
			JSONObject json = jParser.makeHttpRequest(url, "POST", params);
			
			return json;
		}

		protected void onPostExecute(JSONObject json) {

				pDialog.dismiss();
				try {
					String success = json.getString("feedback");
					pDialog.dismiss();
					if (success == "1") {
						// display Toast to show user the ID has been uploaded.
						Context context=getApplicationContext();
						Toast toast = Toast.makeText(context,"Document has been successfully added",Toast.LENGTH_LONG);
						toast.show();
					}
					if(success=="3") {

						// MAke dialog to show user there was an error creating a
						// dialog
						AlertDialogManager diag=new AlertDialogManager();
						
						diag.showAlertDialog(ReportFound.this, "Upload error", "the document has been already been reported", false);
					}
					
					if(success=="2"){
						alertDialog.setTitle("Error");
						alertDialog.setMessage("there was an error reporting the document.");
						alertDialog.setButton("OK",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int which) {
								alertDialog.dismiss();
						}
								});
//						alertDialog.setIcon(R.drawable.icon);
						alertDialog.show();
					}
					
					
				} catch (JSONException E) {
					E.printStackTrace();
				}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ReportFound.this);
			pDialog.setMessage("uploading...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

	}//end of http class
	String docnumber;
	String extradata;
	String location;
	// TODO Auto-generated constructor stub
	String names;
	String selected;
	String userid;
	
	
	
	String username;

	protected void onCreate(Bundle v) {

		super.onCreate(v);
		setContentView(R.layout.activity_reportfound);
		
		
		
		final String[] doctypes = { "National ID", "School ID", "ATM Card",
				"Passport" };

		final Spinner spinner = (Spinner) findViewById(R.id.spinner1);

		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.documents_array,
				android.R.layout.simple_spinner_item);

		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {

				String item = doctypes[pos];

				EditText otherTextinput = (EditText) findViewById(R.id.editTextExtrainfo);
				TextView otherText = (TextView) findViewById(R.id.textViewextrainfo);

				if (item == "National ID") {
					otherText.setVisibility(View.INVISIBLE);
					otherTextinput.setVisibility(View.INVISIBLE);
				}
				if (item == "School ID") {
					otherText.setText("School");
					otherText.setVisibility(View.VISIBLE);
					otherTextinput.setVisibility(View.VISIBLE);

				}
				if (item == "ATM Card") {
					otherText.setText("Bank");
					otherText.setVisibility(View.VISIBLE);
					otherTextinput.setVisibility(View.VISIBLE);
				}
				if (item == "Passport") {
					otherText.setText("Country");
					otherText.setVisibility(View.VISIBLE);
					otherTextinput.setVisibility(View.VISIBLE);
				}

			}

			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		Button reportbutton = (Button) findViewById(R.id.reportfoundbutton);
		reportbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				// fetch all the values in the fields
				EditText namesText = (EditText) findViewById(R.id.editTextNames);
				names = namesText.getText().toString();
				EditText docnumberText = (EditText) findViewById(R.id.editTextNumber);
				docnumber = docnumberText.getText().toString();
				EditText extradataText = (EditText) findViewById(R.id.editTextExtrainfo);
				extradata = extradataText.getText().toString();
				EditText locationText = (EditText) findViewById(R.id.editTextLocation);
				location = locationText.getText().toString();

				selected = spinner.getSelectedItem().toString();

				if (selected== "School id" || selected == "ATM Card" || selected == "Passport") {
					// upload for document that aint national id

					if (names.length() == 0 || docnumber.length() == 0 || extradata.length() == 0 || location.length() == 0) {
						Toast.makeText(getApplicationContext(),
								"Fill in all blanks", Toast.LENGTH_LONG).show();
					} else {
						// send dayta to server
						new ReportDocFound().execute();

					}

				} else {
					// upload for national id
					if (names.length() == 0 || docnumber.length() == 0 || location.length() == 0) {
						Toast.makeText(getApplicationContext(),
								"Fill in all the fields", Toast.LENGTH_LONG).show();
					} else {
						// send dayta to servers
						new ReportDocFound().execute();

					}

				}

			}
		});
		
		
		Button logoutButton=(Button) findViewById(R.id.logoutbutton);
		
		logoutButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new SessionManager(getApplicationContext()).logoutUser();
			}
		});

	}

}
