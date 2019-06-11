package com.example.vpngate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

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
}
