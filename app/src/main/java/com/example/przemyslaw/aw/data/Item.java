package com.example.przemyslaw.aw.data;

import android.os.Build;
import android.support.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Przemyslaw on 2017-06-11.
 */

public class Item implements JSONPopulator {
    private Condition condition;
    private Condition[] forecast;
    double latitude;
    double longitude;

    public Condition getCondition() {
        return condition;
    }

    public Condition[] getForecast() {
        return forecast;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public void populate(JSONObject data) {
        condition = new Condition();
        condition.populate(data.optJSONObject("condition"));

        JSONArray forecastData = data.optJSONArray("forecast");

        latitude = data.optDouble("lat");
        longitude = data.optDouble("long");


        forecast = new Condition[forecastData.length()];

        for (int i = 0; i < forecastData.length(); i++) {
            forecast[i] = new Condition();
            try {
                forecast[i].populate(forecastData.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject();
        try {
            data.put("condition", condition.toJSON());
            data.put("forecast", new JSONArray(forecast));
            data.put("long", longitude);
            data.put("lat", latitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
}
