package layout.large;


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

import layout.MoonFragment;
import layout.SunFragment;

/**
 * Created by Przemyslaw on 2017-06-14.
 */

public class SunMoonFragment extends Fragment {
    TextView eastTime;
    TextView eastAzimuth;
    TextView westTime;
    TextView westAzimuth;
    TextView civilEvening;
    TextView civilMorning;

    Calendar calendar;
    MainActivity mainActivity;
    Channel channel;

    public SunMoonFragment() {
    }

    public static SunMoonFragment newInstance() {
        SunMoonFragment fragment = new SunMoonFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainActivity = (MainActivity) getActivity();

        View view = inflater.inflate(R.layout.fragment_sun_moon, container, false);
        SunFragment sunFragmentL = SunFragment.newInstance();
        MoonFragment moonFragmentL = MoonFragment.newInstance();
        android.support.v4.app.FragmentManager manager = getFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment100, sunFragmentL);
        fragmentTransaction.replace(R.id.fragment101, moonFragmentL);
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