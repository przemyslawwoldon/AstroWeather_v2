package layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.przemyslaw.aw.R;
import com.example.przemyslaw.aw.data.Condition;
import com.example.przemyslaw.aw.data.Units;


public class WeatherConditionFragment extends Fragment {
    private ImageView weatherIconImageView;
    private TextView dayTextView;
    private TextView highTextView;
    private TextView lowTextView;

    public static WeatherConditionFragment newInstance() {
        WeatherConditionFragment fragment = new WeatherConditionFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_condition, container, false);
        weatherIconImageView = (ImageView) view.findViewById(R.id.weatherIconImageView);
        dayTextView = (TextView) view.findViewById(R.id.dayTextView);
        highTextView = (TextView) view.findViewById(R.id.highTextView);
        lowTextView = (TextView) view.findViewById(R.id.lowTextView);

        return view;
    }

    public void loadForecast(Condition forecast, Units units) {
        int weatherIconImageResource = getResources().getIdentifier("icon_" + forecast.getCode(), "drawable", getActivity().getPackageName());
        weatherIconImageView.setImageResource(weatherIconImageResource);
        dayTextView.setText(forecast.getDay());
        highTextView.setText(getString(R.string.temperature_output, forecast.getHighTemperature(), units.getTemperature()));
        lowTextView.setText(getString(R.string.temperature_output, forecast.getLowTemperature(), units.getTemperature()));
    }
}
