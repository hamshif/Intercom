package gid.cs.huji.intercom.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import gid.cs.huji.intercom.activities.ComActivity;
import gid.cs.huji.intercom.R;
import gid.cs.huji.intercom.model.Personnel;

public class PersonnelFragment extends Fragment
{
    private static final String TAG = PersonnelFragment.class.getSimpleName();

    private static LinearLayout ll_call;
    private Personnel personnel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.personnel, container, false);


        ll_call = (LinearLayout) view.findViewById(R.id.ll_call);

        ll_call.setOnClickListener(new View.OnClickListener() {
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
}
