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

import gid.cs.huji.intercom.activities.ComActivity;
import gid.cs.huji.intercom.R;

public class PersonnelFragment extends Fragment
{
    private static final String TAG = PersonnelFragment.class.getSimpleName();

    private static LinearLayout ll_call;

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


//                WebView myWebView = (WebView) findViewById(R.id.webview);
//                myWebView.clearCache(true);
//                myWebView.loadUrl("http://e-10.cs.huji.ac.il:8000/intercom/webrtc_example/?room=432827");

//                Uri uri = Uri.parse("http://e-10.cs.huji.ac.il:8000/intercom/webrtc_example/?room=342862");
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);

            }
        });



        return view;

    }

    private void call()
    {
        Intent intent = new Intent(this.getActivity(), ComActivity.class);
        startActivity(intent);
    }
}
