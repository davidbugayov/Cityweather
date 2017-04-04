package com.dav.cityweather.login;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.dav.cityweather.data.AutoCompleteRequest;
import com.example.dav.cityweather.R;

import java.util.ArrayList;

/**
 * Created by dav on 31.03.17.
 */

 class LoginPresenterImpl implements ILoginPresenter, Filterable{
    private ILoginView mILoginView;
    private ILoginInteractor mILoginInteractor;
    private AutoCompleteRequest1 mAutoCompleteRequest;
    private ArrayList mResultList;
    private Context context;

    LoginPresenterImpl(ILoginView ILoginView,Context context) {
        this.mILoginView = ILoginView;
        this.mILoginInteractor = new LoginInteractorImpl();
        mAutoCompleteRequest= new AutoCompleteRequest1(context);
    }


    @Override
    public ArrayAdapter getArrayAdapter(Context context, int textViewResourceId) {
       return mILoginInteractor.getGooglePlaceAutoComplete(context,textViewResourceId);
    }

   @Override
   public void onDestroy() {
      mILoginView =null;
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
                   ArrayList mResultList = mAutoCompleteRequest.autocomplete(constraint.toString());
                    filterResults.values = mResultList;
                    filterResults.count = mResultList.size();
                    mILoginView.getAdapter().setArray(mResultList);
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                   mILoginView.getAdapter().notifyDataSetChanged();
                } else {
                    mILoginView.getAdapter().notifyDataSetInvalidated();
                }
            }
        };
    }

}
