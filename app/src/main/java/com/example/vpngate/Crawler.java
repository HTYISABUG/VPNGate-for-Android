package com.example.vpngate;

import android.os.AsyncTask;

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

    public Crawler() {
        mServerList = new ArrayList<>();

        refresh();
    }

    private class CrawlerAsyncTask extends AsyncTask<Void, Void, Void> {

        static final String src = "http://www.vpngate.net/api/iphone/";

        static final int timeoutMillis = 30 * 1000;
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


    }

    public void refresh() {
        new CrawlerAsyncTask().execute();
    }

    public ArrayList<ServerInfo> getServerList() {
        return mServerList;
    }
}
