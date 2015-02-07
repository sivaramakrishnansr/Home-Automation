package com.example.clientmobile2;




import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.Toast;

public class MyService extends Service {
	private Socket socket;
	private String content;
	private static final int SERVERPORT = 8020;
	private static final String SERVER_IP = "192.168.0.101";
	public MyService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
    public void onCreate() {
		
		//Toast.makeText(this, "The new Service was Created", Toast.LENGTH_LONG).show();
	
    }

    @Override
    public void onStart(Intent intent, int startId) {
    	new RetrieveFeedTask().execute();
    	content=intent.getStringExtra("message");
    	// For time consuming an long tasks you can launch a new thread here...
    	
		onDestroy();
    }

    @Override
    public void onDestroy() {
        

    }
    
    
    class RetrieveFeedTask extends AsyncTask<Void, Void, Void> {

        private Exception exception;
        @Override
        protected Void doInBackground(Void... v) {
            
        	 
             try {
                 socket = new Socket(SERVER_IP, SERVERPORT);
             } catch (UnknownHostException e1) {
                 // TODO Auto-generated catch block
                 e1.printStackTrace();
             } catch (IOException e1) {
                 // TODO Auto-generated catch block
                 e1.printStackTrace();
             }
            
         	PrintWriter out = null;
     		try {
     			out = new PrintWriter(new BufferedWriter(
     					new OutputStreamWriter(socket.getOutputStream())),
     					true);
     		} catch (IOException e) {
     			// TODO Auto-generated catch block
     			e.printStackTrace();
     		}
     		out.println(content);
             return null;
    			
        }
        @Override
        protected void onPostExecute(Void input)
	    {
	        
	    }
       

		
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			return null;
		}
    }
    
    
    
    
}