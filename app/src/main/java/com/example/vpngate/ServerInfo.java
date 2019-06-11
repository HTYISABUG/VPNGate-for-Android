package com.example.vpngate;

import android.util.Base64;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ServerInfo {

    static private ArrayList<String> mCountryList = new ArrayList<>();

    static public ArrayList<String> countryList() {
        Collections.sort(mCountryList);
        return new ArrayList<>(mCountryList);
    }

    private static final String ipName      = "IP";
    private static final String scoreName   = "Score";
    private static final String pingName    = "Ping";
    private static final String speedName   = "Speed";
    private static final String countryName = "CountryLong";
    private static final String configName  = "OpenVPN_ConfigData_Base64";

    private String mIP;
    private int    mScore;
    private int    mPing;
    private int    mSpeed;
    private String mCountry;
    private String mConfig;
    private List<String> headers;

    public ServerInfo(List<String> headers, String[] cols) {
        this.headers = headers;

        mIP      = cols[indexOf(ipName)];
        mScore   = Integer.valueOf(cols[indexOf(scoreName)]);
        mPing    = Integer.valueOf(cols[indexOf(pingName)]);
        mSpeed   = Integer.valueOf(cols[indexOf(speedName)]);
        mCountry = cols[indexOf(countryName)];
        mConfig  = new String(Base64.decode(cols[indexOf(configName)], Base64.DEFAULT));

        if (!mCountryList.contains(mCountry)) {
            mCountryList.add(mCountry);
        }
    }

    public String ip() {
        return mIP;
    }

    public int score() {
        return mScore;
    }

    public int ping() {
        return mPing;
    }

    public int speed() {
        return mSpeed;
    }

    public String country() {
        return mCountry;
    }

    public String config() {
        return mConfig;
    }

    private int indexOf(String name) {
        return headers.indexOf(name);
    }

}
