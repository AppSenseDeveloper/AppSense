package com.example.appsense;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class GPSCode {
	 private LocationManager locationManager;
		  private String provider;
		  File gpsFile;
		  String path;
		  double latitude,longitude,altitude;
		  float speed;
		  int sampleRate,dataRate,count;
		  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		  
	  LocationListener gpsListener=new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				latitude=location.getLatitude();
				longitude=location.getLongitude();
				altitude=location.getAltitude();
				speed = location.getSpeed();
				Log.d("gps","location saved");
				UpdateGPSFile(gpsFile, latitude, longitude,altitude );
			}
		};
	  
	  
	  
	  public GPSCode(Context context)
	  {
		locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
		provider=locationManager.GPS_PROVIDER;
		Location location = locationManager.getLastKnownLocation(provider);
	//	locationManager.requestLocationUpdates(provider, 0, 0, gpsListener);
	  }
	  
	  
	  public void GetMultipleGPSReadings(int sample,int data,int noOfSamples,File file) throws IOException
	  {
		  sampleRate =sample;
			dataRate=data;
			count=noOfSamples;
			gpsFile=file;
			gpsFile.createNewFile();
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
				System.out.println("Getting Readings");
				onResume();
				}
			};
			
			final ScheduledFuture beeperHandle = scheduler.scheduleAtFixedRate(beeper, 0, x, SECONDS);
			//final ScheduledFuture beeperHandle = scheduler.schedule(beeper, 0, SECONDS);
			scheduler.schedule(new Runnable() 
			{
				public void run()
				 {
				beeperHandle.cancel(true);
				}
			}
			, dur , SECONDS);
			
		}

		public void onResume()
		{
			locationManager.requestLocationUpdates(provider, 0, 0, gpsListener);
			System.out.println(latitude+":"+longitude);
		}
		
		
		public void cnclBeepForAnHour(int x,int y,int dur) 
		{		
			final Runnable beeper2 = new Runnable() 
			{
				public void run() 
				{
				System.out.println("Sensor unregistered");
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
			locationManager.removeUpdates(gpsListener);
			
		}
	
	
	public void UpdateGPSFile(File File,double x, double y, double z)
	{
	try {		    
			System.out.println(File);
			BufferedWriter buf=new BufferedWriter(new FileWriter(File,true));
			SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date date = new Date();
			String sDate= sdf.format(date);
			buf.append(sDate+" "+"\t"+String.valueOf(x)+"\t"+String.valueOf(y)+"\t"+String.valueOf(z)+"\n");
		   buf.close();
		} 
		catch (Exception e) {
			Log.d("error",e.getMessage());
	}
	}
}
