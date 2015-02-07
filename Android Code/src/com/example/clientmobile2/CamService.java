package com.example.clientmobile2;




import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.Toast;

public class CamService extends Service {
	private Socket socket;
	private String content;
	private static final int SERVERPORT = 8040;
	private static final String SERVER_IP = "192.168.1.36";
	Socket socket3 = null;
    InputStream is = null;
    FileOutputStream fos = null;
    BufferedOutputStream bos = null;
    int bufferSize = 0;
    ServerSocket serverSocket3 = null;
	public CamService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
    public void onCreate() {
		//Toast.makeText(this, "Camservice created", Toast.LENGTH_LONG).show();
		//Toast.makeText(this, "The new Service was Created", Toast.LENGTH_LONG).show();
	
    }

    @Override
    public void onStart(Intent intent, int startId) {
    	new RetrieveFeedTask().execute();
    	content=intent.getStringExtra("message");
    	Toast.makeText(this, "CamserviceRunning", Toast.LENGTH_LONG).show();
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
            
        	while(true)
        	{
			    try {
					serverSocket3 = new ServerSocket(8040);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	
			    try {
					socket3 = serverSocket3.accept();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
			try {
				//Delay(3000);
			    is = socket3.getInputStream();

			    bufferSize = socket3.getReceiveBufferSize();
			    //System.out.println("Buffer size: " + bufferSize);
			} catch (IOException ex) {
			   // System.out.println("Can't get socket input stream. ");
			}
			
			
			
			File mediaDir = new File("/sdcard/download/media");
			if (!mediaDir.exists()){
			    mediaDir.mkdir();
			}
			long time=System.currentTimeMillis();
			String name=String.valueOf(time);
			File resolveMeSDCard = new File("/sdcard/download/media/"+name+".jpg");
			try {
				resolveMeSDCard.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			try {
				fos = new FileOutputStream(resolveMeSDCard);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			bos = new BufferedOutputStream(fos);

			byte[] bytes = new byte[bufferSize];

			int count;

			try {
				while ((count = is.read(bytes)) > 0) {
				    bos.write(bytes, 0, count);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				bos.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				bos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				socket3.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				serverSocket3.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        	}
		}
    }
    
    
    
    
}