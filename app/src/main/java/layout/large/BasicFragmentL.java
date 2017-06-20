package layout.large;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.przemyslaw.aw.MainActivity;
import com.example.przemyslaw.aw.R;
import com.example.przemyslaw.aw.data.Channel;
import com.example.przemyslaw.aw.data.Condition;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by Przemyslaw on 2017-06-14.
 */

public class BasicFragmentL extends Fragment {
    private ImageView weatherIconImageView;
    private TextView temperatureTextView;
    private TextView conditionTextView;
    private TextView locationTextView;
    private TextView pressureTextView;

    MainActivity mainActivity;

    public BasicFragmentL() {
    }

    public static BasicFragmentL newInstance() {
        BasicFragmentL fragment = new BasicFragmentL();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mainActivity = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_basic_l, container, false);
        weatherIconImageView = (ImageView) view.findViewById(R.id.weatherIconImageView);
        temperatureTextView = (TextView) view.findViewById(R.id.temperatureTextView);
        conditionTextView = (TextView) view.findViewById(R.id.conditionTextView);
        locationTextView = (TextView) view.findViewById(R.id.locationTextView);
        pressureTextView = (TextView) view.findViewById(R.id.pressureTextView);

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
        }
    }

}

