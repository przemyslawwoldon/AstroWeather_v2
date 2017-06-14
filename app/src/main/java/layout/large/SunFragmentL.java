package layout.large;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;
import com.example.przemyslaw.aw.MainActivity;
import com.example.przemyslaw.aw.R;
import com.example.przemyslaw.aw.data.Channel;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Przemyslaw on 2017-06-14.
 */

public class SunFragmentL extends Fragment {
    TextView eastTime;
    TextView eastAzimuth;
    TextView westTime;
    TextView westAzimuth;
    TextView civilEvening;
    TextView civilMorning;

    Calendar calendar;
    MainActivity mainActivity;

    public SunFragmentL() {
    }

    public static SunFragmentL newInstance() {
        SunFragmentL fragment = new SunFragmentL();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainActivity = (MainActivity) getActivity();

        View view = inflater.inflate(R.layout.fragment_sun_l, container, false);
        eastTime = (TextView) view.findViewById(R.id.SunFargtextViewEastTime);
        eastAzimuth = (TextView) view.findViewById(R.id.SunFargtextViewEastAzimuth);
        civilEvening = (TextView) view.findViewById(R.id.SunFargtextViewCivilEvening);
        civilMorning = (TextView) view.findViewById(R.id.SunFargtextViewCivilDaylight);
        westTime = (TextView) view.findViewById(R.id.SunFargtextViewWestTime);
        westAzimuth = (TextView) view.findViewById(R.id.SunFargtextViewWestAzimuth);

        refreshData();
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
                                                                refreshData();
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

    public void refreshData() {
        Channel channel = mainActivity.getChannel();
        if (channel != null) {
            AstroCalculator.Location astroLocation = new AstroCalculator.Location(channel.getItem().getLatitude(), channel.getItem().getLongitude());
            calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));
            TimeZone timeZone = TimeZone.getDefault();
            int offset = 2;
            AstroDateTime astroDateTime = new AstroDateTime(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                    calendar.get(Calendar.SECOND), offset, timeZone.useDaylightTime());

            AstroCalculator astroCalculator = new AstroCalculator(astroDateTime, astroLocation);
            NumberFormat formatter = new DecimalFormat("#0.00");

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