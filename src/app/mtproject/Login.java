package app.mtproject;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity {
	private static final int REQUEST_CODE = 10;
	EditText un, pw;
	TextView error;
	Button ok;
	ProgressDialog pd;
	ArrayList<NameValuePair> postParameters;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		un = (EditText) findViewById(R.id.et_un);
		pw = (EditText) findViewById(R.id.et_pw);
		ok = (Button) findViewById(R.id.btn_login);
		error = (TextView) findViewById(R.id.tv_error);

		ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				postParameters = new ArrayList<NameValuePair>();
				postParameters.add(new BasicNameValuePair("username", un
						.getText().toString()));
				postParameters.add(new BasicNameValuePair("password", pw
						.getText().toString()));
				String response = null;
				try {
					pd = ProgressDialog.show(v.getContext(), "Working..",
							"Logging the user in", true, false);
					new Thread(new Runnable() {
						public void run() {
							logUserIn(postParameters);
							pd.dismiss();
						}
					}).start();
				} catch (Exception e) {
					un.setText(e.toString());
				}
			}
		});
	}

	private void logUserIn(ArrayList<NameValuePair> postParameters) {
		String response = null;
		String res = null;
		try {
			response = CustomHttpClient.executeHttpPost("http://10.0.2.2/science/login.php", postParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		res = response.toString();
		res = res.replaceAll("\\s+", "");
		if (res.equals("1")) {

			// We launch main activity to get the app running after successful login

			Intent i = new Intent(getApplicationContext(),MtprojectActivity.class);
			i.putExtra("username", un.getText().toString());

			startActivityForResult(i, REQUEST_CODE);
		} else {
			runOnUiThread(new Runnable() {
				public void run() {
					error.setText("Incorrect user/password");
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