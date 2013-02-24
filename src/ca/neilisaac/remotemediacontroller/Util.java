package ca.neilisaac.remotemediacontroller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;

public class Util {
	
	public static void doPlayPause(Context context) {
		pressMediaButton(context, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
	}

	public static void pressMediaButton(Context context, int button) {
		long eventtime = SystemClock.uptimeMillis();

		Intent downIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
		KeyEvent downEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_DOWN, button, 0);
		downIntent.putExtra(Intent.EXTRA_KEY_EVENT, downEvent);
		context.sendOrderedBroadcast(downIntent, null);

		Intent upIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
		KeyEvent upEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_UP, button, 0);
		upIntent.putExtra(Intent.EXTRA_KEY_EVENT, upEvent);
		context.sendOrderedBroadcast(upIntent, null);
	}

	public static void sendMessage(final String server, final int port, final String message) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					Socket socket = new Socket(server, port);
					OutputStream stream = socket.getOutputStream();
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream));

					writer.write(message + "\r\n");
					writer.flush();

				} catch (UnknownHostException e) {
					Log.e("util", e.getMessage());
				} catch (IOException e) {
					Log.e("util", e.getMessage());
				}
			}
		};
		
		new Thread(runnable).start();
	}
	
	public static boolean isServerEnabled(Context context) {
		SharedPreferences prefs = context.getSharedPreferences("prefs",  Context.MODE_PRIVATE);
		return prefs.getBoolean("server_enabled", false);
	}
	
	public static void setServerEnabled(Context context, boolean enabled) {
		SharedPreferences prefs = context.getSharedPreferences("prefs",  Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putBoolean("server_enabled", enabled);
		editor.commit();
	}
	
	public static String getHost(Context context) {
		SharedPreferences prefs = context.getSharedPreferences("prefs",  Context.MODE_PRIVATE);
		return prefs.getString("host", "localhost");
	}
	
	public static void setHost(Context context, String host) {
		SharedPreferences prefs = context.getSharedPreferences("prefs",  Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putString("host", host);
		editor.commit();
	}

	public static int getPort() {
		return 8675;
	}
}
