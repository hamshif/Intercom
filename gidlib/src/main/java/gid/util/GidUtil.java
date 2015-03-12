package gid.util;


import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;

public class GidUtil
{
	private static final String TAG = "Utility";
	private static Toast t;
	
	public static void clearWindow(Activity a)
	{
		a.requestWindowFeature(Window.FEATURE_NO_TITLE);
        a.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
	
	public static void clearTitle(Activity a)
	{
		a.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	public static void dontRotate(Activity a)
	{
		a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	public static boolean checkFile(String path, String tag)
	{
		if(path == null)
		{
			return false;
		}
		
		File f = new File(path);
		boolean exists = false; 
	
		if(f.exists())
		{
			exists = true;
			Log.i(TAG + " called by " + tag, "found file " + f.getName());
		}
		else
		{
			Log.w(TAG + " called by " + tag, "Bug! didn't find file: " + f.getName() + " at path: " + path);
		}
		
		return exists;
	}

	public static void sendMessageToUser(Context context, String message, int duration)
	{
		t = Toast.makeText(context, message, duration);
		t.setGravity(Gravity.CENTER| Gravity.CENTER, 0, 0);
		t.show();
	}
	
	public static boolean isExternalStorageReadOnly()
    { 
        String extStorageState = Environment.getExternalStorageState();
        
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState))
        { 
            return true; 
        } 
        
        return false; 
    } 

    public static boolean isExternalStorageAvailable() 
    { 
        String extStorageState = Environment.getExternalStorageState();
        
        if (Environment.MEDIA_MOUNTED.equals(extStorageState))
        { 
            return true; 
        } 
        
        return false; 
    }

	public static void killToasts() 
	{
		if(t == null)
		{
			return;
		}
		
		t.cancel();
		Log.i(TAG, "Toast cancelled");
	} 
	
	public static String getPicFromDir(String dirPath, String fileName) //throws NoSuchFileException
	{
		if(dirPath == null || fileName == null)
		{
			return null;
		}
		
		File dir = new File(dirPath);
		
		if(dir.isDirectory())
		{
			Log.d(TAG, "directory: " + dirPath + "  was found");
			
			String[] fileList = dir.list();
			
			for(String f: fileList)
			{
				String fileType = null;
				// different file types can't use ends with because of the middle e.g. s0anchor.png / s0.png
				if(f.startsWith(fileName))
				{
					if(f.equals(fileName + ".jpg"))
					{
						fileType = ".jpg";
					}
					else if(f.equals(fileName + ".JPG"))
					{
						fileType = ".JPG";
					}
					else if(f.equals(fileName + ".png"))
					{
						fileType = ".png";
					}
				}
				
				if(fileType != null)
				{
					String s = dirPath + "/" + fileName + fileType;
					Log.d(TAG, "pic file:  " + s + "  was found");
					
					return s;
				}
			}
		}
		else
		{

			Log.e(TAG, "directory: " + dirPath + "   not found!");
		}
		
		Log.e(TAG, "didn't find pic file: " + dirPath + "/" + fileName);
		
		return null;
	}
	
	public static String getAudioFromDir(String dirPath, String fileName) //throws NoSuchFileException
	{
		if(dirPath == null || fileName == null)
		{
			return null;
		}
		
		File dir = new File(dirPath);
		
		if(dir.isDirectory())
		{
			Log.d(TAG, "directory: " + dirPath + "  was found");
			
			String[] fileList = dir.list();
			
			for(String f: fileList)
			{
				String fileType = null;
				// different file types can't use ends with because of the middle e.g. s0anchor.png / s0.png
				if(f.startsWith(fileName))
				{
					if(f.equals(fileName + ".wma"))
					{
						fileType = ".wma";
					}
					else if(f.equals(fileName + ".mp3"))
					{
						fileType = ".mp3";
					}
				}
				
				if(fileType != null)
				{
					String s = dirPath + "/" + fileName + fileType;
					Log.d(TAG, "Audio file:  " + s + "  was found");
					
					return s;
				}
			}
		}
		else
		{
			Log.e(TAG, "directory: " + dirPath + "   not found!");
		}
		
		return null;
	}
	
	public static String fillSpaces(String query)
	{
		String fillspaces = "";
		
		for(int i=0; i<query.length(); i++)
	    {
	    	if(query.charAt(i) == ' ')
	    	{
	    		fillspaces += "%20";
	    	}
	    	else if(query.charAt(i) == '\n')
	    	{
	    		fillspaces += "%0D%0A";
	    	}
	    	else
	    	{
	    		fillspaces += query.charAt(i);
	    	}
	    }
		
		return fillspaces;
	}
}
