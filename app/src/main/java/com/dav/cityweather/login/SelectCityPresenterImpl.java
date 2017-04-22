package com.dav.cityweather.login;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.dav.cityweather.basecore.CityWeatherContentObserver;
import com.dav.cityweather.basecore.ICityWeatherContentListener;

import java.util.ArrayList;

/**
 * Created by dav on 31.03.17.
 */

class SelectCityPresenterImpl implements ISelectCityPresenter, Filterable{
    private ISelectCityView mISelectCityView;
    private ISelectCItyManager mSelectCityManager;
    private Context mContext;
    private ContentObserver mObserver;

    SelectCityPresenterImpl(ISelectCityView ISelectCityView, Context context) {
        this.mISelectCityView = ISelectCityView;
        this.mSelectCityManager = new SelectCityManager();

        mContext=context;
        mObserver= new CityWeatherContentObserver(new SelectCityListener());
        mContext.getContentResolver().registerContentObserver(mSelectCityManager.getUriCity(),false,mObserver);
    }


    @Override
    public Filter filter() {
        return getFilter();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    ArrayList mResultList = mSelectCityManager.getCity(constraint.toString());
                    filterResults.values = mResultList;
                    filterResults.count = mResultList.size();
                    mISelectCityView.getAdapter().setArray(mResultList);
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    mISelectCityView.getAdapter().notifyDataSetChanged();
                } else {
                    mISelectCityView.getAdapter().notifyDataSetInvalidated();
                }
            }
        };
    }

    private class SelectCityListener implements ICityWeatherContentListener{
        @Override
        public void onChange() {

        }
    }
}
