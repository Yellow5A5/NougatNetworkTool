package yellow5a5.nougatnetworktool;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Yellow5A5 on 16/10/9.
 */

public class ActivityLifeAdapter implements Application.ActivityLifecycleCallbacks{

    private Context mContext;
    private String mInitialState;

    public ActivityLifeAdapter(Context context){
        mContext = context;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.e(ActivityLifeAdapter.class.getName(), "onActivityCreated: " + activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.e(ActivityLifeAdapter.class.getName(), "onActivityStarted: " + activity.getClass().getSimpleName());
        NougatNetworkTool.isForegroundFlag++;
        if (!TextUtils.isEmpty(mInitialState)){
            ConnectivityManager mConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetwork = mConnectivityManager.getActiveNetworkInfo();
            if (mNetwork != null && !mNetwork.getTypeName().equals(mInitialState)){
                mContext.sendBroadcast(new Intent(ConnectivityManager.CONNECTIVITY_ACTION));
            }
        }

    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.e(ActivityLifeAdapter.class.getName(), "onActivityResumed: " + activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.e(ActivityLifeAdapter.class.getName(), "onActivityPaused: " + activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.e(ActivityLifeAdapter.class.getName(), "onActivityStopped: " + activity.getClass().getSimpleName());
        NougatNetworkTool.isForegroundFlag--;
        if(NougatNetworkTool.isForegroundFlag == 0){
            ConnectivityManager mConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetwork = mConnectivityManager.getActiveNetworkInfo();
            if (mNetwork != null){
                mInitialState = mNetwork.getTypeName();
            }
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Log.e(ActivityLifeAdapter.class.getName(), "onActivitySaveInstanceState: " + activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.e(ActivityLifeAdapter.class.getName(), "onActivityDestroyed: " + activity.getClass().getSimpleName());
    }
}
