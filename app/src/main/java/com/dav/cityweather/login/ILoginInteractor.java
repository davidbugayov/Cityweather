package com.dav.cityweather.login;

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 * Created by dav on 31.03.17.
 */

public interface ILoginInteractor {
    ArrayAdapter getGooglePlaceAutoComplete(Context context, int textViewResourceId);
}
