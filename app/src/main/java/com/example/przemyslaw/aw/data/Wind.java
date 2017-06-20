package com.example.przemyslaw.aw.data;

import android.os.Build;
import android.support.annotation.RequiresApi;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Przemyslaw on 2017-06-12.
 */

public class Wind implements JSONPopulator {

    private double chill;
    private double direction;
    private double speed;

    public double getChill() {
        return chill;
    }

    public double getDirection() {
        return direction;
    }

    public double getSpeed() {
        return speed;
    }

    @Override
    public void populate(JSONObject data, int i) {
        chill = data.optDouble("chill");
        direction = data.optDouble("direction");
        speed = data.optDouble("speed");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject();
        try {
            data.put("chill", chill);
            data.put("direction", direction);
            data.put("speed", speed);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

}
