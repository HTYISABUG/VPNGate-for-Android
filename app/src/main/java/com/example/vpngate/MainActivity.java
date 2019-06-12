package com.example.vpngate;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private static final String vpnAction       = "net.openvpn.openvpn.CONNECT";
    private static final String vpnPackageName  = "net.openvpn.openvpn";
    private static final String vpnClassName    = "net.openvpn.unified.MainActivity";

    private Crawler mCrawler;

    private Spinner mSpinner;
    private Button mConnect;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        mSpinner = findViewById(R.id.country);
        mConnect = findViewById(R.id.connect);
        mProgressBar = findViewById(R.id.progressBar);

        mCrawler = new Crawler(mSpinner, mConnect, mProgressBar);
    }

    public void onConnectClicked(View view) {
        final String country = mSpinner.getSelectedItem().toString();

        File profile = null;

        for (ServerInfo info : mCrawler.serverList()) {
            if (info.country().equals(country)) {
                File profileDir = new File("/sdcard", "VPNGate");

                if (!profileDir.exists()) {
                    profileDir.mkdir();
                }

                try {
                    profile = File.createTempFile("profile", ".ovpn", profileDir);
                    FileOutputStream out = openFileOutput(profile.getName(), MODE_PRIVATE);

                    out.write(info.config().getBytes());
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            }
        }

        if (profile != null) {
            Intent intent = new Intent(vpnAction);

            intent.setComponent(new ComponentName(vpnPackageName,vpnClassName));
            intent.setDataAndType(Uri.fromFile(profile), "application/x-openvpn-profile");

            startActivity(intent);
        }
    }
}
