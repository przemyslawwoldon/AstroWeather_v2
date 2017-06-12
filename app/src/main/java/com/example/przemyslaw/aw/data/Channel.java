package com.example.przemyslaw.aw.data;

import android.os.Build;
import android.support.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Przemyslaw on 2017-06-11.
 */

public class Channel implements JSONPopulator {
    private Units units;
    private Item item;
    private String location;
    private Atmosphere atmosphere;
    private Wind wind;

    public Wind getWind() {
        return wind;
    }

    public Units getUnits() {
        return units;
    }

    public Item getItem() {
        return item;
    }

    public String getLocation() {
        return location;
    }

    public Atmosphere getAtmosphere() {
        return atmosphere;
    }

    @Override
    public void populate(JSONObject data) {

        units = new Units();
        units.populate(data.optJSONObject("units"));

        item = new Item();
        item.populate(data.optJSONObject("item"));

        atmosphere = new Atmosphere();
        atmosphere.populate(data.optJSONObject("atmosphere"));

        wind = new Wind();
        wind.populate(data.optJSONObject("wind"));

        JSONObject locationData = data.optJSONObject("location");
        String region = locationData.optString("region");
        String country = locationData.optString("country");

        location = String.format("%s, %s", locationData.optString("city"), (region.length() != 0 ? region : country));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public JSONObject toJSON() {

        JSONObject data = new JSONObject();

        try {
            data.put("units", units.toJSON());
            data.put("item", item.toJSON());
            data.put("location", location);
            data.put("atmosphere", atmosphere);
            data.put("wind", wind);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }

}