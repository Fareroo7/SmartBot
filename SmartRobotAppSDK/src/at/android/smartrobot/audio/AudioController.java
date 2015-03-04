package at.android.smartrobot.audio;

import java.util.ArrayList;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

public class AudioController extends Thread {

	public static final int SAMPLE_RATE = 44100;

	private AudioRecord audioRecorder;
	private short[] buffer;
	private boolean isRecording = false;
	
	private double periodicTime = 1 / SAMPLE_RATE;
	
	private ArrayList<AudioEventListener> listeners = new ArrayList<AudioEventListener>();

	public AudioController() {
		int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
				AudioFormat.ENCODING_PCM_16BIT);
		buffer = new short[bufferSize];
		audioRecorder = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
				AudioFormat.ENCODING_PCM_16BIT, bufferSize);
	}
	
	public void addSignalReceiveListener(AudioEventListener listener) {
		listeners.add(listener);
	}

	public void removeSignalReceiveListener(AudioEventListener listener) {
		listeners.remove(listener);
	}

	private synchronized void notifyUSBReceived(AudioEvent e) {
		for (AudioEventListener l : listeners) {
			l.onSignalReceive(e);
		}
	}
	
	public void startListening(){
		isRecording = true;
		this.start();
	}
	
	public void stopListening(){
		isRecording = false;
	}

	@Override
	public void run() {
		while(isRecording){
			int readSize = audioRecorder.read(buffer, 0, buffer.length);
			long timestamp = System.nanoTime();
			if(readSize > 0){
				int signalOffset = analyseSignal(500);
				if(signalOffset > 0){
					timestamp -= (long)((buffer.length - signalOffset) * periodicTime);
					notifyUSBReceived(new AudioEvent(getClass(), timestamp));
				}
			}
			
		}
	}

	private int analyseSignal(int minAmplitude) {
		for(int i = 0; i < buffer.length - 1; i++){
			if(buffer[i] > 0 && buffer[i + 1] < 0 && buffer[i + 2] > 0 && buffer[i + 3] < 0){
				int amplitude = (int) Math.sqrt(buffer[i] * buffer[i] + buffer[i + 1] * buffer[i + 1] + buffer[i + 2] * buffer[i + 2] + buffer[i + 3] * buffer[i + 3]);
				if(amplitude >= minAmplitude){
					return i;
				}
			}
		}
		return -1;
	}
	
	

}