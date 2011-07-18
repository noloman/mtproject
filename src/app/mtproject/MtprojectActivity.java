package app.mtproject;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MtprojectActivity extends Activity {
	/** Called when the activity is first created. */

	boolean callsCheckbox;
	boolean smsCheckbox;
	boolean locationCheckbox;
	String ListPreference, editTextPreference, ringtonePreference,
			secondEditTextPreference, customPref, date, duration, type,
			bodyColumn, addressColumn, dateColumn;
	private callsLoggingService callsService;
	private smsLoggingService smsService;
	private ServiceConnection callsLoggingConnection;
	private ServiceConnection smsLoggingConnection;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Binding to services
		bindingActivityToServices();

		setContentView(R.layout.main);
		getPrefs();
		
		Context context = getApplicationContext();
		
		dumpCallsLog();
		dumpSmsLog();

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
		callsCheckbox = prefs.getBoolean("callsLogChk", true);
		smsCheckbox = prefs.getBoolean("smsLogChk", true);
		locationCheckbox = prefs.getBoolean("locationLogChk", false);

		if (callsCheckbox) {
			ServiceConnection callsLoggingConnection = new ServiceConnection() {

				@Override
				public void onServiceConnected(ComponentName className,
						IBinder service) {
					callsService = ((callsLoggingService.MyBinder) service)
							.getService();
				}

				@Override
				public void onServiceDisconnected(ComponentName className) {
					callsService = null;
				}
			};
		}

		if (smsCheckbox) {
			ServiceConnection smsLoggingConnection = new ServiceConnection() {

				@Override
				public void onServiceConnected(ComponentName className,
						IBinder service) {
					smsService = ((smsLoggingService.MyBinder) service)
							.getService();
				}

				@Override
				public void onServiceDisconnected(ComponentName className) {
					smsService = null;
				}

			};
		}
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
				duration = cur.getString(cur
						.getColumnIndex(CallLog.Calls.DURATION));
				type = cur.getString(cur.getColumnIndex(CallLog.Calls.TYPE));
			} while (cur.moveToNext());
		}
	}

	private void dumpSmsLog() {
		Cursor cursor = getContentResolver().query(Uri.parse("content://sms/"),
				null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				dateColumn = cursor.getString(cursor.getColumnIndex("date"));
				bodyColumn = cursor.getString(cursor.getColumnIndex("body"));
				addressColumn = cursor.getString(cursor
						.getColumnIndex("address"));
			} while (cursor.moveToNext());
		}
	}

	private void bindingActivityToServices() {
		Intent bindIntentCallsLogging = new Intent(MtprojectActivity.this,
				callsLoggingService.class);
		bindService(bindIntentCallsLogging, callsLoggingConnection,
				Context.BIND_AUTO_CREATE);
		Intent bindIntentSmsLogging = new Intent(MtprojectActivity.this,
				smsLoggingService.class);
		bindService(bindIntentSmsLogging, smsLoggingConnection,
				Context.BIND_AUTO_CREATE);
	}
}