package com.dav.cityweather.login;

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 * Created by dav on 31.03.17.
 */

public interface LoginPresenter {
    public ArrayAdapter getArrayAdapter(Context context, int textViewResourceId);
}
