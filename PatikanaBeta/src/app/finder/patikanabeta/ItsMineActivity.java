package app.finder.patikanabeta;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ItsMineActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_itsmine);
		Bundle extras = getIntent().getExtras();

		if (extras != null) {
			final String docnumber = extras.getString("idnumber");
			String doctype = extras.getString("type");
			TextView id = (TextView) findViewById(R.id.idnumber);
			TextView type = (TextView) findViewById(R.id.type);
			id.setText(docnumber);
			type.setText(doctype);

			Button claim = (Button) findViewById(R.id.claimbutton);

			claim.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					String url = "http://10.0.2.2/newptk/dalvik/claim.php";
					EditText names = (EditText) findViewById(R.id.claimnamestext);

					String claimnames = names.getText().toString();

					String idnumber = docnumber;
					List<NameValuePair> paramsdata = new ArrayList<NameValuePair>();
					paramsdata.add(new BasicNameValuePair("number", idnumber));
					paramsdata.add(new BasicNameValuePair("names", claimnames));
					JSONParser jsonparser = new JSONParser();
					JSONObject jobject = jsonparser.makeHttpRequest(url,"POST", paramsdata);

					try {
						String verificationFlag = jobject.getString("verified");

						if (verificationFlag == "passed") {
							// display id found details Activity
							
						} else {
							
						/*	new AlertDialog.Builder(null).setTitle("Delete entry")
						    .setMessage("Are you sure you want to delete this entry?")
						    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						        public void onClick(DialogInterface dialog, int which) { 
						            // continue with delete
						        }
						     })
						    .setNegativeButton("No", new DialogInterface.OnClickListener() {
						        public void onClick(DialogInterface dialog, int which) { 
						            // do nothing
						        }
						     })
						     .show();*/
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

		} else {
			try {
				Class searchintent;
				searchintent = Class
						.forName("app.finder.patikanabeta.SearchActivity");
				Intent intent = new Intent(ItsMineActivity.this, searchintent);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}