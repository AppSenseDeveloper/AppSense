/**
 * 
 */
package com.example.appsense;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;

/**
 * @author garvita
 *
 */
public class AccelerometerCode implements SensorEventListener{ 	
	private SensorManager sm;
	private Sensor accsensor;
	File myFile;
	String path;
	public int sampleRate;
	int dataRate;
	static int count;
	Timer t1;
	Timer t2;
	int flag1=0,i=10;
	
	public AccelerometerCode(Context context)
    {
         sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE); 
         accsensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
         sm.registerListener(this, accsensor, 1000000);
    }
	
	public void func(Timer t)
	{
	if (count == 0)
	{
		System.out.println("func is working");
		t.cancel();
		sm.unregisterListener(this);
	}
	}
	
	
	public void onStart() {
		// TODO Auto-generated method stub
	//	sm.registerListener(this, accsensor, dataRate*1000);
		System.out.println("registered" + count);
		flag1 = 1;
	}

	/* (non-Javadoc)
	 * @see com.example.appsense.Sensing#onStop()
	 */
	
	public void onStop() {
		// TODO Auto-generated method stub
		//sm.unregisterListener(this);
		//sm.
		System.out.println("unregistered" + count);
		flag1 = 0;
	}

	
	public void GetAccReadings(File fileName,int m,int n,int noOfSamples)
	{
		path=Environment.getExternalStorageDirectory().getPath();
		myFile = fileName;
		
		sampleRate=m;
		dataRate=n;
		count=noOfSamples;
		System.out.println(myFile.toString());
		
		//Toast t1=Toast.makeText(getApplicationContext(), "working & started",Toast.LENGTH_SHORT);
		//t1.show();
		
		System.out.println("acc initialized");
		Log.d("a","acc initialized");
		
		/*
		t1=new Timer();
		t2=new Timer();
		
		
		TimerTask start = new TimerTask()
		{
			@Override
			public void run()
			{
				System.out.println("started "+ count);
				count--;
				func(t1);
				onStart();
			}
		};

		TimerTask stop = new TimerTask()
		{
			@Override
			public void run()
			{
				System.out.println("stopped" + count);
				func(t2);
				onStop();
			}
		};
		
		t1.scheduleAtFixedRate(start, sampleRate*1000, sampleRate+dataRate*1000);
		t2.scheduleAtFixedRate(stop,(sampleRate+dataRate)*1000, (sampleRate+dataRate)*1000);
		System.out.println("check");
		*/
		
		
		
		while(i>0)
		{
			CountDownTimer t = new CountDownTimer(5000,10000)
			{
				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					System.out.println("Stopped");
				}

				@Override
				public void onTick(long arg0) {
					// TODO Auto-generated method stub
					System.out.println("Still working");
				}
				
			}.start();
			System.out.println("countdown started");
			
			//t.cancel();
			i--;
		}
	}
	
		
		
	
		
	/* (non-Javadoc)
	 * @see com.example.appsense.Sensing#onStart()
	 */
	
	


	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if (flag1 == 1)
		{
			float x = event.values[0];
			float y = event.values[1];
			float z = event.values[2];
			System.out.println(String.valueOf(x)+"\t"+String.valueOf(y)+"\t"+String.valueOf(z));
			UpdateAccFile(myFile,x, y, z);
		}
	}

public void UpdateAccFile(File file,float x, float y, float z)
	
	{
	
	try {		    
			System.out.println(file);
			BufferedWriter buf=new BufferedWriter(new FileWriter(file,true));
			SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date date = new Date();
			String sDate= sdf.format(date);
			buf.append(sDate+" "+"\t"+String.valueOf(x)+"\t"+String.valueOf(y)+"\t"+String.valueOf(z)+"\n");
		   buf.close();
		} 
		
		catch (Exception e) {
			Log.d("Exception",e.getMessage());
			
		}
		
				
	} 
}
