package gid.cs.huji.intercom.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gid.cs.huji.intercom.adapters.BrowseAdapter;
import gid.cs.huji.intercom.model.Room;
import gid.interfaces.IBrowsable;
import gid.cs.huji.intercom.R;
import gid.cs.huji.intercom.model.Personnel;


public class BrowseFragment extends ListFragment
{
    private static final String TAG ="BrowseFragment";

    private static List<IBrowsable> personnelList;

    private static BrowseAdapter personnel_adapter;

    private static ListView lv;

    private List<IBrowsable> getPersonnelList()
    {
        List<IBrowsable> l = new ArrayList<IBrowsable>();

        HashMap<Integer, HashMap<String, String>> h_personnel = new HashMap<Integer, HashMap<String, String>>();

        String [] peronnel_names =
        {
            "Danny", "Ely", "Jorge", "Raanan", "Tanya", "Yair", "Chana",
            "Dima", "Tomer", "Ephraim", "Pavel", "Naama", "Gideon",
        };

        String [] paths =
        {
            "/img...", "/img...", "/img...", "/img...", "/img...", "/img...", "/img...",
            "/img...", "/img...", "/img...", "/img...", "/img...", "/img...",
        };

        String [] surnames =
        {
            "Braniss", "Levi", "Najenson", "Chermoni", "Kuzmitski", "Yarom", "Slutzkin",
            "Surname", "Klainer", "Silverberg", "Gak", "Shemesh", "Bar",
        };

        ArrayList<Room> rooms = new ArrayList<Room>();


        for(int i=0; i<peronnel_names.length; i++)
        {
            rooms.add(new Room(null, 3, "Rothberg", "B", 1, 3));

            l.add(new Personnel(null, 3, peronnel_names[i], surnames[i], paths[i], rooms.get(i)));
        }

        return l;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        personnel_adapter = new BrowseAdapter(inflater.getContext(), R.layout.list_row_item, getPersonnelList());
        this.setListAdapter(personnel_adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }


    private BrowseFragmentListener callback;

    // Container Activity must implement this interface
    public interface BrowseFragmentListener
    {
        public void onArticleSelected(int position, Personnel personnel);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try
        {
            callback = (BrowseFragmentListener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " must implement" + this.getClass().getSimpleName());
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        Personnel personnel = (Personnel) l.getItemAtPosition(position);

        String selection = personnel.toString();
        Log.d(TAG, "selection: " + selection);
        // Send the event to the host activity
        callback.onArticleSelected(position, personnel);
    }
}
