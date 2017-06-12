package com.example.przemyslaw.aw;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.przemyslaw.aw.data.Channel;
import com.example.przemyslaw.aw.listener.WeatherServiceListener;
import com.example.przemyslaw.aw.service.YahooWeatherService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import layout.AdditionFragment;
import layout.BasicFragment;
import layout.MoonFragment;
import layout.NextDayFragment;
import layout.SunFragment;

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
        setContentView(R.layout.activity_main);
        ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        init();

        loadingDialog = new ProgressDialog(this);
        loadingDialog.setMessage(getString(R.string.loading));
        loadingDialog.setCancelable(false);
        loadingDialog.show();

        startClock();
        MainActivity.setContext(this);
        yahooWeatherService = new YahooWeatherService(this);
        yahooWeatherService.refreshWeather(location);

       /* Context c = MainActivity.getContext();
        File mydir = c.getDir("AstroWeatherPrivateDir", Context.MODE_PRIVATE); //Creating an internal dir;
        File fileWithinMyDir = new File(mydir, channel.getLocation() + ".json"); //Getting a file within the dir.
        if(mydir.exists()) {
            Toast.makeText(MainActivity.this, mydir.getAbsolutePath(), Toast.LENGTH_LONG).show();
        }*/

      /*  try {
            FileOutputStream out = new FileOutputStream(fileWithinMyDir);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/

        /*try {
            Writer output = null;
            File file = new File(mydir, channel.getLocation() + ".json");
            output = new BufferedWriter(new FileWriter(file));
            output.write(channel.toJSON());
            output.close();
            Toast.makeText(getApplicationContext(), "Composition saved", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Composition nie saved", Toast.LENGTH_LONG).show();
        }*/





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
        this.channel = channel;
        Context c = MainActivity.getContext();
        File mydir = c.getDir("AstroWeatherPrivateDir", Context.MODE_PRIVATE);
        if(mydir.exists()) {
            Gson gson = new GsonBuilder()
                    .create();
            try(FileWriter writer = new FileWriter("AstroWeatherPrivateDir/" + channel.getLocation() +".json")) {
                gson.toJson(channel, writer);
            }catch (IOException e) {
                Toast.makeText(MainActivity.this, "Error save to file", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void serviceFailure(Exception exception) {
        if (weatherServicesHasFailed) {
            loadingDialog.hide();
            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();
        }else {
            // error doing reverse geocoding, load weather data from cache
            loadingDialog.hide();
            weatherServicesHasFailed = true;
            Toast.makeText(MainActivity.this, "Load data from memory", Toast.LENGTH_LONG).show();
        }
    }

    public Channel getChannel() {
        return channel;
    }

    public String getTemperatureUnits() {
        return temperatureUnits;
    }
}
