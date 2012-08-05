package app.mtproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SplashActivity extends Activity {
	private static final int REQUEST_CODE = 10;
	Button acceptBtn, cancelBtn;
	TextView splashInstructions;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_layout);

		acceptBtn = (Button) findViewById(R.id.acceptBtn);
		cancelBtn = (Button) findViewById(R.id.cancelBtn);
		splashInstructions = (TextView) findViewById(R.id.splashInstructions);

		acceptBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), LoginActivity.class);
				startActivityForResult(i, REQUEST_CODE);
			}
		});

		cancelBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
			}
		});
	}
}
