package app.mtproject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import cwac.locpoll.*;
import com.commonsware.cwac.wakeful.WakefulIntentService;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.util.Log;

public class LoggingService extends WakefulIntentService {

	Context context;
	long callsTriggerAlarmTime, smsTriggerAlarmTime, locationTriggerAlarmTime;
	private String user_id;
	private String type;
	private String duration;
	private String date;
	private String username;
	private String body;
	private String destination;
	private String latitude, longitude;
	private String serviceToStop;

	AlarmManager alarms;
	PendingIntent callsAlarmIntent, smsAlarmIntent, locationAlarmIntent;
	int alarmType;

	String serviceToStart;
	String dumpCalls;
	SharedPreferences prefs;

	public LoggingService() {
		super("SimpleIntentService");
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

//	@Override
//	protected void onHandleIntent(Intent intent) {
//		
//	}

	private void retrieveUserId() {
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair("username", username));
		String response = null;
		try {
			response = CustomHttpClient.executeHttpPost("http://10.0.2.2/science/getUserId.php", postParameters);
			String res = response.toString();
			res = res.replaceAll("\\s+", "");
			if (res.equals("1")) {
				Log.d(getClass().getSimpleName(), "Successfully retrieved user_id");
				user_id = res;
			} else {
				Log.d(getClass().getSimpleName(), "Error retrieving user_id");
			}
		} catch (Exception e) {
		}
	}

	private void sendCallsData(String user_id) {
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair("user_id", user_id));
		postParameters.add(new BasicNameValuePair("date", date));
		Log.d(date, "Inserted date");
		postParameters.add(new BasicNameValuePair("duration", duration));
		postParameters.add(new BasicNameValuePair("type", type));
		String response = null;
		try {
			response = CustomHttpClient.executeHttpPost("http://10.0.2.2/science/sendCallsData.php", postParameters);
			String res = response.toString();
			res = res.replaceAll("\\s+", "");
			if (res.equals("1")) {
				Log.d(getClass().getSimpleName(), "Insertado en DB!");
			} else {
				Log.d(getClass().getSimpleName(), "Error insertando en la DB");
			}
		} catch (Exception e) {
		}
	}

	private void sendSmsData(String user_id) {
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair("user_id", user_id));
		postParameters.add(new BasicNameValuePair("date", date));
		Log.d(date, "Inserted date");
		postParameters.add(new BasicNameValuePair("body", body));
		postParameters.add(new BasicNameValuePair("destination", destination));
		String response = null;
		try {
			response = CustomHttpClient.executeHttpPost(
					"http://10.0.2.2/science/sendSmsData.php", postParameters);
			String res = response.toString();
			res = res.replaceAll("\\s+", "");
			if (res.equals("1")) {
				Log.d(getClass().getSimpleName(), "Insertado en DB!");
			} else {
				Log.d(getClass().getSimpleName(), "Error insertando en la DB");
			}
		} catch (Exception e) {
		}
	}

	private void sendLocationData(String user_id) {
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair("user_id", user_id));
		postParameters.add(new BasicNameValuePair("latitude", latitude));
		postParameters.add(new BasicNameValuePair("longitude", longitude));
		Log.d(latitude, "latitude");
		Log.d(longitude, "longitude");
		String response = null;
		try {
			response = CustomHttpClient.executeHttpPost(
					"http://10.0.2.2/science/sendLocationData.php",
					postParameters);
			String res = response.toString();
			res = res.replaceAll("\\s+", "");
			if (res.equals("1")) {
				Log.d(getClass().getSimpleName(), "Insertado en DB!");
			} else {
				Log.d(getClass().getSimpleName(), "Error insertando en la DB");
			}
		} catch (Exception e) {
		}
	}

	private void dumpCallsLog() {
		ContentResolver cr = getContentResolver();
		String columns[] = new String[] { CallLog.Calls.DATE,
				CallLog.Calls.DURATION, CallLog.Calls.TYPE };
		Uri mContacts = CallLog.Calls.CONTENT_URI;
		Cursor c = cr.query(mContacts, columns, // Which columns to return
				null, // WHERE clause; which rows to return(all rows)
				null, // WHERE clause selection arguments (none)
				CallLog.Calls.DEFAULT_SORT_ORDER // Order-by clause
				// (ascending by name)

				);
		if (c.moveToFirst()) {
			do {
				// date = c.getString(c.getColumnIndex(CallLog.Calls.DATE));
				try {
					date = create_datestring(c.getLong(c
							.getColumnIndex(CallLog.Calls.DATE)));
				} catch (java.text.ParseException e) {
					e.printStackTrace();
				}
				duration = c
						.getString(c.getColumnIndex(CallLog.Calls.DURATION));
				type = c.getString(c.getColumnIndex(CallLog.Calls.TYPE));
			} while (c.moveToNext());
		}
		retrieveUserId();
		sendCallsData(user_id);
	}

	private void dumpSmsLog() {
		Cursor cursor = getContentResolver().query(Uri.parse("content://sms/"),
				null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				// try {
				// date =
				// create_datestring(cursor.getString(cursor.getColumnIndex("date")));
				// } catch (java.text.ParseException e1) {
				// e1.printStackTrace();
				// }
				date = cursor.getString(cursor.getColumnIndex("date"));
				body = cursor.getString(cursor.getColumnIndex("body"));
				destination = cursor
						.getString(cursor.getColumnIndex("address"));
			} while (cursor.moveToNext());
		}
		retrieveUserId();
		sendSmsData(user_id);
	}

	private void dumpLocationLog(String latitude, String longitude) {
		retrieveUserId();
		sendLocationData(user_id);
	}

	private static String create_datestring(long l)
			throws java.text.ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
		Date finalDate = new Date(l);
		return formatter.format(finalDate);
	}

	private static String create_datestring(String string)
			throws java.text.ParseException {
		android.os.Debug.waitForDebugger();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDateString = sdf.format(string);
		return formattedDateString;
	}

	private Long toLong(int hours) {
		Long frequency = Long.valueOf((((hours * 60) * 60) * 1000));
		return frequency;
	}

	@Override
	protected void doWakefulWork(Intent intent) {
		alarms = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmType = AlarmManager.RTC_WAKEUP;
		context = getApplicationContext();
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		username = intent.getStringExtra("username");
		serviceToStart = intent.getStringExtra("serviceToStart");
		serviceToStop = intent.getStringExtra("serviceToStop");

		if (serviceToStop != null) {
			if (serviceToStop.equals("calls")) {
				Intent stopCalls = new Intent("ACTION_CALLS_LOGGING");
				PendingIntent senderStopCalls = PendingIntent.getBroadcast(this, 0, stopCalls, 0);
				alarms = (AlarmManager) getSystemService(ALARM_SERVICE);
				alarms.cancel(senderStopCalls);
				
			} else if (serviceToStop.equals("sms")) {
				Intent stopSms = new Intent("ACTION_SMS_LOGGING");
				PendingIntent senderStopSms = PendingIntent.getBroadcast(this, 0, stopSms, 0);
				alarms = (AlarmManager) getSystemService(ALARM_SERVICE);
				alarms.cancel(senderStopSms);
			}

			else if (serviceToStop.equals("location")) {
				Intent stopLocation = new Intent(this, LocationPoller.class);
				PendingIntent senderStopLocation = PendingIntent.getBroadcast(this, 0, stopLocation, 0);
				alarms = (AlarmManager) getSystemService(ALARM_SERVICE);
				alarms.cancel(senderStopLocation);
			}
		} else {
			if (serviceToStart != null) {

				if (serviceToStart.equals("dumpCalls")) {
					dumpCallsLog();
				} else if (serviceToStart.equals("dumpSms")) {
					dumpSmsLog();
				} else if (serviceToStart.equals("dumpLocation")) {
					latitude = intent.getStringExtra("latitude");
					longitude = intent.getStringExtra("longitude");
					dumpLocationLog(latitude, longitude);

				} else if (serviceToStart.equals("calls")) {

					String CALLS_ALARM_ACTION = "ACTION_CALLS_LOGGING";
					Intent callsIntentToFire = new Intent(CALLS_ALARM_ACTION);

					callsIntentToFire.putExtra("serviceToStart", "dumpCalls");
					callsIntentToFire.putExtra("username", username);

					callsAlarmIntent = PendingIntent.getBroadcast(context, 0, callsIntentToFire, 0);

					callsTriggerAlarmTime = AlarmManager.ELAPSED_REALTIME_WAKEUP;

					long callsAlarmInterval = 30000L;
					// toLong(Integer.parseInt(prefs.getString(Preferences.CALLS_FREQUENCY_PREF,
					// "86400000")));

					alarms.setRepeating(alarmType, callsTriggerAlarmTime,
							callsAlarmInterval, callsAlarmIntent);

				} else if (serviceToStart.equals("sms")) {

					String SMS_ALARM_ACTION = "ACTION_SMS_LOGGING";
					Intent smsIntentToFire = new Intent(SMS_ALARM_ACTION);

					smsIntentToFire.putExtra("serviceToStart", "dumpSms");
					smsIntentToFire.putExtra("username", username);

					smsAlarmIntent = PendingIntent.getBroadcast(context, 0, smsIntentToFire, 0);
					smsTriggerAlarmTime = AlarmManager.ELAPSED_REALTIME_WAKEUP;
					long smsAlarmInterval = 30000L;
							//toLong(Integer.parseInt(prefs.getString(Preferences.SMS_FREQUENCY_PREF,"86400000")));
					alarms.setRepeating(alarmType, smsTriggerAlarmTime,
							smsAlarmInterval, smsAlarmIntent);
				}

				else if (serviceToStart.equals("location")) {

					Intent locationIntentToFire = new Intent(this, LocationPoller.class);

					//Intent i = new Intent(this, LoggingReceiver.class);
					Intent i = new Intent ("ACTION_LOCATION_LOGGING");

					locationIntentToFire.putExtra("serviceToStart", "dumpLocation");
					locationIntentToFire.putExtra("username", username);

					locationIntentToFire.putExtra(LocationPoller.EXTRA_INTENT, i);
					locationIntentToFire.putExtra(LocationPoller.EXTRA_PROVIDER, LocationManager.GPS_PROVIDER);

					long locationAlarmInterval = 30000L;
							//toLong(Integer.parseInt(prefs.getString(Preferences.LOCATION_FREQUENCY_PREF, "86400000")));

					locationAlarmIntent = PendingIntent.getBroadcast(this, 0, locationIntentToFire, 0);
					alarms.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), locationAlarmInterval, locationAlarmIntent);
				}
			}
		}
	}
}
