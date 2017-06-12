package com.example.przemyslaw.aw.data;

import org.json.JSONObject;

/**
 * Created by Przemyslaw on 2017-06-11.
 */

public interface JSONPopulator {
    void populate(JSONObject data);

    JSONObject toJSON();
}
