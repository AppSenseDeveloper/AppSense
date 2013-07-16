package com.example.appsense;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
//import java.util.TimerTask;

//import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
//import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;

import static java.util.concurrent.TimeUnit.SECONDS;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;


public class BeeperControl{ 	
	
		public static SensorManager sm;
		public  Sensor accsensor;
		static File myFile;
		static String path;
		public static int sampleRate;
		static int dataRate;
		static int count;
		static //Timer t1;
		//Timer t2;
		int flag=0;
		//Timer t2;
		int i=10;
		private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		SensorEventListener listen=new SensorEventListener() {
			
			@Override
			public void onSensorChanged(SensorEvent event) {
				// TODO Auto-generated method stub
				float x = event.values[0];
				float y = event.values[1];
				float z = event.values[2];
				System.out.println(String.valueOf(x)+"\t"+String.valueOf(y)+"\t"+String.valueOf(z));
				UpdateAccFile(myFile,x, y, z);
			}
			
			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
				// TODO Auto-generated method stub
				
			}
		};
		
	public BeeperControl(Context context,File f)
    {
         sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE); 
         accsensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(listen, accsensor, 1000000);
        myFile=f;
         beepForAnHour();
         cnclBeepForAnHour();
         
       //  GetAccReadings(myFile, 5, 10, 10);
    }
	
	public void beepForAnHour() 
	{
		final Runnable beeper = new Runnable() 
		{
			public void run() 
			{
			System.out.println("beep");
			System.out.println("Getting Readings");
			onResume();
			}
		};
		
		final ScheduledFuture beeperHandle = scheduler.scheduleAtFixedRate(beeper, 0, 10, SECONDS);
		//final ScheduledFuture beeperHandle = scheduler.schedule(beeper, 0, SECONDS);
		scheduler.schedule(new Runnable() 
		{
			public void run()
			 {
			beeperHandle.cancel(true);
			System.out.println("Readings over");
			//scheduler.shutdown();
			//sm.unregisterListener(listen, accsensor);
			//GetAccReadings(myFile, 5, 10, 10);
			}
		}
		, 60 , SECONDS);
		
	}

	public void onResume()
	{
		sm.registerListener(listen, accsensor, 1000000);
		flag=1;
	}
	
	
	public void cnclBeepForAnHour() 
	{		
		final Runnable beeper2 = new Runnable() 
		{
			public void run() 
			{
			System.out.println("cancelled");
			System.out.println("Sensor unregistered");
			onPause();
			}
		};
		final ScheduledFuture beeperHandle2 = scheduler.scheduleAtFixedRate(beeper2, 5, 10, SECONDS);
		scheduler.schedule(new Runnable() 
		{
			public void run()
			 {
			beeperHandle2.cancel(true);
			System.out.println("Readings over");
			onPause();
			scheduler.shutdown();
			}
		}
		, 60 , SECONDS);
	}
	public void onPause()
	{
		sm.unregisterListener(listen, accsensor);
		flag=0;
	}
	
	
	public static void GetAccReadings(File fileName,int m,int n,int noOfSamples)
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
	}
	
	
	public static void UpdateAccFile(File file,float x, float y, float z)
	{
		if(flag==1)
		{
		try
		{		    
			System.out.println(file);
			BufferedWriter buf=new BufferedWriter(new FileWriter(file,true));
			SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date date = new Date();
			String sDate= sdf.format(date);
			buf.append(sDate+" "+"\t"+String.valueOf(x)+"\t"+String.valueOf(y)+"\t"+String.valueOf(z)+"\n");
		   buf.close();
		} 
		
		catch (Exception e)
		{
			Log.d("Exception",e.getMessage());
		}
		}			
	} 
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*class BeeperControl extends AccelerometerCode
	{
		Context a;
		private final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		//AccelerometerCode acc=new AccelerometerCode(a);
		

			public static void main(String[] args)
			{
			beepForAnHour();
			cnclBeepForAnHour();
			//scheduler.sh
			}

			public BeeperControl(Context context)
			{
				super(context);
			}
			
			public static void beepForAnHour() 
			{
				final Runnable beeper = new Runnable() 
				{
					public void run() 
					{
					System.out.println("beep");
					System.out.println("Getting Readings");
					
					}
				};
				final ScheduledFuture beeperHandle = scheduler.scheduleAtFixedRate(beeper, 0, 10, SECONDS);
				scheduler.schedule(new Runnable() 
				{
					public void run()
					 {
					beeperHandle.cancel(true);
					System.out.println("Readings over");
					}
				}
				, 60 , SECONDS);
				
			}

			public static void cnclBeepForAnHour() 
			{
				final Runnable beeper2 = new Runnable() 
				{
					public void run() 
					{
					System.out.println("cancelled");
					System.out.println("Sensor unregistered");
					}
				};
				final ScheduledFuture beeperHandle2 = scheduler.scheduleAtFixedRate(beeper2, 5, 10, SECONDS);
				scheduler.schedule(new Runnable() 
				{
					public void run()
					 {
					beeperHandle2.cancel(true);
					System.out.println("Readings over");
					}
				}
				, 60 , SECONDS);
			}
			
			
		}

	*/
	
	/*
 * 
 */
	//These functions were used when I was implementing SensorEventListener
	/*@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) 
	{
		// TODO Auto-generated method stub
			float x = event.values[0];
			float y = event.values[1];
			float z = event.values[2];
			System.out.println(String.valueOf(x)+"\t"+String.valueOf(y)+"\t"+String.valueOf(z));
			UpdateAccFile(myFile,x, y, z);
	}*/

	
	/*
	 * Functions used with TimerTask implementations
	 * 
	public void func(Timer t)
	{
		if (count == 0)
		{
			System.out.println("func is working");
			t.cancel();
			sm.unregisterListener(this);
		}
	}
		
	public void onStart() 
	{
		// TODO Auto-generated method stub
		//	sm.registerListener(this, accsensor, dataRate*1000);
		System.out.println("registered" + count);
		flag1 = 1;
	}

	public void onStop()
	{
		// TODO Auto-generated method stub
		//sm.unregisterListener(this);
		//sm.
		System.out.println("unregistered" + count);
		flag1 = 0;
	}
	*/
		
		/*
		 * 
		 * One way to implement timers
		 * Working properly independently but not with accelerometer sensor
		 * 
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
		
		/*
		 * Another way to implement timer task
		 * This is not working properly
		 * 
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
	*/	

}