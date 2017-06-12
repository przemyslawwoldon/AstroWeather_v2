package com.example.przemyslaw.aw;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.przemyslaw.aw.data.Channel;
import com.example.przemyslaw.aw.db.City;
import com.example.przemyslaw.aw.db.DaoMaster;
import com.example.przemyslaw.aw.db.DaoSession;
import com.example.przemyslaw.aw.listener.WeatherServiceListener;
import com.example.przemyslaw.aw.service.YahooWeatherService;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, WeatherServiceListener{
    private Button commitCity;
    private Button commitAll;

    private TextView textViewCity;
    private Spinner spinnerCity;
    private CheckBox tF;
    private CheckBox tC;
    private EditText time;

    YahooWeatherService yahooWeatherService;
    ArrayList<String> listaUlubionychMiast = new ArrayList<String>();

    DaoSession daoSession;
    StringBuffer sb = new StringBuffer();

    private int timeRefr = 0;
    private String tempUnits;

    public final static String TIME_REFRESH = "Settings time refresh";
    public final static String TEMP_UNITS = "Settings degree longitude";
    public final static String LOCATION = "Settings degree latitude";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();

        Intent intent = getIntent();
        String tR = intent.getStringExtra(MainActivity.M_TIME_REFRESH );
        String temperatreUnits = intent.getStringExtra(MainActivity.M_TEMPERATURE_UNITS);
        String location = intent.getStringExtra(MainActivity.M_LOCATION);
        if((tR != null) && (temperatreUnits != null) && (location != null)) {
            timeRefr = Integer.parseInt(tR);
            tempUnits = temperatreUnits;
            updateData(location);
        }

        tF.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(tF.isChecked()) {
                    tC.setChecked(false);
                }
                if(!tC.isChecked() && !tF.isChecked()){
                    tC.setChecked(true);
                }
            }
        });
        tC.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(tC.isChecked()) {
                    tF.setChecked(false);
                }
                if(!tC.isChecked() && !tF.isChecked()){
                    tC.setChecked(true);
                }
            }
        });
        configureDb();

        spinnerCity.setOnItemSelectedListener(this);
        listaUlubionychMiast.add("Lodz, Lodz");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listaUlubionychMiast);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(dataAdapter);

        commitCity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String newCity  = textViewCity.getText().toString();
                yahooWeatherService.refreshWeather(newCity);
            }
        });

        commitAll.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(checkRefresh() && sb.length() != 0) {
                    //Toast.makeText(SettingsActivity.this, sb.toString(), Toast.LENGTH_LONG).show();
                    Intent intentM = new Intent(SettingsActivity.this, MainActivity.class);
                    intentM.putExtra(TIME_REFRESH, String.valueOf(timeRefr));
                    if(tF.isChecked())
                        intentM.putExtra(TEMP_UNITS, "f");
                    else
                        intentM.putExtra(TEMP_UNITS, "c");
                    String loc = sb.toString();
                    intentM.putExtra(LOCATION, loc);
                    SettingsActivity.this.startActivity(intentM);
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        if(sb.length() != 0)
            sb.delete(0, sb.length() - 1);
        sb.append(item);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(parent.getContext(), "Nic nie wybrano", Toast.LENGTH_LONG).show();
    }

    public void init(){
        yahooWeatherService = new YahooWeatherService(this);
        commitCity = (Button) findViewById(R.id.buttonCommitCity);
        commitAll = (Button) findViewById(R.id.buttonCommitAll);
        textViewCity = (TextView) findViewById(R.id.textViewCity);
        spinnerCity = (Spinner) findViewById(R.id.mySpinner);
        time = (EditText) findViewById(R.id.editTextTime);
        tF = (CheckBox) findViewById(R.id.checkBoxF);
        tC = (CheckBox) findViewById(R.id.checkBoxC);
        tF.setChecked(false);
        tC.setChecked(true);
    }

    public void updateData(String cityFromMain) {
        time.setText(String.valueOf(timeRefr));
        if(tempUnits.equals("f")) {
            tF.setChecked(true);
            tC.setChecked(false);
        } else {
            tC.setChecked(true);
            tF.setChecked(false);
        }
        yahooWeatherService.refreshWeather(cityFromMain);
    }

    public boolean checkRefresh(){
        String str1 = time.getText().toString();
        try {
            timeRefr = Integer.parseInt(str1);
            if(timeRefr < 1 || timeRefr > 60)
                return false;
            else
                return true;
        }catch(NumberFormatException n){
            return false;
        }
    }

    @Override
    public void serviceSuccess(Channel channel) {
        boolean flagIsCity = false;
        String cityToAdd = channel.getLocation();
        for(int i = 0; i < listaUlubionychMiast.size(); i += 1) {
            if(listaUlubionychMiast.get(i).equals(cityToAdd)) {
                flagIsCity = true;
                break;
            }
        }
        if(flagIsCity == true) {
            Toast.makeText(SettingsActivity.this, "Error miasto juz istnieje", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(SettingsActivity.this, "Dodano miasto", Toast.LENGTH_LONG).show();
            City cityAdd = new City(null, channel.getLocation(), channel.getItem().getLatitude(), channel.getItem().getLongitude());
            dbInsert(cityAdd);
            listaUlubionychMiast.add(channel.getLocation());
        }
    }

    @Override
    public void serviceFailure(Exception exception) {
        Toast.makeText(SettingsActivity.this, "Error", Toast.LENGTH_LONG).show();
    }

    public void configureDb() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "newAstro.db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        daoSession.getCityDao().detachAll();
    }

    public void dbInsert(City c) {
        daoSession.insertOrReplace(c);
    }

   /* public void dbGetCityAndCheckList() {
        List<City> users =  daoSession.loadAll(City.class);
        if(users.size() != 0) {
            for(int i = 0; i < users.size(); i += 1) {
                String cityToAdd = users.get(i).getName();
                boolean flagIsCity = false;
                for(int j = 0; j < listaUlubionychMiast.size(); j += 1) {
                    if(listaUlubionychMiast.get(j).equals(cityToAdd)) {
                        flagIsCity = true;
                        break;
                    }
                } if(flagIsCity == false)
                    listaUlubionychMiast.add(cityToAdd);
            }
        }
    }*/

}
