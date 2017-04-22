package com.dav.cityweather.login;

import android.content.Context;
import android.net.Uri;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by dav on 31.03.17.
 */

public interface ISelectCItyManager {
   ArrayList<String> getCity(java.lang.String symbols);
    Uri getUriCity();
}
