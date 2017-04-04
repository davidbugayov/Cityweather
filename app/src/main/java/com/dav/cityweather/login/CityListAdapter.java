package com.dav.cityweather.login;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by dav on 04.04.17.
 */

public class CityListAdapter extends ArrayAdapter {

    private ArrayList mResultList;
    private ILoginPresenter mIloginPresenter;
    public CityListAdapter(@NonNull Context context, @LayoutRes int resource, ILoginPresenter iLoginPresenter) {
        super(context, resource);
        mIloginPresenter=iLoginPresenter;
    }

    public void setArray ( ArrayList resultList){
        mResultList=resultList;
    }

    @Override
    public int getCount() {
        return mResultList.size();
    }

    @Override
    public Object getItem(int index) {
        return mResultList.get(index);
    }

}
