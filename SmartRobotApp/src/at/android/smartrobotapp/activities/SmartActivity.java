package at.android.smartrobotapp.activities;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import at.android.smartrobot.audio.AudioController;
import at.android.smartrobot.audio.AudioEvent;
import at.android.smartrobot.audio.AudioEventListener;
import at.android.smartrobot.network.UDPController;
import at.android.smartrobot.network.UDPReceiveEvent;
import at.android.smartrobot.network.UDPReceiveListener;
import at.android.smartrobot.usb.USBController;
import at.android.smartrobot.usb.USBReceiveEvent;
import at.android.smartrobot.usb.USBReceiveListener;
import at.android.smartrobotapp.helpers.SmartHandler;
import at.htl.enginecontrol.EngineTask;

public class SmartActivity extends ActionBarActivity implements UDPReceiveListener, USBReceiveListener,
		AudioEventListener {


	public static final String TAG = "SmartActivity";

	// UI
	public Button btnSend;

	// Connections
	// public USBController usbController;
	public UDPController udpController;
	public AudioController audioController;
	public USBController usbController;

	// Handler
	public SmartHandler handler = null;

	public boolean isWaitingForSignal = false;
	public long timeSendRequest;
	public long timeReceiveAcknowlage;
	public long timeReceiveSignal;
	
	public long udpRuntime;
//	public long serverRuntime;
	
	public boolean receivedAck=false;
	public boolean receivedSig=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_smart);

		handler = new SmartHandler(getApplicationContext());

		initConnections();

		initUI();

		btnSend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					usbController.send("A");
					timeSendRequest = System.nanoTime();
				} catch (IOException e) {
					handler.sendEmptyMessage(0);
				}
			}
		});

	}

	private void initUI() {
		btnSend = (Button) findViewById(R.id.btnSend);
	}

	private void initConnections() {
		try {
			// usbController = new USBController(getApplicationContext());
			udpController = new UDPController(50001, 8, "192.168.88.248", 50000);
		} catch (UnknownHostException | SocketException e) {
			// TODO
			e.printStackTrace();
		}

		udpController.addUDPReceiveListener(this);
		udpController.startListening();

		audioController = new AudioController();
		audioController.addSignalReceiveListener(this);
		audioController.startListening();
		
		usbController = new USBController(this);
		usbController.addUSBReceiveListener(this);
	}

	@Override
	protected void onDestroy() {
		if (udpController != null)
			udpController.stopListening();
		if (audioController != null)
			audioController.stopListening();
		if(usbController != null)
			usbController.onDestroy();
		super.onDestroy();
	}
		
	@Override
	protected void onPause() {
		if(usbController != null)
			usbController.onPause();
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		if(usbController != null)
			usbController.onResume();
		super.onResume();
	}
	
	@Override
	public void onUSBReceive(USBReceiveEvent e) {
		Message m = new Message();
		m.obj = "02";
		m.what = 1;
		handler.sendMessage(m);
	}

	@Override
	public void onUDPReceive(UDPReceiveEvent e) {
		timeReceiveAcknowlage = e.getTimestamp();
		udpRuntime= (timeReceiveAcknowlage-timeSendRequest)/2;
		receivedAck=true;
		
		if(receivedAck && receivedSig){
			calcDistance();
			receivedAck=false;
			receivedSig=false;
		}
		
	}

	@Override
	public void onSignalReceive(AudioEvent e) {
//		if (isWaitingForSignal) {
			timeReceiveSignal = e.getTimestamp();
			receivedSig=true;
			
			if(receivedAck && receivedSig){
				calcDistance();
				receivedAck=false;
				receivedSig=false;
			}
			
//		}
	}

	public void calcDistance(){
		Message msg = new Message();
		msg.what=1;
		long runtime = timeReceiveSignal - (timeSendRequest + udpRuntime);
		Log.d(TAG, "Rec. Sig.: " + timeReceiveSignal);
		Log.d(TAG, "Req.: " + timeSendRequest);
		Log.d(TAG, "UDP Run.: " + udpRuntime);
		Log.d(TAG, "Diff.: " + (timeReceiveSignal - timeSendRequest));
		timeReceiveAcknowlage = 0;
		timeReceiveSignal = 0;
		timeSendRequest = 0;
		udpRuntime = 0;
		double distance = 331.5 * (((double)runtime/1000000));
		msg.obj=(int)distance;
		handler.sendMessage(msg);
	}
	
	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.smart, menu);
	// return true;
	// }

	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// // Handle action bar item clicks here. The action bar will
	// // automatically handle clicks on the Home/Up button, so long
	// // as you specify a parent activity in AndroidManifest.xml.
	// int id = item.getItemId();
	// if (id == R.id.action_settings) {
	// return true;
	// }
	// return super.onOptionsItemSelected(item);
	// }
}