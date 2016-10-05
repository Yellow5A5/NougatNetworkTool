package yellow5a5.nougatnetworktool;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by Yellow5A5 on 16/10/4.
 */

public class NougatNetworkTool {


    private static NougatNetworkTool mInstance;
    private static final Object obj = new Object();

    private Context mContext;
    private ConnectivityManager mConnectivityManager;
    private TelephonyManager mTelephonyManager;
    private static List<NetworkListener> mListenerList = new LinkedList<>();
    ;


    private int mNowNetworkType = -1;

    public static NougatNetworkTool getInstance() {
        if (mInstance == null) {
            synchronized (obj) {
                if (mInstance == null) {
                    return new NougatNetworkTool();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context) {
        mContext = context;
        mConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        NetworkReceiver mNetworkReceiver = new NetworkReceiver();
        context.registerReceiver(mNetworkReceiver, filter);
    }

    public boolean isConnected() {
        NetworkInfo netInfo = mConnectivityManager.getActiveNetworkInfo();
        return netInfo.isConnected();
    }

    public static void addNetworkListener(@NonNull NetworkListener l) {
        mListenerList.add(l);
    }

    public static void removeNetworkListener(@NonNull NetworkListener l) {
        if (mListenerList.contains(l)) {
            mListenerList.remove(l);
        }
    }

    public static int getNetworkClass(int networkType) {
        switch (networkType) {
            case Constants.NETWORK_TYPE_GPRS:
            case Constants.NETWORK_TYPE_GSM:
            case Constants.NETWORK_TYPE_EDGE:
            case Constants.NETWORK_TYPE_CDMA:
            case Constants.NETWORK_TYPE_1xRTT:
            case Constants.NETWORK_TYPE_IDEN:
                return Constants.NETWORK_CLASS_2_G;
            case Constants.NETWORK_TYPE_UMTS:
            case Constants.NETWORK_TYPE_EVDO_0:
            case Constants.NETWORK_TYPE_EVDO_A:
            case Constants.NETWORK_TYPE_HSDPA:
            case Constants.NETWORK_TYPE_HSUPA:
            case Constants.NETWORK_TYPE_HSPA:
            case Constants.NETWORK_TYPE_EVDO_B:
            case Constants.NETWORK_TYPE_EHRPD:
            case Constants.NETWORK_TYPE_HSPAP:
            case Constants.NETWORK_TYPE_TD_SCDMA:
                return Constants.NETWORK_CLASS_3_G;
            case Constants.NETWORK_TYPE_LTE:
                return Constants.NETWORK_CLASS_4_G;
            case Constants.NETWORK_TYPE_IWLAN:
                return Constants.NETWORK_CLASS_WIFI;
            default:
                return Constants.NETWORK_CLASS_UNKNOWN;
        }
    }

//    class NetworkReceiver extends BroadcastReceiver {
//        private static boolean firstConnect = true;
//        @Override
//        synchronized public void onReceive(Context context, Intent intent) {
//
//            if (mListenerList.isEmpty()) {
//                return;
//            }
//            final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//            final NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
//            if (activeNetInfo != null) {
//                if(firstConnect) {
//                    // do subroutines here
//                    firstConnect = false;
//                }
//            }
//            else {
//                firstConnect= true;
//            }
//
//
//            int mTempNetworkType = getNetworkClass(mTelephonyManager.getNetworkType());
//            boolean tempIsConnect = !intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
//
//            Log.e(NetworkReceiver.class.getName(), "============Test 1 ============= ");
//            if (mConnectivityManager.getActiveNetworkInfo() != null) {
//                Log.e(NetworkReceiver.class.getName(), "onReceive: " + mConnectivityManager.getActiveNetworkInfo().toString());
//            }
//            Log.e(NetworkReceiver.class.getName(), "============Test 2 ============= ");
//            if (tempIsConnect != isConnect) {
//                isConnect = tempIsConnect;
//                if (isConnect) {
//                    for (int i = 0; i < mListenerList.size(); i++) {
//                        mListenerList.get(i).onConnect(mTempNetworkType);
//                    }
//                } else {
//                    for (int i = 0; i < mListenerList.size(); i++) {
//                        mListenerList.get(i).onNoConnect();
//                    }
//                }
//            }
//        }
//    }

    private int mTypeMark = -1;
    private boolean firstConnect;
    private String mTypeName;

    public class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (mListenerList.isEmpty()) {
                return;
            }
            final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetInfo != null) {
                if (!activeNetInfo.getTypeName().equals(mTypeName)) {
                    mTypeName = activeNetInfo.getTypeName();
                    int typeclass = typeNameToTypeClass(mTypeName);
//                                    mTypeMark = getNetworkClass(mTelephonyManager.getNetworkType());

                    Log.e(NetworkReceiver.class.getName(), "onReceive CON: " + typeclass);
                    for (int i = 0; i < mListenerList.size(); i++) {
                        mListenerList.get(i).onConnect(typeclass);
                    }
                }
            } else {
                int typeclass = typeNameToTypeClass(mTypeName);
                Log.e(NetworkReceiver.class.getName(), "onReceive UN: " + typeclass);
                for (int i = 0; i < mListenerList.size(); i++) {
                    mListenerList.get(i).onNoConnect(typeclass);
                }
                mTypeName = "";
            }
        }

        private int typeNameToTypeClass(String typeName) {
            int typeClass = 0;
            switch (typeName) {
                case Constants.TYPE_WIFI:
                    typeClass = Constants.NETWORK_CLASS_WIFI;
                    break;

                case Constants.TYPE_MOBILE:
                    typeClass = Constants.NETWORK_CLASS_4_G;
                    break;
            }
            return typeClass;
        }
    }
}
