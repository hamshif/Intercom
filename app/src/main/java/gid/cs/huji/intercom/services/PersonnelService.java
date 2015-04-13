package gid.cs.huji.intercom.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
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
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import gid.cs.huji.intercom.json_deserialize.JsonToPersonnel;
import gid.cs.huji.intercom.model.Personnel;
import gid.cs.huji.intercom.model.Room;
import gid.cs.huji.intercom.sqlite_db.IntercomDBHelper;
import gid.cs.huji.intercom.sqlite_db.PersonnelDao;
import gid.cs.huji.intercom.sqlite_db.RoomDao;


public class PersonnelService extends IntentService
{
    private static final String TAG = PersonnelService.class.getSimpleName();

    public static final String PERSONNEL_BUNDLE = "PERSONNEL_BUNDLE";
    public static final String PROPERTY_QUERY_TYPE = "QUERY_TYPE";
    public static final String PROPERTY_QUERY = "PROPERTY_QUERY";

    private static final String SERVER_URL = "https://e-10.cs.huji.ac.il:8000/intercom/personnel_map";
//    private static final String SERVER_URL = "http://e-10:8000/intercom/personnel_map";
    public static final String PERSONNEL_MESSENGER = "personnel_messenger";
    public static final String PERSONNEL_UPDATE_PROGRESS = "personnel_update_process";

    public static final String MSG_PERSISTED_DATA = "persisted_data";


    private PersonnelAsync personnelAsync;
    private Messenger personnelMessenger;


    public PersonnelService()
    {
        super("PersonnelService");

        Log.d(TAG, "constructor reached");

        personnelAsync = new PersonnelAsync(this);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        Log.d(TAG, "onHandleIntent");

        Bundle bundle = intent.getExtras().getBundle(PERSONNEL_BUNDLE);

        String query = bundle.getString(PROPERTY_QUERY_TYPE);

        personnelMessenger = (Messenger) intent.getExtras().get(PERSONNEL_MESSENGER);

        personnelAsync.doInBackground(new String[]{"searchQuery", query});

    }


    private class PersonnelAsync extends AsyncTask<String, Integer, Integer>
    {
        Context context;

        public PersonnelAsync(Context context)
        {
            this.context = context;
        }

        @Override
        protected Integer doInBackground(String... params)
        {
            String s = getRequest(SERVER_URL + "?location=chimichanga");

            Log.d(TAG, "This is the result of the get: " + s);

            if(s == null || s == "")
            {
                return null;
            }

            tellUI("got data");

            JsonToPersonnel jsonToPersonnel = new JsonToPersonnel();
            HashMap<String, Object> personnelMap = jsonToPersonnel.deserialize(s);

            tellUI("deserialized json");
////            //TODO remove this it's only for debugging!!!
//            try
//            {
//                context.deleteDatabase(IntercomDBHelper.DB_NAME);
//            }
//            catch (Exception e)
//            {
//                Log.e(TAG, "", e);
//            }

            IntercomDBHelper intercomDBHelper = new IntercomDBHelper(context);
            SQLiteDatabase db = intercomDBHelper.getWritableDatabase();

            RoomDao roomDao = new RoomDao(db);
            PersonnelDao personnelDao = new PersonnelDao(db);

////            //TODO remove this it's only for debugging!!!
//            try
//            {
//                roomDao.dropTable();
//                personnelDao.dropTable();
//            }
//            catch (Exception e)
//            {
//                Log.e(TAG, "", e);
//            }




            roomDao.createTable();
            personnelDao.createTable();


            Object a = personnelMap.get(Room.ROOMS);


            HashMap<Integer, Room> rooms = null;

            if(a != null)
            {
                rooms = (HashMap)personnelMap.get(Room.ROOMS);
            }
            else
            {
                return null;
            }


            Room per_room;


            //TODO create update necessity logic and model in server to abide a less brutal approach


            for (Map.Entry<Integer, Room> entry : rooms.entrySet())
            {
                per_room = roomDao.persistObject(entry.getValue());
                entry.setValue(per_room);
            }

            ArrayList<Personnel> personnel_list = (ArrayList)personnelMap.get(Personnel.PERSONNELS);

            for(Personnel personnel: personnel_list)
            {
                personnelDao.persistObject(personnel);
            }

            tellUI(MSG_PERSISTED_DATA);


            return null;
        }

        private String getRequest(String searchQuery)
        {
            StringBuilder builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();

            try {
                // Create a trust manager that does not validate certificate chains
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(
                        TrustManagerFactory.getDefaultAlgorithm());
                // Initialise the TMF as you normally would, for example:
                tmf.init((KeyStore) null);

                TrustManager[] trustManagers = tmf.getTrustManagers();
                final X509TrustManager origTrustmanager = (X509TrustManager) trustManagers[0];

                TrustManager[] wrappedTrustManagers = new TrustManager[]{
                        new X509TrustManager() {
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return origTrustmanager.getAcceptedIssuers();
                            }

                            public void checkClientTrusted(X509Certificate[] certs, String authType) {
//                                try {
//                                    origTrustmanager.checkClientTrusted(certs, authType);
//                                } catch (CertificateException e) {
//                                    e.printStackTrace();
//                                }
                            }

                            public void checkServerTrusted(X509Certificate[] certs, String authType) {
//                                try {
//                                    origTrustmanager.checkServerTrusted(certs, authType);
//                                } catch (CertificateExpiredException e) {
//                                } catch (CertificateException e) {
//                                    e.printStackTrace();
//                                }
                            }
                        }
                };

                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, wrappedTrustManagers, null);
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

                Log.d(TAG, "");
            }
            catch (Exception e)
            {
                Log.e(TAG, " ", e);
            }


            Log.d(TAG, "dandy");

            HttpGet httpGet = new HttpGet(searchQuery);


            InputStream content = null;

            try
            {
                Log.d(TAG, "candy");

                HttpResponse response = client.execute(httpGet);

                Log.d(TAG, "mandy mo");

                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();

                Log.d(TAG, "Status code is: " + statusCode);

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
                Log.e(TAG, "ClientProtocolException", e);
            }
            catch (IOException e)
            {
                Log.e(TAG, "IOException", e);
                Log.d(TAG, e.getMessage());
            }
            catch (Exception e)
            {
                Log.e(TAG, "Exception ", e);
            }
            finally
            {
                try
                {
                    if(content != null)
                    {
                        content.close();
                    }
                }
                catch (IOException e)
                {
                    Log.e(TAG, "", e);
                }
            }

            return builder.toString();
        }
    }





    private void tellUI(String msg)
    {
        Bundle bundle = new Bundle();
        bundle.putString(PERSONNEL_UPDATE_PROGRESS, msg);

        Log.d(TAG , "got here1");

        Message message = new Message();
        message.setData(bundle);

        try
        {
            personnelMessenger.send(message);
        }
        catch (RemoteException e)
        {
            // TODO Auto-generated catch block
            Log.e(TAG, "", e);
        }
    }


}
