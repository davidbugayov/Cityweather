package com.dav.cityweather.login;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dav.cityweather.basecore.CityWeatherContentObserver;
import com.example.dav.cityweather.R;

/**
 * Created by dav on 30.03.17.
 */

public class SelectCityActivity extends AppCompatActivity implements ISelectCityView, AdapterView.OnItemClickListener {


    private SelectCityPresenterImpl mISelectCityPresenter;
    private  ProgressBar mProgressBar;
    private CityListAdapter cityListAdapter;

    @Override
    public void initComponent() {
        mProgressBar =(ProgressBar)this.findViewById(R.id.progressLoadCity);
        cityListAdapter = new CityListAdapter(this,R.layout.list_item_autocomplete);
        AutoCompleteTextView autoCompleteTextView =(AutoCompleteTextView)this.findViewById(R.id.autoCompleteGooglePlace);
        autoCompleteTextView.setAdapter(cityListAdapter);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mISelectCityPresenter.filter().filter(s);
            }
        });
        autoCompleteTextView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mISelectCityPresenter = new SelectCityPresenterImpl(this,this);
        initComponent();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void showProgress() {
            mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
            mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showError() {
        Toast.makeText(this,this.getString(R.string.failed_load_city),Toast.LENGTH_LONG).show();
    }

    @Override
    public CityListAdapter getAdapter() {
        return cityListAdapter;
    }
}

