package com.example.przemyslaw.aw.service;

import android.content.Context;
import android.os.AsyncTask;

import com.example.przemyslaw.aw.R;
import com.example.przemyslaw.aw.data.Channel;
import com.example.przemyslaw.aw.listener.WeatherServiceListener;

import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Przemyslaw on 2017-06-13.
 */

public class WeatherCacheService {
    private Context context;
    private Exception error;
    private final String CACHED_WEATHER_FILE = "weather.json";

    public WeatherCacheService(Context context) {
        this.context = context;
    }

    public void save(final Channel channel) {
        new AsyncTask<Channel, Void, Void>() {
            @Override
            protected Void doInBackground(Channel[] channels) {

                FileOutputStream outputStream;
                FileWriter fw;

                try {
                    outputStream = context.openFileOutput("Test.json", Context.MODE_PRIVATE);
                    fw = new FileWriter(outputStream.getFD());

                    //writeJsonStream(fw, channels[0]);
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }.execute(channel);
    }

    public Channel load(final WeatherServiceListener listener) {

        new AsyncTask<WeatherServiceListener, Void, Channel>() {
            private WeatherServiceListener weatherListener;

            @Override
            protected Channel doInBackground(WeatherServiceListener[] serviceListeners) {
                weatherListener = serviceListeners[0];
                try {
                    FileInputStream inputStream = context.openFileInput(CACHED_WEATHER_FILE);
                    StringBuilder cache = new StringBuilder();
                    int content;
                    while ((content = inputStream.read()) != -1) {
                        cache.append((char) content);
                    }
                    inputStream.close();
                    JSONObject jsonCache = new JSONObject(cache.toString());
                    Channel channel = new Channel();
                    channel.populate(jsonCache, 1);
                    return channel;
                } catch (FileNotFoundException e) { // cache file doesn't exist
                    error = new CacheException(context.getString(R.string.cache_exception));
                } catch (Exception e) {
                    error = e;
                }
                return null;
            }
            @Override
            protected void onPostExecute(Channel channel) {
                if (channel == null && error != null) {
                    weatherListener.serviceFailure(error);
                } else {
                    weatherListener.serviceSuccess(channel);
                }
            }
        }.execute(listener);
        return null;
    }

    private class CacheException extends Exception {
        CacheException(String detailMessage) {
            super(detailMessage);
        }
    }
}
