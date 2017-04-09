package com.dav.cityweather.login;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.dav.cityweather.data.GooglePlacesAutocompleteAdapter;

/**
 * Created by dav on 31.03.17.
 */

public class SelectCityInteractorImpl implements ISelectCItyInteractor {

    @Override
    public ArrayAdapter getGooglePlaceAutoComplete(Context context, int textViewResourceId){
        return  new GooglePlacesAutocompleteAdapter(context,textViewResourceId);
    }
}
