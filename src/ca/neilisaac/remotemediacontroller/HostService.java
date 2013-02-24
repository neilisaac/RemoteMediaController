package ca.neilisaac.remotemediacontroller;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class HostService extends Service {

	LocalBinder binder;
	HostThread hostThread;

	public HostService() {
		binder = new LocalBinder();
	}
	
	public void start() {
		Log.d("host", "starting host");
		
		stop();
		
		hostThread = new HostThread(this);
		new Thread(hostThread).start();
	}
	
	public void stop() {
		if (hostThread != null) {
			Log.d("host", "stopping host");
			
			hostThread.shutdown();
			hostThread = null;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	@Override
	public void onCreate() {
	}

	@Override
	public void onDestroy() {
		stop();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int id) {
		super.onStartCommand(intent, flags, id);
		
		start();

		return Service.START_STICKY;
	}

	public class LocalBinder extends Binder {
		public HostService getService() {
			return HostService.this;
		}
	}
}
