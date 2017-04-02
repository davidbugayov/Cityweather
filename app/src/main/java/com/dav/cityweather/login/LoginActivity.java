package com.dav.cityweather.login;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.dav.cityweather.R;

/**
 * Created by dav on 30.03.17.
 */

public class LoginActivity extends AppCompatActivity implements LoginView, AdapterView.OnItemClickListener {


    private LoginPresenter mLoginPresenter;
    private  ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLoginPresenter = new LoginPresenterImpl(this);
    }

    @Override
    public void initComponent() {
        mProgressBar =(ProgressBar)this.findViewById(R.id.progressLoadCity);
        AutoCompleteTextView autoCompleteTextView =(AutoCompleteTextView)this.findViewById(R.id.autoCompleteGooglePlace);
        autoCompleteTextView.setText("Mos");
        autoCompleteTextView.setAdapter(mLoginPresenter.getArrayAdapter(this,R.layout.list_item_autocomplete));
        autoCompleteTextView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onDestroy(){
        mLoginPresenter.onDestroy();
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

}

