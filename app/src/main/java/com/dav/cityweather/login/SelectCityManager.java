package com.dav.cityweather.login;

import android.content.Context;
import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by dav on 09.04.17.
 */

public class SelectCityManager implements  ISelectCItyManager{
    private static final String SELECT_CITY = "SELECT_CITY";
    private static final String PATH_GET_CITY = "PATH_GET_CITY";
    @Override
    public ArrayList<String> getCity(String symbols){
        SelectCityApiMapper selectCityApiMapper = new SelectCityApiMapper();
        return selectCityApiMapper.getCity(symbols);
    }

    @Override
    public Uri getUriCity() {
        return Uri.parse(SELECT_CITY+" "+PATH_GET_CITY);
    }
}
