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
    public void populate(JSONObject data, int i) {

        units = new Units();
        item = new Item();
        atmosphere = new Atmosphere();
        wind = new Wind();

        try {
            units.populate(data.getJSONObject("units"), 0);
            item.populate(data.getJSONObject("item"), 0);
            atmosphere.populate(data.getJSONObject("atmosphere"), 0);
            wind.populate(data.getJSONObject("wind"), 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

if(i == 0) {
    JSONObject locationData = data.optJSONObject("location");
    String region = locationData.optString("region");
    String country = locationData.optString("country");

    location = String.format("%s, %s", locationData.optString("city"), (region.length() != 0 ? region : country));



}else {
    location = data.optString("location");

}



    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public JSONObject toJSON() {

        JSONObject data = new JSONObject();

        try {
            data.put("units", units.toJSON());
            data.put("item", item.toJSON());
            data.put("location", location);
            data.put("atmosphere", atmosphere.toJSON());
            data.put("wind", wind.toJSON());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }

}