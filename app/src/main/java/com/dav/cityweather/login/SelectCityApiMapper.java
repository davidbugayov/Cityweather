package com.dav.cityweather.login;

import android.content.Context;

import com.dav.cityweather.basecore.HttpConnector;
import com.example.dav.cityweather.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by dav on 09.04.17.
 */

public class SelectCityApiMapper {

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static String API_KEY = "AIzaSyDoOTpo1GGM_zbcLL83ZNGyItX1n7A48pk";


    public ArrayList<String> getCity(String symbols) {
        ArrayList<String> city = new ArrayList<>();
        HttpConnector httpConnector = new HttpConnector();
        SelectCityResponse selectCityResponse = new SelectCityResponse();
        try {
            StringBuilder request = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            request.append("?key=" + API_KEY);
            request.append("&input=" + URLEncoder.encode(symbols, "utf8"));
            selectCityResponse.setDescription((String)httpConnector.sendReciveData(request.toString()););
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return city;
    }
}
