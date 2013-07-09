package com.example.appsense;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class Activity2 extends Activity{

	private CheckBox acc,gps,mic,cam;
	public TextView t,tx,ty,tz,tlat,tlong;
	private SensorManager sm;
	private LocationManager service;
	private LocationListener gpsListener;
	private Sensor accsensor;
	public String fileName="accData.csv",provider,readings,path;
	public int sampleRate;
	double latitude,longitude,altitude;
	float speed;
	int dataRate,count,noOfCam;
	Button btn;
	Criteria criteria;
	Location location;
	File myFile;
	public static int photoNumber=0;
	File gpsFile;
	public Camera camera;
	SurfaceView sv;
	//	EventSource evs;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity2);
		   //get the sensor service
		
		//writeToSDFile();
		final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Activity2.this);

		sampleRate= pref.getInt("Gsampling", -1);
		dataRate= pref.getInt("Gcollection", -1);
		count=pref.getInt("Gcount", -1);
		
		path=Environment.getExternalStorageDirectory().getPath();
		System.out.println(path.toString());
		System.out.print("read from editor");
		
		//sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		service=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
		//accsensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		//sm2=new MyLocationListener();
		noOfCam=Camera.getNumberOfCameras();
		acc=(CheckBox)findViewById(R.id.chkAcc);
		gps=(CheckBox)findViewById(R.id.chkGPS);
		mic=(CheckBox)findViewById(R.id.chkMic);
		cam=(CheckBox)findViewById(R.id.chkCam);
		t=(TextView)findViewById(R.id.textView1);
		tx=(TextView)findViewById(R.id.textx);
		ty=(TextView)findViewById(R.id.texty);
		tz=(TextView)findViewById(R.id.textz);
		tlat=(TextView)findViewById(R.id.textLat);
		tlong=(TextView)findViewById(R.id.textLong);
		btn=(Button)findViewById(R.id.btnSens);
		//evs=new EventSource(sampleRate, dataRate ,count);
		
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity2, menu);
		return true;
	}
	
	
	public void onClick(View v) throws IOException {
		// TODO Auto-generated method stub
		if(acc.isChecked())
		{
			path=Environment.getExternalStorageDirectory().getPath();
			myFile = new File(path,"accText.txt");
			AccelerometerCode cd=new AccelerometerCode(this);
			cd.GetAccReadings(myFile, sampleRate, dataRate, count);
			/*if(sm!=null)
			{			
				path=Environment.getExternalStorageDirectory().getPath();
				myFile = new File(path,"accText.txt");
				Log.d("a","acc started");
				//Toast t1=Toast.makeText(getApplicationContext(), "working & started",Toast.LENGTH_SHORT);
				//t1.show();
				sm.registerListener(this, accsensor, sampleRate*1000*1000);
			}
			else
			{
				//Toast t=Toast.makeText(getApplicationContext(), "no acc available", Toast.LENGTH_SHORT);
				//t.show();
				Log.d("problem","no accelerometer available");
			}	
			//collect data for accelerometer
			 * 
			 */
		}
		
		if(gps.isChecked())
		{
			Log.d("ps", "gps checked");
			gpsFile=new File(path,"gpsReadings.txt");
		      try {
				gpsFile.createNewFile();
				Log.d("ps", "File created");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		      
		    service.requestLocationUpdates(LocationManager.GPS_PROVIDER, sampleRate*1000*1000,0, gpsListener);
		    Log.d("msg","listener registered");
		    //UpdateGPSFile(gpsFile, latitude, longitude,altitude );
		   // tlat.setText("Latitude" +"\t\t"+latitude);
			//tlong.setText("Longitude" +"\t\t"+longitude);
			
			gpsListener=new LocationListener() {
				
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
					speed=location.getSpeed();
					Log.d("gps","location saved");
					tlat.setText("Latitude" +"\t\t"+latitude);
					tlong.setText("Longitude" +"\t\t"+longitude);
					//Toast.makeText(Activity2.this, String.valueOf(latitude)+String.valueOf(longitude), Toast.LENGTH_SHORT).show();
					UpdateGPSFile(gpsFile, latitude, longitude,altitude );
				}
			};
			
			
			
			//to stop it from collecting data during the pause period use removeUpdates
			
			//service.removeUpdates(gpsListener);
		//collect data for GPS
		}
		
		if(mic.isChecked())
		{
			//collect data for microphone
		}
		
		if(cam.isChecked())
		{
		}
		//Toast.makeText(getApplicationContext(), "started", Toast.LENGTH_LONG).show();
	}

	/*@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	
	 
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		
		   float x = event.values[0];
		   float y = event.values[1];
		   float z = event.values[2];
		  
		   tx.setText("X axis" +"\t\t"+x);
		   ty.setText("Y axis" + "\t\t" +y);
		   tz.setText("Z axis" +"\t\t" +z);
		   readings= x + "\t"+y+"\t"+z+"\n";
		  
	UpdateAccFile(myFile,x, y, z);
	}
	
	**/
	
	
	
/*public void UpdateAccFile(File File,float x, float y, float z)
	
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
			t.append(e.getMessage());
			
		}
		
				
	} */

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
		t.append(e.getMessage());
		
	}
	
			
} 

	public void stop(View v)
	{
		//sm.unregisterListener(this);
		//service.removeUpdates(gpsListener);
	}



}
