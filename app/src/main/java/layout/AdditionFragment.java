package layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.przemyslaw.aw.MainActivity;
import com.example.przemyslaw.aw.R;
import com.example.przemyslaw.aw.data.Channel;



public class AdditionFragment extends Fragment {
    private TextView windSpeed;
    private TextView windDirection;
    private TextView atmosphereHumidity;
    private TextView atmosphereVisibility;

    MainActivity mainActivity;

    public AdditionFragment() {
    }

    public static AdditionFragment newInstance() {
        AdditionFragment fragment = new AdditionFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mainActivity = (MainActivity) getActivity();

        View view = inflater.inflate(R.layout.fragment_addition, container, false);
        windSpeed = (TextView) view.findViewById(R.id.textViewSpeed);;
        windDirection = (TextView) view.findViewById(R.id.textViewDirection);;
        atmosphereHumidity = (TextView) view.findViewById(R.id.textViewHumidity);
        atmosphereVisibility = (TextView) view.findViewById(R.id.textViewVisibility);

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
        if(channel != null) {
            windSpeed.setText(channel.getWind().getSpeed() + channel.getUnits().getSpeed());
            windDirection.setText(String.valueOf(channel.getWind().getDirection()));
            atmosphereHumidity.setText(String.valueOf(channel.getAtmosphere().getHumidity()));
            atmosphereVisibility.setText(String.valueOf(channel.getAtmosphere().getVisibility()));
        }
    }
}
