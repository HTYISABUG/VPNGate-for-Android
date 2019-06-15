package com.example.vpngate;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private static final String vpnAction       = "net.openvpn.openvpn.CONNECT";
    private static final String vpnPackageName  = "net.openvpn.openvpn";
    private static final String vpnClassName    = "net.openvpn.unified.MainActivity";
    private static final String vpnStoreLink    = "https://play.google.com/store/apps/details?id=net.openvpn.openvpn";

    private static final float IMAGE_SCREEN_WIDTH_AUTO = 0.73f;

    private Crawler mCrawler;

    private LinearLayout mWrapper;
    private Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        try {
            getPackageManager().getApplicationInfo(vpnPackageName, 0);
        } catch (PackageManager.NameNotFoundException notFound) {
            if (Util.networkAvailable(this)) {
                redirectToStore();
            } else {
                Toast toast = Toast.makeText(this, R.string.no_network, Toast.LENGTH_SHORT);
                toast.show();
            }

            finishAffinity();
        }

        // set background quarter circle width and height
        ImageView background_image = (ImageView) findViewById(R.id.background);
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int imageViewSize = (int) ((float) screenWidth * IMAGE_SCREEN_WIDTH_AUTO);
        background_image.getLayoutParams().width = imageViewSize;
        background_image.getLayoutParams().height = imageViewSize;

        mWrapper = findViewById(R.id.linearLayoutWrapper);
        mSpinner = findViewById(R.id.country);

        Button mConnect = findViewById(R.id.connect);
        ProgressBar mProgressBar = findViewById(R.id.progressBar);


        mCrawler = new Crawler(mWrapper, mSpinner, mConnect, mProgressBar);
        onRefreshClicked(null);

    }

    public void onConnectClicked(View view) {
        final String country = mSpinner.getSelectedItem().toString();

        File profile = null;

        for (ServerInfo info : mCrawler.serverList()) {
            if (info.country().equals(country)) {
                File profileDir = new File(Environment.getExternalStorageDirectory(), "VPNGate");

                if (!profileDir.exists()) {
                    profileDir.mkdir();
                }

                try {
                    profile = File.createTempFile("vpngate-profile-", "-" + country + ".ovpn", profileDir);
                    FileOutputStream out = new FileOutputStream(profile);

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

    public void onRefreshClicked(View view) {
        if (Util.networkAvailable(this)) {
            mCrawler.refresh();
        } else {
            Toast toast = Toast.makeText(this, R.string.no_network, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void redirectToStore() {
        Uri uri = Uri.parse(vpnStoreLink);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}
