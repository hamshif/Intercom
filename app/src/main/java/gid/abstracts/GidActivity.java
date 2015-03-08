package gid.abstracts;

import android.app.Activity;

/**
 * Created by gideonbar on 08/02/15.
 */
public abstract class GidActivity extends Activity
{
    protected abstract void setParameters();
    protected abstract void setLayoutParameters();
    protected abstract void setLayout();
    protected abstract void setInteractables();
}
