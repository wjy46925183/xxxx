package gongren.com.dlg.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;

import gongren.com.dlg.activity.BaseActivity;

public class BaseFragment extends Fragment {

    protected BaseActivity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (BaseActivity) activity;
        Log.e("DLG","fragment页面=="+ this.getClass().getSimpleName());
    }
}
