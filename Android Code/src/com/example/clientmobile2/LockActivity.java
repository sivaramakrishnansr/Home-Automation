package com.example.clientmobile2;


import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.UnknownHostException;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class LockActivity extends ActionBarActivity {
	public int foo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);  
		setContentView(R.layout.activity_lock);

		
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
	            if ((e2.getX() - e1.getX()) > sensitvity) {
	                SwipeRight();
	            }

	            return true;
	        }

	    };

	     GestureDetector gestureDetector = new GestureDetector(
	            simpleOnGestureListener);



	    private void SwipeRight() {
	    	
	    	 Intent intent = new Intent(LockActivity.this, MainActivity.class);
	    	 //Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation,R.anim.animation2).toBundle();
	    	 overridePendingTransition(android.R.anim.slide_out_right,android.R.anim.slide_in_left);  
	    	 startActivity(intent);
	    	 
	        

	    }
	public void buttonOKclicked(View v)
    {
    	TextView text = (TextView) findViewById(R.id.text1);
    	foo = Integer.parseInt(text.getText().toString().trim());
    	if(foo == 1234)
    	{
    		Intent mIntent = new Intent(this, MyService.class);
    		mIntent.putExtra("message", "4");
    		startService(mIntent);
    		
    	}
    	else
    	{
    		 Toast t = Toast.makeText(getApplicationContext(),
                     "Incorrect Password",
                      Toast.LENGTH_SHORT);
              t.show();
    	}
    	
    	
    	}
  
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lock, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_lock, container,
					false);
			return rootView;
		}
	}

}
