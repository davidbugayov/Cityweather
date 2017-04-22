package com.dav.cityweather.basecore;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

/**
 * Created by dav on 08.04.17.
 */

public class CityWeatherContentObserver extends ContentObserver {
    private ICityWeatherContentListener mListener;

    public CityWeatherContentObserver(ICityWeatherContentListener listener) {
        super(new Handler());
        mListener = listener;
    }

    @Override
    public boolean deliverSelfNotifications() {
        return true;
    }

    @Override
    public void onChange(boolean selfChange) {
        this.onChange(selfChange, null);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        if(mListener!= null)
            mListener.onChange();
    }
}
