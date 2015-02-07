package com.example.clientmobile2;

import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

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


import java.util.ArrayList;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;



import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.gesture.Prediction;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemSelectedListener {
	//creating two sockets . one to connect to the server running on linino
	//and the other for connecting to the computer to play music
	
	//initialising socket variables
	private Socket socket;
	private Socket socket2;
	//write the local ip of linino assigned by the dhcp
	//make sure that the port number is the same used for the server 
	//program
	private static final int SERVERPORT = 8020;
	private static final String SERVER_IP = "192.168.0.101";
	//write the ip of the computer at which the music player is runnnig
	
	private static final int SERVERPORT2 = 8036;
	private static final String SERVER_IP2 = "192.168.0.101";
	
    //Variables required for the voice functionality	
    protected static final int RESULT_SPEECH = 1;
    private ImageButton btnSpeak;
    private TextView txtText;
    
    //variables required for camera application
    private static final int SERVERPORT3 = 4466;
	private static final String SERVER_IP3 = "192.168.1.35";
	Socket socket3 = null;
    InputStream is = null;
    FileOutputStream fos = null;
    BufferedOutputStream bos = null;
    int bufferSize = 0;
    ServerSocket serverSocket3 = null;
    Thread t1;
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		
		
		 
		//initialising the voice functionality
		txtText = (TextView) findViewById(R.id.txtText);
	    btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
	    btnSpeak.setOnClickListener(new View.OnClickListener() {
	           @Override
	           public void onClick(View v) {
	               Intent intent = new Intent(
	                       RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
	               intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
	               try {
	                   startActivityForResult(intent, RESULT_SPEECH);
	                   txtText.setText("");
	               } catch (ActivityNotFoundException a) {
	                   Toast t = Toast.makeText(getApplicationContext(),
	                           "Opps! Your device doesn't support Speech to Text",
	                           Toast.LENGTH_SHORT);
	                   t.show();
	               }
	           }
	       }); 
		
	   // ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

		// This schedule a runnable task every 2 minutes
		//scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
		  //public void run() {
		//	  new Thread(new ClientThread3()).start();
		  //}
	//	}, 0, 2, TimeUnit.SECONDS);
	    //starting two threads which establishes the connection between the client 
		//to the two different servers
		
        
       /* if(getIntent().getExtras() != null)
	    {
	    Bundle extras=getIntent().getExtras();
		String msg = extras.getString("keyMessage");
		
		if(msg!=null)
		{
		 
          int foo = Integer.parseInt(msg);
          openDoor();
		}
	    }
        else{
        	/*new Thread(new ClientThread()).start();
            new Thread(new ClientThread2()).start();
            new Thread(new ClientThread3()).start();
        }*/
		
		//t1=new Thread(new ClientThread());
		//t1.start();
        //new Thread(new ClientThread2()).start();
        //new Thread(new ClientThread3()).start();
	}
   
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        super.dispatchTouchEvent(event);
        return gestureDetector.onTouchEvent(event);
    }

    SimpleOnGestureListener simpleOnGestureListener = new SimpleOnGestureListener() {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                float velocityY) {

            float sensitvity = 50;
            if ((e1.getX() - e2.getX()) > sensitvity) {
                SwipeLeft();
            } else if ((e2.getX() - e1.getX()) > sensitvity) {
                SwipeRight();
            }

            return true;
        }

    };

     GestureDetector gestureDetector = new GestureDetector(
            simpleOnGestureListener);



    private void SwipeLeft() {
    	
    	 Intent intent = new Intent(MainActivity.this, LockActivity.class);
    	 
    	 //Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation,R.anim.animation2).toBundle();
    	 overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);  
    	 startActivity(intent);
    	 
        

    }


     private void SwipeRight() { 

    	 
    	 Intent intent = new Intent(MainActivity.this, GalActivity.class);
 	   
 	   
    	 overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);  
    	 startActivity(intent); 
     }
    
    
    
	//optional functionality fo the spinner which couldbe added for music selection from phone
	public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 1:
            	try{
            		//sending a message to the linino to play the song 
            		PrintWriter out = new PrintWriter(new BufferedWriter(
            				new OutputStreamWriter(socket2.getOutputStream())),
            				true);
            		out.println("1");
            		}
            		catch (UnknownHostException e) {
            			e.printStackTrace();
            		} catch (IOException e) {
            			e.printStackTrace();
            		} catch (Exception e) {
            			e.printStackTrace();
            		}
            	break;
            case 2:
            	try{
            		//sending a message to the linino to play the song 
            		PrintWriter out = new PrintWriter(new BufferedWriter(
            				new OutputStreamWriter(socket2.getOutputStream())),
            				true);
            		out.println("2");
            		}
            		catch (UnknownHostException e) {
            			e.printStackTrace();
            		} catch (IOException e) {
            			e.printStackTrace();
            		} catch (Exception e) {
            			e.printStackTrace();
            		}
                break;
            case 3:
            	try{
            		//sending a message to the linino to play the song 
            		PrintWriter out = new PrintWriter(new BufferedWriter(
            				new OutputStreamWriter(socket2.getOutputStream())),
            				true);
            		out.println("3");
            		}
            		catch (UnknownHostException e) {
            			e.printStackTrace();
            		} catch (IOException e) {
            			e.printStackTrace();
            		} catch (Exception e) {
            			e.printStackTrace();
            		}
                break;

        }
    }
		
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	//Voice application - conversion of speech to text happens here
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	switch (requestCode) {
	  case RESULT_SPEECH: {
	     if (resultCode == RESULT_OK && null != data) {

	               ArrayList<String> text = data
	                       .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

	         txtText.setText(text.get(0));
	         String abc=(String) txtText.getText();
	     	 
 		 Toast t = Toast.makeText(getApplicationContext(),
                   abc,
                    Toast.LENGTH_SHORT);
            t.show();
            switch(abc)
            {
            case "hard on": onClickon(null);
            break;
            
            case "light on": onClickon(null);
            break;
            
            case "light off": onClickoff(null);
            break;
            
            case "fan on": onClickon2(null);
            break;
            
            case "fan off": onClickoff2(null);
            break;
            }
            }
	           break;
	       }

	       }
	   
}
	//Opens the Gallery to view pictures
	public void sendGallery(View view) 
	{
	    try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//Function to call moodlighting
	public void moodLight(View view) {
		Intent mIntent = new Intent(this, FBService.class);
		mIntent.putExtra("message", "0");
		startService(mIntent);
	}
	//Opens the lock page
		public void sendLock(View view) 
		{
		    Intent intent = new Intent(MainActivity.this, LockActivity.class);
		    startActivity(intent);
		}
		public void updateGallery(View view) {
			Intent mIntent = new Intent(this, CamService.class);
			startService(mIntent);
		}
	//function to turn device 1 on
		public void onClickon(View view) {
			Intent mIntent = new Intent(this, MyService.class);
			mIntent.putExtra("message", "0");
			startService(mIntent);
		}
	//function to turn device 1 off
	public void onClickoff(View view) {
		Intent mIntent = new Intent(this, MyService.class);
		mIntent.putExtra("message", "1");
		startService(mIntent);
	}
	//function to turn deveice 2 on
	public void onClickon2(View view) {
		Intent mIntent = new Intent(this, MyService.class);
		mIntent.putExtra("message", "2");
		startService(mIntent);
	}
	//function to turn device 2 off
	public void onClickoff2(View view) {
		Intent mIntent = new Intent(this, MyService.class);
		mIntent.putExtra("message", "3");
		startService(mIntent);
	}
	
	
	public void openDoor() {
		Intent mIntent = new Intent(this, MyService.class);
		mIntent.putExtra("message", "4");
		startService(mIntent);
	}
	
	//Thread which establishes the socket communication with the linino
	/*class ClientThread implements Runnable {
		
		
		@Override
		public void run() {

			try {
				InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

				socket = new Socket(serverAddr, SERVERPORT);

			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}

	}
	//Thread which establishes the socket communication with the linino
	class ClientThread2 implements Runnable {

		@Override
		public void run() {

			try {
				InetAddress serverAddr = InetAddress.getByName(SERVER_IP2);

				socket2 = new Socket(serverAddr, SERVERPORT2);

			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}

	}*/

	class ClientThread3 implements Runnable {

		@Override
		public void run() {
		try{
			try {
			    serverSocket3 = new ServerSocket(8040);
			} catch (IOException ex) {
			    System.out.println("Can't setup server on this port number. ");
			}

			

			try {
			    socket3 = serverSocket3.accept();
			} catch (IOException ex) {
			    System.out.println("Can't accept client connection. ");
			}

			try {
			    is = socket3.getInputStream();

			    bufferSize = socket3.getReceiveBufferSize();
			    System.out.println("Buffer size: " + bufferSize);
			} catch (IOException ex) {
			    System.out.println("Can't get socket input stream. ");
			}
			
			
			//if(bufferSize > 0)
			//{
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
			//}
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
		}
		}

	}
	
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}


	
	}