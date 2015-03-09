package gid.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import gid.gidutil.GidUtil;
import gid.interfaces.IBrowsable;
import gid.cs.huji.intercom.R;
import gid.cs.huji.intercom.listeners.PersonnelListener;

public class BrowseAdapter extends ArrayAdapter<IBrowsable>
{
	private static final String TAG ="BrowseAdapter";

	private Context context;
    private static int layoutResourceId;    
    private List<IBrowsable> list;

    private PersonnelListener personnelListener;

    
    public BrowseAdapter(Context context, int layoutResourceId, List<IBrowsable> list)
    {
        super(context, layoutResourceId, list);
        BrowseAdapter.layoutResourceId = layoutResourceId;
        this.context = context;
        this.list = list;
        personnelListener = new PersonnelListener();
        
        Log.d(TAG, "created browse adapter");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        ListObjectHolder holder = null;
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new ListObjectHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.rowItemImage);
            holder.imgIcon.setImageResource(R.drawable.cauldron);

            holder.imgIcon.setOnClickListener(personnelListener);
            
            holder.imgIcon.setAdjustViewBounds(true);
            holder.imgIcon.setMaxHeight(30);
            holder.imgIcon.setMaxWidth(30);
            holder.txtTitle = (TextView)row.findViewById(R.id.rowItemText);
            
            row.setTag(holder);
        }
        else
        {
            holder = (ListObjectHolder)row.getTag();
        }
        
        IBrowsable listItem = null;
        
        if(position >= 0 && position <= list.size()-1)
        {
        	listItem = list.get(position);
        	
        	 if(listItem != null)
             {
     	        holder.txtTitle.setText(listItem.getBrowseText());
		     	
     	        String picPath = listItem.getBrowseImagePath();
     	        
     	        if(GidUtil.checkFile(picPath, TAG))
     	        {
     	        	Bitmap pic = BitmapFactory.decodeResource(context.getResources(), R.drawable.cauldron);
     	        	
     	        	try
     	        	{
     	        		pic = decodeFile(picPath);
     	        	}
     	        	catch (Exception e)
     	        	{
						// TODO: handle exception
     	        		Log.e(TAG, "couldn't find a pic file at: " + picPath);
     	        		pic = BitmapFactory.decodeResource(context.getResources(), R.drawable.cauldron);
     	        		
					}
     	        	
     	    		holder.imgIcon.setImageBitmap(pic);
     	        }
             }
        }
        
        
        Log.d(TAG, "Safly finished inflating view at position: " + position);
        return row;
    }

	private Bitmap decodeFile(String picPath)
    {
		// TODO better images faster response
		
        try 
        {
        	final int REQUIRED_SIZE=20;
        	
        	BitmapFactory.Options o = new BitmapFactory.Options ();
            o.inSampleSize = 16;   // for 1/2 the image to be loaded
            Bitmap bmp = Bitmap.createScaledBitmap (BitmapFactory.decodeFile(picPath, o), REQUIRED_SIZE, REQUIRED_SIZE, false);
            
            return bmp;
        	/*
        	
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inSampleSize = 10;
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //The new size we want to scale to
            final int REQUIRED_SIZE=20;

            //Find the correct scale value. It should be the power of 2.
            int scale=8;
            
            
            
            //BitmapFactory.Option otps = new BitmapFactory.Options ();
           // opts.inSampleSize = 2;   // for 1/2 the image to be loaded
           // Bitmap thumb = Bitmap.createScaledBitmap (BitmapFactory.decodeFile(photoPath, opts), 96, 96, false)
            
            while(o.outWidth/scale/2 >= REQUIRED_SIZE && o.outHeight/scale/2 >= REQUIRED_SIZE)
            {
                scale*=2;
            }

            //Decode within Sample Size
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
            
            */
            
        } 
        catch (Exception e)
        {
        	
        }
        
        return null;
    }
    

	static class ListObjectHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
    }
}

