package gid.cs.huji.intercom.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import gid.cs.huji.intercom.activities.ComActivity;
import gid.cs.huji.intercom.R;
import gid.cs.huji.intercom.model.Personnel;
import gid.cs.huji.intercom.services.PersonnelService;
import gid.util.GidUtil;


public class PersonnelFragment extends Fragment
{
    private static final String TAG = PersonnelFragment.class.getSimpleName();

    private PersonnelFragmentListener callback;


    private static ImageView iv_toggle;
    private static ImageView iv_bell;
    private Personnel personnel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.personnel, container, false);


        iv_toggle = (ImageView) view.findViewById(R.id.iv_toggle);

        iv_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Log.d(TAG, "famfasta");
                callback.toggle();
            }
        });


        iv_bell = (ImageView) view.findViewById(R.id.iv_bell);

        iv_bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "");
                call();
            }
        });

        return view;
    }

    private void call()
    {
        Intent intent = new Intent(this.getActivity(), ComActivity.class);

        Bundle bundle = new Bundle();

        if(personnel == null)
        {
            GidUtil gidUtil = new GidUtil();

            gidUtil.tellUser(this.getActivity(), "Call Who?", Toast.LENGTH_LONG);

            return;
        }

        bundle.putSerializable(Personnel.PERSONNEL, personnel);
        intent.putExtra(PersonnelService.PERSONNEL_BUNDLE, bundle);

        startActivity(intent);
    }


    public void setPersonnel(Personnel personnel)
    {
        this.personnel = personnel;
        TextView tv_header = (TextView) getView().findViewById(R.id.tv_header);
        tv_header.setText(personnel.getBrowseText());

        TextView tv_room = (TextView) getView().findViewById(R.id.tv_room);
        tv_room.setText(personnel.getRoom().getBrowseText());
    }


    public interface PersonnelFragmentListener
    {
        public void toggle();
    }


    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try
        {
            callback = (PersonnelFragmentListener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " must implement" + this.getClass().getSimpleName());
        }
    }
}
