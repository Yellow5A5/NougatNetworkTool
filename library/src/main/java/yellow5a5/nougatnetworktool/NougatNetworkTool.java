package yellow5a5.nougatnetworktool;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
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
    private List<NetworkListener> mListenerList = new LinkedList<>();

    private String mTypeName = "";

    public static NougatNetworkTool getInstance() {
        if (mInstance == null) {
            synchronized (obj) {
                if (mInstance == null) {
                    mInstance = new NougatNetworkTool();
                    return mInstance;
                }
            }
        }
        return mInstance;
    }

    public void init(Context context) {
        mContext = context;
        mConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        mTelephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        NetworkReceiver mNetworkReceiver = new NetworkReceiver();
        context.registerReceiver(mNetworkReceiver, filter);
    }

    public void registerNetworkListener(@NonNull NetworkListener l) {
        mListenerList.add(l);
    }

    public void unRegisterNetworkListener(@NonNull NetworkListener l) {
        if (mListenerList.contains(l)) {
            mListenerList.remove(l);
        }
    }

    public boolean isNetworkAvailable() {
        mConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = mConnectivityManager.getActiveNetworkInfo();
        if (netInfo == null) {
            return false;
        } else {
            return netInfo.isAvailable();
        }
    }

    public boolean isMobileAvailable(){
        mConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (netInfo == null) {
            return false;
        } else {
            return netInfo.isAvailable();
        }
    }

    public boolean isWifiAvailable(){
        mConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (netInfo == null) {
            return false;
        } else {
            return netInfo.isAvailable();
        }
    }

    /**
     * This does not meaning the current state of the network.
     *
     * @return the tpye of network.
     */
    public int getNetworkTypeClass() {
        switch (mTelephonyManager.getNetworkType()) {
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

    private void connectResponse(String typeName) {
        int typeClass = NetworkUtil.typeNameToTypeClass(typeName);
        Log.e(NetworkReceiver.class.getName(), "onReceive CON: " + typeClass);
        for (int i = 0; i < mListenerList.size(); i++) {
            mListenerList.get(i).onConnect(typeClass);
        }
    }

    private void unConnectResponse(String typeName) {
        int typeClass = NetworkUtil.typeNameToTypeClass(typeName);
        Log.e(NetworkReceiver.class.getName(), "onReceive UN: " + typeClass);
        for (int i = 0; i < mListenerList.size(); i++) {
            mListenerList.get(i).onNoConnect(typeClass);
        }
    }

    public void recNetworkChangeBroadcast(){
        if (mListenerList.isEmpty()) {
            return;
        }
        mConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetInfo = mConnectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null) {
            if (!activeNetInfo.getTypeName().equals(mTypeName)) {
                mTypeName = activeNetInfo.getTypeName();
                connectResponse(mTypeName);
            }
        } else {
            if(!TextUtils.isEmpty(mTypeName)){
                unConnectResponse(mTypeName);
                mTypeName = "";
            }
        }
    }


    public class NetworkReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            recNetworkChangeBroadcast();
        }
    }
}
