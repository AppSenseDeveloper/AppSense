package com.example.appsense;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.*;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	private EditText e1,e2,e3;
	public static int App_Sampling=0;
	public static int App_Collection=0;
	public static int App_Count=0;
	Button b;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		b=(Button)findViewById(R.id.ChooseSensors);
		
		e1=(EditText)findViewById(R.id.textSampling);
		e2=(EditText)findViewById(R.id.textCollection);
		e3=(EditText)findViewById(R.id.textCount);
				
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
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
