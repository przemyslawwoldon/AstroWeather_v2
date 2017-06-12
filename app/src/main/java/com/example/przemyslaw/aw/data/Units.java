package com.example.przemyslaw.aw.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Przemyslaw on 2017-06-11.
 */

public class Units implements JSONPopulator {
    private String temperature;
    private String pressure;
    private String speed;

    public String getSpeed() {
        return speed;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getPressure() {
        return pressure;
    }

    @Override
    public void populate(JSONObject data) {
        pressure = data.optString("pressure");
        temperature = data.optString("temperature");
        speed = data.optString("speed");
    }

    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject();

        try {
            data.put("temperature", temperature);
            data.put("pressure", pressure);
            data.put("speed", speed);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }
}