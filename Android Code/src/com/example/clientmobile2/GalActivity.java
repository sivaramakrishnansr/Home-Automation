package com.example.clientmobile2;
import com.example.clientmobile2.R;
import java.io.File;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;
import android.os.Build;

public class GalActivity extends ActionBarActivity {
	private String[] FilePathStrings;
    private String[] FileNameStrings;
    private File[] listFile;
    Gallery gallerry;
    GridViewAdapter adapter;
    File file;

    @Override
    public void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);  
           setContentView(R.layout.fragment_gal);
           
           // Check for SD Card
           if (!Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                  Toast.makeText(this, "Error! No SDCARD Found!", Toast.LENGTH_LONG)
                               .show();
           } else {
                  file = new File(Environment.getExternalStorageDirectory()
                               + File.separator + "/download/media");
                  file.mkdirs();
           }

           if (file.isDirectory()) {
                  listFile = file.listFiles();
                  FilePathStrings = new String[listFile.length];
                  FileNameStrings = new String[listFile.length];
                  
                  String len=Integer.toString(listFile.length);
                  Toast.makeText(this, len, Toast.LENGTH_LONG)
                  .show();

                  for (int i = 0; i <listFile.length; i++) {
                        FilePathStrings[i] = listFile[i].getAbsolutePath();
                        FileNameStrings[i] = listFile[i].getName();
                        
                  }
           }

           gallerry = (Gallery) findViewById(R.id.gridview);
           adapter = new GridViewAdapter(this, FilePathStrings, FileNameStrings);
           gallerry.setSpacing(2);
           gallerry.setAdapter(adapter);

           gallerry.setOnItemClickListener(new OnItemClickListener() {

                  @Override
                  public void onItemClick(AdapterView<?> parent, View view,
                               int position, long id) {

                        zoomImage(position);
                  }

           });
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
            }

            return true;
        }

    };

     GestureDetector gestureDetector = new GestureDetector(
            simpleOnGestureListener);



   

     private void SwipeLeft() { 

         
    	Intent intent = new Intent(GalActivity.this, MainActivity.class);
 	    startActivity(intent);
 	   overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right); 
    }
    
    
    
    public static Bitmap getResizedBitmap(Bitmap image, int newHeight, int newWidth) {
    	int width = image.getWidth();
    	int height = image.getHeight();
    	float scaleWidth = ((float) newWidth) / width;
    	float scaleHeight = ((float) newHeight) / height;
    	// create a matrix for the manipulation
    	Matrix matrix = new Matrix();
    	// resize the bit map
    	matrix.postScale(scaleWidth, scaleHeight);
    	// recreate the new Bitmap
    	Bitmap resizedBitmap = Bitmap.createBitmap(image, 0, 0, width, height,
    	        matrix, false);
    	return resizedBitmap;
    	}
    private void zoomImage(int position) {
           //final Dialog dialog = new Dialog(MainActivity.this);
           //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
           //dialog.getWindow().setBackgroundDrawableResource(
             //           android.R.color.transparent);
           //dialog.setContentView(R.layout.image_zoomdialog);
           ImageView imageview = (ImageView) findViewById(R.id.imageView1);
           Bitmap bmp = BitmapFactory.decodeFile(FilePathStrings[position]);
           Bitmap bmp2=getResizedBitmap(bmp,100,100);
           if(bmp2 != null)
           {
           imageview.setImageBitmap(bmp2);
           }
           //dialog.show();
          
    }


}

 class GridViewAdapter extends BaseAdapter {

       // Declare variables
       private Activity activity;
       private String[] filepath;
       private String[] filename;

       private static LayoutInflater inflater = null;

       public GridViewAdapter(Activity a, String[] fpath, String[] fname) {
              activity = a;
              filepath = fpath;
              filename = fname;
              inflater = (LayoutInflater) activity
                           .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

       }

       public static Bitmap getResizedBitmap(Bitmap image, int newHeight, int newWidth) {
       	int width = image.getWidth();
       	int height = image.getHeight();
       	float scaleWidth = ((float) newWidth) / width;
       	float scaleHeight = ((float) newHeight) / height;
       	// create a matrix for the manipulation
       	Matrix matrix = new Matrix();
       	// resize the bit map
       	matrix.postScale(scaleWidth, scaleHeight);
       	// recreate the new Bitmap
       	Bitmap resizedBitmap = Bitmap.createBitmap(image, 0, 0, width, height,
       	        matrix, false);
       	return resizedBitmap;
       	}

       public int getCount() {
              return filepath.length;

       }

       public Object getItem(int position) {
              return position;
       }

       public long getItemId(int position) {
              return position;
       }

       public View getView(int position, View convertView, ViewGroup parent) {
             
                // TODO Auto-generated method stub
        ImageView i = new ImageView(activity);
        
        Bitmap bmp = BitmapFactory.decodeFile(filepath[position]);
        Bitmap bmp2=getResizedBitmap(bmp,100,100);
        if(bmp2 != null){
        //i.setImageResource(mImageIds[position]);
        i.setLayoutParams(new Gallery.LayoutParams(200, 200));
        i.setScaleType(ImageView.ScaleType.FIT_XY);
        i.setImageBitmap(bmp2);
              
        }
        return i;
       }
}
