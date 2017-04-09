package com.dav.cityweather.login;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.dav.cityweather.basecore.CityWeatherContentObserver;
import com.dav.cityweather.basecore.ICityWeatherContentListener;

import java.util.ArrayList;

/**
 * Created by dav on 31.03.17.
 */

 class SelectCityPresenterImpl implements ISelectCityPresenter, Filterable {
    private ISelectCityView mISelectCityView;
    private ISelectCItyInteractor mISelectCItyInteractor;
    private AutoCompleteRequest1 mAutoCompleteRequest;
    private ArrayList mResultList;
    private Context mContext;
    private ContentObserver mObserver;
    private SelectCityManager selectCityManager;

    SelectCityPresenterImpl(ISelectCityView ISelectCityView, Context context) {
        this.mISelectCityView = ISelectCityView;
        this.mISelectCItyInteractor = new SelectCityInteractorImpl();
        mContext=context;
       // mAutoCompleteRequest= new AutoCompleteRequest1(context);
        mObserver= new CityWeatherContentObserver(new onSelectCityListenerImpl());
        selectCityManager = new SelectCityManager();
        //// TODO: 09.04.17
       // mContext.getContentResolver().registerContentObserver(,null,mObserver);
    }

    private class onSelectCityListenerImpl implements ICityWeatherContentListener{

        @Override
        public void onChange() {

        }
    }

    @Override
    public ArrayAdapter getArrayAdapter(Context context, int textViewResourceId) {
       return mISelectCItyInteractor.getGooglePlaceAutoComplete(context,textViewResourceId);
    }

    public void registerObservers(Uri uri, ContentResolver resolver, ContentObserver observer) {
        resolver.registerContentObserver(uri, false, observer);
    }

   @Override
   public void onDestroy() {
      mISelectCityView =null;
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
                   ArrayList mResultList = selectCityManager.getCity(constraint.toString());
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


}
