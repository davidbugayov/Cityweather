package com.dav.cityweather.login;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;

import com.example.dav.cityweather.R;

/**
 * Created by dav on 30.03.17.
 */

public class LoginActivity extends AppCompatActivity implements LoginView, AdapterView.OnItemClickListener {


    private LoginPresenter mLoginPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLoginPresenter = new LoginPresenterImpl(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void initAutoComplete() {

    }
}

