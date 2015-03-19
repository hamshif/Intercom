package gid.cs.huji.intercom.activities;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkView;

import gid.cs.huji.intercom.R;
import gid.cs.huji.intercom.model.Personnel;
import gid.cs.huji.intercom.services.PersonnelService;

/**
 * Created by gideonbar on 08/03/15.
 */
public class ComActivity extends Activity
{
    private XWalkView xWalkWebView;
    private static final String TAG = ComActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setContentView(R.layout.activity_com);

        Bundle bundle = this.getIntent().getExtras().getBundle(PersonnelService.PERSONNEL_BUNDLE);

        Personnel personnel = (Personnel)bundle.getSerializable(Personnel.PERSONNEL);
        Log.d(TAG, "Muhta " + personnel.getBrowseText());

        xWalkWebView=(XWalkView)findViewById(R.id.xwalkWebView);
        xWalkWebView.clearCache(true);
        xWalkWebView.load("http://e-10.cs.huji.ac.il:8000/intercom/webrtc_example/", null);

        // turn on debugging
        XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (xWalkWebView != null) {
            xWalkWebView.pauseTimers();
            xWalkWebView.onHide();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (xWalkWebView != null) {
            xWalkWebView.resumeTimers();
            xWalkWebView.onShow();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (xWalkWebView != null) {
            xWalkWebView.onDestroy();
        }
    }

}
