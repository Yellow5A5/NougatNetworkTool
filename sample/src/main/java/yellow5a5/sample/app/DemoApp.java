package yellow5a5.sample.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import yellow5a5.nougatnetworktool.ActivityLifeAdapter;
import yellow5a5.nougatnetworktool.NougatNetworkTool;

/**
 * Created by Yellow5A5 on 16/10/4.
 */

public class DemoApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        NougatNetworkTool.getInstance().init(this);
        registerActivityLifecycleCallbacks(new ActivityLifeAdapter(this));
    }
}
