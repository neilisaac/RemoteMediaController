package ca.neilisaac.remotemediacontroller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (Util.isServerEnabled(context)) {
			Intent startServiceIntent = new Intent(context, HostService.class);
			context.startService(startServiceIntent);
		}
	}

}
