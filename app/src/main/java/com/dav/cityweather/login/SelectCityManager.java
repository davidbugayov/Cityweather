package com.dav.cityweather.login;

import android.content.Context;
import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by dav on 09.04.17.
 */

public class SelectCityManager {

    ArrayList<String> getCity(String symbols){
        SelectCityApiMapper selectCityApiMapper = new SelectCityApiMapper();
        return selectCityApiMapper.getCity(symbols);
    }
}
