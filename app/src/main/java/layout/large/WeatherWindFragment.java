package layout.large;

/**
 * Created by Przemyslaw on 2017-06-14.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class WeatherWindFragment extends Fragment {
    private ImageView weatherIconImageView;
    private TextView temperatureTextView;
    private TextView conditionTextView;
    private TextView locationTextView;
    private TextView pressureTextView;

    private TextView windSpeed;
    private TextView windDirection;
    private TextView atmosphereHumidity;
    private TextView atmosphereVisibility;

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


        windSpeed = (TextView) view.findViewById(R.id.textViewSpeed);;
        windDirection = (TextView) view.findViewById(R.id.textViewDirection);;
        atmosphereHumidity = (TextView) view.findViewById(R.id.textViewHumidity);
        atmosphereVisibility = (TextView) view.findViewById(R.id.textViewVisibility);
        weatherIconImageView = (ImageView) view.findViewById(R.id.weatherIconImageView);
        temperatureTextView = (TextView) view.findViewById(R.id.temperatureTextView);
        conditionTextView = (TextView) view.findViewById(R.id.conditionTextView);
        locationTextView = (TextView) view.findViewById(R.id.locationTextView);
        pressureTextView = (TextView) view.findViewById(R.id.pressureTextView);





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
        channel = mainActivity.getChannel();
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
        }
    }
}