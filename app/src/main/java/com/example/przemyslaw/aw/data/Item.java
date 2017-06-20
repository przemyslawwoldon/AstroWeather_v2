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
    public void populate(JSONObject data, int j) {
        condition = new Condition();
        JSONArray forecastData = data.optJSONArray("forecast");
        forecast = new Condition[forecastData.length()];
        try {
            condition.populate(data.getJSONObject("condition"), 0);
            for (int i = 0; i < forecastData.length(); i++) {
                forecast[i] = new Condition();
                forecast[i].populate(forecastData.getJSONObject(i), 0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        latitude = data.optDouble("lat");
        longitude = data.optDouble("long");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject();
        try {
            JSONArray array = new JSONArray();
            array.put(0, forecast[0].toJSON());
            array.put(1, forecast[1].toJSON());
            array.put(2, forecast[2].toJSON());
            array.put(3, forecast[3].toJSON());
            array.put(4, forecast[4].toJSON());
            array.put(5, forecast[5].toJSON());
            array.put(6, forecast[6].toJSON());
            array.put(7, forecast[7].toJSON());
            data.put("condition", condition.toJSON());
            data.put("forecast", array);
            data.put("long", longitude);
            data.put("lat", latitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

}
