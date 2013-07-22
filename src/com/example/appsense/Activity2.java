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
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class Activity2 extends Activity implements Callback{

	private CheckBox acc,gps,mic,cam;
	public TextView t,tx,ty,tz,tlat,tlong;
	private LocationManager service;
	private LocationListener gpsListener;
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
	
	SurfaceHolder h;
	
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity2);
		 
		final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Activity2.this);

		sampleRate= pref.getInt("Gsampling", -1);
		dataRate= pref.getInt("Gcollection", -1);
		count=pref.getInt("Gcount", -1);
		
		path=Environment.getExternalStorageDirectory().getPath();
		System.out.println(path.toString());
		System.out.print("read from editor");
		
		
		service=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
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
			cd.GetAccReadings(sampleRate, dataRate, count,myFile);
		}
		
		if(gps.isChecked())
		{
			path=Environment.getExternalStorageDirectory().getPath();
			gpsFile=new File(path,"gpsText.txt");
			
			GPSCode gpscd=new GPSCode(this);
			gpscd.GetMultipleGPSReadings(sampleRate, dataRate, count, gpsFile);
		    
		}
		
		if(mic.isChecked())
		{
			//collect data for microphone
			MicrophoneCode mic=new MicrophoneCode();
			mic.RecordMicData(sampleRate, dataRate, count);
		}
		
		if(cam.isChecked())
		{
			Camera c=null;
			c.open(CameraInfo.CAMERA_FACING_BACK);
			c.getParameters();
			c.setDisplayOrientation(180);
			
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


	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}



}
