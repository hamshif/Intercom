package gid.cs.huji.intercom.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;


public class PersonnelService extends IntentService
{
    private static final String TAG = PersonnelService.class.getSimpleName();

    public static final String PERSONNEL_BUNDLE = "PERSONNEL_BUNDLE";
    public static final java.lang.String PROPERTY_QUERY_TYPE = "QUERY_TYPE";
    public static final String PROPERTY_QUERY = "PROPERTY_QUERY";

    private PersonnelAsync personnelAsync;


    public PersonnelService()
    {
        super("PersonnelService");

        Log.d(TAG, "constructor reached");

        personnelAsync = new PersonnelAsync();
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        Log.d(TAG, "onHandleIntent");

        Bundle bundle = intent.getExtras().getBundle(PERSONNEL_BUNDLE);

        String type = bundle.getString(PROPERTY_QUERY_TYPE);

    }


    private class PersonnelAsync extends AsyncTask<String, Integer, Integer>
    {


        @Override
        protected Integer doInBackground(String... params)
        {
            
            
            return null;
        }
    }


}
