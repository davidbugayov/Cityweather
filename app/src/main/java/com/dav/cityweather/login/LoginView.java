package com.dav.cityweather.login;


/**
 * Created by dav on 30.03.17.
 */

interface LoginView  {
    void initComponent();
    void showProgress();
    void hideProgress();
    void showError();
}
