package com.dav.cityweather;

/**
 * Created by dav on 04.04.17.
 */

public interface IOnLoadDataListener {
    void onStartLoad();
    void onLoadFailed();
    void onSuccess();
}
