package com.example.appsense;

import java.io.File;
import java.io.IOException;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.media.MediaRecorder;
import android.media.MediaRecorder.AudioEncoder;
import android.media.MediaRecorder.AudioSource;
import android.media.MediaRecorder.OutputFormat;
import android.preference.PreferenceManager;
import android.widget.*;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity {

	private EditText e1,e2,e3;
	public static int App_Sampling=0;
	public static int App_Collection=0;
	public static int App_Count=0;
	Button b,start,stop,play,stopPlay;
	String fd="abc.3gp";
	MediaRecorder m=null;
	MediaPlayer mp=null;
	File audiofile=null;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		b=(Button)findViewById(R.id.ChooseSensors);
		
		e1=(EditText)findViewById(R.id.textSampling);
		e2=(EditText)findViewById(R.id.textCollection);
		e3=(EditText)findViewById(R.id.textCount);
		start=(Button)findViewById(R.id.temp);
		stop=(Button)findViewById(R.id.temp2);
		play=(Button)findViewById(R.id.temp3);
		stopPlay=(Button)findViewById(R.id.button1);
		
		
				
		b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				App_Sampling=Integer.parseInt(e1.getText().toString());
				App_Collection=Integer.parseInt(e2.getText().toString());
				App_Count=Integer.parseInt(e3.getText().toString());
				Log.d("values", "three values entered");
				
				SharedPreferences spf=PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
				Editor edit_spf=spf.edit();
				edit_spf.putInt("Gsampling", App_Sampling);
				edit_spf.putInt("Gcollection", App_Collection);
				edit_spf.putInt("Gcount", App_Count);
				edit_spf.commit();
				Log.d("commiting", "values saved to shared preferences");
						
				Log.d("intent","launching new activity");
				Intent i=new Intent(getApplicationContext(),Activity2.class);
				startActivity(i);
			}
			
		});
		
		start.setOnClickListener(new View.OnClickListener(
				) {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				m=new MediaRecorder();
				File sampleDir = Environment.getExternalStorageDirectory();
		        try {
		            audiofile = File.createTempFile("sound", ".3gp", sampleDir);
		        } catch (IOException e) {
		            Log.e("TAG", "sdcard access error");
		            return;
		        }
				m.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
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
		});
		
		OnClickListener playSound=new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					mp.setDataSource(fd);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					mp.prepare();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mp.start();
			}
		};
		
	OnClickListener stopplay=new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mp.release();
				mp=null;
			}
		};
		
		OnClickListener stopMic=new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				m.stop();
				m.release();
				m=null;
			}
		};
		
		stop.setOnClickListener(stopMic);
		play.setOnClickListener(playSound);
		stopPlay.setOnClickListener(stopplay);
	}
		
			

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
