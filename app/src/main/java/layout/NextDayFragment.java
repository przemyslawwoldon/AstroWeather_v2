package layout;

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


public class NextDayFragment extends Fragment {
    private ImageView weatherIconImageView1;
    private TextView dayTextView1;
    private TextView highTextView1;
    private TextView lowTextView1;

    private ImageView weatherIconImageView2;
    private TextView dayTextView2;
    private TextView highTextView2;
    private TextView lowTextView2;

    private ImageView weatherIconImageView3;
    private TextView dayTextView3;
    private TextView highTextView3;
    private TextView lowTextView3;



    MainActivity mainActivity;

    public NextDayFragment() {
    }

    public static NextDayFragment newInstance() {
        NextDayFragment fragment = new NextDayFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mainActivity = (MainActivity) getActivity();

        View view = inflater.inflate(R.layout.fragment_next_day, container, false);

        weatherIconImageView1 = (ImageView) view.findViewById(R.id.weatherIconImageView1);
        dayTextView1 = (TextView) view.findViewById(R.id.dayTextView1);
        highTextView1 = (TextView) view.findViewById(R.id.highTextView1);
        lowTextView1 = (TextView) view.findViewById(R.id.lowTextView1);

        weatherIconImageView2 = (ImageView) view.findViewById(R.id.weatherIconImageView2);
        dayTextView2 = (TextView) view.findViewById(R.id.dayTextView2);
        highTextView2 = (TextView) view.findViewById(R.id.highTextView2);
        lowTextView2 = (TextView) view.findViewById(R.id.lowTextView2);

        weatherIconImageView3 = (ImageView) view.findViewById(R.id.weatherIconImageView3);
        dayTextView3 = (TextView) view.findViewById(R.id.dayTextView3);
        highTextView3 = (TextView) view.findViewById(R.id.highTextView3);
        lowTextView3 = (TextView) view.findViewById(R.id.lowTextView3);

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
            NumberFormat formatter = new DecimalFormat("#0.00");



            Condition[] forecast = channel.getItem().getForecast();
            int weatherIconImageResource = getActivity().getResources().getIdentifier("icon_" + forecast[0].getCode(), "drawable", getActivity().getPackageName());
            weatherIconImageView1.setImageResource(weatherIconImageResource);
            dayTextView1.setText(forecast[0].getDay());
            if(units.equals("c")) {
                highTextView1.setText(getString(R.string.temperature_output, forecast[0].getHighTemperature(), channel.getUnits().getTemperature()));
                lowTextView1.setText(getString(R.string.temperature_output, forecast[0].getLowTemperature(), channel.getUnits().getTemperature()));
            }else {
                double cH = (forecast[0].getHighTemperature() * 1.8 ) + 32;
                double cL = (forecast[0].getLowTemperature() * 1.8 ) + 32;
                highTextView1.setText(formatter.format(cH) + " °F");
                lowTextView1.setText(formatter.format(cL) + " °F");
            }

            int weatherIconImageResource2 = getActivity().getResources().getIdentifier("icon_" + forecast[1].getCode(), "drawable", getActivity().getPackageName());
            weatherIconImageView2.setImageResource(weatherIconImageResource2);
            dayTextView2.setText(forecast[1].getDay());
            if(units.equals("c")) {
                highTextView2.setText(getString(R.string.temperature_output, forecast[1].getHighTemperature(), channel.getUnits().getTemperature()));
                lowTextView2.setText(getString(R.string.temperature_output, forecast[1].getLowTemperature(), channel.getUnits().getTemperature()));
            }else {
                double cH = (forecast[1].getHighTemperature() * 1.8 ) + 32;
                double cL = (forecast[1].getLowTemperature() * 1.8 ) + 32;
                highTextView2.setText(formatter.format(cH) + " °F");
                lowTextView2.setText(formatter.format(cL) + " °F");
            }

            int weatherIconImageResource3 = getActivity().getResources().getIdentifier("icon_" + forecast[2].getCode(), "drawable", getActivity().getPackageName());
            weatherIconImageView3.setImageResource(weatherIconImageResource3);
            dayTextView3.setText(forecast[2].getDay());
            if(units.equals("c")) {
                highTextView3.setText(getString(R.string.temperature_output, forecast[2].getHighTemperature(), channel.getUnits().getTemperature()));
                lowTextView3.setText(getString(R.string.temperature_output, forecast[2].getLowTemperature(), channel.getUnits().getTemperature()));
            }else {
                double cH = (forecast[2].getHighTemperature() * 1.8 ) + 32;
                double cL = (forecast[2].getLowTemperature() * 1.8 ) + 32;
                highTextView3.setText(formatter.format(cH) + " °F");
                lowTextView3.setText(formatter.format(cL) + " °F");
            }
        }
    }

}
