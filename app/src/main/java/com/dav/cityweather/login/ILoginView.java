package com.dav.cityweather.login;


/**
 * Created by dav on 30.03.17.
 */

interface ILoginView {
    void initComponent();
    CityListAdapter getAdapter();
    void showProgress();
    void hideProgress();
    void showError();
}
