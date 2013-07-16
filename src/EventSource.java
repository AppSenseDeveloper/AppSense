import java.util.concurrent.*;


/*
public class EventSource {
	
	//TimeUnit ;
	static int Count;
	int onTime,offTime;

	ScheduledExecutorService service;
	
	ScheduledFuture scfuture;
	
public EventSource()
{
	Count=0;
	onTime=0;
}

public void execute()
{
	service=Executors.newScheduledThreadPool(4);
	scfuture=service.scheduleWithFixedDelay(new Runnable(){
		public void run()
		{
			Count++;
			System.out.println("running");
			try {
				service.wait(onTime*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}, 0, offTime, TimeUnit.SECONDS);
	
	
	if(Count==10)
	{
		System.out.print("stopped");
		service.shutdown();
	}
}
}*/

public class EventSource
{
		public static interface TickEventHandler
		{
			void invoke();
		}
		public TickEventHandler OnEvent;
		public TickEventHandler OffEvent;

		 int OnTime;
		 int OffTime;
		
		public ScheduledExecutorService dt;
		public ScheduledFuture scfuture;

		public final void Start()
		{
			//dt.Start();
		}

		public final void Stop()
		{
			///dt.Stop();
		}

		public EventSource(final int OnTime, final int OffTime)
		{
								//dt = Executors.newScheduledThreadPool(1);
								//dt.Interval = new TimeSpan(0, 1, 0);
								//C# TO JAVA CONVERTER TODO TASK: Java has no equivalent to C#-style event wireups:
								//dt.Tick += new System.EventHandler(dt_Tick);
			dt=Executors.newScheduledThreadPool(1);
			scfuture=dt.scheduleWithFixedDelay(new Runnable(){
				public void run()
				{
					if (isOn)
					{
						if (count >= OnTime)
						{
							if (OnEvent != null)
							{
								OnEvent.invoke();
							}
							isOn = false;
							count = 0;
						}
					}
					else
					{
						if (count >= OffTime)
						{
							if (OffEvent != null)
							{
								OffEvent.invoke();
							}
							isOn = true;
							count = 0;
						}
					}
					count++;

				}
			}, 0, 1, TimeUnit.SECONDS);
			
			
			this.OnTime = OnTime;
			this.OffTime = OffTime;
		}

		private boolean isOn = true;
		private int count = 0;

		/*private void run(Object sender)
		{
			if (isOn)
			{
				if (count >= OnTime)
				{
					if (OnEvent != null)
					{
						OnEvent.invoke();
					}
					isOn = false;
					count = 0;
				}
			}
			else
			{
				if (count >= OffTime)
				{
					if (OffEvent != null)
					{
						OffEvent.invoke();
					}
					isOn = true;
					count = 0;
				}
			}
			count++;

		}*/

}
