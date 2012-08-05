package app.mtproject;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpActivity extends Activity {
	private static final int REQUEST_CODE = 10;
	Button mSendButton;
	ArrayList<NameValuePair> postParameters;
	EditText mUsername, mPassword, mUserAge, mUserCountry;
	Spinner mUserSexSpinner;
	ProgressDialog mProgressDialog;
	TextView mErrorTv;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup_layout);
		mUserAge = (EditText) findViewById(R.id.UserAgeEditText);
		mUserCountry = (EditText) findViewById(R.id.UserCountryEditText);
		mUserSexSpinner = (Spinner) findViewById(R.id.UserSexSpinner);
		mErrorTv = (TextView) findViewById(R.id.SignUpErrorTextView);
		mUsername = (EditText) findViewById(R.id.UsernameEditText);
		mPassword = (EditText) findViewById(R.id.PasswordEditText);
		mSendButton = (Button) findViewById(R.id.RegisterButton);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.UserSexValues,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mUserSexSpinner.setAdapter(adapter);
		mUserSexSpinner.setSelection(0);

		mSendButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				postParameters = new ArrayList<NameValuePair>();
				postParameters.add(new BasicNameValuePair("username", mUsername
						.getText().toString()));
				postParameters.add(new BasicNameValuePair("password", mPassword
						.getText().toString()));
				postParameters.add(new BasicNameValuePair("user_age", mUserAge
						.getText().toString()));
				postParameters.add(new BasicNameValuePair("user_sex",
						mUserSexSpinner.getSelectedItem().toString()));
				postParameters.add(new BasicNameValuePair("user_country",
						mUserCountry.getText().toString()));
				String response = null;
				try {
					mProgressDialog = ProgressDialog.show(SignUpActivity.this,
							"Working..", "User registration in progress", true,
							false);
					new Thread(new Runnable() {
						public void run() {
							registerUser(postParameters);
							mProgressDialog.dismiss();
						}
					}).start();
				} catch (Exception e) {
					mErrorTv.setText(e.toString());
				}
			}
		});
	}

	private void registerUser(ArrayList<NameValuePair> postParameters) {
		String response = null;
		String res = null;
		try {
			response = CustomHttpClient.executeHttpPost(
					"http://10.0.2.2/science/RegisterUser.php", postParameters);
			Log.i("MT", postParameters.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		res = response.toString();
		res = res.replaceAll("\\s+", "");
		if (res.equals("1")) {

			// We launch main activity to get the app running after successful
			// login

			Intent i = new Intent(getApplicationContext(),
					MtprojectActivity.class);
			i.putExtra("username", mUsername.getText().toString());
			startActivityForResult(i, REQUEST_CODE);
		} else {
			runOnUiThread(new Runnable() {
				public void run() {
					mErrorTv.setText("There was a problem. Please try again later.");
				}
			});
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			if (data.hasExtra("returnKey1")) {
				Toast.makeText(this, data.getExtras().getString("returnKey1"),
						Toast.LENGTH_SHORT).show();
			}
		}
	}
}