package gid.cs.huji.intercom.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class PersonnelService extends IntentService
{
    private static final String TAG = PersonnelService.class.getSimpleName();

    public static final String PERSONNEL_BUNDLE = "PERSONNEL_BUNDLE";
    public static final String PROPERTY_QUERY_TYPE = "QUERY_TYPE";
    public static final String PROPERTY_QUERY = "PROPERTY_QUERY";

    private static final String SERVER_URL = "http://e-10:8000/intercom/personnel_map";


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


        personnelAsync.doInBackground(new String[]{"searchQuery", type});

    }


    private class PersonnelAsync extends AsyncTask<String, Integer, Integer>
    {

        @Override
        protected Integer doInBackground(String... params)
        {
            String s = getRequest(SERVER_URL + "?location=chimichanga");

            Log.d(TAG, "This is the result of the get: " + s);

            return null;
        }

        private String getRequest(String searchQuery)
        {
            StringBuilder builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();

            HttpGet httpGet = new HttpGet(searchQuery);

            InputStream content = null;

            try
            {
                HttpResponse response = client.execute(httpGet);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();

                if (statusCode == 200)
                {
                    HttpEntity entity = response.getEntity();
                    content = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;

                    while ((line = reader.readLine()) != null)
                    {
                        builder.append(line);
                    }
                }
                else
                {
                    Log.e(TAG, "Status code is: " + statusCode);
                }
            }
            catch (ClientProtocolException e)
            {
                Log.e(TAG, e.getLocalizedMessage());
            }
            catch (IOException e)
            {
                Log.e(TAG, e.getLocalizedMessage());
            }
            finally
            {
                try
                {
                    content.close();
                }
                catch (IOException e)
                {
                    Log.e(TAG, e.getLocalizedMessage());
                }
            }

            return builder.toString();
        }


        private String downloadFile(String searchQuery)
        {
            StringBuilder builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();

            HttpGet httpGet = new HttpGet(searchQuery);

            InputStream content = null;

            try
            {
                HttpResponse response = client.execute(httpGet);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();

                if (statusCode == 200)
                {
                    HttpEntity entity = response.getEntity();
                    content = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;

                    while ((line = reader.readLine()) != null)
                    {
                        builder.append(line);
                    }
                }
                else
                {
                    Log.e(TAG, "Failed to download file");
                }
            }
            catch (ClientProtocolException e)
            {
                Log.e(TAG, e.getLocalizedMessage());
            }
            catch (IOException e)
            {
                Log.e(TAG, e.getLocalizedMessage());
            }
            finally
            {
                try
                {
                    content.close();
                }
                catch (IOException e)
                {
                    Log.e(TAG, e.getLocalizedMessage());
                }
            }

            return builder.toString();
        }

    }


}
