package com.example.appsense;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Date;


public class EventSource
{
static int count;
static int offTime;
static int onTime;

public EventSource(int sampleDuration,int collDuration,int noOfSamples)
{
	onTime=sampleDuration;
	offTime=collDuration;
	count=noOfSamples;
}



public static void func(Timer t)
{
if (count == 0)
{
	t.cancel();

}
}

public void sensing()
{
	
	final Timer t1;
	final Timer t2;
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
			//onStart();
		}
	};

	TimerTask stop = new TimerTask()
	{
		@Override
		public void run()
		{
			System.out.println("stopped" + count);
			func(t2);
			//onStop();
		}
	};
	t1.scheduleAtFixedRate(start, offTime, offTime+onTime);
	t2.scheduleAtFixedRate(stop, offTime+onTime, offTime+onTime);
	System.out.println("check");
	
}
}