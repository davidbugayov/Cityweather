package com.dav.cityweather.login;

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 * Created by dav on 31.03.17.
 */

 class LoginPresenterImpl implements LoginPresenter {
    private LoginView mLoginView;
    private LoginInteractor mLoginInteractor;

     LoginPresenterImpl(LoginView loginView) {
        this.mLoginView = loginView;
        this.mLoginInteractor = new LoginInteractorImpl();
    }


    @Override
    public ArrayAdapter getArrayAdapter(Context context, int textViewResourceId) {
       return mLoginInteractor.getGooglePlaceAutoComplete(context,textViewResourceId);
    }

}
