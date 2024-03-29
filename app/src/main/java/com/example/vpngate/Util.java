package com.example.vpngate;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

class Util {

    public static boolean networkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();

        return info == null || info.isConnected();
    }

}
