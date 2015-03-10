package gid.abstracts;

import android.support.v4.app.Fragment;

public abstract class GidFragment extends Fragment
{
    protected abstract void getParameters();
    protected abstract void setLayoutParameters();
    protected abstract void setLayout();
    protected abstract void setInteractables();
}
