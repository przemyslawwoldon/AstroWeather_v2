package layout;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.przemyslaw.aw.R;
import com.example.przemyslaw.aw.data.Channel;
import com.example.przemyslaw.aw.data.Condition;
import com.example.przemyslaw.aw.data.Units;
import com.example.przemyslaw.aw.listener.WeatherServiceListener;
import com.example.przemyslaw.aw.service.YahooWeatherService;


public class BasicFragment extends Fragment implements WeatherServiceListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ImageView weatherIconImageView;
    private TextView temperatureTextView;
    private TextView conditionTextView;
    private TextView locationTextView;
    private TextView pressureTextView;


    YahooWeatherService yahooWeatherService;
    int i=0;

    public BasicFragment() {
    }

    public static BasicFragment newInstance(String location, String time) {
        BasicFragment fragment = new BasicFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, location);
        args.putString(ARG_PARAM2, time);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final String s1 =(String) getArguments().getSerializable(ARG_PARAM1);
        final String s2 =(String) getArguments().getSerializable(ARG_PARAM2);

        View view = inflater.inflate(R.layout.fragment_basic, container, false);
        weatherIconImageView = (ImageView) view.findViewById(R.id.weatherIconImageView);
        temperatureTextView = (TextView) view.findViewById(R.id.temperatureTextView);
        conditionTextView = (TextView) view.findViewById(R.id.conditionTextView);
        locationTextView = (TextView) view.findViewById(R.id.locationTextView);
        pressureTextView = (TextView) view.findViewById(R.id.pressureTextView);
        yahooWeatherService = new YahooWeatherService(this);
        yahooWeatherService.refreshWeather(s1);


        return view;
    }

    @Override
    public void serviceSuccess(Channel channel) {
        Condition condition = channel.getItem().getCondition();
        Units units = channel.getUnits();
        Condition[] forecast = channel.getItem().getForecast();
        Resources resources = getActivity().getResources();
        int weatherIconImageResource = resources.getIdentifier("icon_" + condition.getCode(), "drawable", getActivity().getPackageName());
        weatherIconImageView.setImageResource(weatherIconImageResource);
        temperatureTextView.setText(getString(R.string.temperature_output, condition.getTemperature(), units.getTemperature()));
        conditionTextView.setText(condition.getDescription());
        locationTextView.setText(channel.getLocation());
        //pressureTextView.setText(String.valueOf(channel.getAtmoshpere().getPressure()));
    }

    @Override
    public void serviceFailure(Exception exception) {

    }
}
