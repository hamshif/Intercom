package gid.cs.huji.intercom.listeners;


import android.util.Log;
import android.view.View;

public class PersonnelListener implements View.OnClickListener
{
    private static final String TAG ="PersonnelListener";

    @Override
    public void onClick(View v)
    {
        Log.d(TAG, "clickedy click");
    }
}
