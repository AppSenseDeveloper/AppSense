package com.example.appsense;

import android.os.CountDownTimer;

public class Sensing {
	
	public static void Main(String args[])
	
	{
		int i=10;
	CountDownTimer t = new CountDownTimer(50000,10000)
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
			
		};
		
		while(i>0)
		{
			t.start();
			System.out.println("countdown started");
			i--;
			
		}
	}
	
}
