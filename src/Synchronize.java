public class Synchronize implements Runnable
{
	public Thread t;
	public String dataToUpload = null;
	Synchronize(String newData)
	{
		System.out.println("Creating an instance of Synchronize");
		if(newData!= null)
		{
			dataToUpload = newData;
		}
	}
	public void start ()
	{
	      System.out.println("Starting synchronize thread");
	      if (t == null)
	      {
	         t = new Thread ();
	         t.start ();
	      }
	}
	public void run()
	{
		if (dataToUpload!=null)
		{
		    Upload u = new Upload(dataToUpload);  
			u.start();
	    	u.run();
	    	dataToUpload = null;
		}
		
		Download d = new Download();
	    try{
	    	
	    	d.start();
	    	d.run();
	    	
	    	System.out.println(" Exiting Download thread ");
		    Thread.sleep(5000);
		    run();
	    
	    	}
	    catch (Exception e)
	    {
	    	System.out.println("exception occured");
	    	e.printStackTrace();
	    }
	    
    	//System.out.println(" Exiting upload thread ");

		}
	
		//System.out.println(" Exiting the started synchronize thread");
	
}