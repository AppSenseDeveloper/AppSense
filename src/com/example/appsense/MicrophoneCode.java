package com.example.appsense;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;
import android.media.MediaRecorder;
import android.media.MediaRecorder.AudioEncoder;

public class MicrophoneCode {
	
	MediaRecorder m=null;
	MediaPlayer mp=null;
	File audiofile=null;
	static int fileNumber=0;
	String path; 
	public int sampleRate,dataRate,count;
	
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	
	public MicrophoneCode()
	{
		
	}
	
	public void RecordMicData(int sample,int data,int noOfSamples)
	{
		sampleRate =sample;
		dataRate=data;
		count=noOfSamples;
		int total=count*(sampleRate+dataRate);
		beepForAnHour(sampleRate+dataRate,total);
		cnclBeepForAnHour(sampleRate+dataRate,dataRate,total);
	}
	
	public void beepForAnHour(int x,int dur) 
	{
		final Runnable beeper = new Runnable() 
		{
			public void run() 
			{
			System.out.println("Recording audio");
			onResume();
			}
		};
		
		final ScheduledFuture beeperHandle = scheduler.scheduleAtFixedRate(beeper, 0, x, SECONDS);
		//final ScheduledFuture beeperHandle = scheduler.schedule(beeper, 0, SECONDS);
		scheduler.schedule(new Runnable() 
		{
			public void run()
			 {
				onPause();
			beeperHandle.cancel(true);
			}
		}
		, dur , SECONDS);
		
	}

	public void onResume()
	{
		fileNumber++;
		m=new MediaRecorder();
		File sampleDir = Environment.getExternalStorageDirectory();
        
        	path=sampleDir.getPath();
        	audiofile=new File(path,"mic_"+fileNumber+".3gp");
            //audiofile = File.createTempFile("sound", ".3gp", sampleDir);
       		         
		m.setAudioSource(MediaRecorder.AudioSource.MIC);
		m.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		m.setAudioEncoder(AudioEncoder.AMR_NB);
		m.setOutputFile(audiofile.getAbsolutePath());
		
		try {
			m.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.v("TAG",e.getMessage());
		}
		m.start();
	}
	
	
	public void cnclBeepForAnHour(int x,int y,int dur) 
	{		
		final Runnable beeper2 = new Runnable() 
		{
			public void run() 
			{
			System.out.println("Recording Stopped");
			onPause();
			}
		};
		final ScheduledFuture beeperHandle2 = scheduler.scheduleAtFixedRate(beeper2, y, x, SECONDS);
		scheduler.schedule(new Runnable() 
		{
			public void run()
			 {
				onPause();
			beeperHandle2.cancel(true);
			System.out.println("Readings over");
			scheduler.shutdown();
			}
		}
		, dur , SECONDS);
	}
	public void onPause()
	{
		m.stop();
		m.release();
		m=null;
		
	}

}
