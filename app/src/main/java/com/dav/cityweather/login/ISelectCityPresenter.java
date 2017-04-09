package com.dav.cityweather.login;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import java.util.ArrayList;

/**
 * Created by dav on 31.03.17.
 */

public interface ISelectCityPresenter {
    ArrayAdapter getArrayAdapter(Context context, int textViewResourceId);
    void onDestroy();
    Filter filter();
}
