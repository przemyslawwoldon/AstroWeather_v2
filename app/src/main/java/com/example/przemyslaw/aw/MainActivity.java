package com.example.przemyslaw.aw;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.przemyslaw.aw.data.Channel;
import com.example.przemyslaw.aw.listener.WeatherServiceListener;
import com.example.przemyslaw.aw.service.YahooWeatherService;

import java.util.Calendar;

import layout.BasicFragment;

public class MainActivity extends AppCompatActivity implements WeatherServiceListener{

    Button b;
    TextView actualTime;
    TextView longitude;
    TextView latitude;
    private Calendar calendar;
    private int hour;
    private int minute;
    private int second;

    YahooWeatherService yws;
    private WeatherServiceListener l;

    private static int timeRefr = 15;
    private static StringBuffer sb = new StringBuffer();

    public final static String M_TIME_REFRESH = "Main time refresh";
    public final static String M_TEMPERATURE_UNITS = "Temperature units";
    public final static String M_LOCATION = "Main degree latitude";

    String temperatuerUnits = "f";
    String localization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        init();
        startClock();

        Intent intent = getIntent();
        String tR = intent.getStringExtra(SettingsActivity.TIME_REFRESH);
        String location = intent.getStringExtra(SettingsActivity.LOCATION);
        String tempUnits = intent.getStringExtra(SettingsActivity.TEMP_UNITS);
         if((tR != null) && (location != null) && (tempUnits != null)) {
            timeRefr = Integer.parseInt(tR);
             yws.refreshWeather(location);

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
        //    switch(pos) {
          //      default:
                //    return SunFragment.newInstance("Lodz, Lodz", "15");
            //    case 0:
              //      return SunFragment.newInstance("Lodz, Lodz", "15");
                //case 1:
                    return BasicFragment.newInstance("Lodz, Lodz", "15");
            //}*/
        }

        @Override
        public int getCount() {
            return 1;
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
                settingsIntent.putExtra(M_TEMPERATURE_UNITS, temperatuerUnits);
                settingsIntent.putExtra(M_LOCATION, "aaaa");
                MainActivity.this.startActivity(settingsIntent);
                return true;
            case R.id.update:
                return true;
            default:
                return false;
        }
    }
    @Override
    public void serviceSuccess(Channel channel) {
        Toast.makeText(MainActivity.this, "Error1", Toast.LENGTH_LONG).show();
    }

    @Override
    public void serviceFailure(Exception exception) {
        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();
    }

}
