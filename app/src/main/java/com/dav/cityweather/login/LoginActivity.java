package com.dav.cityweather.login;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.dav.cityweather.R;

/**
 * Created by dav on 30.03.17.
 */

public class LoginActivity extends AppCompatActivity implements ILoginView, AdapterView.OnItemClickListener {


    private ILoginPresenter mILoginPresenter;
    private  ProgressBar mProgressBar;
    private CityListAdapter cityListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mILoginPresenter = new LoginPresenterImpl(this,this);
        initComponent();
    }

    @Override
    public void initComponent() {
        mProgressBar =(ProgressBar)this.findViewById(R.id.progressLoadCity);
        cityListAdapter = new CityListAdapter(this,R.layout.list_item_autocomplete,mILoginPresenter);
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
                mILoginPresenter.filter().filter(s);
            }
        });
        autoCompleteTextView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onDestroy(){
        mILoginPresenter.onDestroy();
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

