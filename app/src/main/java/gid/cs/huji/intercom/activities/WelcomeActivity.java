package gid.cs.huji.intercom.activities;


import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;

import gid.cs.huji.intercom.model.Room;
import gid.cs.huji.intercom.services.PersonnelService;
import gid.cs.huji.intercom.sqlite_db.IntercomDBHelper;
import gid.cs.huji.intercom.sqlite_db.PersonnelDao;
import gid.cs.huji.intercom.sqlite_db.RoomDao;
import gid.util.GidUtil;
import gid.cs.huji.intercom.fragments.PersonnelFragment;
import gid.cs.huji.intercom.fragments.BrowseFragment;
import gid.cs.huji.intercom.fragments.HeaderFragment;
import gid.cs.huji.intercom.model.Personnel;

import gid.cs.huji.intercom.R;
/**
 * Created by gideonbar on 08/03/15.
 */
public class WelcomeActivity extends FragmentActivity implements BrowseFragment.BrowseFragmentListener, PersonnelFragment.PersonnelFragmentListener
{
    private static final String TAG = WelcomeActivity.class.getSimpleName();

    FragmentManager fm;


    private FrameLayout onScreen;

    private PersonnelFragment personnelFragment;
    private BrowseFragment browseFragment;
    private HeaderFragment headerFragment;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        GidUtil.clearWindow(WelcomeActivity.this);
        super.onCreate(savedInstanceState);

        if (findViewById(R.id.fragment_container) != null)
        {
            if (savedInstanceState != null)
            {
                return;
            }
        }

        setParameters();
        createGUI();
        getData();
    }

    private void setParameters() {

    }

    private void getData()
    {
        Bundle bundle = new Bundle();

        String searchQuery = "{\"query\": \"yo\"}";

        Intent intent = new Intent(this, PersonnelService.class);

        bundle.putString(PersonnelService.PROPERTY_QUERY_TYPE, "JSON");
        bundle.putString(PersonnelService.PROPERTY_QUERY, searchQuery);

        intent.putExtra(PersonnelService.PERSONNEL_BUNDLE, bundle);

        Messenger uiMessenger = new Messenger(new PersonnelHandler());
        intent.putExtra(PersonnelService.PERSONNEL_MESSENGER, uiMessenger);

        startService(intent);

        Log.d(TAG, "Finished search()");


        startService(intent);
    }

    private void createGUI()
    {
        fm = getSupportFragmentManager();

        onScreen = (FrameLayout) getLayoutInflater().inflate(R.layout.intercom, null);
//        LinearLayout onScreen = (LinearLayout) getLayoutInflater().inflate(R.layout.intercom, null);
        setContentView(onScreen);

        personnelFragment = new PersonnelFragment();
        personnelFragment.setArguments(getIntent().getExtras());
        fm.beginTransaction().add(R.id.fragment_container, personnelFragment).commit();

//            LinearLayout ll = (LinearLayout) getLayoutInflater().inflate(R.layout.list_browse, null);
//            onScreen.addView(ll);

        browseFragment = new BrowseFragment();
        browseFragment.setArguments(getIntent().getExtras());
        fm.beginTransaction().add(R.id.list_container, browseFragment).commit();



        headerFragment = new HeaderFragment();
        headerFragment.setArguments(getIntent().getExtras());
        fm.beginTransaction().add(R.id.fragment_container, headerFragment).commit();

        fm.beginTransaction()
//            .setCustomAnimations(android.R.animator., android.R.animator.fade_out)
            .hide(personnelFragment)
            .commit();

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if(browseFragment != null)
        {
            Log.d("Fantastic", "snooff " + browseFragment.getListView().getMeasuredHeight() + " smooff");
        }
    }

    @Override
    public void onArticleSelected(int position, Personnel personnel)
    {
//        String text = personnel.getBrowseText() + "\n" + personnel.getRoom();
//        Log.d(TAG, text);
//
//        Context context = getApplicationContext();
//
//        int duration = Toast.LENGTH_SHORT;
//
//        Toast toast = Toast.makeText(context, text, duration);
//        toast.show();

        if(personnelFragment != null)
        {
            fm.beginTransaction()
//            .setCustomAnimations(android.R.animator., android.R.animator.fade_out)
                .show(personnelFragment)
                .commit();
            personnelFragment.setPersonnel(personnel);
        }
    }

    @Override
    public void toggle()
    {
        if(personnelFragment != null)
        {
            fm.beginTransaction()
//            .setCustomAnimations(android.R.animator., android.R.animator.fade_out)
                .hide(personnelFragment)
                .commit();
        }

    }


    private class PersonnelHandler extends Handler
    {
        private final String TAG = PersonnelHandler.class.getSimpleName();

        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);

            String s = msg.getData().getString(PersonnelService.PERSONNEL_UPDATE_PROGRESS);

            Log.d(TAG, "received message: " + s);

            if(s.equals(PersonnelService.MSG_PERSISTED_DATA))
            {
                IntercomDBHelper intercomDBHelper = new IntercomDBHelper(WelcomeActivity.this);
                SQLiteDatabase db = intercomDBHelper.getReadableDatabase();

                PersonnelDao personnelDao = new PersonnelDao(db);

                ArrayList<Personnel> personnelList = personnelDao.getPersonnelList(null);

                browseFragment.setPersonnelList(personnelList);

                //TODO test all rooms to see how many id's

                RoomDao roomDao = new RoomDao(db);

                HashMap<Integer, Room> rooms = roomDao.getIDHash(null);

            }



        }
    }
}
