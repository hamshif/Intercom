package gid.cs.huji.intercom.JSBind;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import org.xwalk.core.XWalkView;
import gid.cs.huji.intercom.activities.ComActivity;
import gid.cs.huji.intercom.model.Personnel;

import org.xwalk.core.JavascriptInterface;

public class JS_Com_Bind
{
    private static final String TAG = JS_Com_Bind.class.getSimpleName();
    private Context context;
    private XWalkView xWalkWebView;
    private Personnel personnel;

    /**
     * Instantiate the interface and set the context
     */
    public JS_Com_Bind(Context c, XWalkView xWalkWebView, Personnel personnel)
    {
        context = c;
        this.xWalkWebView = xWalkWebView;
        this.personnel = personnel;
    }

    /**
     * Show a toast from the web page
     */
    @JavascriptInterface
    public void showToast(String toast)
    {
        Log.d(TAG, "showToast(String toast)");
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
    }


    @JavascriptInterface
    public String getPersonnel()
    {
        String p = "{ \"id\": " + personnel.getId() + ", \"browse_text\": \"" + personnel.getBrowseText() + "\"}";
        Log.d(TAG, "getPersonnel: " + p);
//        Toast.makeText(context, p, Toast.LENGTH_SHORT).show();

        return p;
    }

	    
	    
    public void refreshWebView()
    {
        Toast.makeText(context, "refreshing....", Toast.LENGTH_SHORT).show();

        xWalkWebView.clearCache(true);
        xWalkWebView.load(ComActivity.COM_URL, null);
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showDialog(String text)
    {
        Log.d("Shpootser", text);
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(text)
               .setTitle("Answer");

        builder.setNegativeButton("close", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id)
            {
                // User cancelled the dialog
            }
        });

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();

        dialog.show();
    }
	    
}
