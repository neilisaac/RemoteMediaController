package ca.neilisaac.remotemediacontroller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import android.content.Context;
import android.util.Log;

public class HostThread implements Runnable {

	private boolean alive;
	private Context context;
	

	public HostThread(Context context) {
		this.alive = false;
		this.context = context;
	}
	
	public void shutdown() {
		alive = false;
		Util.sendMessage("localhost", Util.getPort(), "0");
	}
	
	@Override
	public void run() {
		alive = true;
		
		ServerSocket server;
		InputStream stream;
		BufferedReader reader;

		Log.d("thread", "starting thread");
		
		try {
			server = new ServerSocket(Util.getPort());
			server.setReuseAddress(true);
			
		} catch (IOException e) {
			Log.e("server", e.getMessage());
			alive = false;
			return;
		}
		
		while (alive) {
			try {
				Socket socket = server.accept();
				stream = socket.getInputStream();
				reader = new BufferedReader(new InputStreamReader(stream));
				
				String message = reader.readLine();
				
				Log.d("thread", "processing message: " + message);
				
				int key = Integer.parseInt(message);
				
				if (key == 0) {
					server.close();
					break;
				}
				
				Util.pressMediaButton(context, key);
				
			} catch (IOException e) {
				Log.e("server", e.getMessage());
			}
		}
		
		Log.d("thread", "stopping thread");
	}
	
}