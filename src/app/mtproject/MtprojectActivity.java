package app.mtproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MtprojectActivity extends Activity {
	/** Called when the activity is first created. */

	boolean CheckboxPreference;
	String ListPreference;
	String editTextPreference;
	String ringtonePreference;
	String secondEditTextPreference;
	String customPref;
	String date = null;
	String duration = null;
	String type = null;
	String bodyColumn;
	String addressColumn;
	String dateColumn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		getPrefs();
		Context context = getApplicationContext();
		dumpCallsLog();
		dumpSmsLog();
		// getLastCallLogEntry(context);
		Button prefBtn = (Button) findViewById(R.id.prefsBtn);
		TextView dateLog = (TextView) findViewById(R.id.dateLog);
		TextView durationLog = (TextView) findViewById(R.id.durationLog);
		TextView typeLog = (TextView) findViewById(R.id.typeLog);
		TextView smsDateLog = (TextView) findViewById(R.id.textView1);
		TextView smsBodyLog = (TextView) findViewById(R.id.textView2);
		TextView smsAddressLog = (TextView) findViewById(R.id.textView3);
		dateLog.setText(date);
		durationLog.setText(duration);
		typeLog.setText(type);
		smsDateLog.setText(addressColumn);
		smsBodyLog.setText(bodyColumn);
		smsAddressLog.setText(dateColumn);
		prefBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Explicit intent to call the preferences
				Intent settingsActivity = new Intent(getBaseContext(),
						Preferences.class);
				startActivity(settingsActivity);
			}
		});
	}

	private void getPrefs() {
		// Get the xml/preferences.xml preferences
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		CheckboxPreference = prefs.getBoolean("callsLogChk", true);
		CheckboxPreference = prefs.getBoolean("smsLogChk", true);
		CheckboxPreference = prefs.getBoolean("locationLogChk", false);
	}

	private void dumpCallsLog() {
		// An array specifying which columns to return.

		String columns[] = new String[] { CallLog.Calls.DATE,
				CallLog.Calls.DURATION, CallLog.Calls.TYPE };
		Uri mContacts = CallLog.Calls.CONTENT_URI;
		Cursor cur = managedQuery(mContacts, columns, // Which columns to return
				null, // WHERE clause; which rows to return(all rows)
				null, // WHERE clause selection arguments (none)
				CallLog.Calls.DEFAULT_SORT_ORDER // Order-by clause (ascending
													// by name)

		);
		if (cur.moveToFirst()) {
			do {
				// Get the field values
				date = cur.getString(cur.getColumnIndex(CallLog.Calls.DATE));
				duration = cur.getString(cur.getColumnIndex(CallLog.Calls.DURATION));
				type = cur.getString(cur.getColumnIndex(CallLog.Calls.TYPE));
			} while (cur.moveToNext());
		}
	}

	private void dumpSmsLog() {
		Cursor cursor = getContentResolver().query(Uri.parse("content://sms/"), null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				dateColumn = cursor.getString(cursor.getColumnIndex("date"));
				bodyColumn = cursor.getString(cursor.getColumnIndex("body"));
				addressColumn = cursor.getString(cursor.getColumnIndex("address"));
			} while (cursor.moveToNext());
		}
	}
}