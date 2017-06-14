package layout.large;

/**
 * Created by Przemyslaw on 2017-06-14.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.przemyslaw.aw.MainActivity;
import com.example.przemyslaw.aw.R;
import com.example.przemyslaw.aw.data.Channel;

import java.util.Calendar;

import layout.AdditionFragment;
import layout.BasicFragment;

/**
 * Created by Przemyslaw on 2017-06-14.
 */

public class WeatherWindFragment extends Fragment {
    TextView eastTime;
    TextView eastAzimuth;
    TextView westTime;
    TextView westAzimuth;
    TextView civilEvening;
    TextView civilMorning;

    Calendar calendar;
    MainActivity mainActivity;
    Channel channel;

    public WeatherWindFragment() {
    }

    public static WeatherWindFragment newInstance() {
        WeatherWindFragment fragment = new WeatherWindFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainActivity = (MainActivity) getActivity();

        View view = inflater.inflate(R.layout.fragment_weather_wind, container, false);
        BasicFragment sunFragmentL = BasicFragment.newInstance();
        AdditionFragment moonFragmentL = AdditionFragment.newInstance();
        android.support.v4.app.FragmentManager manager = getFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment1, sunFragmentL);
        fragmentTransaction.replace(R.id.fragment2, moonFragmentL);
        fragmentTransaction.commit();

        refreshData();
        /*Thread t = new Thread() {
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
        t.start();*/

        return view;
    }

    public void refreshData() {
        channel = mainActivity.getChannel();
    }
}