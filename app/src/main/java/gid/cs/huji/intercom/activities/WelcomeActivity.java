package gid.cs.huji.intercom.activities;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import gid.cs.huji.intercom.services.PersonnelService;
import gid.gidutil.GidUtil;
import gid.interfaces.IBrowsable;
import gid.cs.huji.intercom.fragments.PersonnelFragment;
import gid.cs.huji.intercom.fragments.BrowseFragment;
import gid.cs.huji.intercom.fragments.HeaderFragment;
import gid.cs.huji.intercom.model.Personnel;

import gid.cs.huji.intercom.R;
/**
 * Created by gideonbar on 08/03/15.
 */
public class WelcomeActivity extends FragmentActivity implements BrowseFragment.BrowseFragmentListener
{
    private static final String TAG = WelcomeActivity.class.getSimpleName();

    private FrameLayout onScreen;

    private PersonnelFragment personnelFragment;
    private BrowseFragment browseFragment;
    private HeaderFragment headerFragment;

    private static ImageView iv_call;

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

//        Messenger uiMessenger = new Messenger(new SearchHandler());
//        intent.putExtra(CATEGORY_SEARCH_MESSANGER, uiMessenger);

        startService(intent);

        Log.d(TAG, "Finished search()");



        startService(intent);
    }

    private void createGUI()
    {
        onScreen = (FrameLayout) getLayoutInflater().inflate(R.layout.intercom, null);
//        LinearLayout onScreen = (LinearLayout) getLayoutInflater().inflate(R.layout.intercom, null);
        setContentView(onScreen);

        personnelFragment = new PersonnelFragment();
        personnelFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, personnelFragment).commit();

//            LinearLayout ll = (LinearLayout) getLayoutInflater().inflate(R.layout.list_browse, null);
//            onScreen.addView(ll);

        browseFragment = new BrowseFragment();
        browseFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.list_container, browseFragment).commit();



        headerFragment = new HeaderFragment();
        headerFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, headerFragment).commit();
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
            personnelFragment.setPersonnel(personnel);
        }
    }
}
