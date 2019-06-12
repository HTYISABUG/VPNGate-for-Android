package com.example.vpngate;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Crawler {

    private ArrayList<ServerInfo> mServerList;

    private Spinner mSpinner;
    private Button mConnect;
    private ProgressBar mProgressBar;

    public Crawler(Spinner spinner, Button connect, ProgressBar progressBar) {
        mServerList = new ArrayList<>();

        mSpinner = spinner;
        mConnect = connect;
        mProgressBar = progressBar;

        refresh();
    }

    private class CrawlerAsyncTask extends AsyncTask<Void, Void, Void> {

        static final String src = "http://www.vpngate.net/api/iphone/";
        static final int timeoutMillis = 30 * 1000;

        @Override
        protected void onPreExecute() {
            mSpinner.setEnabled(false);
            mConnect.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                URL url = new URL(src);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setConnectTimeout(timeoutMillis);

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                CSVReader reader = new CSVReaderBuilder(in).withSkipLines(1).build();
                final List<String> headers = Arrays.asList(reader.readNext());

                String[] nextLine;
                while ((nextLine = reader.readNext()) != null && headers.size() == nextLine.length) {
                    mServerList.add(new ServerInfo(headers, nextLine));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ArrayAdapter adapter = new ArrayAdapter<String>(
                    mSpinner.getContext(),
                    R.layout.spinner_item_selected,
                    ServerInfo.countryList()
            );
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

            mSpinner.setAdapter(adapter);

            mSpinner.setEnabled(true);
            mConnect.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    public void refresh() {
        new CrawlerAsyncTask().execute();
    }

    public ArrayList<ServerInfo> serverList() {
        return mServerList;
    }
}
