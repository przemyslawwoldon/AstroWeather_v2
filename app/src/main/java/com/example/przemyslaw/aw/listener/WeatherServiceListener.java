package com.example.przemyslaw.aw.listener;

import com.example.przemyslaw.aw.data.Channel;

/**
 * Created by Przemyslaw on 2017-06-11.
 */

public interface WeatherServiceListener {

        void serviceSuccess(Channel channel);

        void serviceFailure(Exception exception);

}
