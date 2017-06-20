package com.example.przemyslaw.aw.data;

import android.os.Build;
import android.support.annotation.RequiresApi;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Przemyslaw on 2017-06-12.
 */

public class Atmosphere implements JSONPopulator {

    private double humidity;
    private double pressure;
    private double rising;
    private double visibility;

    public double getHumidity() {
        return humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public double getRising() {
        return rising;
    }

    public double getVisibility() {
        return visibility;
    }

    @Override
    public void populate(JSONObject data, int i) {
        pressure = data.optDouble("pressure");
        humidity = data.optDouble("humidity");
        rising = data.optDouble("rising");
        visibility = data.optDouble("visibility");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject();
        try {
            data.put("pressure", pressure);
            data.put("humidity", humidity);
            data.put("rising", rising);
            data.put("visibility", visibility);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

}
