package com.example.przemyslaw.aw.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Przemyslaw on 2017-06-12.
 */

public class LocationResult implements JSONPopulator {

    private String address;

    public String getAddress() {
        return address;
    }

    @Override
    public void populate(JSONObject data, int i) {
        address = data.optString("formatted_address");
    }

    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject();

        try {
            data.put("formatted_address", address);
        } catch (JSONException e) {}

        return data;
    }
}
