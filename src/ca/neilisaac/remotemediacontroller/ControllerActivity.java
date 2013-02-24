package ca.neilisaac.remotemediacontroller;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class ControllerActivity extends Activity {
	
	private static final int[] BUTTONS = {
		R.id.button_play,
		R.id.button_pause,
		R.id.button_stop,
		R.id.button_next,
		R.id.button_prev,
		R.id.button_down,
		R.id.button_mute,
		R.id.button_up,
	};
	
	private static final int[] MEDIA = {
		KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE,
		KeyEvent.KEYCODE_MEDIA_PAUSE,
		KeyEvent.KEYCODE_MEDIA_STOP,
		KeyEvent.KEYCODE_MEDIA_NEXT,
		KeyEvent.KEYCODE_MEDIA_PREVIOUS,
		KeyEvent.KEYCODE_VOLUME_DOWN,
		KeyEvent.KEYCODE_VOLUME_MUTE,
		KeyEvent.KEYCODE_VOLUME_UP,
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_controller);
		
		for (int i = 0; i < BUTTONS.length; i++) {
			ImageButton button = (ImageButton) findViewById(BUTTONS[i]);
			final int action = MEDIA[i];
			
			button.setOnClickListener(new ImageButton.OnClickListener() {
				@Override
				public void onClick(View view) {
					String host = Util.getHost(getBaseContext());
					if (host.equals(""))
						host = "localhost";
					
					Util.sendMessage(host, Util.getPort(), Integer.toString(action));
					
				}
			});
		}
		
		Switch enable = (Switch) findViewById(R.id.switch_enable);
		enable.setChecked(Util.isServerEnabled(this));
		enable.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton view, boolean checked) {
				Context context = getBaseContext();
				Util.setServerEnabled(context, checked);
				
				if (checked) {
					Intent startServiceIntent = new Intent(context, HostService.class);
					context.startService(startServiceIntent);
				} else {
					context.stopService(new Intent(ControllerActivity.this, HostService.class));
				}
			}
		});
		
		EditText host = (EditText) findViewById(R.id.form_server);
		host.setText(Util.getHost(this));
		host.setOnEditorActionListener(new OnEditorActionListener(){
			@Override
			public boolean onEditorAction(TextView view, int id, KeyEvent key) {
				EditText host = (EditText) findViewById(R.id.form_server);
				Util.setHost(getBaseContext(), host.getText().toString());
				return true;
			}
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		if (Util.isServerEnabled(this)) {
			// check if server is running; if not, enable it
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_controller, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int blah, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			Toast.makeText(this, "No settings", Toast.LENGTH_SHORT).show();
			return true;
			
		default:
			return false;
		}
	}

}
