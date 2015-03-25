package gid.cs.huji.intercom.activities;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkView;

import gid.cs.huji.intercom.JSBind.JS_Com_Bind;
import gid.cs.huji.intercom.R;
import gid.cs.huji.intercom.model.Personnel;
import gid.cs.huji.intercom.services.PersonnelService;

/**
 * Created by gideonbar on 08/03/15.
 */
public class ComActivity extends Activity{

    private static final String TAG = ComActivity.class.getSimpleName();

    public static final String COM_URL = "http://e-10.cs.huji.ac.il:8000/intercom/webrtc_example/";

    private XWalkView xWalkView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setContentView(R.layout.activity_com);

        Bundle bundle = this.getIntent().getExtras().getBundle(PersonnelService.PERSONNEL_BUNDLE);

        Personnel personnel = (Personnel)bundle.getSerializable(Personnel.PERSONNEL);
        Log.d(TAG, "Muhta " + personnel.getBrowseText());


//        Don't know how this helps if the preferences are connected?
        XWalkPreferences.setValue("enable-javascript", true);
        XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);

        xWalkView=(XWalkView)findViewById(R.id.xwalkWebView);
        xWalkView.addJavascriptInterface(new JS_Com_Bind(this, xWalkView, personnel), "Android");

        xWalkView.clearCache(true);
        xWalkView.load(COM_URL, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (xWalkView != null) {
            xWalkView.pauseTimers();
            xWalkView.onHide();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (xWalkView != null) {
            xWalkView.resumeTimers();
            xWalkView.onShow();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (xWalkView != null) {
            xWalkView.onDestroy();
        }
    }
}
