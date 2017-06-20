package layout.large;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;
import com.example.przemyslaw.aw.MainActivity;
import com.example.przemyslaw.aw.R;
import com.example.przemyslaw.aw.data.Channel;
import com.example.przemyslaw.aw.data.Condition;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Przemyslaw on 2017-06-20.
 */

public class ComplexFragment  extends Fragment {

    private TextView windSpeed;
    private TextView windDirection;
    private TextView atmosphereHumidity;
    private TextView atmosphereVisibility;

    TextView eastTime;
    TextView eastAzimuth;
    TextView westTime;
    TextView westAzimuth;
    TextView civilEvening;
    TextView civilMorning;

    TextView riseTime;
    TextView setTime;
    TextView fullTime;
    TextView newTime;
    TextView phase;
    TextView synodicDay;

    private ImageView weatherIconImageView;
    private TextView temperatureTextView;
    private TextView conditionTextView;
    private TextView locationTextView;
    private TextView pressureTextView;


    Calendar calendar;
    MainActivity mainActivity;

    public ComplexFragment() {
    }

    public static ComplexFragment newInstance() {
        ComplexFragment fragment = new ComplexFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mainActivity = (MainActivity) getActivity();

        View view = inflater.inflate(R.layout.fragment_complex, container, false);
        windSpeed = (TextView) view.findViewById(R.id.textViewSpeed);;
        windDirection = (TextView) view.findViewById(R.id.textViewDirection);;
        atmosphereHumidity = (TextView) view.findViewById(R.id.textViewHumidity);
        atmosphereVisibility = (TextView) view.findViewById(R.id.textViewVisibility);

        weatherIconImageView = (ImageView) view.findViewById(R.id.weatherIconImageView);
        temperatureTextView = (TextView) view.findViewById(R.id.temperatureTextView);
        conditionTextView = (TextView) view.findViewById(R.id.conditionTextView);
        locationTextView = (TextView) view.findViewById(R.id.locationTextView);
        pressureTextView = (TextView) view.findViewById(R.id.pressureTextView);

        eastTime = (TextView) view.findViewById(R.id.SunFargtextViewEastTime);
        eastAzimuth = (TextView) view.findViewById(R.id.SunFargtextViewEastAzimuth);
        civilEvening = (TextView) view.findViewById(R.id.SunFargtextViewCivilEvening);
        civilMorning = (TextView) view.findViewById(R.id.SunFargtextViewCivilDaylight);
        westTime = (TextView) view.findViewById(R.id.SunFargtextViewWestTime);
        westAzimuth = (TextView) view.findViewById(R.id.SunFargtextViewWestAzimuth);

        riseTime = (TextView) view.findViewById(R.id.MoonFargtextViewRiseMoon);
        setTime = (TextView) view.findViewById(R.id.MoonFargtextViewSetMoon);
        fullTime = (TextView) view.findViewById(R.id.MoonFargtextViewFullMoon);
        newTime = (TextView) view.findViewById(R.id.MoonFargtextViewNewMoon);
        phase = (TextView) view.findViewById(R.id.MoonFargtextViewPhase);
        synodicDay = (TextView) view.findViewById(R.id.MoonFargtextViewSynodicDay);


        setData();
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        if (isAdded()) {
                            getActivity().runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                setData();
                                                            }
                                                        }
                            );
                        }
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();

        return view;
    }

    public void setData() {
        Channel channel = mainActivity.getChannel();
        String units = mainActivity.getTemperatureUnits();
        if(channel != null) {
            windSpeed.setText(channel.getWind().getSpeed() + channel.getUnits().getSpeed());
            windDirection.setText(String.valueOf(channel.getWind().getDirection()));
            atmosphereHumidity.setText(String.valueOf(channel.getAtmosphere().getHumidity()));
            atmosphereVisibility.setText(String.valueOf(channel.getAtmosphere().getVisibility()));

            Condition condition = channel.getItem().getCondition();
            int weatherIconImageResource = getActivity().getResources().getIdentifier("icon_" + condition.getCode(), "drawable", getActivity().getPackageName());
            weatherIconImageView.setImageResource(weatherIconImageResource);
            NumberFormat formatter = new DecimalFormat("#0.00");
            if(units.equals("c"))
                temperatureTextView.setText(getString(R.string.temperature_output, condition.getTemperature(), channel.getUnits().getTemperature()));
            else {
                double c = (condition.getTemperature() * 1.8 ) + 32;
                temperatureTextView.setText(formatter.format(c) + " °F");
            }
            conditionTextView.setText(condition.getDescription());
            locationTextView.setText(channel.getLocation());
            pressureTextView.setText(formatter.format(channel.getAtmosphere().getPressure()) + " " + channel.getUnits().getPressure());

            AstroCalculator.Location astroLocation = new AstroCalculator.Location(channel.getItem().getLatitude(), channel.getItem().getLongitude());
            calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));
            TimeZone timeZone = TimeZone.getDefault();
            int offset = 2;
            AstroDateTime astroDateTime = new AstroDateTime(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                    calendar.get(Calendar.SECOND), offset, timeZone.useDaylightTime());
            AstroCalculator astroCalculator = new AstroCalculator(astroDateTime, astroLocation);

            AstroDateTime moonRise = astroCalculator.getMoonInfo().getMoonrise();
            AstroDateTime moonSet = astroCalculator.getMoonInfo().getMoonset();
            riseTime.setText(moonRise.getHour() + " : " + moonRise.getMinute() + " : " + moonRise.getSecond());
            setTime.setText(moonSet.getHour() + " : " + moonSet.getMinute() + " : " + moonSet.getSecond());
            AstroDateTime moonNew = astroCalculator.getMoonInfo().getNextNewMoon();
            AstroDateTime moonFull = astroCalculator.getMoonInfo().getNextFullMoon();
            fullTime.setText(moonFull.getDay() + "." + moonFull.getMonth() + "." + moonFull.getYear() + ", "
                    + moonFull.getHour() + " : " + moonFull.getMinute() + " : " + moonFull.getSecond());
            newTime.setText(moonNew.getDay() + "." + moonNew.getMonth() + "." + moonNew.getYear() + ", "
                    + moonNew.getHour() + " : " + moonNew.getMinute() + " : " + moonNew.getSecond());
            NumberFormat formatter2 = new DecimalFormat("#0.0000");
            double moonPhaseTemp = astroCalculator.getMoonInfo().getIllumination();
            moonPhaseTemp *= 100;
            phase.setText(formatter2.format(moonPhaseTemp));
            calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getTimeZone("GMT+2:00"));
            synodicDay.setText(formatter2.format(astroCalculator.getMoonInfo().getAge()));


            AstroDateTime sunRise = astroCalculator.getSunInfo().getSunrise();
            eastTime.setText(sunRise.getHour() + " : " + sunRise.getMinute() + " : " + sunRise.getSecond());
            eastAzimuth.setText(formatter.format(astroCalculator.getSunInfo().getAzimuthRise()));
            AstroDateTime sunSet = astroCalculator.getSunInfo().getSunset();
            westTime.setText(sunSet.getHour() + " : " + sunSet.getMinute() + " : " + sunSet.getSecond());
            westAzimuth.setText(formatter.format(astroCalculator.getSunInfo().getAzimuthSet()));
            AstroDateTime sunTwilightEvening = astroCalculator.getSunInfo().getTwilightEvening();
            AstroDateTime sunTwilightMorning = astroCalculator.getSunInfo().getTwilightMorning();
            civilEvening.setText(sunTwilightEvening.getHour() + " : " + sunTwilightEvening.getMinute() + " : " + sunTwilightEvening.getSecond());
            civilMorning.setText(sunTwilightMorning.getHour() + " : " + sunTwilightMorning.getMinute() + " : " + sunTwilightMorning.getSecond());
        }
    }
}
