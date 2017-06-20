package com.example.przemyslaw.aw;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.przemyslaw.aw.data.Channel;
import com.example.przemyslaw.aw.listener.WeatherServiceListener;
import com.example.przemyslaw.aw.service.YahooWeatherService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

import layout.AdditionFragment;
import layout.BasicFragment;
import layout.MoonFragment;
import layout.NextDayFragment;
import layout.SunFragment;
import layout.large.AdditionFragmentL;
import layout.large.BasicFragmentL;
import layout.large.MoonFragmentL;
import layout.large.NextDayFragmentL;
import layout.large.NextDayFragmentLand;
import layout.large.SunFragmentL;
import layout.large.SunMoonFragment;
import layout.large.WeatherWindFragment;

public class MainActivity extends AppCompatActivity implements WeatherServiceListener{

    private TextView actualTime;
    private TextView longitude;
    private TextView latitude;
    private ProgressDialog loadingDialog;

    private Calendar calendar;
    private int hour;
    private int minute;
    private int second;

    YahooWeatherService yahooWeatherService;
    Channel channel;

    private static int timeRefr = 15;
    private static StringBuffer sb = new StringBuffer();

    public final static String M_TIME_REFRESH = "Main time refresh";
    public final static String M_TEMPERATURE_UNITS = "Temperature units";
    public final static String M_LOCATION = "Main degree latitude";

    String temperatureUnits = "f";
    String location = "chicago, il";

    private boolean weatherServicesHasFailed = false;


    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    public static void setContext(Context mmContext) {
        mContext = mmContext;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            switch (getResources().getConfiguration().orientation){
                case Configuration.ORIENTATION_PORTRAIT:
                    setContentView(R.layout.activity_main);
                    break;
                case Configuration.ORIENTATION_LANDSCAPE:
                    setContentView(R.layout.activity_main);
                    break;
            }
            actualTime = (TextView) findViewById(R.id.textViewActualTime);
            longitude = (TextView) findViewById(R.id.textViewActualLong);
            latitude = (TextView) findViewById(R.id.textViewActualLat);
            ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
            pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        } else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            switch (getResources().getConfiguration().orientation) {
                case Configuration.ORIENTATION_PORTRAIT:
                    setContentView(R.layout.activity_main_large);

                    BasicFragmentL basicFragmentL = BasicFragmentL.newInstance();
                    AdditionFragmentL additionFragmentL = AdditionFragmentL.newInstance();
                    NextDayFragmentL nextDayFragmentL = NextDayFragmentL.newInstance();
                    SunFragmentL sunFragmentL = SunFragmentL.newInstance();
                    MoonFragmentL moonFragmentL = MoonFragmentL.newInstance();

                   /* android.app.FragmentManager manager = getFragmentManager();
                    android.app.FragmentTransaction fragmentTransaction = manager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment1, basicFragmentL);
                    fragmentTransaction.replace(R.id.fragment2, additionFragmentL);
                    fragmentTransaction.replace(R.id.fragment3, sunFragmentL);
                    fragmentTransaction.replace(R.id.fragment4, moonFragmentL);
                    fragmentTransaction.replace(R.id.fragment5, nextDayFragmentL);
                    fragmentTransaction.commit();*/
                    break;
                case Configuration.ORIENTATION_LANDSCAPE:
                    setContentView(R.layout.activity_main_large_land);
                    ViewPager pager = (ViewPager) findViewById(R.id.viewPagerLarge);
                    pager.setAdapter(new MyPagerAdapterLarge(getSupportFragmentManager()));
                    break;
            }
            actualTime = (TextView) findViewById(R.id.textViewActualTime);
            longitude = (TextView) findViewById(R.id.textViewActualLong);
            latitude = (TextView) findViewById(R.id.textViewActualLat);
        }
   // init();

        MainActivity.setContext(this);
        yahooWeatherService = new YahooWeatherService(this);
        loadingDialog = new ProgressDialog(this);
        loadingDialog.setMessage(getString(R.string.loading));
        loadingDialog.setCancelable(false);
        loadingDialog.show();
        yahooWeatherService.refreshWeather(location);
        startClock();


        Thread timeTread = new Thread(){
            @Override
            public void run(){
                try{
                    while (!isInterrupted()){
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run(){
                               //yahooWeatherService.refreshWeather("chicago, il");
                            }
                        });
                        Thread.sleep(60000 * timeRefr);
                    }
                }catch(InterruptedException e){}
            }
        };
        timeTread.start();

        Intent intent = getIntent();
        String tR = intent.getStringExtra(SettingsActivity.TIME_REFRESH);
        String locationFromSettings = intent.getStringExtra(SettingsActivity.LOCATION);
        String tempUnits = intent.getStringExtra(SettingsActivity.TEMP_UNITS);
         if((tR != null) && (locationFromSettings != null) && (tempUnits != null)) {
            timeRefr = Integer.parseInt(tR);
            temperatureUnits = tempUnits;
            location = locationFromSettings;
            yahooWeatherService.refreshWeather(location);
        }
    }

    public void init() {
        actualTime = (TextView) findViewById(R.id.textViewActualTime);
        longitude = (TextView) findViewById(R.id.textViewActualLong);
        latitude = (TextView) findViewById(R.id.textViewActualLat);
    }

    public void startClock(){
        Thread timeTread = new Thread(){
            @Override
            public void run(){
                try{
                    while (!isInterrupted()){
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run(){
                                calendar = Calendar.getInstance();
                                hour = calendar.get(Calendar.HOUR_OF_DAY);
                                minute = calendar.get(Calendar.MINUTE);
                                second = calendar.get(Calendar.SECOND);
                                actualTime.setText(String.valueOf(hour) + ":" + String.valueOf(minute) + ":" + String.valueOf(second));
                            }
                        });
                        Thread.sleep(1000);
                    }
                }catch(InterruptedException e){}
            }
        };
        timeTread.start();
    }

    public static class MyPagerAdapterLarge extends FragmentPagerAdapter {
        public MyPagerAdapterLarge(FragmentManager fm) {
            super(fm);
        }
        @Override
        public android.support.v4.app.Fragment getItem(int pos) {
            switch(pos) {
                case 0:
                    return SunMoonFragment.newInstance();
                case 1:
                    return WeatherWindFragment.newInstance();
                case 2:
                    return NextDayFragmentLand.newInstance();
                default:
                    return WeatherWindFragment.newInstance();
            }
        }
        @Override
        public int getCount() {
            return 4;
        }
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public android.support.v4.app.Fragment getItem(int pos) {
            switch(pos) {
                default:
                    return BasicFragment.newInstance();
                case 0:
                    return BasicFragment.newInstance();
                case 1:
                    return AdditionFragment.newInstance();
                case 2:
                    return NextDayFragment.newInstance();
                case 3:
                    return SunFragment.newInstance();
                case 4:
                    return MoonFragment.newInstance();
            }
        }
        @Override
        public int getCount() {
            return 6;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu); //return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                settingsIntent.putExtra(M_TIME_REFRESH, String.valueOf(timeRefr));
                settingsIntent.putExtra(M_TEMPERATURE_UNITS, temperatureUnits);
                settingsIntent.putExtra(M_LOCATION, location);
                MainActivity.this.startActivity(settingsIntent);
                return true;
            case R.id.update:
                loadingDialog.show();
                yahooWeatherService.refreshWeather(location);
                return true;
            default:
                return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void serviceSuccess(Channel channel) {
        loadingDialog.hide();
        longitude.setText(String.valueOf(channel.getItem().getLongitude()));
        latitude.setText(String.valueOf(channel.getItem().getLatitude()));
        location = channel.getLocation();
        this.channel = channel;
        String filename = location + ".json";
        File myFile = new File(filename);
        if(myFile.exists())
            myFile.delete();

        String stringToFile = channel.toJSON().toString();
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(stringToFile.getBytes());
            outputStream.close();
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void serviceFailure(Exception exception) {
        if (weatherServicesHasFailed) {
            loadingDialog.hide();
            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();
        }else {
            loadingDialog.hide();
            weatherServicesHasFailed = true;
            Toast.makeText(MainActivity.this, "Load data from memory", Toast.LENGTH_LONG).show();

            String filename = location + ".json";
            String ret = "";
            try {
                InputStream inputStream = MainActivity.getContext().openFileInput(filename);
                if ( inputStream != null ) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String receiveString = "";
                    StringBuilder stringBuilder = new StringBuilder();
                    while ( (receiveString = bufferedReader.readLine()) != null ) {
                        stringBuilder.append(receiveString);
                    }
                    inputStream.close();
                    ret = stringBuilder.toString();

                }
            }
            catch (FileNotFoundException e) {
                Log.e("login activity", "File not found: " + e.toString());
                Toast.makeText(MainActivity.this, "File not found", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Log.e("login activity", "Can not read file: " + e.toString());
                Toast.makeText(MainActivity.this, "Can not read file", Toast.LENGTH_LONG).show();
            }
            try {
                JSONObject data = new JSONObject(ret);
                Channel channel1 = new Channel();
                channel1.populate(data, 1);
                this.channel = channel1;
                longitude.setText(String.valueOf(channel.getItem().getLongitude()));
                latitude.setText(String.valueOf(channel.getItem().getLatitude()));
                Toast.makeText(MainActivity.this, channel1.getLocation(), Toast.LENGTH_LONG).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public Channel getChannel() {
        return channel;
    }

    public String getTemperatureUnits() {
        return temperatureUnits;
    }
}
