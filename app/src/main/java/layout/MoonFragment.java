package layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class MoonFragment extends Fragment {
    TextView riseTime;
    TextView setTime;
    TextView fullTime;
    TextView newTime;
    TextView phase;
    TextView synodicDay;

    Calendar calendar;
    MainActivity mainActivity;

    public MoonFragment() {
    }

    public static MoonFragment newInstance() {
        MoonFragment fragment = new MoonFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mainActivity = (MainActivity) getActivity();

        View view = inflater.inflate(R.layout.fragment_moon, container, false);
        riseTime = (TextView) view.findViewById(R.id.MoonFargtextViewRiseMoon);
        setTime = (TextView) view.findViewById(R.id.MoonFargtextViewSetMoon);
        fullTime = (TextView) view.findViewById(R.id.MoonFargtextViewFullMoon);
        newTime = (TextView) view.findViewById(R.id.MoonFargtextViewNewMoon);
        phase = (TextView) view.findViewById(R.id.MoonFargtextViewPhase);
        synodicDay = (TextView) view.findViewById(R.id.MoonFargtextViewSynodicDay);

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

            NumberFormat formatter = new DecimalFormat("#0.0000");
            double moonPhaseTemp = astroCalculator.getMoonInfo().getIllumination();
            moonPhaseTemp *= 100;
            phase.setText(formatter.format(moonPhaseTemp));

            calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getTimeZone("GMT+2:00"));

            synodicDay.setText(formatter.format(astroCalculator.getMoonInfo().getAge()));
        }
    }
}
