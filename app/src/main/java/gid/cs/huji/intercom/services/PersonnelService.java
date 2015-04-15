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

import org.apache.http.client.ClientProtocolException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
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
    }


    // always verify the host - dont check for certificate
    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    /**
     * Trust every server - dont check for any certificate
     */
    private static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }

            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
        } };

        // Install the all-trusting trust manager
        try
        {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        }
        catch (Exception e)
        {
            Log.e(TAG, "Exception ", e);
        }
    }

    private String getRequest(String searchQuery)
    {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = null;

        try
        {
            URL url = new URL(searchQuery);

            HttpURLConnection http = null;

            if (url.getProtocol().toLowerCase().equals("https"))
            {
                trustAllHosts();
                HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
                https.setHostnameVerifier(DO_NOT_VERIFY);
                http = https;
            }
            else
            {
                http = (HttpURLConnection) url.openConnection();
            }

            InputStream content = http.getInputStream();

            reader = new BufferedReader(new InputStreamReader(content));
            String line;

            while ((line = reader.readLine()) != null)
            {
                builder.append(line);
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
                if(reader != null)
                {
                    reader.close();
                }
            }
            catch (IOException e)
            {
                Log.e(TAG, "", e);
            }
        }

        return builder.toString();
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
