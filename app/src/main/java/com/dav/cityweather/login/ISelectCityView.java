package com.dav.cityweather.login;


/**
 * Created by dav on 30.03.17.
 */

interface ISelectCityView {
    void initComponent();
    CityListAdapter getAdapter();
    void showProgress();
    void hideProgress();
    void showError();
}
